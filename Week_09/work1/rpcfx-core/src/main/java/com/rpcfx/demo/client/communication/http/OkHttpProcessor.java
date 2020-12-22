package com.rpcfx.demo.client.communication.http;

import com.alibaba.fastjson.JSON;
import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.client.communication.CommunicationProcessor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * @author jasper 2020/12/21 下午5:12
 * @version 1.0.0
 * @desc
 */
public class OkHttpProcessor implements CommunicationProcessor {
    @Override
    public RpcfxResponse doRequest(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: " + respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
