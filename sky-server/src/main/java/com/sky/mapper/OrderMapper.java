package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 修改订单状态
     * @param orderPayStatus
     * @param orderStatus
     * @param checkOutTime
     * @param orderNumber
     */
    @Select("update orders set pay_status = #{orderPayStatus}, status = #{orderStatus}, checkout_time = #{checkOutTime} where number = #{orderNumber}")
    void updateStatus(Integer orderPayStatus, Integer orderStatus, LocalDateTime checkOutTime, String orderNumber);

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单

 * 这是一个接口方法，用于通过订单ID获取订单信息
     * @param id 订单的唯一标识符，类型为Long
     * @return 返回一个Orders对象，包含订单的详细信息
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer toBeConfirmed);


    /**
     * 查询订单待支付已过多久时间
     *
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("time") LocalDateTime time);

    /**
     * 查询指定时间区间内的营业额数据
     *
     * @param map
     * @return
     */
    Double selectTurnoverByDate(Map<String, Object> map);

    /**
     * 查询指定时间区间内的订单总数和有效订单数
     *
     * @param map
     * @return
     */
    Integer countOrder(Map<String, Object> map);
}
