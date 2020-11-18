package com.jasper.config;

import com.jasper.bean.ISchool;
import com.jasper.bean.Klass;
import com.jasper.bean.School;
import com.jasper.bean.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasper
 */
@Configuration
@ConditionalOnClass({Klass.class, Student.class})
public class SchoolConfiguration {
    static {
        System.out.println("SchoolConfiguration init......");
    }

    @Bean
    public ISchool schoolBeanGenerate() {
        return new School();
    }
}
