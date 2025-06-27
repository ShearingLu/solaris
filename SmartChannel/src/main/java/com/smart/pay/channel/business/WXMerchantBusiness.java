package com.smart.pay.channel.business;

import com.smart.pay.channel.pojo.WXMerchant;

public interface WXMerchantBusiness {

	WXMerchant findByRealChannelCode(String channelTag);

	WXMerchant findByAppid(String string);

	WXMerchant findByAppidAndRealType(String appid, String realtype);

}
