package com.smart.pay.payment.business.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.payment.business.WithdrawBusiness;
import com.smart.pay.payment.pojo.WithdrawCondition;
import com.smart.pay.payment.repository.WithdrawConditionRepository;


@Service
public class WithdrawBusinessImpl implements WithdrawBusiness{

	
	@Autowired
	private WithdrawConditionRepository withdrawConditionRepository;
	
	@Transactional
	@Override
	public WithdrawCondition createOrUpdateWithdrawCondition(WithdrawCondition withdrawCondition) {
		List<WithdrawCondition> list = withdrawConditionRepository.findAll();
		WithdrawCondition withdrawdata = null;
		if(list != null && list.size() >0) {
			withdrawdata =  list.get(0);
		}else {
			withdrawdata =  new WithdrawCondition();
		}
		
		withdrawdata.setUpdateTime(new Date());
		withdrawdata.setCreateTime(new Date());
		withdrawdata.setDayLimit(withdrawCondition.getDayLimit());
		withdrawdata.setOuterFee(withdrawCondition.getOuterFee());
		withdrawdata.setSingleLimit(withdrawCondition.getSingleLimit());
		withdrawdata.setSingleMinLimit(withdrawCondition.getSingleMinLimit());
		withdrawConditionRepository.saveAndFlush(withdrawdata);
		return withdrawdata;
	}
	
	
	

	@Override
	public WithdrawCondition queryWithdrawCondtion() {
		
		List<WithdrawCondition> list = withdrawConditionRepository.findAll();
		if(list != null && list.size() >0) {
			return list.get(0);
		}
		
		return null;
	}

}
