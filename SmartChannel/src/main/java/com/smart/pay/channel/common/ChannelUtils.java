package com.smart.pay.channel.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChannelUtils.class);
	
	public static final String URL="/v1.0/transactionclear/payment/update";
	
	//public final String updateNewApiurl="/v1.0/transactionclear/payment/update";
	
	public static String getCallBackUrl(String ipAddress) {
		LOG.info("ChannelUtils.getCallBackUrl from ip:"+ipAddress);
		return URL;
	}
}
