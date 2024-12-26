package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface workSpaceService {
    /**
     * 获取今日运营数据
     * @return
     */
    BusinessDataVO getBusinessDate();

    /**
     * 获取停用启用套餐数量
     * @return
     */
    SetmealOverViewVO getsetmealOverViewVO();

    /**
     * 获取停用启用菜品数量
     * @return
     */
    DishOverViewVO getDishOverViewVO();

    /**
     * 查询订单--已取消数量,已取消数量,待派送数量，待接单数量
     * @return
     */
    OrderOverViewVO getOrderOverViewVO();
}
