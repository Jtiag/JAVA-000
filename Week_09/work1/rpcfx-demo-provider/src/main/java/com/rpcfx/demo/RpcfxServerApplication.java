package com.rpcfx.demo;

import com.rpcfx.demo.client.communication.netty.server.RpcNettyServer;
import com.rpcfx.demo.api.OrderService;
import com.rpcfx.demo.api.UserService;
import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResolver;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.api.ServiceProviderDesc;
import com.rpcfx.demo.provider.DemoResolver;
import com.rpcfx.demo.provider.OrderServiceImpl;
import com.rpcfx.demo.provider.UserServiceImpl;
import com.rpcfx.demo.server.RpcfxInvoker;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

/**
 * @author jasper
 */
@SpringBootApplication
@RestController
public class RpcfxServerApplication implements ApplicationRunner {
    private final RpcNettyServer rpcNettyServer;

    @Autowired
    RpcfxInvoker invoker;

    public RpcfxServerApplication(RpcNettyServer rpcNettyServer) {
        this.rpcNettyServer = rpcNettyServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(RpcfxServerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // start zk client
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("127.0.0.1:2181")
                .namespace("rpcfx")
                .retryPolicy(retryPolicy).build();
        client.start();

        // register service
        // xxx "io.kimmking.rpcfx.demo.api.UserService"

        String userService = UserService.class.getName();
        registerService(client, userService);
        String orderService = OrderService.class.getName();
        registerService(client, orderService);

        nettyServerRun();
    }


    private void nettyServerRun() {
        try {
            rpcNettyServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rpcNettyServer.destroy();
        }
    }

    private static void registerService(CuratorFramework client, String service) throws Exception {
        ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
                .host(InetAddress.getLocalHost().getHostAddress())
                .port(8080).serviceClass(service).build();
        // String userServiceSescJson = JSON.toJSONString(userServiceSesc);

        try {
            if (null == client.checkExists().forPath("/" + service)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath("/" + service, "service".getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        client.create().withMode(CreateMode.EPHEMERAL).
                forPath("/" + service + "/" + userServiceSesc.getHost() + "_" + userServiceSesc.getPort(), "provider".getBytes());
    }

    //    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return invoker.invoke(request);
    }
}
