package com.spring.two.service;

import com.jasper.bean.ISchool;
import com.jasper.bean.Klass;
import com.jasper.bean.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jasper
 */
@Service
public class MyBeanService {
    @Autowired
    private Student student;
    @Autowired
    private Klass klass;
    @Autowired
    private ISchool school;

    public void callStudent()
    {
        System.out.println(student.toString());
    }
    public void callKlass()
    {
        klass.dong();
    }
    public void callSchool()
    {
        school.ding();
    }
    public void createStudent()
    {
        Student student = this.student.create();
        System.out.println(student);
    }

}
