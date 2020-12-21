package com.jtiag.rpcfx.demo.consumer;

import com.jtiag.rpcfx.client.Rpcfx;
import com.jtiag.rpcfx.demo.api.Order;
import com.jtiag.rpcfx.demo.api.OrderService;
import com.jtiag.rpcfx.demo.api.User;
import com.jtiag.rpcfx.demo.api.UserService;
import com.jtiag.rpcfx.api.Filter;
import com.jtiag.rpcfx.api.LoadBalancer;
import com.jtiag.rpcfx.api.Router;
import com.jtiag.rpcfx.api.RpcfxRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById
		Rpcfx rpcfx = new Rpcfx();
		UserService userService = rpcfx.create(UserService.class, "http://localhost:8080/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());

		OrderService orderService = rpcfx.create(OrderService.class, "http://localhost:8080/");
		Order order = orderService.findOrderById(1992129);
		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));

		//
		UserService userService2 = rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

//		SpringApplication.run(RpcfxClientApplication.class, args);
	}

	private static class TagRouter implements Router {
		@Override
		public List<String> route(List<String> urls) {
			return urls;
		}
	}

	private static class RandomLoadBalancer implements LoadBalancer {
		@Override
		public String select(List<String> urls) {
			return urls.get(0);
		}
	}

	@Slf4j
	private static class CuicuiFilter implements Filter {
		@Override
		public boolean filter(RpcfxRequest request) {
			log.info("filter {} -> {}", this.getClass().getName(), request.toString());
			return true;
		}
	}
}



