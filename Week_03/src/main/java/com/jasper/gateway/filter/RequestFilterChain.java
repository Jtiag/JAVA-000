package com.jasper.gateway.filter;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

/**
 * @author jasper
 */
public class RequestFilterChain extends ChannelInboundHandlerAdapter {
    private List<HttpRequestFilter> filters = Lists.newArrayList();

    public void addFilter(HttpRequestFilter filter) {
        this.filters.add(filter);
    }

    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        for (HttpRequestFilter filter : filters) {
            filter.filter(fullRequest, ctx);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        this.filter(fullHttpRequest, ctx);
        // 继续处理下一个handler
        ctx.fireChannelRead(msg);
    }
}
