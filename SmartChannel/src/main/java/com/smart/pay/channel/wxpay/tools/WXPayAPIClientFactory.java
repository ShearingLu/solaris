package com.smart.pay.channel.wxpay.tools;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smart.pay.channel.business.WXMerchantBusiness;
import com.smart.pay.channel.pojo.WXMerchant;
/**
 * 微信配置工厂
 * @author Robin-QQ/WX:354476429
 * @date 2018年5月30日
 */
@Component
public class WXPayAPIClientFactory {
	
	@Autowired
	private WXMerchantBusiness wXMerchantBusiness;
	
	private static Map<String,WXPayConfigImpl> wXPayConfigImplMap = new HashMap<String,WXPayConfigImpl>();
	
	public WXPayConfigImpl getWXPayConfigImpl(String channelTag) {
		if (wXPayConfigImplMap.containsKey(channelTag)) {
			return wXPayConfigImplMap.get(channelTag);
		}
		WXPayConfigImpl wXPayConfigImpl = null;
		WXMerchant wXMerchant = wXMerchantBusiness.findByRealChannelCode(channelTag);
		if (wXMerchant != null) {
			wXPayConfigImpl = new WXPayConfigImpl(wXMerchant.getAppid(), wXMerchant.getMchId(), wXMerchant.getPrivateKey());
			wXPayConfigImplMap.put(channelTag, wXPayConfigImpl);
			return wXPayConfigImpl;
		}
		return null;
	}
}
