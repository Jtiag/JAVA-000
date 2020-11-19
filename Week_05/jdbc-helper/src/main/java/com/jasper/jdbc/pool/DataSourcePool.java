package com.jasper.jdbc.pool;


import java.sql.Connection;

/**
 * @author jasper
 */
public interface DataSourcePool {
    Connection getConnection();

    void close(Connection connection);
}
