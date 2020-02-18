package com.pay.dao;

import com.pay.entity.BalanceInfo;
import com.pay.entity.BalanceInfoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BalanceInfoMapper {
	long countByExample(BalanceInfoExample example);

	int deleteByPrimaryKey(String cardNo);

	int insert(BalanceInfo record);

	int insertSelective(BalanceInfo record);

	List<BalanceInfo> selectByExample(BalanceInfoExample example);

	BalanceInfo selectByPrimaryKey(String cardNo);

	int updateByPrimaryKeySelective(BalanceInfo record);

	int updateByPrimaryKey(BalanceInfo record);
}