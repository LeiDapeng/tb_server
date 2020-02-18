package com.pay.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.pay.basic.exception.BusinessException;
import com.pay.basic.exception.enums.BusinessExceptionTypeEnum;
import com.pay.basic.utils.DateUtils;
import com.pay.dao.BalanceInfoMapper;
import com.pay.entity.BalanceInfo;
import com.pay.entity.BalanceInfoExample;
import com.pay.service.TradeServiceInterface;

/**
 * 
 * @Title: SignTradeServiceImpl.java
 * @Description: 账户签约交易流程
 * @author: 雷大鹏
 * @date: 2020-02-10 04:05:20
 */
@Service
public class TradeServiceImpl implements TradeServiceInterface {
	@Autowired
	BalanceInfoMapper balanceInfoMapper;

	/**
	 * 交易流程
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String doTrade(Map reqParamsMap) {

		String fromAccount = (String) reqParamsMap.get("fromAccount");
		String toAccount = (String) reqParamsMap.get("toAccount");
		String money = (String) reqParamsMap.get("money");
		String error = (String) reqParamsMap.get("error");

		// 收款方账户
		BalanceInfoExample fromAccountExample = new BalanceInfoExample();
		fromAccountExample.createCriteria().andAccountNoEqualTo(fromAccount);
		List<BalanceInfo> list = balanceInfoMapper.selectByExample(fromAccountExample);
		if (list.size() == 0) {
			throw new BusinessException(BusinessExceptionTypeEnum.ACCOUNT_ALREADY, "付款账户[" + fromAccount + "]不存在");
		}
		if (list.get(0).getBalance().doubleValue() < new BigDecimal(money).doubleValue()) {
			throw new BusinessException(BusinessExceptionTypeEnum.ACCOUNT_ALREADY, "付款账户[" + fromAccount + "]余额不足");
		}

		String oldFromBalance = list.get(0).getBalance().toString();
		list.get(0).setBalance(list.get(0).getBalance().subtract(new BigDecimal(money)));

		// 扣除金额
		int count = balanceInfoMapper.updateByPrimaryKey(list.get(0));

		// 新增金额
		BalanceInfoExample toAccountExample = new BalanceInfoExample();
		toAccountExample.createCriteria().andAccountNoEqualTo(toAccount);
		list = balanceInfoMapper.selectByExample(toAccountExample);
		if (list.size() == 0) {
			throw new BusinessException(BusinessExceptionTypeEnum.ACCOUNT_ALREADY, "付款账户[" + fromAccount + "]不存在");
		}
		String oldToBalance = list.get(0).getBalance().toString();
		list.get(0).setBalance(list.get(0).getBalance().add(new BigDecimal(money)));

		count = balanceInfoMapper.updateByPrimaryKey(list.get(0));

		BalanceInfo fromAccontInfo = balanceInfoMapper.selectByExample(fromAccountExample).get(0);
		BalanceInfo toAccontInfo = balanceInfoMapper.selectByExample(toAccountExample).get(0);

		if (error.equals("01")) {
			throw new BusinessException(BusinessExceptionTypeEnum.LIMIT, "事务测试");
		}
		Map returnMap = new HashMap();
		returnMap.put("money", money);
		returnMap.put("fromAccount", fromAccontInfo.getCardNo());
		returnMap.put("fromAccountOldBalance", oldFromBalance);
		returnMap.put("fromAccountBalance", fromAccontInfo.getBalance());

		returnMap.put("toAccount", toAccontInfo.getCardNo());
		returnMap.put("toAccountOldBalance", oldToBalance);
		returnMap.put("toAccountBalance", toAccontInfo.getBalance());

		return JSON.toJSONString(returnMap);
	}
}
