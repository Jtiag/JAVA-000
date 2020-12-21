package com.jtiag.rpcfx.client;

import com.jtiag.rpcfx.api.Filter;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

/**
 * @author jasper 2020/12/21 下午5:38
 * @version 1.0.0
 * @desc
 */
public class ByteBuddyProxy implements RpcfxProxy{
    /**
     * 创建服务代理
     *
     * @param serviceClass 服务类名
     * @param url          调用url
     * @param filters      过滤器
     * @return
     */
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> serviceClass, String url, Filter... filters) {
        return (T) new ByteBuddy().subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url,filters)))
                .make()
                .load(ByteBuddyProxy.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
