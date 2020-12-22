package com.rpcfx.demo.config;

import com.rpcfx.demo.api.OrderService;
import com.rpcfx.demo.api.RpcfxResolver;
import com.rpcfx.demo.api.UserService;
import com.rpcfx.demo.provider.DemoResolver;
import com.rpcfx.demo.provider.OrderServiceImpl;
import com.rpcfx.demo.provider.UserServiceImpl;
import com.rpcfx.demo.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasper 2020/12/21 下午10:41
 * @version 1.0.0
 * @desc
 */
@Configuration
public class ConfigBean {
    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver) {
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver() {
        return new DemoResolver();
    }

    // 能否去掉name
    //

    // annotation


    @Bean(name = "com.rpcfx.demo.api.UserService")
    public UserService createUserService() {
        return new UserServiceImpl();
    }

    @Bean(name = "com.rpcfx.demo.api.OrderService")
    public OrderService createOrderService() {
        return new OrderServiceImpl();
    }
}
