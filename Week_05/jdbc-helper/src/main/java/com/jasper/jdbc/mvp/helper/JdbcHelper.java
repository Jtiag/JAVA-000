package com.jasper.jdbc.mvp.helper;

import com.google.common.collect.Lists;
import com.jasper.jdbc.mvp.helper.config.ConfigurationManager;
import com.jasper.jdbc.mvp.helper.constants.MysqlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jasper
 */
@Slf4j
@Component
public class JdbcHelper {
    static {
        try {
            String driver = ConfigurationManager.getProperty(MysqlConstants.JDBC_DRIVER);
            Class.forName(driver);
        } catch (Exception e) {
            log.error("init db driver failed", e);
        }
    }

    private static volatile JdbcHelper instance = null;

    /**
     * @return JdbcHelper
     */
    @PostConstruct
    private static void init() {
        log.info("init JdbcHelper....");
        instance = new JdbcHelper();
    }

    /**
     * 数据库连接池
     */
    private final LinkedList<Connection> datasource = Lists.newLinkedList();

    private JdbcHelper() {
        int datasourceSize = ConfigurationManager.getInteger(
                MysqlConstants.JDBC_DATASOURCE_SIZE);
        for (int i = 0; i < datasourceSize; i++) {
            String url;
            String user;
            String password;
            url = ConfigurationManager.getProperty(MysqlConstants.JDBC_URL);
            user = ConfigurationManager.getProperty(MysqlConstants.JDBC_USER);
            password = ConfigurationManager.getProperty(MysqlConstants.JDBC_PASSWORD);
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                datasource.push(conn);
            } catch (Exception e) {
                log.error("create dataSource pool failed", e);
            }
        }
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
    public static interface QueryCallback {

        /**
         * 处理查询结果
         *
         * @param rs
         * @throws Exception
         */
        void process(ResultSet rs) throws Exception;

    }
}
