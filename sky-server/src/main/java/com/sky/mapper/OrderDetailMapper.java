package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细数据
     * @param orderDetailList
     */
    void inserts(List<OrderDetail> orderDetailList);

    /**
     * 更据OrderId查询订单菜品详情
     * @param orderId
     * @return
     */
    List<OrderDetail> getByOrderId(Long orderId);
}
