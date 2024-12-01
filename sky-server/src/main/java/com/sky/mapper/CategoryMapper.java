package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    Page<Category> pageSearch(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> searchByType(Integer type);
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    @AutoFill(value = OperationType.INSERT)
    @Insert("INSERT INTO sky_take_out.category" +
            "(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addCategory(Category category);

    @Delete("delete from sky_take_out.category where id=#{id}")
    void delete(Long id);
}
