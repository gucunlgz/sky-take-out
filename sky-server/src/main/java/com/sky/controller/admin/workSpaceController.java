package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.service.workSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/admin/workspace")
public class workSpaceController {

    @Autowired
    private workSpaceService workspaceService;

    /**
     * 获取今日运营数据
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        log.info("获取今日{}运营数据", LocalDateTime.now());
        BusinessDataVO businessDataVO =workspaceService.getBusinessDate();
        return Result.success(businessDataVO);
    }

    /**
     * 查询已启用套餐，停用套餐数量
     * @return
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmealOverView() {
        log.info("查询已启用套餐，停用套餐数量");
        SetmealOverViewVO setmealOverViewVO=workspaceService.getsetmealOverViewVO();
        return Result.success(setmealOverViewVO);
    }

    /**
     * 查询已启用菜品，停用菜品的数量
     * @return
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getDishOverViewVO() {
        log.info("查询已启用菜品，停用菜品的数量");
        DishOverViewVO dishOverViewVO=workspaceService.getDishOverViewVO();
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询订单--已取消数量,已取消数量,待派送数量，待接单数量
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOrderOverViewVO() {
        log.info("查询订单--已取消数量,已取消数量,待派送数量，待接单数量");
        OrderOverViewVO orderOverViewVO=workspaceService.getOrderOverViewVO();
        return Result.success(orderOverViewVO);
    }

}
