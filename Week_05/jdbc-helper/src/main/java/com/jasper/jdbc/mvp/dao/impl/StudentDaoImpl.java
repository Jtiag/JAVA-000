package com.jasper.jdbc.mvp.dao.impl;

import com.jasper.jdbc.mvp.dao.StudentDao;
import com.jasper.jdbc.mvp.domain.Student;
import com.jasper.jdbc.mvp.helper.JdbcHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

/**
 * @author jasper
 */
@Repository
public class StudentDaoImpl implements StudentDao {
    @Autowired
    private JdbcHelper jdbcHelper;
    @Override
    public void save(Student student) {
        Connection connection = jdbcHelper.getConnection();
    }

    @Override
    public void updateById(int id) {

    }

    @Override
    public Student queryById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }
}
