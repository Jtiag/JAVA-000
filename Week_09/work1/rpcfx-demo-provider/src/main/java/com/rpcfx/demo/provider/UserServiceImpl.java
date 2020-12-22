package com.rpcfx.demo.provider;

import com.rpcfx.demo.api.User;
import com.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
