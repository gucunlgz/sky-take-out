package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@Slf4j
@RequestMapping("/user/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/list")
    public Result<List<Setmeal>> searchSetmealById(Long categoryId) {
        log.info("查询分类为{}的套餐", categoryId);
        Setmeal setmeal= Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        List<Setmeal> setmeals= setmealService.searchSetmealBycategoryId(setmeal);
       return Result.success(setmeals);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> searchDishBySetmealId(@PathVariable Long id) {
        log.info("查询id为{}套餐包含的菜品",id);
        List<DishItemVO> dishItemVOS = setmealService.searchDishItemBySetmealId(id);
        return Result.success(dishItemVOS);
    }

}
