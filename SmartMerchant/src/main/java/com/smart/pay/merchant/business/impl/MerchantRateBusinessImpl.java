package com.smart.pay.merchant.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.MerchantRateBusiness;
import com.smart.pay.merchant.pojo.MerchantRate;
import com.smart.pay.merchant.repository.MerchantRateRepository;

@Service
public class MerchantRateBusinessImpl implements MerchantRateBusiness{

	
	@Autowired
	private MerchantRateRepository merchantRateRepository;
	
	@Transactional
	@Override
	public MerchantRate createMerchantRate(MerchantRate merchantRate) {
		
		return merchantRateRepository.saveAndFlush(merchantRate);
	}

	@Transactional
	@Override
	public void delMerchantRate(String merchantId, String channelCode) {
		merchantRateRepository.deleteMerchantRateByMerchantId(merchantId, channelCode);
	}

	@Override
	public MerchantRate queryMerchantRate(String merchantId, String channelCode) {
	
		return merchantRateRepository.findMerchantRateByMerchantId(merchantId, channelCode);
	}
	
	public List<MerchantRate> queryMerchantRate(String merchantId){
		
		return merchantRateRepository.findMerchantRateByMerchantId(merchantId);
	}

}
