package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取营业额统计信息
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList=getTimeList(startDate,endDate);
        String dateString = StringUtils.join(dateList,",");
        List<Double> dateAmount=new ArrayList<>();
        for (LocalDate date:dateList){
            LocalDateTime begin=date.atStartOfDay();
            LocalDateTime end=begin.plusDays(1);
            Map<String,Object> map=new HashMap<>();
            map.put("begin",begin);
            map.put("end",end);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover==null?0:turnover;
            dateAmount.add(turnover);
        }
        String amountString = StringUtils.join(dateAmount,",");
        return new TurnoverReportVO(dateString,amountString);
    }

    /**
     * 统计用户信息
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList=getTimeList(startDate,endDate);
        String dateString = StringUtils.join(dateList,",");

        //获取每日添加用户数量及总用户数量
        List<Integer> registerUser=new ArrayList<>();
        List<Integer> sumUser=new ArrayList<>();
        for (LocalDate date:dateList){
            LocalDateTime begin=date.atStartOfDay();
            LocalDateTime end=begin.plusDays(1);
            Map<String,Object> map=new HashMap<>();
            map.put("begin",begin);
            map.put("end",end);
            Integer count=userMapper.countUserByTime(map);
            map.remove("begin");
            Integer sum=userMapper.countUserByTime(map);
            sumUser.add(sum);
            registerUser.add(count);
        }
        String amountString = StringUtils.join(registerUser,",");
        String sumAmountString = StringUtils.join(sumUser,",");
        //获取没日所有用户数量
        return new UserReportVO(dateString,amountString,sumAmountString);
    }

    /**
     * 统计订单信息
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = getTimeList(startDate, endDate);
        List<Integer> orderCountList=new ArrayList<>();
        List<Integer> validOrderCountList=new ArrayList<>();
        Integer totalOrderCount=0;
        Integer totalValidOrderCount=0;
        for (LocalDate date:dateList){
            LocalDateTime begin=date.atStartOfDay();
            LocalDateTime end=begin.plusDays(1);
            Map<String,Object> map=new HashMap<>();
            map.put("begin",begin);
            map.put("end",end);
            Integer orderCount =orderMapper.getOrdersSumByStatusAndTime(map);
            orderCount=orderCount==null?0:orderCount;
            totalOrderCount+=orderCount;
            orderCountList.add(orderCount);
            map.put("status",Orders.COMPLETED);
            Integer validOrderCount=orderMapper.getOrdersSumByStatusAndTime(map);
            validOrderCount=validOrderCount==null?0:validOrderCount;
            totalValidOrderCount+=validOrderCount;
            validOrderCountList.add(validOrderCount);
        }
        String dateString = StringUtils.join(dateList,",");
        String orderSting = StringUtils.join(orderCountList,",");
        String validOrderSting = StringUtils.join(validOrderCountList,",");
        Double rate=totalOrderCount==0?0:1.0*totalValidOrderCount/totalOrderCount;
        return new OrderReportVO(dateString,orderSting,validOrderSting,totalOrderCount,totalValidOrderCount,rate);

    }

    /**
     * 统计销量top10
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate startDate, LocalDate endDate) {
        Map<String,Object> map=new HashMap<>();
        map.put("start",startDate);
        map.put("end",endDate);
        map.put("status",Orders.COMPLETED);
        List<GoodsSalesDTO>  goodsSalesDTOS=orderMapper.getOrdersTop10ByTime(map);
        List<String> listName = goodsSalesDTOS.stream().map(GoodsSalesDTO::getName).toList();
        String joinName = StringUtils.join(listName, ",");
        List<Integer> listNumber=goodsSalesDTOS.stream().map(GoodsSalesDTO::getNumber).toList();
        String numberString = StringUtils.join(listNumber,",");
        return new SalesTop10ReportVO(joinName,numberString);
    }

    private List<LocalDate> getTimeList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList=new ArrayList<>();
        if(startDate.equals(endDate)){
            dateList.add(startDate);
        }
        for (LocalDate date=startDate;!date.equals(endDate);date=date.plusDays(1)) {
            dateList.add(date);
        }
        return dateList;
    }
}
