package com.pay.trade.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pay.basic.exception.BusinessException;
import com.pay.basic.utils.DateUtils;
import com.pay.service.TradeServiceInterface;
import com.pay.utils.CtxUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Title: TradeHander.java
 * @Description: 交易处理入口
 * @author: 雷大鹏
 * @date: 2020-02-10 04:00:36
 */
@Slf4j
public class TradeHander extends Thread {
	private Socket socket;

	public TradeHander(Socket socket) {
		this.socket = socket;
	}

	@SuppressWarnings("unused")
	public void run() {
		try {
			handleSocket();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 根据交易码,获取对应交易处理流程
	 * @param msgTp
	 * @return
	 * @author: 雷大鹏
	 * @date: 2020-02-10 04:16:38
	 */
	public TradeServiceInterface getTradeService(String msgTp) {
		TradeServiceInterface tradeService = null;
		if ("epcc.101.001.01".equals(msgTp)) {
			tradeService = (TradeServiceInterface) CtxUtils.getBean("accountSignServiceImpl");
		} else if ("epcc.201.001.01".equals(msgTp)) {
			tradeService = (TradeServiceInterface) CtxUtils.getBean("tradeServiceImpl");
		}
		return tradeService;
	}

	/**
	 * 跟客户端Socket进行通信
	 * 
	 * @throws Exception
	 */
	private void handleSocket() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		String line;
		try {
			line = br.readLine();
			log.info("===>接收报文===[" + line + "]");
			Map reqParamsMap = JSON.parseObject(line, Map.class);
			String msgTp = (String) reqParamsMap.get("MsgTp");
			String responseData;
			try {
				// 交易处理流程
				responseData = getTradeService(msgTp).doTrade(reqParamsMap);
			} catch (BusinessException e) {
				log.error("===业务异常===", e);
				Map returnMap = new HashMap();
				returnMap.put("SysRtnCd", "PS00098");
				returnMap.put("SysRtnDesc", "交易失败[" + e.getErrorMsg() + "]");
				returnMap.put("SysRtnTm", DateUtils.getTradeDate());
				returnMap.put("SgnNo", reqParamsMap.get("SgnNo"));
				responseData = JSON.toJSONString(returnMap);
			} catch (Exception e) {
				log.error("===系统异常===", e);
				Map returnMap = new HashMap();
				returnMap.put("SysRtnCd", "PS00098");
				returnMap.put("SysRtnDesc", "交易失败[" + e.getMessage() + "]");
				returnMap.put("SysRtnTm", DateUtils.getTradeDate());
				returnMap.put("SgnNo", reqParamsMap.get("SgnNo"));
				responseData = JSON.toJSONString(returnMap);
			}
			log.info("<===返回报文===[" + responseData + "]");
			out.println(responseData);
			out.flush();
			out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}