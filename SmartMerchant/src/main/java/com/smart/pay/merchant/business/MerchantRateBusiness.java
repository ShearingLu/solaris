package com.smart.pay.merchant.business;

import java.util.List;

import com.smart.pay.merchant.pojo.MerchantRate;

public interface MerchantRateBusiness {
	
	public MerchantRate createMerchantRate(MerchantRate merchantRate);
	
	public void delMerchantRate(String merchantId, String channelCode);
	
	public MerchantRate queryMerchantRate(String merchantId, String channelCode);
	
	public List<MerchantRate> queryMerchantRate(String merchantId);
}
