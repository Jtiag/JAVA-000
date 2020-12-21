package com.jtiag.trans.inventory.service;

import com.jtiag.trans.inventory.dto.InventoryDTO;
import com.jtiag.trans.inventory.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jasper 2020/12/18 下午4:49
 * @version 1.0.0
 * @desc
 */
@Service("inventoryService")
public class InventoryService {
    @Autowired
    private InventoryMapper inventoryMapper;

    public Boolean decrease(InventoryDTO inventoryDTO) {
        return inventoryMapper.decrease(inventoryDTO) > 0;
    }
}
