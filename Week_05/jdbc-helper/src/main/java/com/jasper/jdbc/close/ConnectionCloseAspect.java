//package com.jasper.jdbc.close;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * @author jasper
// */
//@Aspect
//@Slf4j
//public class ConnectionCloseAspect {
//
//    @Pointcut(value = "execution(* com.jasper.jdbc.helper.*.JdbcHelper.execute*(..))")
//    public void point() {
//    }
//
//    @AfterReturning(value = "point()",returning = "connection")
//    public void close(Connection connection) {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException throwables) {
//                log.error("close failed", throwables);
//            }
//        }
//    }
//}
