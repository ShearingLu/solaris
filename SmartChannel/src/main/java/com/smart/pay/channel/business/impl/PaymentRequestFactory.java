package com.smart.pay.channel.business.impl;

import com.smart.pay.channel.business.ChannelDetailBusiness;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.pojo.ChannelDetail;
import com.smart.pay.channel.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentRequestFactory {
	
	/**
	 * 支付宝WAP支付通道
	 */
	@Autowired
	private ALiWapPayPaymentRequestBusinessImpl aLiPayPaymentRequestBusinessImpl;
	
	
	@Autowired
	private WXWapPayPaymentRequestBusinessImpl wXWapPayPaymentRequestBusinessImpl;
	
	@Autowired
	private AlipayPersonPayRequestBusinessImpl alipayPersonalPayRequestBusinessImpl;
	
	/**
	 * 支付转账通道
	 */
	@Autowired
	private ALiToAccountPayPaymentRequestBusinessImpl aLiToAccountPayPaymentRequestBusinessImpl;

	@Autowired
	private ChannelDetailBusiness channelDetailBusiness;

	private static Map<String,PaymentRequestBusiness> topRequestMap = new HashMap<>();

	public PaymentRequestBusiness getTopChannelRequestImpl(String channelTag) {
		if (topRequestMap.containsKey(channelTag)) {
			return topRequestMap.get(channelTag);
		}
		ChannelDetail channelDetail = channelDetailBusiness.findByChannelTag(channelTag);
		if (channelDetail != null) {
			PaymentRequestBusiness bean = (PaymentRequestBusiness) SpringContextUtil.getBeanOfType(channelDetail.getBeanName());
			topRequestMap.put(channelTag, bean);
			return bean;
		}else {
			return null;
		}
	}
	
}
