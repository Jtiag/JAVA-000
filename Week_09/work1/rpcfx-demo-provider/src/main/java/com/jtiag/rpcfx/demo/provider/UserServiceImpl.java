package com.jtiag.rpcfx.demo.provider;

import com.jtiag.rpcfx.demo.api.User;
import com.jtiag.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
