package com.jitag.sharding;
import com.jitag.sharding.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jasper
 */
@SpringBootApplication
public class ShardingApplication implements CommandLineRunner {
    @Autowired
    private OrderService orderService;
    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        orderService.saveOrders();
    }
}
