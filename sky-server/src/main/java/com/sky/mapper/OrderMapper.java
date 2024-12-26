package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询历史订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageSearchHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    Orders getById(Long id);

    /**
     * 统计某种状态订单数量
     * @param status
     * @return
     */
    Integer countStatus(Integer status);

    /**
     * 查询超时订单
     * @param status
     * @param time
     * @return
     */
    List<Orders> getTimeoutOrders(Integer status, LocalDateTime time);

    /**
     * 根据订单状态获取订单
     * @param status
     * @return
     */
    List<Orders> getOrdersByStatus(Integer status);

    /**
     * 根据时间，订单状态获取订单数量
     * @param status
     * @param time
     * @return
     */
    List<Orders> getOrdersByStatusAndTime(Integer status, LocalDateTime time);

    /**
     * 根据时间，订单状态获取总销售金额
     * @param map
     * @return
     */
    Double sumByMap(Map<String, Object> map);


    /**
     * 根据时间订单状态获取订单数量
     * @param map
     * @return
     */
    Integer getOrdersSumByStatusAndTime(Map<String, Object> map);

    /**
     * 统计指定时间内销量前top10
     * @return
     */
    List<GoodsSalesDTO> getOrdersTop10ByTime(Map<String, Object> map);
}
