package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<PageResult> pageSearch(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageSearch(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    @GetMapping("/list")
    public Result<List> searchByType(@RequestParam Integer type) {
        log.info("查询类型为{}的分类",type);
        List<Category> list=categoryService.searchByType(type);
        return Result.success(list);
    }
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id) {
        log.info("启用或禁用{}号分类",id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("添加分类{}",categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    public Result deleteCategory(@RequestParam Long id) {
        log.info("删除{}号category",id);
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
