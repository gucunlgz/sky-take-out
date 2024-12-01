package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pageSearch(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> searchByType(Integer type);

    void startOrStop(Integer status, long id);

    void updateCategory(CategoryDTO categoryDTO);

    void addCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}
