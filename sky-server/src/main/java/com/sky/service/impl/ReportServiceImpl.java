package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //1. 创建日期列表, 获取日期
        List<LocalDate> dateList = new ArrayList<>();

        while (!begin.isAfter(end)) {// 等价于begin <= end
            begin = begin.plusDays(1);// 日期加1
            dateList.add(begin);//把所有日期加入dateList
        }

        String dateStr = StringUtils.join(dateList, "','");// 将日期列表转换为字符串，格式为"2023-01-01','2023-01-02"
        log.info("营业额日期: {}", dateStr);

        //2. 查询1中每天对应的已完成订单的营业额数据
        List<Double> turnoverList = new ArrayList<>();
        for(LocalDate date : dateList) {
            log.info("查询{}的营业额数据", date);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 当天00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 当天23:59:59

            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.selectTurnoverByDate(map); // 查询营业额数据
            turnoverList.add(turnover == null ? 0.0 : turnover);// 添加营业额数据, 如果营业额为null，则设为0.0


        }
        String format = StringUtils.join(turnoverList, ",");// 将营业额列表转换为字符串，格式为"0.0,0.0,0.0"
        log.info("营业额数据: {}", format);



        return TurnoverReportVO.builder()
                .dateList(dateStr)
                .turnoverList(format)
                .build();//返回营业额统计VO对象
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {

        //1. 创建日期列表, 获取日期
        List<LocalDate> dateList = new ArrayList<>();

        while (!begin.isAfter(end)) {// 等价于begin <= end
            begin = begin.plusDays(1);// 日期加1
            dateList.add(begin);//把所有日期加入dateList
        }

        String dateStr = StringUtils.join(dateList, "','");// 将日期列表转换为字符串，格式为"2023-01-01','2023-01-02"
        log.info("用户数量日期: {}", dateStr);

        //2. 查询1中每天对应的总用户数量和新增用户数量
        List<Integer> totalUserList = new ArrayList<>();// 总用户数量列表
        List<Integer> newUserList = new ArrayList<>();// 新增用户数量列表

        for(LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 当天00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 当天23:59:59

            Map<String, Object> map = new HashMap<>();

            map.put("endTime", endTime);

            Integer totalUser = userMapper.countUserByDate(map);
            totalUserList.add(totalUser == null ? 0 : totalUser);
            log.info("{}总用户数量:{} ",endTime, totalUser);
            log.info("--------");

            map.put("beginTime", beginTime);
            Integer newUser = userMapper.countUserByDate(map);
            newUserList.add(newUser == null ? 0 : newUser);
            log.info("{}新增用户数量:{}", beginTime, newUser);
            log.info("--------");
        }
        String totalU = StringUtils.join(totalUserList, ",");// 将总用户列表转换为字符串，格式为"0,0,0"
        log.info("总用户数据: {}", totalU);
        String newU = StringUtils.join(newUserList, ",");// 将新增用户列表转换为字符串，格式为"0,0,0"
        log.info("新增用户数据: {}", newU);




        return UserReportVO.builder()
                .dateList(dateStr)
                .totalUserList(totalU)
                .newUserList(newU)
                .build();//返回用户数量统计VO对象
    }
}
