package com.jasper.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

@Data
@EnableConfigurationProperties(Klass.class)
public class Klass {
    @Autowired
    List<Student> students;
    public void dong() {
        System.out.println(this.getStudents());
    }

}
