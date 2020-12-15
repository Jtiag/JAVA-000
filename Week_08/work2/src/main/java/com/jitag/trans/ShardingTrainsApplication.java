package com.jitag.trans;
import com.jitag.trans.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author jasper
 */
@SpringBootApplication
@EnableTransactionManagement
public class ShardingTrainsApplication implements CommandLineRunner {
    @Autowired
    private OrderService orderService;
    public static void main(String[] args) {
        SpringApplication.run(ShardingTrainsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        orderService.saveOrders();
    }
}
