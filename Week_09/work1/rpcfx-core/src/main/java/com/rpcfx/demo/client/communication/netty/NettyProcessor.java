package com.rpcfx.demo.client.communication.netty;

import com.alibaba.fastjson.JSON;
import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.client.communication.CommunicationProcessor;
import com.rpcfx.demo.client.communication.netty.client.RpcNettyClientSync;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author jasper 2020/12/21 下午10:21
 * @version 1.0.0
 * @desc
 */
@Slf4j
public class NettyProcessor implements CommunicationProcessor {
    @Override
    public RpcfxResponse doRequest(RpcfxRequest rpcfxRequest, String url) throws IOException {
        // 客户端使用的 netty，发送请求到服务端，拿到结果（自定义结构：rpcfxResponse)
        log.info("Client send request to Server");
        RpcfxResponse rpcResponse;
        try {
            rpcResponse = RpcNettyClientSync.getInstance().getResponse(rpcfxRequest, url);
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        log.info("Client receive response Object");
        assert rpcResponse != null;
        if (!rpcResponse.isStatus()) {
            log.info("Client receive exception");
            rpcResponse.getException().printStackTrace();
            return null;
        }

        // 序列化成对象返回
        log.info("Response:: " + rpcResponse.getResult());
        return rpcResponse;
    }
}
