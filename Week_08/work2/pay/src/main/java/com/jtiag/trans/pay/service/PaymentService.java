package com.jtiag.trans.pay.service;
import com.jtiag.trans.account.dto.AccountDTO;
import com.jtiag.trans.account.service.AccountService;
import com.jtiag.trans.inventory.dto.InventoryDTO;
import com.jtiag.trans.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jasper 2020/12/18 下午5:10
 * @version 1.0.0
 * @desc
 */
@Service
@Slf4j
@ComponentScan("com.jtiag.trans")
public class PaymentService {
    private final AccountService accountService;
    private final InventoryService inventoryService;

    @Autowired(required = false)
    public PaymentService(AccountService accountService, InventoryService inventoryService) {
        this.accountService = accountService;
        this.inventoryService = inventoryService;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void makePayment(AccountDTO accountDTO, InventoryDTO inventoryDTO) {
        //扣除用户余额
        accountService.payment(accountDTO);
        log.info("扣除用户 userId=[{}] amount=[{}] 成功", accountDTO.getUserId(), accountDTO.getAmount());
        //进入扣减库存操作
        inventoryService.decrease(inventoryDTO);
        log.info("扣减库存： productId=[{}] count=[{}] 成功", inventoryDTO.getProductId(), inventoryDTO.getCount());
    }
}
