package com.smart.pay.channel.business;

import com.smart.pay.channel.pojo.ALiMerchant;

public interface ALiMerchantBusiness {

	ALiMerchant findByRealChannelCode(String channelTag);

	ALiMerchant findByAppid(String appid);

	ALiMerchant findALiMerchantByAppidAndrealType(String appid, String realtype);
}
