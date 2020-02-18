package com.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @Title: WebApplication.java
 * @Description: @EnableTransactionManagement开始事务
 * @author: 雷大鹏
 * @date: 2020-02-03 08:53:17
 */
@SpringBootApplication
@EnableTransactionManagement
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}
