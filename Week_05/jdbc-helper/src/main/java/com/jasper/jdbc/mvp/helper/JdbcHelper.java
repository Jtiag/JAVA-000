package com.jasper.jdbc.mvp.helper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jasper
 */
@Slf4j
@Component
public class JdbcHelper {
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

    @PostConstruct
    private void init() {
        log.info("init JdbcHelper....");
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

    /**
     * 数据库连接池
     */
    private final LinkedList<Connection> datasource = Lists.newLinkedList();

    private JdbcHelper() {
    }

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

    /**
     * insert update query delete
     *
     * @param sql
     */
    public void execute(final String sql) {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = getConnection();

            statement = conn.createStatement();
            statement.execute(sql);
            log.info("[{}] execute success", sql);
        } catch (Exception e) {
            log.error("[{}] execute failed\n{}", sql, e);
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
            close(statement);
        }
    }

    /**
     * query
     *
     * @param sql
     */
    public void executeQuery(final String sql, QueryCallback queryCallback) {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();

            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            queryCallback.process(resultSet);
            log.info("[{}] execute success", sql);
        } catch (Exception e) {
            log.error("[{}] execute failed\n{}", sql, e);
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
            close(resultSet, statement);
        }
    }

    /**
     * 执行增删改SQL语句
     *
     * @param sql
     * @param params
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Object[] params) {
        int rtn = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rtn = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
            close(pstmt);
        }

        return rtn;
    }

    /**
     * 执行查询SQL语句
     *
     * @param sql
     * @param params
     * @param callback
     */
    public void executeQuery(String sql, Object[] params,
                             QueryCallback callback) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();

            callback.process(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
            close(rs, pstmt);
        }
    }

    private void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    log.error("close failed", e);
                }
            }
        }

    }

    public int[] executeBatch(String sql, List<Object[]> paramsList) {
        int[] rtn = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            if (paramsList != null && paramsList.size() > 0) {
                for (Object[] params : paramsList) {
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                    pstmt.addBatch();
                }
            }
            rtn = pstmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
            close(pstmt);
        }

        return rtn;
    }

    /**
     * 静态内部类：查询回调接口
     */
    public interface QueryCallback {

        /**
         * 处理查询结果
         *
         * @param rs
         * @throws Exception
         */
        void process(ResultSet rs) throws Exception;

    }
}
