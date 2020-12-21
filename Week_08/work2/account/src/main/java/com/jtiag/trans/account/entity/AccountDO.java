package com.jtiag.trans.account.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jasper 2020/12/18 下午4:22
 * @version 1.0.0
 * @desc
 */
@Data
public class AccountDO {

    private Integer id;

    private String userId;

    private BigDecimal balance;

    private BigDecimal freezeAmount;

    private Date createTime;

    private Date updateTime;
}
