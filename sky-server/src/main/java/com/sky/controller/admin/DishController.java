package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private  DishService dishService;

    @GetMapping("/page")
    public Result<PageResult> pageSearch(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询{}", dishPageQueryDTO);
       PageResult dishPage= dishService.pageSearch(dishPageQueryDTO);
       return  Result.success(dishPage);
    }

    @GetMapping("list")
    public Result<List<Dish>> searchByCategoryId(@RequestParam Long categoryId) {
        log.info("查询categoryId为{}的dish", categoryId);
        List<Dish> list =dishService.searchByCategoryId(categoryId);
        return Result.success(list);
    }
    @GetMapping("{id}")
    public Result<DishVO> searchById(@PathVariable Long id) {
        log.info("查询id为{}的dish",id);
        DishVO dishVO=dishService.searchById(id);
        return Result.success(dishVO);
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id) {
        log.info("启用或禁用id为：{}的菜",id);
        dishService.startOrStop(status,id);
        return Result.success();
    }
    @DeleteMapping()
    public Result deleteDish(@RequestParam ArrayList<Long> ids) {
        log.info("删除id为{}的菜品及口味",ids);
        dishService.delete(ids);
        return Result.success();
    }

    @PostMapping()
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品:{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @PutMapping
    public Result updateDish(@RequestBody  DishDTO dishDTO) {
        log.info("修改菜品：{}",dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
}
}
