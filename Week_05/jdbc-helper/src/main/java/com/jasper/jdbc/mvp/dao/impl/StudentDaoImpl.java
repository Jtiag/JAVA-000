package com.jasper.jdbc.mvp.dao.impl;

import com.google.common.collect.Lists;
import com.jasper.jdbc.mvp.dao.StudentDao;
import com.jasper.jdbc.mvp.domain.Student;
import com.jasper.jdbc.mvp.helper.JdbcHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author jasper
 */
@Repository
public class StudentDaoImpl implements StudentDao {
    private final String tableName = "t_student";
    @Autowired
    private JdbcHelper jdbcHelper;

    @Override
    public void save(Student student) {
        int id = student.getId();
        String name = student.getName();
        String sql = String.format("insert into %s(id,name) values(%s,'%s')", tableName, id, name);
        jdbcHelper.execute(sql);
    }

    @Override
    public void updateById(int id, String name) {
        String sql = String.format("update %s set name='%s' where id=%s", tableName, name, id);
        jdbcHelper.execute(sql);
    }

    @Override
    public Student queryById(int id) {
        Student student = new Student();
        String sql = String.format("select * from %s where id=%s", tableName, id);
        jdbcHelper.executeQuery(sql, new JdbcHelper.QueryCallback() {
            /**
             * 处理查询结果
             *
             * @param rs
             * @throws Exception
             */
            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    int stuId = rs.getInt("id");
                    String name = rs.getString("name");
                    student.setId(stuId);
                    student.setName(name);
                }
            }
        });
        return student;
    }

    @Override
    public List<Student> queryAll() {
        List<Student> students = Lists.newArrayListWithCapacity(16);
        String sql = String.format("select * from %s", tableName);
        jdbcHelper.executeQuery(sql, rs -> {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Student student = new Student(id, name);
                students.add(student);
            }
        });
        return students;
    }

    @Override
    public void deleteById(int id) {
        String sql = String.format("delete from %s where id=%s", tableName, id);
        jdbcHelper.execute(sql);
    }
}
