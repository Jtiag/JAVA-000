package com.train.work;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * http client
 *
 * @author jasper
 */
@Slf4j
public class HttpClientService {
    /**
     * http client
     */
    private static CloseableHttpClient httpClient;

    /**
     * 使用Pooling connection manager构建http客户端
     */
    @PostConstruct
    public void initHttpClient() {
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
    @PreDestroy
    public void closeHttpClient() {
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
    public Map<String, String> postRequest(HttpEntity entity, String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置获取连接超时时间
                .setConnectionRequestTimeout(5000)
                // 设置连接上服务器(握手成功)超时时间
                .setConnectTimeout(5000)
                // 设置服务器返回数据(response)超时时间
                .setSocketTimeout(50000)
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(entity);
        boolean isSuccess;
        Map<String, String> resMap = Maps.newHashMapWithExpectedSize(3);
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
                        resMap.put("result", buf.toString());
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
        return resMap;
    }

    /**
     * get 请求
     *
     * @param url request url
     * @return
     */
    public Map<String, String> getRequest(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置获取连接超时时间
                .setConnectionRequestTimeout(5000)
                // 设置连接上服务器(握手成功)超时时间
                .setConnectTimeout(5000)
                // 设置服务器返回数据(response)超时时间
                .setSocketTimeout(5000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        boolean isSuccess;
        Map<String, String> resMap = Maps.newHashMapWithExpectedSize(3);
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
                        resMap.put("result", buf.toString());
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
        return resMap;
    }

    /**
     * 封装request的消息为StringEntity
     *
     * @param result StringEntity
     * @return
     */
    public StringEntity getStringEntity(Object result) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("json process error", e);
        }
        return new StringEntity(jsonString, "UTF-8");
    }

    public static void main(String[] args) {
        HttpClientService httpClientService = new HttpClientService();
        Map<String, String> request = httpClientService.getRequest("http://localhost:8801/test");
        System.out.println();
    }
}