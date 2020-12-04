package com.jtiag.sharding.route.mvp;

/**
 * 主从同步有：异步复制、同步复制、半同步复制
 * 1.修改操作路由到主库
 * 2.读操作路由到从库
 *
 * 注意点：主从延迟的问题
 *
 *
 * @author jasper
 */
public interface ReadWriteRoute {
    // 更删改操作

    // 查询操作
}
