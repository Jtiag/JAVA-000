package com.jtiag.rpcfx.client;

import com.jtiag.rpcfx.api.Filter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author jasper 2020/12/21 下午5:33
 * @version 1.0.0
 * @desc
 */
@Slf4j
public class CglibProxy implements RpcfxProxy {
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
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new RpcfxInvocationHandler(serviceClass, url, filters));
        enhancer.setSuperclass(serviceClass);
        log.info("client cglib proxy instance create and return");
        return (T) enhancer.create();
    }
}
