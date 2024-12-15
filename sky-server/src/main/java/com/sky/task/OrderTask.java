package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class OrderTask {
    @Autowired
   private OrderMapper orderMapper;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void processTimeoutTask() {
        log.info("处理超时订单:{}",LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        Integer status=Orders.PENDING_PAYMENT;
        List<Orders> orders=orderMapper.getTimeoutOrders(status,time);

        if(orders!=null&& !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时，自动取消");
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder(){
        log.info("处理一直配送中订单{}",LocalDateTime.now());
        Integer status=Orders.DELIVERY_IN_PROGRESS;
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders=orderMapper.getOrdersByStatusAndTime(status,time);
        if(orders!=null&& !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

}
