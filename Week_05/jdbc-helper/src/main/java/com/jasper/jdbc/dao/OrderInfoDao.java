package com.jasper.jdbc.dao;

import com.jasper.jdbc.domain.OrderInfo;
import com.jasper.jdbc.domain.Student;

import java.util.List;

/**
 * @author jasper
 */
public interface OrderInfoDao {
    void saveByStatement(OrderInfo orderInfo);
    void saveByPreStatement(List<Object[]> paramsList);

}
