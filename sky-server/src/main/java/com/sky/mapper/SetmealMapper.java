package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    Page<SetmealVO> pageSearch(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select count(*) from sky_take_out.setmeal where category_id=#{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insertSermeal(Setmeal setmeal);


    SetmealVO searchSetmealById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Delete("delete  from sky_take_out.setmeal where id=#{id}")
    void deleteById(Long id);


    List<Setmeal> searchSetmealBycategoryId(Setmeal setmeal);

    List<DishItemVO> searchDishItemBySetmealId(Long id);

    @Select("select sky_take_out.setmeal.category_id from sky_take_out.setmeal where id=#{id}")
    Long getCategoryIdById(Long id);

    /**
     * 根据订单状态查询订单数量
     * @param status
     * @return
     */
    Integer countByStatus(Integer status);
}
