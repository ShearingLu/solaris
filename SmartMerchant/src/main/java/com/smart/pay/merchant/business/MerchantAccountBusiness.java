package com.smart.pay.merchant.business;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.merchant.pojo.MerchantAccount;
import com.smart.pay.merchant.pojo.MerchantAccountHistory;

public interface MerchantAccountBusiness {

	
	/**
	 * 新增一笔收单
	 * 0 代表收款
	 * 2代表分润
	 * 
	 * **/
	public MerchantAccount addCollectMoney(String merchantId, BigDecimal amount,  String ordercode,  String fromType);
	
	
	/**发起一笔提现， 并冻结相应的账户提现额度*/
	public MerchantAccount addNewWithdraw(String merchantId, BigDecimal amount, String ordercode);
	
	
	/**提现失败解冻一笔提现**/
	public MerchantAccount  unFreezeAmount(String merchantId, BigDecimal unfreezeAmount, String ordercode);

	
	/**提现成功解冻一笔提现， 并扣除用户账户**/
	public MerchantAccount  unFreezeAndUpdateAccount(String merchantId, BigDecimal amount, String ordercode);
	


	
	public MerchantAccount queryMerchantAccountByMerchantId(String merchantId);
	
	
	
	public Page<MerchantAccountHistory>  pageMerchantAccountHistory(String merchantId, Date startTime, Date endTime, Pageable pageAble);
	
}
