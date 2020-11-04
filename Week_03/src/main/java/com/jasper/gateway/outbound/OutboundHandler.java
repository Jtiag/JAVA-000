package com.jasper.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface OutboundHandler {
    Logger logger = LoggerFactory.getLogger(OutboundHandler.class);

    void handle(final Object msg, final ChannelHandlerContext serverCtx);

    default String getMsg(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        String msg = null;
        try (InputStream content = entity.getContent();
             BufferedInputStream bis = new BufferedInputStream(content);
             ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            int result = bis.read();
            while (result != -1) {
                buf.write(result);
                result = bis.read();
            }
            msg = buf.toString();
        } catch (Exception e) {
            logger.error("getMsg failed", e);
        }
        return msg;
    }

}
