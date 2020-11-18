package com.spring.two;

import com.spring.two.service.MyBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jasper
 */
@SpringBootApplication
public class MybeanSpringBootApplication implements CommandLineRunner {
    @Autowired
    private MyBeanService myBeanService;
    public static void main(String[] args) {
        SpringApplication.run(MybeanSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        myBeanService.callKlass();
        myBeanService.callSchool();
        myBeanService.callStudent();
        myBeanService.createStudent();
        myBeanService.callStudent();
    }
}
