package com.jasper.gateway.outbound.netty4;

import com.jasper.gateway.listener.GatewayListener;
import com.jasper.gateway.outbound.OutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;

@Slf4j
public class NettyHttpClientOutboundHandler implements OutboundHandler {
    /**
     * http://ip:port
     */
    private String backendUrl;

    public NettyHttpClientOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public void connect(String host, int port, Object msg, ChannelHandlerContext serverCtx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpClientCodec());
                    ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                    ch.pipeline().addLast(new NettyHttpClientHandler(serverCtx));
                }
            });
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            URI uri = new URI(fullHttpRequest.uri());
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getPath());
            request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set(HttpHeaderNames.HOST, "localhost");
            // Start the client.
            SocketAddress address = new InetSocketAddress(host, port);
            ChannelFuture channelFuture = b.connect(address).sync();/*.addListener(new GatewayListener(serverCtx, msg))*/
            channelFuture.channel().writeAndFlush(request);
//            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            serverCtx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
            serverCtx.close();
            workerGroup.shutdownGracefully();
        } finally {
//            workerGroup.shutdownGracefully();
        }

    }

    /**
     * @param msg
     * @param ctx server端的
     */
    @Override
    public void handle(Object msg, ChannelHandlerContext ctx) {
        String[] splitBackend = backendUrl.split("//");
        String backendInfo = splitBackend[1];
        String[] backendIpAndPort = backendInfo.split(":");
        if (backendIpAndPort.length != 2) {
            log.error("proxy url is wrong");
            return;
        }
        String proxyIp = backendIpAndPort[0];
        int port = Integer.parseInt(backendIpAndPort[1]);
        try {
            connect(proxyIp, port, msg, ctx);
        } catch (Exception e) {
            log.error("connect {}:{} failed {}", proxyIp, port, e);
        }

    }
}