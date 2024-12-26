package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;


    /**
     * 获取营业额统计信息
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@RequestParam("begin") LocalDate startDate,@RequestParam("end") LocalDate endDate) {
        log.info("获取营业额统计信息{}-{}",startDate,endDate);
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(startDate,endDate);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@RequestParam("begin") LocalDate startDate,@RequestParam("end") LocalDate endDate) {
        log.info("统计用户信息{}-{}",startDate,endDate);
        UserReportVO userReportVO = reportService.getUserStatistics(startDate,endDate);
        return Result.success(userReportVO);
    }

    /**
     * 统计订单信息
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatistics(@RequestParam("begin") LocalDate startDate,@RequestParam("end") LocalDate endDate) {
        log.info("统计订单信息{}-{}",startDate,endDate);
        OrderReportVO orderReportVO=reportService.getOrderStatistics(startDate,endDate);
        return Result.success(orderReportVO);
    }

    /**
     * 统计销量top10
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10Statistics(@RequestParam("begin") LocalDate startDate,@RequestParam("end") LocalDate endDate) {
        log.info("统计在{}-{}时间段内销量top10",startDate,endDate);
        SalesTop10ReportVO salesTop10ReportVO= reportService.getSalesTop10Statistics(startDate,endDate);
        return Result.success(salesTop10ReportVO);
    }


}
