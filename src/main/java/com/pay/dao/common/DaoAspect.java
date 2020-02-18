package com.pay.dao.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Title: DaoAspect.java
 * @Description: 数据操作切面,主要计算交易耗时
 * @author: 雷大鹏
 * @date: 2020-02-03 08:54:15
 */
@Aspect
@Component
@Slf4j
public class DaoAspect {

	private ThreadLocal<Map> tlocal = new ThreadLocal<Map>();

	@Pointcut("execution(public * com.pay.dao..*.*(..))")
	public void daoAspect() {
	}

	@Before("daoAspect()")
	public void doBefore(JoinPoint joinPoint) {
		try {
			long beginTime = System.currentTimeMillis();

			Map optLog = new HashMap();
			optLog.put("dao_beginTime", beginTime);
			tlocal.set(optLog);
		} catch (Exception e) {
			log.error("***请求报文分析失败doBefore()***", e);
		}
	}

	@AfterReturning(returning = "result", pointcut = "daoAspect()")
	public void doAfterReturning(Object result) {
		Map optLog = tlocal.get();
		try {
			// 处理完请求，返回内容

			long beginTime = (long) optLog.get("dao_beginTime");
			log.info("<--数据操作层逻辑,开始时间[" + beginTime + "],耗时：[" + (System.currentTimeMillis() - beginTime) + "]");

		} catch (Exception e) {
			log.error("***响应报文分析失败doAfterReturning()***", e);
		}
		optLog = null;
	}

}