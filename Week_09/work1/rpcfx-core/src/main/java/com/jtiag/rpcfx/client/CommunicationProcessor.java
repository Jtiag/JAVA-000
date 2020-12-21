package com.jtiag.rpcfx.client;

import com.jtiag.rpcfx.api.RpcfxRequest;
import com.jtiag.rpcfx.api.RpcfxResponse;
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
