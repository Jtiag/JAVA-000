package com.jasper.jdbc.helper;

import com.jasper.jdbc.pool.DataSourcePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author jasper
 */
@Slf4j
@Component
public class JdbcHelper {
        @Qualifier("mySimplePool")
//    @Qualifier("hikariPool")
    @Autowired()
    private DataSourcePool dataSourcePool;

    private JdbcHelper() {
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
            conn = dataSourcePool.getConnection();

            statement = conn.createStatement();
            statement.execute(sql);
            log.info("[{}] execute success", sql);
        } catch (Exception e) {
            log.error("[{}] execute failed\n{}", sql, e);
        } finally {
            if (conn != null) {
                dataSourcePool.close(conn);
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
            conn = dataSourcePool.getConnection();

            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            queryCallback.process(resultSet);
            log.info("[{}] execute success", sql);
        } catch (Exception e) {
            log.error("[{}] execute failed\n{}", sql, e);
        } finally {
            if (conn != null) {
                dataSourcePool.close(conn);
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
            conn = dataSourcePool.getConnection();
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
                dataSourcePool.close(conn);
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
            conn = dataSourcePool.getConnection();
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
                dataSourcePool.close(conn);
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
            conn = dataSourcePool.getConnection();
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
            log.info("run sql [{}] success",sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dataSourcePool.close(conn);
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
