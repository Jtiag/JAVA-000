package com.rpcfx.demo.client.communication.http;

import com.alibaba.fastjson.JSON;
import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.client.communication.CommunicationProcessor;
import org.apache.http.entity.StringEntity;

/**
 * @author jasper 2020/12/21 下午6:05
 * @version 1.0.0
 * @desc
 */
public class HttpClientProcessor implements CommunicationProcessor {
    @Override
    public RpcfxResponse doRequest(RpcfxRequest req, String url) {
        StringEntity stringEntity = HttpClient.getStringEntity(req);

        String response = HttpClient.postRequest(stringEntity, url);
        System.out.println("resp json: " + response);
        return JSON.parseObject(response, RpcfxResponse.class);
    }
}
