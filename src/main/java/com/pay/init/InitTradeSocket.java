
package com.pay.init;

import org.springframework.stereotype.Service;

import com.pay.trade.handler.TradeHander;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @Title: SocketService.java
 * @Description: 交易请求监听
 * @author: 雷大鹏
 * @date: 2020-02-10 04:38:31
 */
@Service
@Slf4j
public class InitTradeSocket implements Runnable {

	// 监听端口
	private int port = 20001;

	@PostConstruct
	public void start() {
		log.info("===初始化内容,端口监听[" + port + "]===");
		new Thread(new InitTradeSocket()).start();
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				new TradeHander(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
