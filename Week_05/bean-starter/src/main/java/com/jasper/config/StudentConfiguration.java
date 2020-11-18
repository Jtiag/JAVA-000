package com.jasper.config;

import com.jasper.bean.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasper
 */
@Configuration
public class StudentConfiguration {
    static {
        System.out.println("StudentConfiguration init.....");
    }

    @Bean
    public Student studentBeanEGenerate() {
        return new Student();
    }
}
