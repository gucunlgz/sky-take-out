package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@Slf4j
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    private DishService dishService;


    @GetMapping("/list")
    public Result<List<DishVO>> getDishVOByCategoryId(@RequestParam Long categoryId) {
        log.info("查询分类为{}下的菜品", categoryId);
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        List<DishVO> dishVOS=dishService.getDishVOBycategoryId(dish);
        return Result.success(dishVOS);
    }

}
