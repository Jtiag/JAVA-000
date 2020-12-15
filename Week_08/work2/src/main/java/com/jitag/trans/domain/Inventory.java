package com.jitag.trans.domain;

import lombok.Data;

/**
 * @author jasper 2020/12/15 下午3:58
 * @version 1.0.0
 * @desc
 */
@Data
public class Inventory {
    private long id;
    String productId;
    int totalInventory;
    int lockInventory;
}
