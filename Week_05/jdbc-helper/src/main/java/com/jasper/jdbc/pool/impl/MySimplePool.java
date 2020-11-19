package com.jasper.jdbc.pool.impl;

import com.google.common.collect.Lists;
import com.jasper.jdbc.pool.DataSourcePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

/**
 * @author jasper
 */
@Component
@Slf4j
public class MySimplePool implements DataSourcePool {
    @Value("${jdbc.driver}")
    private String driverName;
    @Value("${jdbc.pool.size}")
    private int poolSize;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;

    /**
     * 数据库连接池
     */
    private final LinkedList<Connection> datasource = Lists.newLinkedList();

    @PostConstruct
    private void init() {
        log.info("init pool....");
        try {
            String driver = driverName;
            Class.forName(driver);
            log.info("load {} success", driverName);
            int poolSize = this.poolSize;
            for (int i = 0; i < poolSize; i++) {
                String url;
                String user;
                String password;
                url = this.url;
                user = this.user;
                password = this.password;
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    datasource.push(conn);
                } catch (Exception e) {
                    log.error("create pool failed", e);
                }
            }
        } catch (Exception e) {
            log.error("load db driver failed", e);
        }
    }

    @Override
    public synchronized Connection getConnection() {
        while (datasource.size() == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
        return datasource.poll();
    }

    @Override
    public void close(Connection connection) {
        if (connection != null) {
            datasource.push(connection);
        }
    }
}
