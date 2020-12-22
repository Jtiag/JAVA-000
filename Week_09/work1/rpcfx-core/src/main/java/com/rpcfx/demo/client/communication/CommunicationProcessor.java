package com.rpcfx.demo.client.communication;

import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResponse;
import okhttp3.MediaType;

import java.io.IOException;

/**
 * @author jasper 2020/12/21 下午5:10
 * @version 1.0.0
 * @desc
 */
public interface CommunicationProcessor {
    MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
    RpcfxResponse doRequest(RpcfxRequest req, String url) throws IOException;
}
