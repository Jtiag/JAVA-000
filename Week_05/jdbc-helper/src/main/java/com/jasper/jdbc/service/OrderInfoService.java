package com.jasper.jdbc.service;

import com.google.common.collect.Lists;
import com.jasper.jdbc.dao.OrderInfoDao;
import com.jasper.jdbc.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderInfoService {
    @Autowired
    private OrderInfoDao orderInfoDao;

    public void save1MillionByStatement() {
        int mount = 1000000;
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
            orderInfo.setUserId(i);
            orderInfo.setTotalAmount(new BigDecimal(i));
            orderInfoDao.saveByStatement(orderInfo);
        }
        System.out.println("statement insert oneMillion cost=" + (System.currentTimeMillis() - start));
    }

    public void save1MillionByPreStatement() {
        int oneMillion = 1000000;
        int batchSize = 50000;
        int cycleSize = oneMillion / batchSize;
        long start = System.currentTimeMillis();
        for (int i = 0; i < cycleSize; i++) {
            List<Object[]> paramsList = Lists.newArrayListWithCapacity(batchSize);
            for (int j = 0; j < batchSize; j += 1) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Object[] objects = new Object[9];
                objects[0] = new BigDecimal(j);
                objects[1] = j;
                objects[2] = 1;
                objects[3] = "微信";
                objects[4] = j;
                objects[5] = timestamp;
                objects[6] = timestamp;
                objects[7] = timestamp;
                objects[8] = timestamp;
                paramsList.add(objects);
            }
            System.out.println("current cycle = " + i);
            orderInfoDao.saveByPreStatement(paramsList);
        }
        System.out.println("preStatement with hikari pool insert oneMillion cost=" + (System.currentTimeMillis() - start));
    }
}
