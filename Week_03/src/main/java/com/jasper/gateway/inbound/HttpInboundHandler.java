package com.jasper.gateway.inbound;

import com.jasper.gateway.outbound.OutboundHandler;
import com.jasper.gateway.outbound.netty4.NettyHttpClientOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private final String proxyServer;
    private NettyHttpClientOutboundHandler handler;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new NettyHttpClientOutboundHandler(this.proxyServer);
//        handler = new HttpOutboundHandler(this.proxyServer);
    }

    @Override
    public void channelRead(ChannelHandlerContext serverCtx, Object msg) {
        /*if (msg instanceof FullHttpRequest)*/ {
            try {
                FullHttpRequest fullRequest = (FullHttpRequest) msg;
                handler.handle(msg, serverCtx);
//                String[] splitBackend = proxyServer.split("//");
//                String backendInfo = splitBackend[1];
//                String[] backendIpAndPort = backendInfo.split(":");
//                if (backendIpAndPort.length != 2) {
//                    log.error("proxy url is wrong");
//                    return;
//                }
//                String proxyIp = backendIpAndPort[0];
//                int port = Integer.parseInt(backendIpAndPort[1]);
//                handler.connect(proxyIp,port,msg,serverCtx);
            } catch (Exception e) {
                log.error("some error happened", e);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }/* else if (msg instanceof String) {
            String result = (String) msg;
            HttpInboundResponse httpInboundResponse = new HttpInboundResponse();
            try {
                httpInboundResponse.handleResponse(serverCtx, result);
            } catch (Exception e) {
                log.error("handleResponse failed", e);
            }
        }*/
    }


}
