package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishMapper {

    Page<DishVO> pageSearch(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from sky_take_out.dish where category_id=#{categoryId}")
    List<Dish> searchByCategoryId(Long categoryId);


    DishVO searchById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    void deleteById(ArrayList<Long> ids);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
}
