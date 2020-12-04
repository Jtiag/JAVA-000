package com.jtiag.sharding.service;

import com.jtiag.sharding.dao.master.UserDaoMaster;
import com.jtiag.sharding.dao.slave.UserDaoSlave;
import com.jtiag.sharding.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jasper
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDaoMaster userDaoMaster;
    @Autowired
    private UserDaoSlave userDaoSlave;

    public User findUserById(int id) {
        User user = null;
        try {
            user = userDaoSlave.findUserById(id);
            if (user == null) {
                user = userDaoMaster.findUserById(id);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return user;
    }

    public void saveUser(User user) {
        try {
            userDaoMaster.save(user);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void updateMobileById(int id, String mobile) {
        try {
            userDaoMaster.updateMobileById(id, mobile);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public User findUserById4Sharding(int id) {
        User user = null;
        try {
            user = userDaoMaster.findUserById(id);
        } catch (Exception e) {
            log.error("", e);
        }
        return user;
    }

    public void saveUser4Sharding(User user) {
        try {
            userDaoMaster.save(user);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void updateMobileById4Sharding(int id, String mobile) {
        try {
            userDaoMaster.updateMobileById(id, mobile);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
