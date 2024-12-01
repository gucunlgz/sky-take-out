package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.ArrayList;

public interface SetmealService {
    PageResult pageSearch(SetmealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetmealDTO setmealDTO);

    SetmealVO searchSetmealById(Long id);

    void startOrStopSetmeal(Integer status, Long id);

    void update(SetmealDTO setmealDTO);

    void delete(ArrayList<Long> ids);
}
