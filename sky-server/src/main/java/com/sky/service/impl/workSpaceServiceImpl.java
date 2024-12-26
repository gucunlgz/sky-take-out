package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.workSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class workSpaceServiceImpl implements workSpaceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;
    /**
     * 获取今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessDate() {
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = startTime.plusDays(1);
        Map<String,Object> map = new HashMap<>();
        map.put("begin",startTime);
        map.put("end",endTime);
        Integer orderCount=orderMapper.getOrdersSumByStatusAndTime(map);
        orderCount=orderCount==null?0:orderCount;
        Integer  newUsers=userMapper.countUserByTime(map);
        newUsers=newUsers==null?0:newUsers;
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map);
        turnover=turnover==null?0:turnover;
        Integer validOrderCount=orderMapper.getOrdersSumByStatusAndTime(map);
        validOrderCount=validOrderCount==null?0:validOrderCount;

        Double unitPrice=validOrderCount==0?0:turnover/validOrderCount;

        Double orderCompletionRate=orderCount==0?0:1.0*validOrderCount/orderCount;

        return new BusinessDataVO(turnover,validOrderCount,orderCompletionRate,unitPrice,newUsers);


    }

    /**
     * 获取启用停用套餐数量
     * @return
     */
    @Override
    public SetmealOverViewVO getsetmealOverViewVO() {
      Integer sold=setmealMapper.countByStatus(StatusConstant.ENABLE);
      Integer discontinued=setmealMapper.countByStatus(StatusConstant.DISABLE);
      return new SetmealOverViewVO(sold,discontinued);

    }

    /**
     * 获取停用启用菜品数量
     * @return
     */
    @Override
    public DishOverViewVO getDishOverViewVO() {
        Integer sold=dishMapper.countByStatus(StatusConstant.ENABLE);
        Integer discontinued=dishMapper.countByStatus(StatusConstant.DISABLE);
        return new DishOverViewVO(sold,discontinued);
    }

    /**
     * 查询订单--已取消数量,已取消数量,待派送数量，待接单数量
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverViewVO() {

        //待接单数量
        Integer waitingOrders=orderMapper.countStatus(Orders.TO_BE_CONFIRMED);

        //待派送数量
       Integer deliveredOrders=orderMapper.countStatus(Orders.CONFIRMED);

        //已完成数量
       Integer completedOrders=orderMapper.countStatus(Orders.COMPLETED);

        //已取消数量
       Integer cancelledOrders=orderMapper.countStatus(Orders.CANCELLED);

        //全部订单
        Integer allOrders=orderMapper.countStatus(null);


        return new OrderOverViewVO(waitingOrders,deliveredOrders,completedOrders,cancelledOrders,allOrders);
    }


}
