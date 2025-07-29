package com.example.customerprofilesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // <-- 添加这个注解
public class CustomerProfileSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerProfileSystemApplication.class, args);
	}

}