package com.jtiag.sharding.controller;

import com.jtiag.sharding.domain.User;
import com.jtiag.sharding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jasper
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findUserById")
    public User findUserById(@RequestParam int id) {
        return userService.findUserById(id);
    }
}
