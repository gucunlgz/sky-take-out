package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishFlavorMapper {

    @Select("select * from sky_take_out.dish_flavor where dish_id=#{dishId}")
    public List<DishFlavor> getDishFlavorsByDishId(Long dishId);

    void deleteByDishId(ArrayList<Long> ids);

    void insertFlavors(List<DishFlavor> flavors);


}
