package com.jtiag.rpcfx.client;


import com.alibaba.fastjson.parser.ParserConfig;
import com.jtiag.rpcfx.api.Filter;
import com.jtiag.rpcfx.api.LoadBalancer;
import com.jtiag.rpcfx.api.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jasper
 */
public final class Rpcfx {
    private final RpcfxProxy rpcfxProxy;

    public Rpcfx(RpcfxProxy rpcfxProxy) {
        this.rpcfxProxy = rpcfxProxy;
    }

    public Rpcfx() {
        this.rpcfxProxy = new JdkProxy();
    }

    static {
        ParserConfig.getGlobalInstance().addAccept("com.jtiag");
    }

    public <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router,
                                             LoadBalancer loadBalance, Filter filter) {

        // 加filte之一

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>();
        // 1. 简单：从zk拿到服务提供的列表
        // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）

        List<String> urls = router.route(invokers);

        // router, loadbalance
        String url = loadBalance.select(urls);

        return (T) create(serviceClass, url, filter);

    }

    public <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {

        // 0. 替换动态代理 -> AOP
        return rpcfxProxy.create(serviceClass, url, filters);

    }

}
