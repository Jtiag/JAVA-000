package com.jasper.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@EnableConfigurationProperties(School.class)
public class School implements ISchool {
    // Resource
    @Autowired
    Klass class1;

    @Autowired
    Student student;

    public void ding() {
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student);

    }
}
