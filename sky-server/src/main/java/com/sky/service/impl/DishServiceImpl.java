package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public PageResult pageSearch(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageSearch(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Dish> searchByCategoryId(Long categoryId) {
       return dishMapper.searchByCategoryId(categoryId);
    }

    @Override
    public DishVO searchById(Long id) {
        DishVO dishVO = dishMapper.searchById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.getDishFlavorsByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startOrStop(Integer status, Long id) {
        Long categoryId=dishMapper.getCategoryIdById(id);
        redisTemplate.delete("dish_"+categoryId);
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ArrayList<Long> ids) {
        //判断当前菜品是否在售
        for(Long id:ids){
            DishVO dishVO = dishMapper.searchById(id);
            if(Objects.equals(dishVO.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否在套餐中
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&& !setmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        //删除口味表中数据
        dishFlavorMapper.deleteByDishId(ids);

        //删除菜品表中数据
        dishMapper.deleteById(ids);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        //清除redis缓存
        redisTemplate.delete("dish_"+dishDTO.getCategoryId());

        //保存菜品数据
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //保存口味数据
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&& !flavors.isEmpty()){
            flavors.forEach(flavor->{
                flavor.setDishId(id);
            });

            dishFlavorMapper.insertFlavors(flavors);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDish(DishDTO dishDTO) {
        Long categoryId = dishMapper.getCategoryIdById(dishDTO.getId());

        //清除redis缓存
        redisTemplate.delete("dish_"+categoryId);
        redisTemplate.delete("dish_"+dishDTO.getCategoryId());

        //修改菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //修改口味数据
        Long id = dish.getId();
        ArrayList<Long> arrayList=new ArrayList<>();
        arrayList.add(id);
        dishFlavorMapper.deleteByDishId(arrayList);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&& !flavors.isEmpty()){
            flavors.forEach(flavor->{
                flavor.setDishId(id);
            });

            dishFlavorMapper.insertFlavors(flavors);
        }
    }


    //用户端根据categoryId(分类id)查询菜品，redis
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DishVO> getDishVOBycategoryId(Dish dish) {
        //redis查询
        String key="dish_"+dish.getCategoryId();
        List<DishVO> dishVOS = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(dishVOS!=null&&!dishVOS.isEmpty()){
            return dishVOS;
        }

        dishVOS = dishMapper.selectByCategoryId(dish);

        for(DishVO d:dishVOS){
            List<DishFlavor> dishFlavors = dishFlavorMapper.getDishFlavorsByDishId(d.getId());
            d.setFlavors(dishFlavors);
        }
        redisTemplate.opsForValue().set(key,dishVOS);
        return dishVOS;
    }

}

