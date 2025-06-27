package com.smart.pay.merchant.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.MerchantTransactionStatisBusiness;
import com.smart.pay.merchant.pojo.MerchantChannelTransactionStatis;
import com.smart.pay.merchant.repository.MerchantChannelStatisRepository;

@Service
public class MerchantTransactionStatisBusinessImpl  implements MerchantTransactionStatisBusiness{

	
	@Autowired
	private MerchantChannelStatisRepository merchantChannelStatisRepository;
	
	@Transactional
	@Override
	public MerchantChannelTransactionStatis createMerchantChannelTransactionStatis(
			MerchantChannelTransactionStatis statis) {
		
		return merchantChannelStatisRepository.saveAndFlush(statis);
	}

	@Override
	public MerchantChannelTransactionStatis queryMerchantChannelTransactionStatis(String merchantId, String channelCode,
			String statisDate) {
		return merchantChannelStatisRepository.findMerchantChannelTransactionStatis(merchantId, channelCode, statisDate);
	}

}
