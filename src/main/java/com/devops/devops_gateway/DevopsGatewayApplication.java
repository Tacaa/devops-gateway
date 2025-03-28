package com.devops.devops_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DevopsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevopsGatewayApplication.class, args);
	}

}
