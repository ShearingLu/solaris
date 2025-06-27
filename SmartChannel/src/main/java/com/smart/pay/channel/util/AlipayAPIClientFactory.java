/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.smart.pay.channel.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.smart.pay.channel.business.ALiMerchantBusiness;
import com.smart.pay.channel.pojo.ALiMerchant;


/**
 * API调用客户端工厂
 * 
 * @author taixu.zqq
 * @version $Id: AlipayAPIClientFactory.java, v 0.1 2014年7月23日 下午5:07:45 taixu.zqq Exp $
 */
@Component
public class AlipayAPIClientFactory {

	@Autowired
	private ALiMerchantBusiness aLiMerchantBusiness;
    
    private static Map<String,AlipayClient> alipayClientMap = new HashMap<String,AlipayClient>();
    
    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public AlipayClient getAlipayClient(String channelTag){
    	if(alipayClientMap.containsKey(channelTag)) {
    		return alipayClientMap.get(channelTag);
    	}
    	AlipayClient alipayClient = null;
    	ALiMerchant aLiMerchant = aLiMerchantBusiness.findByRealChannelCode(channelTag);
    	if(aLiMerchant != null) {
            alipayClient = new DefaultAlipayClient(AlipayServiceEnvConstants.ALIPAY_GATEWAY, aLiMerchant.getAppid(), 
            		aLiMerchant.getPrivateKey(), "json", AlipayServiceEnvConstants.CHARSET,aLiMerchant.getaLiPublicKey(), AlipayServiceEnvConstants.SIGN_TYPE);
            alipayClientMap.put(channelTag, alipayClient);
            return alipayClient;
    	}
    	return null;
    }
}
