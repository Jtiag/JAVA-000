package com.jasper.jdbc.mvp.dao;

import com.jasper.jdbc.mvp.domain.Student;

import java.util.List;

/**
 * @author jasper
 */
public interface StudentDao {
    void save(Student student);

    void updateById(int id,String name);

    Student queryById(int id);
    List<Student> queryAll();
    void deleteById(int id);
}
