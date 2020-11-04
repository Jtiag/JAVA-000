package com.jasper.gateway.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayListener implements ChannelFutureListener {

    private ChannelHandlerContext context;
    private Object msg;

    public GatewayListener(ChannelHandlerContext handlerContext, Object msg) {
        this.context = handlerContext;
        this.msg = msg;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            try {
                future.channel().writeAndFlush(msg);
            } catch (Exception e) {
                future.channel().close();
                log.error("some error happened", e);
            }
        } else {
            context.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_TIMEOUT));
            context.close();
        }
    }
}
