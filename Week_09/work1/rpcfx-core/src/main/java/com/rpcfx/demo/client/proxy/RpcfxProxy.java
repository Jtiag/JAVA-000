package com.rpcfx.demo.client.proxy;

import com.rpcfx.demo.api.Filter;

/**
 * @author jasper 2020/12/21 下午4:35
 * @version 1.0.0
 * @desc
 */
public interface RpcfxProxy {
    /**
     * 创建服务代理
     *
     * @param serviceClass 服务类名
     * @param url          调用url
     * @param filters      过滤器
     * @return
     */
    public <T> T create(final Class<T> serviceClass, final String url, Filter... filters);
}
