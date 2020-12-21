package com.jtiag.trans.pay;

import com.jtiag.trans.account.dto.AccountDTO;
import com.jtiag.trans.inventory.dto.InventoryDTO;
import com.jtiag.trans.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

/**
 * @author jasper 2020/12/18 下午5:05
 * @version 1.0.0
 * @desc
 */
@SpringBootApplication(scanBasePackages = {"com.jtiag.trans.account", "com.jtiag.trans.*"})
public class MainRunningApplication implements CommandLineRunner {
    @Autowired
    private PaymentService paymentService;

    public static void main(String[] args) {
        SpringApplication.run(MainRunningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAmount(new BigDecimal(1));
        accountDTO.setUserId("10000");

        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(1);
        inventoryDTO.setProductId("1");

        paymentService.makePayment(accountDTO, inventoryDTO);
    }
}
