package com.jtiag.rpcfx.client;

import com.jtiag.rpcfx.api.Filter;

import java.lang.reflect.Proxy;

/**
 * @author jasper 2020/12/21 下午4:42
 * @version 1.0.0
 * @desc
 */
public class JdkProxy implements RpcfxProxy {
    /**
     * 创建服务代理
     *
     * @param serviceClass 服务类名
     * @param url          调用url
     * @param filters      过滤器
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> serviceClass, String url, Filter... filters) {
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass},
                new RpcfxInvocationHandler(serviceClass, url, filters));
    }
}
