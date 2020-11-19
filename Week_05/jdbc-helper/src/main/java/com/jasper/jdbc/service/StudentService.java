package com.jasper.jdbc.service;

import com.jasper.jdbc.dao.StudentDao;
import com.jasper.jdbc.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 研究一下JDBC接口和数据库连接池，掌握它们的设计和用法：
 * 1）使用JDBC原生接口，实现数据库的增删改查操作。
 * 2）使用事务，PrepareStatement方式，批处理方式，改进上述操作。
 * 3）配置Hikari连接池，改进上述操作。提交代码到Github。
 *
 * @author jasper
 */
@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;

    public void save(Student student) {
        studentDao.save(student);
    }

    public void update(int id,String name) {
        studentDao.updateById(id, name);
    }

    public Student query(int id) {
        Student student = studentDao.queryById(id);
        return student;
    }

    public void delete(int id) {
        studentDao.deleteById(1);
    }

    public List<Student> queryAll() {
        return studentDao.queryAll();
    }
}
