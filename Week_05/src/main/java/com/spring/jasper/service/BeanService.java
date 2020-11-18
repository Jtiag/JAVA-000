package com.spring.jasper.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author jasper
 */
//@Service
public class BeanService {

    public BeanService() {
        System.out.println("BeanService init success");
    }

    public void sayHello() {
        System.out.println("bean assemble to spring success");
    }
}
