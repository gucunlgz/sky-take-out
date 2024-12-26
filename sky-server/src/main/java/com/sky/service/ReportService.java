package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 获取营业额统计信息
     * @param startDate
     * @param endDate
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 统计用户信息
     * @param startDate
     * @param endDate
     * @return
     */
    UserReportVO getUserStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 统计订单信息
     * @param startDate
     * @param endDate
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 统计销量top10
     * @param startDate
     * @param endDate
     * @return
     */
    SalesTop10ReportVO getSalesTop10Statistics(LocalDate startDate, LocalDate endDate);
}
