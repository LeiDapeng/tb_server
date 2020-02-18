package com.pay.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
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
public class AccountSignServiceImpl implements TradeServiceInterface {
	@Autowired
	BalanceInfoMapper balanceInfoMapper;

	/**
	 * 交易流程
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String doTrade(Map reqParamsMap) {

		// 验证签约状态
		BalanceInfoExample example = new BalanceInfoExample();
		example.createCriteria().andCardNoEqualTo(reqParamsMap.get("IDNo").toString());
		if (balanceInfoMapper.countByExample(example) != 0) {
			throw new BusinessException(BusinessExceptionTypeEnum.ACCOUNT_ALREADY, "账户[" + reqParamsMap.get("IDNo") + "]已签约");
		}

		// 签约
		BalanceInfo balanceInfo = new BalanceInfo();
		balanceInfo.setCardNo(reqParamsMap.get("IDNo").toString());
		balanceInfo.setAccountNo(reqParamsMap.get("SgnAcctId").toString());
		balanceInfo.setBalance(new BigDecimal(0));
		int count = balanceInfoMapper.insert(balanceInfo);
		Map returnMap = new HashMap();
		returnMap.put("SysRtnCd", "0000");
		returnMap.put("SysRtnDesc", "签约成功");
		returnMap.put("SysRtnTm", DateUtils.getTradeDate());
		returnMap.put("SgnNo", reqParamsMap.get("SgnNo"));
		returnMap.put("BizStsCd", "success");
		returnMap.put("BizStsDesc", "交易成功");
		returnMap.put("SgnAcctIssrId", reqParamsMap.get("IssrId"));
		returnMap.put("SgnAcctTp", reqParamsMap.get("SgnAcctTp"));
		returnMap.put("SgnAcctId", reqParamsMap.get("SgnAcctId"));
		returnMap.put("SgnAcctNm", reqParamsMap.get("SgnAcctNm"));
		returnMap.put("SgnAcctLvl", "一级账户");
		returnMap.put("InstgId", reqParamsMap.get("IssrId"));
		returnMap.put("InstgAcct", reqParamsMap.get("IssrId"));
		returnMap.put("TrxCtgy", reqParamsMap.get("TrxCtgy"));
		returnMap.put("TrxId", reqParamsMap.get("TrxId"));
		returnMap.put("TrxDtTm", reqParamsMap.get("TrxDtTm"));

		return JSON.toJSONString(returnMap);
	}
}
