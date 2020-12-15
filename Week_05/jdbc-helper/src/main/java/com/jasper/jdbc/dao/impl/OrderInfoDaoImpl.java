package com.jasper.jdbc.dao.impl;

import com.jasper.jdbc.dao.OrderInfoDao;
import com.jasper.jdbc.domain.OrderInfo;
import com.jasper.jdbc.helper.JdbcHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderInfoDaoImpl implements OrderInfoDao {
    private final String tableName = "t_order_info";
    @Autowired
    private JdbcHelper jdbcHelper;

    @Override
    public void saveByStatement(OrderInfo orderInfo) {
        BigDecimal totalAmount = orderInfo.getTotalAmount();
        long userId = orderInfo.getUserId();
        int orderStatus = orderInfo.getOrderStatus();
        String paymentWay = orderInfo.getPaymentWay();
        String outTradeNo = orderInfo.getOutTradeNo();
        Timestamp createTime = orderInfo.getCreateTime();
        Timestamp payTime = orderInfo.getPayTime();
        Timestamp deliveryTime = orderInfo.getDeliveryTime();
        Timestamp dealCompletionTime = orderInfo.getDealCompletionTime();
        String sql = String.format("insert into %s(total_amount,user_id,order_status,payment_way," +
                        "out_trade_no,create_time,pay_time,delivery_time,deal_completion_time) " +
                        "values(%s,%s,%s,'%s','%s','%s','%s','%s','%s')", tableName, totalAmount, userId, orderStatus,
                paymentWay, outTradeNo, createTime, payTime, deliveryTime, dealCompletionTime);
        jdbcHelper.execute(sql);
    }

    @Override
    public void saveByPreStatement(List<Object[]> paramsList) {


        String sql = String.format("insert into %s(total_amount,user_id,order_status,payment_way," +
                        "out_trade_no,create_time,pay_time,delivery_time,deal_completion_time) " +
                        "values(?,?,?,?,?,?,?,?,?)",tableName);
        jdbcHelper.executeBatch(sql,paramsList);
    }
}
