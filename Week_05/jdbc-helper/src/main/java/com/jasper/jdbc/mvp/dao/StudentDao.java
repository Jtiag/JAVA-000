package com.jasper.jdbc.mvp.dao;

import com.jasper.jdbc.mvp.domain.Student;

/**
 * @author jasper
 */
public interface StudentDao {
    void save(Student student);

    void updateById(int id);

    Student queryById(int id);

    void deleteById(int id);
}
