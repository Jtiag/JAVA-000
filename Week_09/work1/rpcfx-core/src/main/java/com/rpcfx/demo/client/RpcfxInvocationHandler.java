package com.rpcfx.demo.client;

import com.alibaba.fastjson.JSON;
import com.rpcfx.demo.api.Filter;
import com.rpcfx.demo.api.RpcfxRequest;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.client.communication.CommunicationProcessor;
import com.rpcfx.demo.client.communication.http.OkHttpProcessor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author jasper 2020/12/21 下午4:57
 * @version 1.0.0
 * @desc
 */
public class RpcfxInvocationHandler implements InvocationHandler, MethodInterceptor {

    private final Class<?> serviceClass;
    private final String url;
    private final Filter[] filters;
    private final CommunicationProcessor communicationProcessor;

    public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url, CommunicationProcessor communicationProcessor,
                                      Filter... filters) {
        this.serviceClass = serviceClass;
        this.url = url;
        this.filters = filters;
        this.communicationProcessor = communicationProcessor;
    }

    public RpcfxInvocationHandler(Class<?> serviceClass, String url, Filter[] filters) {
        this.serviceClass = serviceClass;
        this.url = url;
        this.filters = filters;
        this.communicationProcessor = new OkHttpProcessor();
    }
    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        return process(method, params, url);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return process(method, objects, url);
    }

    private Object process(Method method, Object[] params, String url) throws Throwable {
        // 加filter地方之二
        // mock == true, new Student("hubao");

        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(params);

        if (null != filters) {
            for (Filter filter : filters) {
                if (!filter.filter(request)) {
                    return null;
                }
            }
        }
        RpcfxResponse response = communicationProcessor.doRequest(request, url);

        // 加filter地方之三
        // Student.setTeacher("cuijing");

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException

        return JSON.parse(response.getResult().toString());
    }
}