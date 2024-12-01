package com.sky.controller.admin;



import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;



    @GetMapping("/page")
    public Result<PageResult> pageSearch(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐{}", setmealPageQueryDTO);
        PageResult pageResult= setmealService.pageSearch(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> searchSetmealById(@PathVariable Long id) {
        log.info("查询{}号套餐",id);
        SetmealVO setmealVo=setmealService.searchSetmealById(id);
        return Result.success(setmealVo);
    }


    @PostMapping("/status/{status}")
    public Result startOrStopSetmeal(@PathVariable Integer status,@RequestParam Long id) {
        log.info("起售或停售{}号套餐",id);
        setmealService.startOrStopSetmeal(status,id);
        return Result.success();
    }
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @DeleteMapping()
    public Result deleteSetmeal(@RequestParam ArrayList<Long> ids) {
        log.info("删除{}号套餐",ids);
        setmealService.delete(ids);
        return Result.success();
    }
}
