package com.jasper.gateway.inbound;

import com.jasper.gateway.filter.HeaderRequestFilter;
import com.jasper.gateway.filter.RequestFilterChain;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private String proxyServer;

    public HttpInboundInitializer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        RequestFilterChain filterChain = new RequestFilterChain();
        HeaderRequestFilter headerRequestFilter = new HeaderRequestFilter();
        filterChain.addFilter(headerRequestFilter);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(filterChain)
                .addLast(new HttpInboundHandler(this.proxyServer));
    }
}
