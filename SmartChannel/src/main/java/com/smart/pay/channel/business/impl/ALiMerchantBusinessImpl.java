package com.smart.pay.channel.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.pay.channel.business.ALiMerchantBusiness;
import com.smart.pay.channel.pojo.ALiMerchant;
import com.smart.pay.channel.repository.ALiMerchantRepository;

@Service
public class ALiMerchantBusinessImpl implements ALiMerchantBusiness {
	
	@Autowired
	private ALiMerchantRepository aLiMerchantRepository;

	@Override
	public ALiMerchant findByRealChannelCode(String channelTag) {
		return aLiMerchantRepository.findByRealChannelCode(channelTag);
	}

	@Override
	public ALiMerchant findByAppid(String appid) {
		return aLiMerchantRepository.findByAppid(appid);
	}

	@Override
	public ALiMerchant findALiMerchantByAppidAndrealType(String appid, String realtype) {
		return aLiMerchantRepository.findALiMerchantByAppidAndrealType(appid, realtype);
	}
	
}
