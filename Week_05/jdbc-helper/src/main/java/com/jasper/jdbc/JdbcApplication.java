package com.jasper.jdbc;

import com.jasper.jdbc.domain.Student;
import com.jasper.jdbc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author jasper
 */
@SpringBootApplication
public class JdbcApplication implements CommandLineRunner {
    @Autowired
    private StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(JdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        studentService.save(new Student(1, "zhangsan"));
        Student stu = studentService.query(1);
        System.out.println(stu);
        studentService.update(1, "wangwu");
        stu = studentService.query(1);
        System.out.println(stu);
        studentService.save(new Student(2, "lisi"));
        List<Student> students = studentService.queryAll();
        for (Student student : students) {
            System.out.println(student);
        }
        studentService.delete(1);
        stu = studentService.query(1);
        System.out.println(stu);

        System.out.println("===============================");
    }
}
