package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询用户历史订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    OrderVO getOrderById(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancelOrder(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * admin端分页条件查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult pageSearchOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询各种状态订单数量
     * @return
     */
    OrderStatisticsVO getOrderStatistics();

    /**
     * 根据订单号查询订单详情
     * @param id
     * @return
     */
    OrderVO getOrderVO(Long id);

    /**
     * 接单
     * @param ordersConfirmDTO
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 派送订单
     * @param id
     */
    void deliveryOrder(Long id);

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * admin端取消订单
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 完成订单
     * @param id
     */
    void completeOrder(Long id);

    /**
     * 催单
     * @param id
     */
    void remind(Long id);
}
