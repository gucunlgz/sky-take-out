package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前商品是否在购物车内
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        //是，对数量进行修改
        if(list!=null&& !list.isEmpty()){
            ShoppingCart cart=list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        }
        //否，插入新的数据
        else {
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId!=null){
                DishVO dishVO = dishMapper.searchById(dishId);
                shoppingCart.setImage(dishVO.getImage());
                shoppingCart.setName(dishVO.getName());
                shoppingCart.setAmount(dishVO.getPrice());

            }
            else {
                Long setmealId = shoppingCartDTO.getSetmealId();
                SetmealVO setmeal = setmealMapper.searchSetmealById(setmealId);
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart=ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
       return shoppingCartMapper.list(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanByUserId(userId);
    }

    /**
     * 删除购物车某个套餐或菜品
     * @param shoppingCartDTO
     */
    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //判断当前菜品数量是否超过1
        if(list!=null&& !list.isEmpty()){
            ShoppingCart cart=list.get(0);
            Integer number = cart.getNumber();
            cart.setCreateTime(LocalDateTime.now());
            if(number>1){
                cart.setNumber(number-1);
                shoppingCartMapper.updateNumberById(cart);
            }
            else {
                shoppingCartMapper.deleteById(cart);
            }
        }


    }


}
