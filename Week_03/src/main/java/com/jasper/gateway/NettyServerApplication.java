package com.jasper.gateway;


import com.jasper.gateway.inbound.HttpInboundServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerApplication {

    public final static String GATEWAY_NAME = "NIOGateway";
    public final static String GATEWAY_VERSION = "1.0.0";

    public static void main(String[] args) {
        String proxyServer = System.getProperty("proxyServer", "http://localhost:8801");
        String proxyPort = System.getProperty("proxyPort", "8888");
        String bossGroupThreadSize = System.getProperty("bossGroupThreadSize", "1");
        String workerGroupThreadSize = System.getProperty("workerGroupThreadSize", "4");

        //  http://localhost:8888/api/hello  ==> gateway API
        //  http://localhost:8088/api/hello  ==> backend service
        int bgThreadSize = Integer.parseInt(bossGroupThreadSize);
        int wgThreadSize = Integer.parseInt(workerGroupThreadSize);
        int port = Integer.parseInt(proxyPort);
        log.info("{} {}  starting...", GATEWAY_NAME, GATEWAY_VERSION);
        HttpInboundServer server = new HttpInboundServer(port, proxyServer, bgThreadSize, wgThreadSize);
        log.info("{} {} started at http://localhost:{} for server:{}", GATEWAY_NAME, GATEWAY_VERSION, port, proxyServer);
        try {
            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
