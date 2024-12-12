package com.sky.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.ak}")
    private String ak;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private WeChatPayUtil weChatPayUtil;
    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //检测地址是否存在
        AddressBook addressBook=new AddressBook();
        addressBook.setId(ordersSubmitDTO.getAddressBookId());
        AddressBook address = addressBookMapper.getAddressBookById(addressBook);
        if(address==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //检查购物中是否为空
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list==null||list.isEmpty()){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        //向订单表插入数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());//下单时间
        orders.setPayStatus(Orders.UN_PAID);//支付状态
        orders.setStatus(Orders.PENDING_PAYMENT);//订单状态待付款
        orders.setNumber(String.valueOf(Instant.now().toEpochMilli()));//设置订单号
        orders.setPhone(address.getPhone());
        orders.setConsignee(address.getConsignee());//收货人
        orders.setUserId(BaseContext.getCurrentId());
        String add=address.getProvinceName()+address.getCityName()+address.getDistrictName()+address.getDetail();
        orders.setAddress(add);//地址
        checkOutOfRange(add);
        orderMapper.insert(orders);

        //向订单明细表插入数据
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for(ShoppingCart cart:list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }

        //批量插入
        orderDetailMapper.inserts(orderDetailList);

        //清空购物车
        shoppingCartMapper.cleanByUserId(BaseContext.getCurrentId());

        //封装结果数据
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .build();

    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
//        Long userId = BaseContext.getCurrentId();
//       User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }

//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
        OrderPaymentVO vo =new OrderPaymentVO();
        paySuccess(ordersPaymentDTO.getOrderNumber());//直接修改订单信息
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 查询用户历史订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        List<OrderVO> orderVOS=new ArrayList<>();
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page=orderMapper.pageSearchHistoryOrders(ordersPageQueryDTO);
        if(page!=null&&page.getTotal()>0){
            for (Orders orders:page){
                OrderVO orderVO=new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                List<OrderDetail> orderDetails=orderDetailMapper.getByOrderId(orders.getId());
                orderVO.setOrderDetailList(orderDetails);
                orderVOS.add(orderVO);
            }
        }
        return new PageResult(page != null ? page.getTotal() : 0,orderVOS);


    }

    /**
     * 根据id查询订单信息
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderById(Long id) {
       Orders orders= orderMapper.getById(id);
       List<OrderDetail> orderDetail=orderDetailMapper.getByOrderId(id);
       OrderVO orderVO=new OrderVO();
       BeanUtils.copyProperties(orders,orderVO);
       orderVO.setOrderDetailList(orderDetail);
       return orderVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        Orders orders= orderMapper.getById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(orders.getPayStatus()>2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders ordersDB=Orders.builder()
                .id(id)
                .cancelTime(LocalDateTime.now())
                .cancelReason("用户取消订单")
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(ordersDB);

    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repetition(Long id) {
        Orders orders= orderMapper.getById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        }
        List<OrderDetail> orderDetail=orderDetailMapper.getByOrderId(id);
        if(orderDetail==null|| orderDetail.isEmpty()){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        List<ShoppingCart> shoppingCarts = orderDetail.stream().map(order -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(order, shoppingCart, "id");
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).toList();
        shoppingCartMapper.insertBatch(shoppingCarts);

    }

    /**
     * admin分页条件查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageSearchOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageSearchHistoryOrders(ordersPageQueryDTO);

        List<OrderVO> orderVOS=new ArrayList<>();
        if(page!=null&&page.getTotal()>0){
            for (Orders orders:page){
                OrderVO orderVO=new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                //构建菜品名称数量信息
                List<OrderDetail> orderDetail=orderDetailMapper.getByOrderId(orders.getId());
                    List<String> list = orderDetail.stream().map(od -> {
                        return od.getName() + "*" + od.getNumber();
                    }).toList();
                String orderDishes = String.join(",", list);
                orderVO.setOrderDishes(orderDishes);
                orderVOS.add(orderVO);
            }
        }
        return new PageResult(page != null ? page.getTotal() : 0,orderVOS);
        }

    /**
     * 查询各种状态订单数量
     * @return
     */
    @Override
    public OrderStatisticsVO getOrderStatistics() {
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 将查询出的数据封装到orderStatisticsVO中响应
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 根据订单号查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderVO(Long id) {
        Orders orders= orderMapper.getById(id);
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        List<OrderDetail> orderDetail=orderDetailMapper.getByOrderId(id);
        List<String> list = orderDetail.stream().map(od -> {
            return od.getName() + "*" + od.getNumber();
        }).toList();
        String orderDishes = String.join(",", list);
        orderVO.setOrderDishes(orderDishes);
        orderVO.setOrderDetailList(orderDetail);
        return orderVO;
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     */
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders ordersDB=orderMapper.getById(ordersConfirmDTO.getId());
        if(ordersDB==null||!ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders=Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void deliveryOrder(Long id) {
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    @Override
    public void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //缺少退款


        Orders orders=Orders.builder()
                .id(ordersRejectionDTO.getId())
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(orders);

    }

    /**
     * admin端取消订单
     * @param ordersCancelDTO
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            //用户已支付，需要退款,缺少
            log.info("申请退款：");
        }

        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void completeOrder(Long id) {
        Orders ordersDB = orderMapper.getById(id);
        if(ordersDB==null||!ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 百度地图定位
     * @param address
     */
    private void checkOutOfRange(String address){
        String shopLocation=location(shopAddress);
        String location=location(address);
        Double distance = distance(location, shopLocation);
        if(distance>5000){
            throw new OrderBusinessException("超出配送范围");
        }
    }

    //获取经纬度
    private String location(String address){
        Map<String,String> map=new HashMap<>();
        map.put("address",address);
        map.put("ak",ak);
        map.put("output", "json");
        map.put("callback", "showLocation");
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3/", map);
        int startIndex = json.indexOf('{');
        int endIndex = json.lastIndexOf('}');
        String jsonStr = json.substring(startIndex, endIndex + 1);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if(jsonObject.getIntValue("status")!=0){
            throw new OrderBusinessException("地址信息解析错误");
        }
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lng =  location.getString("lng");
        String lat =  location.getString("lat");
        return lat+","+lng;
    }

    private Double distance(String origin,String destination){
        Map<String,String> map=new HashMap<>();
        map.put("origins",origin);
        map.put("destinations",destination);
        map.put("ak",ak);
        String json=HttpClientUtil.doGet("https://api.map.baidu.com/routematrix/v2/driving", map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if(jsonObject.getIntValue("status")!=0){
            throw new OrderBusinessException("路线信息计算错误");
        }
        JSONArray resultArray = jsonObject.getJSONArray("result");
        JSONObject firstElement = resultArray.getJSONObject(0);
        JSONObject distanceObject = firstElement.getJSONObject("distance");
        String text = distanceObject.getString("text");
        String numberStr;
        if(text.endsWith("米")) {
            numberStr = text.substring(0, text.length() - 1);
            return Double.parseDouble(numberStr);
        }else {
            numberStr = text.substring(0, text.length() - 2);
            return Double.parseDouble(numberStr)*1000;
        }

        // 将截掉后的字符串转换为整数


    }
}
