package com.jitag.sharding.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author jasper
 */
@Data
public class OrderInfo {
    private long id;
    private long orderId;
    private BigDecimal totalAmount;
    private long userId;
    private int orderStatus;
    private String paymentWay;
    private String outTradeNo;
    private Timestamp createTime;
    private Timestamp payTime;
    private Timestamp deliveryTime;
    private Timestamp dealCompletionTime;
}