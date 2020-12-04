package com.jtiag.sharding.domain;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author jasper
 */
@Data
public class User {
    private long user;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private int totalDataNum;
    private Timestamp gmtModify;
    private Timestamp gmtCreated;
}
