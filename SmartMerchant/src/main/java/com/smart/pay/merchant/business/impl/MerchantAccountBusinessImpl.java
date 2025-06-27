package com.smart.pay.merchant.business.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.MerchantAccountBusiness;
import com.smart.pay.merchant.pojo.MerchantAccount;
import com.smart.pay.merchant.pojo.MerchantAccountFreezeHistory;
import com.smart.pay.merchant.pojo.MerchantAccountHistory;
import com.smart.pay.merchant.repository.MerchantAccountFreezeHistoryRepository;
import com.smart.pay.merchant.repository.MerchantAccountHistoryRepository;
import com.smart.pay.merchant.repository.MerchantAccountRepository;

@Service
public class MerchantAccountBusinessImpl implements  MerchantAccountBusiness{

	@Autowired
	private MerchantAccountRepository merchantAccountRepository;
	
	
	@Autowired
	private MerchantAccountHistoryRepository merchantAccountHistoryRepository;
	
	
	@Autowired
	private MerchantAccountFreezeHistoryRepository merchantAccountFreezeHistoryRepository;




	@Override
	public MerchantAccount queryMerchantAccountByMerchantId(String merchantId) {
		return merchantAccountRepository.findMerchantAccountByMerchantId(merchantId);
	}



	@Override
	public Page<MerchantAccountHistory> pageMerchantAccountHistory(String merchantId, Date startTime, Date endTime,
			Pageable pageAble) {
		
		
		
		if(startTime == null) {
			
			return merchantAccountHistoryRepository.findMerchantAccountHistoryByMerchantId(merchantId, pageAble);
			
		}else {
			
			if(endTime != null) {
				
				return merchantAccountHistoryRepository.findMerchantAccountHistoryByMerchantIdByStartEndTime(merchantId, startTime, endTime, pageAble);
			
			}else {
				
				return merchantAccountHistoryRepository.findMerchantAccountHistoryByMerchantIdByStartTime(merchantId, startTime, pageAble);
				
			}
		}
		

	}



	@Transactional
	@Override
	public MerchantAccount addCollectMoney(String merchantId, BigDecimal amount, String ordercode, String fromType) {
		
		MerchantAccount merchantAccount = merchantAccountRepository.findMerchantAccountByMerchantIdLock(merchantId);
		
		
		/**新增一条变动历史*/
		MerchantAccountHistory  merchantAccountHistory = new MerchantAccountHistory();
		merchantAccountHistory.setAddOrSub("0");  //增加
		merchantAccountHistory.setAmount(amount);
		merchantAccountHistory.setCreateTime(new Date());
		merchantAccountHistory.setFromType(fromType);
		merchantAccountHistory.setCurAmount(merchantAccount.getAmount().add(amount));
		merchantAccountHistory.setMerchantId(merchantId);
		merchantAccountHistory.setOrderCode(ordercode);
		merchantAccountHistory.setUpdateTime(new Date());
		merchantAccountHistoryRepository.saveAndFlush(merchantAccountHistory);
		
		merchantAccount.setAmount(merchantAccountHistory.getCurAmount());
		merchantAccount.setUpdateTime(new Date());
		merchantAccountRepository.saveAndFlush(merchantAccount);
		
		return merchantAccount;

	}


	@Transactional
	@Override
	public MerchantAccount addNewWithdraw(String merchantId, BigDecimal amount, String ordercode) {
		
		MerchantAccount merchantAccount = merchantAccountRepository.findMerchantAccountByMerchantIdLock(merchantId);
		
		/**新增一笔提现历史**/
		MerchantAccountFreezeHistory accountFreezeHistory = new MerchantAccountFreezeHistory();
		accountFreezeHistory.setAddOrSub("0");  //冻结
		accountFreezeHistory.setFreezeAmount(amount);
		accountFreezeHistory.setCreateTime(new Date());
		accountFreezeHistory.setCurFreezeAmount(merchantAccount.getFreezeAmount().add(amount));
		accountFreezeHistory.setFromType("0");  //提现
		accountFreezeHistory.setMerchantId(merchantId);
		accountFreezeHistory.setOrderCode(ordercode);
		accountFreezeHistory.setUpdateTime(new Date());
		merchantAccountFreezeHistoryRepository.saveAndFlush(accountFreezeHistory);
		
		
		merchantAccount.setFreezeAmount(merchantAccount.getFreezeAmount().add(amount));
		merchantAccount.setAmount(merchantAccount.getAmount().subtract(amount));
		merchantAccount.setUpdateTime(new Date());
		merchantAccountRepository.saveAndFlush(merchantAccount);
		
		return merchantAccount;
	}


	@Transactional
	@Override
	public MerchantAccount unFreezeAmount(String merchantId, BigDecimal unfreezeAmount, String ordercode) {
		
		MerchantAccount merchantAccount = merchantAccountRepository.findMerchantAccountByMerchantIdLock(merchantId);
		
		
		/**新增一笔解冻历史**/
		MerchantAccountFreezeHistory accountFreezeHistory = new MerchantAccountFreezeHistory();
		accountFreezeHistory.setAddOrSub("1");  //解冻
		accountFreezeHistory.setFreezeAmount(unfreezeAmount);
		accountFreezeHistory.setCreateTime(new Date());
		accountFreezeHistory.setCurFreezeAmount(merchantAccount.getFreezeAmount().subtract(unfreezeAmount));
		accountFreezeHistory.setFromType("0");  //提现
		accountFreezeHistory.setMerchantId(merchantId);
		accountFreezeHistory.setOrderCode(ordercode);
		accountFreezeHistory.setUpdateTime(new Date());
		merchantAccountFreezeHistoryRepository.saveAndFlush(accountFreezeHistory);
		
		
		merchantAccount.setFreezeAmount(merchantAccount.getFreezeAmount().subtract(unfreezeAmount));
		merchantAccount.setAmount(merchantAccount.getAmount().add(unfreezeAmount));
		merchantAccount.setUpdateTime(new Date());
		merchantAccountRepository.saveAndFlush(merchantAccount);
		
		return merchantAccount;
	}


	@Transactional
	@Override
	public MerchantAccount unFreezeAndUpdateAccount(String merchantId, BigDecimal amount, String ordercode) {
		
		MerchantAccount merchantAccount = merchantAccountRepository.findMerchantAccountByMerchantIdLock(merchantId);
		
		/**新增一笔解冻历史**/
		MerchantAccountFreezeHistory accountFreezeHistory = new MerchantAccountFreezeHistory();
		accountFreezeHistory.setAddOrSub("1");  //解冻
		accountFreezeHistory.setFreezeAmount(amount);
		accountFreezeHistory.setCreateTime(new Date());
		accountFreezeHistory.setCurFreezeAmount(merchantAccount.getFreezeAmount().subtract(amount));
		accountFreezeHistory.setFromType("0");  //提现
		accountFreezeHistory.setMerchantId(merchantId);
		accountFreezeHistory.setOrderCode(ordercode);
		accountFreezeHistory.setUpdateTime(new Date());
		merchantAccountFreezeHistoryRepository.saveAndFlush(accountFreezeHistory);
		
		
		/**一条变动历史*/
		MerchantAccountHistory  merchantAccountHistory = new MerchantAccountHistory();
		merchantAccountHistory.setAddOrSub("1"); //减少
		merchantAccountHistory.setAmount(amount);
		merchantAccountHistory.setCreateTime(new Date());
		merchantAccountHistory.setFromType("1");  //提现
		merchantAccountHistory.setCurAmount(merchantAccount.getAmount());
		merchantAccountHistory.setMerchantId(merchantId);
		merchantAccountHistory.setOrderCode(ordercode);
		merchantAccountHistory.setUpdateTime(new Date());
		merchantAccountHistoryRepository.saveAndFlush(merchantAccountHistory);
		
		merchantAccount.setFreezeAmount(merchantAccount.getFreezeAmount().subtract(amount));
		merchantAccount.setUpdateTime(new Date());
		merchantAccountRepository.saveAndFlush(merchantAccount);
		
		return merchantAccount;
	}

}
