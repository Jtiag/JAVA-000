package com.train.jitag.dao;


import com.train.jitag.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @author jasper
 */
@Mapper
public interface OrderInfoDao {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Select("select * from t_order_info where id=#{id}")
    OrderInfo findOrderInfoById(int id);

    /**
     * 插入一个用户
     *
     * @param orderInfo
     */
    @Insert("insert into t_order_info(order_id,total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time," +
            "delivery_time,deal_completion_time) values(#{orderId},#{totalAmount},#{userId},#{orderStatus},#{paymentWay}," +
            "#{outTradeNo},#{createTime},#{payTime},#{deliveryTime},#{dealCompletionTime})")
    void save(OrderInfo orderInfo);

    /**
     * 根据id更新用户电话
     *
     * @param id
     * @param orderStatus
     */
    @Update("update t_order_info set order_status=#{orderStatus} where id=#{id}")
    void updateMobileById(int id, int orderStatus);

    /**
     * 根据id删除用户
     *
     * @param id
     */
    @Delete("delete from t_order_info where id=#{id}")
    void deleteById(int id);
}
