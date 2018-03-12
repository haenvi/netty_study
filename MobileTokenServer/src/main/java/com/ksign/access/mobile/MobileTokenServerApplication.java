package com.ksign.access.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.ksign.access.mobile.server.NettyServer;

@SpringBootApplication
public class MobileTokenServerApplication {

	//@Autowired private ApplicationContext context;
	
	//@Autowired NettyServer nettyServer;
	public static void main(String[] args) {
		//SpringApplication.run(MobileTokenServerApplication.class, args);
		
		ConfigurableApplicationContext context = SpringApplication.run(MobileTokenServerApplication.class, args);
        NettyServer nettyServer = context.getBean(NettyServer.class);
        
        nettyServer.start();
	}
}
