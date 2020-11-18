package com.spring.jasper.config;

import com.spring.jasper.service.BeanService;
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
