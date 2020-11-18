package com.spring.one.config;

import com.spring.one.service.BeanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasper
 */
@Configuration
public class ConfigBean {
    @Bean
    public BeanService initBean() {
        return new BeanService();
    }
}
