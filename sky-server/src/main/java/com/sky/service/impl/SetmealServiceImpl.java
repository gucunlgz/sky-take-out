package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;
    @Override
    public PageResult pageSearch(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> result=setmealMapper.pageSearch(setmealPageQueryDTO);
        return new PageResult(result.getTotal(),result.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insertSermeal(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });

        setmealDishMapper.insert(setmealDishes);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SetmealVO searchSetmealById(Long id) {
       SetmealVO setmealVo= setmealMapper.searchSetmealById(id);
       List<SetmealDish> setmealDishes=setmealDishMapper.searchSetmealDishBySetmealId(id);
       setmealVo.setSetmealDishes(setmealDishes);
       return setmealVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startOrStopSetmeal(Integer status, Long id) {
        //检查当前套餐中是否有停售的菜品
        if(Objects.equals(status,StatusConstant.ENABLE)){
         List<Dish> dishes=dishMapper.getStatusBySetmealId(id);
         if(dishes!=null&& !dishes.isEmpty()){
             dishes.forEach(dish -> {
                 if(Objects.equals(dish.getStatus(),StatusConstant.DISABLE)){
                     throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                 }
             });
         }
        }

        Setmeal setmeal=Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SetmealDTO setmealDTO) {
        //修改套餐数据
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        //修改套餐中菜品数据
        Long id=setmeal.getId();
        setmealDishMapper.deleteBySetmealId(id);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes!=null&& !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
        }
        setmealDishMapper.insert(setmealDishes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ArrayList<Long> ids) {
        for(Long id:ids){
            SetmealVO setmealVO = setmealMapper.searchSetmealById(id);
            if(Objects.equals(setmealVO.getStatus(),StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        ids.forEach(id->{
            setmealDishMapper.deleteBySetmealId(id);
            setmealMapper.deleteById(id);
        });
    }
}
