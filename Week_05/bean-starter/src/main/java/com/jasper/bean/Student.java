package com.jasper.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EnableConfigurationProperties(Student.class)
@ConfigurationProperties(prefix = "stu")
public class Student implements Serializable {

    private int id;
    private String name;

    public void init() {
        System.out.println("hello...........");
    }

    public Student create() {
        return new Student(101, "KK101");
    }
}
