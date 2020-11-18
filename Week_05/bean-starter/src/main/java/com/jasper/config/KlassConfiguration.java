package com.jasper.config;

import com.jasper.bean.Klass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 给前面课程提供的Student/Klass/School实现自动配置和Starter。
 * <p>
 * 和application没在同一级又两种方法实现自动装配
 * 1.使用@Import
 * 2.使用spring.factories
 *
 * @author jasper
 */
@Configuration
@ConditionalOnClass(name = "com.jasper.bean.Student")
public class KlassConfiguration {
    static {
        System.out.println("KclassConfiguration init.....");
    }

    @Bean
    public Klass klassGenerate() {
        return new Klass();
    }
}
