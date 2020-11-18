package com.spring.jasper;

import com.spring.jasper.service.BeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jasper
 */
@SpringBootApplication
public class SpringBeanAssemble implements CommandLineRunner {
    @Autowired
    private BeanService beanService;

    public static void main(String[] args) {
//        SpringBeanAssemble springBeanAssemble = new SpringBeanAssemble();
//        springBeanAssemble.methodBase2Xml();
//        springBeanAssemble.methodBase2Annotation();
        SpringApplication.run(SpringBeanAssemble.class, args);
    }

    /**
     * d. 使用@Configuration 构建bean
     * 1. 屏蔽掉 @Service注解
     * 2. 使用@Autowired 注入BeanService
     */
    private void methodBase2SpringConfiguration() {
        beanService.sayHello();
    }

    /**
     * c.使用spring boot
     * 可以去掉applicationContext.xml BeanService类上加上 @Service && 使用@Autowired注解注入装配的bean
     */
    private void methodBase2SpringBootAutowired() {
        beanService.sayHello();
    }

    /**
     * a.
     * 1.注释掉applicationContext.xml中关于beanService的配置 &&
     * 2.在BeanService类上加上 @Service
     */
    private void methodBase2Annotation() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanService beanService = (BeanService) context.getBean("beanService");
        beanService.sayHello();
    }

    /**
     * b.
     * 1.打开applicationContext.xml中关于beanService的配置 &&
     * 2.去除BeanService类上上的注解@Service
     */
    private void methodBase2Xml() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanService beanService = (BeanService) context.getBean("beanService");
        beanService.sayHello();
    }


    @Override
    public void run(String... args) throws Exception {
//        methodBase2SpringBootAutowired();
        methodBase2SpringConfiguration();
    }
}
