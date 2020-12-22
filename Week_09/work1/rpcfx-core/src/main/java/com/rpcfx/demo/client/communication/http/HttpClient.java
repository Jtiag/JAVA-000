package com.rpcfx.demo.client.communication.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jasper 2020/12/21 下午5:59
 * @version 1.0.0
 * @desc
 */
@Slf4j
public class HttpClient {
    /**
     * http client
     */
    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(cm.getMaxTotal());
        cm.setValidateAfterInactivity(10000);

        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        log.info("http client create success");
    }

    /**
     * 关闭http client
     */
    public static void closeHttpClient() {
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("close http client failed ", e);
        }
    }

    /**
     * post 请求
     *
     * @param entity HTTP entity
     * @param url    request url
     * @return
     */
    public static String postRequest(HttpEntity entity, String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置获取连接超时时间
                .setConnectionRequestTimeout(5000)
                // 设置连接上服务器(握手成功)超时时间
                .setConnectTimeout(5000)
                // 设置服务器返回数据(response)超时时间
                .setSocketTimeout(600000)
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(entity);
        boolean isSuccess;
        String res = "";
        try {
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                isSuccess = (statusCode == HttpStatus.SC_OK);

                if (!isSuccess) {
                    log.error("http request failed response code is {} the request url is {}", statusCode, url);
                } else {
                    try (InputStream content = response.getEntity().getContent();
                         BufferedInputStream bis = new BufferedInputStream(content);
                         ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
                        int result = bis.read();
                        while (result != -1) {
                            buf.write(result);
                            result = bis.read();
                        }
                        res = buf.toString();
                    }
                }
            } catch (IOException e) {
                log.error("http request url {} error: {}", url, e);
            } finally {
                if (response != null) {
                    // 该行非常重要否则http client将不会再重用连接，保证entity被消费完
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            }
        } catch (Exception e) {
            log.error("postRequest error: ", e);
        }
        return res;
    }

    /**
     * get 请求
     *
     * @param url request url
     * @return
     */
    public static String getRequest(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置获取连接超时时间
                .setConnectionRequestTimeout(5000)
                // 设置连接上服务器(握手成功)超时时间
                .setConnectTimeout(5000)
                // 设置服务器返回数据(response)超时时间
                .setSocketTimeout(600000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        boolean isSuccess;
        String res = "";
        try {
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                isSuccess = (statusCode == HttpStatus.SC_OK);
                if (!isSuccess) {
                    log.error("http request failed response code is {} the request url is {}", statusCode, url);
                } else {
                    try (InputStream content = response.getEntity().getContent();
                         BufferedInputStream bis = new BufferedInputStream(content);
                         ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
                        int result = bis.read();
                        while (result != -1) {
                            buf.write(result);
                            result = bis.read();
                        }
                        res = buf.toString();
                    }
                }
            } catch (Exception e) {
                log.error("http request url {} error: {}", url, e);
            } finally {
                if (response != null) {
                    // 该行非常重要否则http client将不会再重用连接，保证entity被消费完
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            }
        } catch (Exception e) {
            log.error("getRequest error: ", e);
        }
        return res;
    }

    /**
     * 封装request的消息为StringEntity
     *
     * @param result StringEntity
     * @return
     */
    public static StringEntity getStringEntity(Object result) {
        String jsonString = JSON.toJSONString(result);
        System.out.println("req json: " + jsonString);
        return new StringEntity(jsonString, "UTF-8");
    }
}
