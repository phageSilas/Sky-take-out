package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    public OrderTask(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 处理超时未支付的订单
     * 查询下单时间为 15 分钟前且状态为待支付的订单，将其状态更新为已取消
     * 
     * @return 无返回值
     */
    @Scheduled(cron = "0 * * * * ? ")//每分钟触发一次
    public void processTimeoutOrder(){
        try {
            log.info("处理超时订单:{}", LocalDateTime.now());

            // 计算 15 分钟前的时间点
            LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

            // 查询 15 分钟前状态为待支付的订单列表
            List<Orders> ordersList= orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

            // 遍历并更新符合条件的订单状态为已取消
            if(ordersList !=null&& ordersList.size()>0){
                for(Orders orders : ordersList){
                    orders.setStatus(Orders.CANCELLED);
                    orders.setCancelReason("订单超时，自动取消");
                    orders.setCancelTime(LocalDateTime.now());
                    orderMapper.update(orders);
                }
            }
            
            log.info("超时订单处理完成，处理数量：{}", ordersList != null ? ordersList.size() : 0);
        } catch (Exception e) {
            log.error("处理超时订单失败", e);
        }
    }


    @Scheduled(cron ="0 0 1 * * ?")//每天凌晨一点触发
    public void processDeliveryOrder(){
        try {
            log.info("处理处于派送中的订单:{}",LocalDateTime.now());

            LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

            List<Orders> ordersList= orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

            if(ordersList !=null&& ordersList.size()>0){
                for(Orders orders : ordersList){
                    orders.setStatus(Orders.COMPLETED);
                    orders.setCancelReason("派送超时，自动完成订单");
                    orders.setCancelTime(LocalDateTime.now());
                    orderMapper.update(orders);
                }
            }
            
            log.info("派送订单处理完成，处理数量：{}", ordersList != null ? ordersList.size() : 0);
        } catch (Exception e) {
            log.error("处理派送订单失败", e);
        }
    }
}
