package com.jasper.gateway.outbound.netty4;

import com.jasper.gateway.inbound.HttpInboundResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.net.URI;

@Slf4j
public class NettyHttpClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext serverCtx;
    /**
     * /test
     */
    private FullHttpRequest fullHttpRequest;
    private HttpInboundResponse httpInboundResponse;
    public NettyHttpClientHandler(ChannelHandlerContext serverCtx) {
        this.serverCtx = serverCtx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
//        URI uri = new URI(fullHttpRequest.uri());
//        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getPath());
//        request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
//        request.headers().set(HttpHeaderNames.HOST, "localhost");
//        ctx.writeAndFlush(request);
        log.info("channel is active，and flush request");
        httpInboundResponse = new HttpInboundResponse();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        serverCtx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        String result = null;
        log.info("response = [{}]", msg);
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf buf = response.content();
            result = buf.toString(CharsetUtil.UTF_8);
            log.info("result = [{}]", result);
        }
//        serverCtx.writeAndFlush(msg);
        msgProcess(msg,ctx);
    }

    private void msgProcess(Object msg,ChannelHandlerContext ctx) {
        String result;
        log.info("response = [{}]", msg);
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf buf = response.content();
            result = buf.toString(CharsetUtil.UTF_8);
            log.info("result = [{}]", result);
            try {
                httpInboundResponse.handleResponse(serverCtx, result);
            } catch (Exception e) {
                log.error("handleResponse failed", e);
            }finally {
                ChannelFuture channelFuture = ctx.channel().close();
                try {
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}