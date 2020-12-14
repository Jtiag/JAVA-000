package com.train.jitag.service;

import com.train.jitag.dao.OrderInfoDao;
import com.train.jitag.domain.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author jasper
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderInfoDao orderInfoDao;

    public OrderInfo findUserById(int id) {
        OrderInfo user = null;
        try {
            user = orderInfoDao.findOrderInfoById(id);
        } catch (Exception e) {
            log.error("", e);
        }
        return user;
    }

    public void saveOrder(OrderInfo orderInfo) {
        try {
            orderInfoDao.save(orderInfo);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void updateOrderById(int id, int orderStatus) {
        try {
            orderInfoDao.updateMobileById(id, orderStatus);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void saveOrders() {
        int mount = 17;
//        int mount = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < mount; i++) {
            OrderInfo orderInfo = new OrderInfo();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            orderInfo.setCreateTime(timestamp);
            orderInfo.setDealCompletionTime(timestamp);
            orderInfo.setDeliveryTime(timestamp);
            orderInfo.setOrderStatus(0);
            orderInfo.setOutTradeNo("" + i);
            orderInfo.setPaymentWay("支付宝");
            orderInfo.setPayTime(timestamp);
            orderInfo.setTotalAmount(new BigDecimal(i));
            orderInfo.setUserId(0);
            orderInfo.setId(i);
            saveOrder(orderInfo);
        }
        log.info("statement insert cost= [{}]", (System.currentTimeMillis() - start));
    }
}
