package com.jasper.jdbc.pool.impl;

import com.jasper.jdbc.pool.DataSourcePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jasper
 */
@Component
@Slf4j
public class HikariPool implements DataSourcePool {
    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("require connection failed", e);
        }
        return connection;
    }

    @Override
    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                log.error("", throwables);
            }
        }
    }
}
