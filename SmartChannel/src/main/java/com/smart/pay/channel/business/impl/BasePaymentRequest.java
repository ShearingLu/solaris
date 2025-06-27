package com.smart.pay.channel.business.impl;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.channel.util.Util;

import net.sf.json.JSONObject;

@Service
public class BasePaymentRequest {
	
	@Value("${callback.ipAddress}")
	public String callBackIpAddress;
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 全局支付成功返回页面
	 */
	public final String GLOBAL_RETURN_URL = "/smartchannel/pay/success";
	
	
	@Autowired
	private Util util;
	
	
	/**
	 * 根据订单号获得订单信息
	 * @author Robin-QQ/WX:354476429 
	 * @date 2018年4月20日  
	 * @param orderCode
	 * @return
	 */
	public JSONObject getOrderByOrderCode(String orderCode) {
		
		URI uri = util.getServiceUrl("smartclearing", "error url request!");
        String url = uri.toString() + "/smartclearing/transaction/order/query";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("order_code", orderCode);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject resultJSONObject = JSONObject.fromObject(result);
		return resultJSONObject;
	}
	
	/**
	 * 根据订单号orderCode,修改orderStatus和realChannelOrderCode
	 * @author Robin-QQ/WX:354476429 
	 * @date 2018年4月20日  
	 * @param orderCode
	 * @param orderStatus
	 * @param realChannelOrderCode
	 * @return
	 */
	public JSONObject updateOrderByOrderCode(String orderCode,String orderStatus,String realChannelOrderCode) {
		
		LOG.info("ordercode========"+orderCode);
		LOG.info("orderStatus========"+orderStatus);
		LOG.info("realChannelOrderCode========"+realChannelOrderCode);
		URI uri = util.getServiceUrl("smartclearing", "error url request!");
        String url = uri.toString() + "/smartclearing/order/update";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("order_code", orderCode);
		requestEntity.add("order_status", orderStatus);
		requestEntity.add("real_channel_order_code", realChannelOrderCode);
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
		JSONObject resultJSONObject = JSONObject.fromObject(result);
		return resultJSONObject;
	}
	
	public JSONObject getChannelDetil(String realChannelOrderCode) {
		LOG.info("realChannelOrderCode========"+realChannelOrderCode);
		URI uri = util.getServiceUrl("smartpayment", "error url request!");
        String url = uri.toString() + "/smartpayment/platform/pay/realchannel/all";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
		requestEntity.add("real_channel_code", realChannelOrderCode);
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
		JSONObject resultJSONObject = JSONObject.fromObject(result);
		return resultJSONObject;
	}
}
