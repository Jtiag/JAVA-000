package com.rpcfx.demo.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rpcfx.demo.api.RpcfxResolver;
import com.rpcfx.demo.api.RpcfxResponse;
import com.rpcfx.demo.api.RpcfxRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射 this.applicationContext.getBean(serviceClass);
        Object service = resolver.resolve(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            // dubbo, fastjson,
            assert method != null;
            Object result = method.invoke(service, request.getParams());
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        Method[] methods = klass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

}
