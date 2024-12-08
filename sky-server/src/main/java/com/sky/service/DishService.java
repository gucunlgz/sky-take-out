package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.ArrayList;
import java.util.List;

public interface DishService {
    PageResult pageSearch(DishPageQueryDTO dishPageQueryDTO);

    List<Dish> searchByCategoryId(Long categoryId);


    DishVO searchById(Long id);

    void startOrStop(Integer status, Long id);

    void delete(ArrayList<Long> ids);

    void addDish(DishDTO dishDTO);

    void updateDish(DishDTO dishDTO);

    List<DishVO> getDishVOBycategoryId(Dish dish);
}
