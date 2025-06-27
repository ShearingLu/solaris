package com.smart.pay.channel.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.pay.channel.business.WXMerchantBusiness;
import com.smart.pay.channel.pojo.WXMerchant;
import com.smart.pay.channel.repository.WXMerchantRepository;

@Service
public class WXMerchantBusinessImpl implements WXMerchantBusiness{

	@Autowired
	private WXMerchantRepository wXMerchantRepository;
	
	@Override
	public WXMerchant findByRealChannelCode(String channelTag) {
		return wXMerchantRepository.findByRealChannelCode(channelTag);
	}

	@Override
	public WXMerchant findByAppid(String appid) {
		return wXMerchantRepository.findByAppid(appid);
	}

	@Override
	public WXMerchant findByAppidAndRealType(String appid, String realtype) {
		return wXMerchantRepository.findByAppidAndRealType(appid,realtype);
	}
	
}
