package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加{}到购物车", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();

    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查询用户{}的购物车", BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts=shoppingCartService.list();
        return Result.success(shoppingCarts);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空用户{}购物车", BaseContext.getCurrentId());
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 删除购物车某个菜品或套餐
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除{}号用户下的{}", BaseContext.getCurrentId(), shoppingCartDTO.getDishId()==null?shoppingCartDTO.getSetmealId()+"号套餐":shoppingCartDTO.getDishId()+"菜品");
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }

}
