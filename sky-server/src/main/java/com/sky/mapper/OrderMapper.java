package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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

    List<Orders> getOrdersByStatus(Integer status);

    List<Orders> getOrdersByStatusAndTime(Integer status, LocalDateTime time);
}
