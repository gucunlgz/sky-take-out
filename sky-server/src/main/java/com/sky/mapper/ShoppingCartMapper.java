package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据userId,dishId,setmealId,dishFlavor动态查询购物车数据
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 添加菜品或套餐数量
     * @param cart
     */
    @Update("update  sky_take_out.shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(ShoppingCart cart);

    /**
     * 添加购物车数据
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 更据用户id清空购物车
     * @param userId
     */
    void cleanByUserId(Long userId);

    /**
     * 更据id删除购物车数据
     * @param cart
     */
    void deleteById(ShoppingCart cart);

    /**
     * 批量插入购物车数据
     * @param shoppingCarts
     */
    void insertBatch(List<ShoppingCart> shoppingCarts);
}
