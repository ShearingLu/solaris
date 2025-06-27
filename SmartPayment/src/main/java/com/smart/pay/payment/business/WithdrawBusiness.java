package com.smart.pay.payment.business;

import com.smart.pay.payment.pojo.WithdrawCondition;

public interface WithdrawBusiness {

	public WithdrawCondition createOrUpdateWithdrawCondition(WithdrawCondition withdrawCondition);
	
	
	public WithdrawCondition queryWithdrawCondtion();
	
}
