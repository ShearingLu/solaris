package com.smart.pay.merchant.business;

import com.smart.pay.merchant.pojo.MerchantChannelTransactionStatis;

public interface MerchantTransactionStatisBusiness {

	
	/**创建一个交易统计**/
	public  MerchantChannelTransactionStatis  createMerchantChannelTransactionStatis(MerchantChannelTransactionStatis statis);
	
	
	/***/
	public MerchantChannelTransactionStatis queryMerchantChannelTransactionStatis(String merchantId,  String channelCode, String statisDate);
	
}
