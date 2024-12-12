package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 条件分页查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> pageSearchOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("条件分页查询用户{}订单", BaseContext.getCurrentId());
        PageResult pageResult=orderService.pageSearchOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询各种状态订单数量
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> getOrderStatistics(){
        log.info("查询各种状态订单数量");
        OrderStatisticsVO orderStatistics=orderService.getOrderStatistics();
        return Result.success(orderStatistics);
    }

    /**
     * 根据订单号查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    private Result<OrderVO> getOrderVO(@PathVariable Long id){
        log.info("查询{}订单详情",id);
        OrderVO orderVO=orderService.getOrderVO(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接{}号订单",ordersConfirmDTO.getId());
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        log.info("派送{}号订单",id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒绝{}号单",ordersRejectionDTO.getId());
        orderService.rejectionOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
       log.info("取消{}号订单",ordersCancelDTO.getId());
       orderService.cancelOrder(ordersCancelDTO);
       return Result.success();
    }

    /**
     * 订单完成
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        log.info("订单{}完成",id);
        orderService.completeOrder(id);
        return Result.success();
    }
 }

