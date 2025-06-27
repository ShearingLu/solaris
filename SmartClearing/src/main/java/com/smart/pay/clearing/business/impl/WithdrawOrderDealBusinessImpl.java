package com.smart.pay.clearing.business.impl;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.clearing.business.WithdrawOrderDealBusiness;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.clearing.repository.PaymentOrderRepository;
import com.smart.pay.clearing.util.Util;
import com.smart.pay.common.tools.Constss;

@Service
public class WithdrawOrderDealBusinessImpl implements WithdrawOrderDealBusiness{

	@Autowired
	private PaymentOrderRepository  paymentOrderRepository;
	
	
	@Autowired
	private Util util;
	
	@Override
	public Map<String, Object> dealOrder(String ordercode, String orderStatus, String realChannelOrdercode) {
		
		try {
		PaymentOrder paymentOrder =  paymentOrderRepository.findPaymentOrderBycode(ordercode);
		/***/
		if(orderStatus.equalsIgnoreCase("1")) {
			
			 URI uri = util.getServiceUrl("smartmerchant", "error url request!");
	         String url = uri.toString() + "/smartmerchant/merchant/account/unfreezeandupdate";
	         MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	         requestEntity.add("merchant_id", paymentOrder.getMerchantId());
	         requestEntity.add("amount", paymentOrder.getAmount().toString());
	         requestEntity.add("order_code", ordercode);
	         RestTemplate restTemplate = new RestTemplate();
	         restTemplate.postForObject(url, requestEntity, String.class);
			
		}else {
			
			
			 URI uri = util.getServiceUrl("smartmerchant", "error url request!");
	         String url = uri.toString() + "/smartmerchant/merchant/account/unfreeze";
	         MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	         requestEntity.add("merchant_id", paymentOrder.getMerchantId());
	         requestEntity.add("amount", paymentOrder.getAmount().toString());
	         requestEntity.add("order_code", ordercode);
	         RestTemplate restTemplate = new RestTemplate();
	         restTemplate.postForObject(url, requestEntity, String.class);
		}
		
		
		paymentOrder.setOrderStatus(orderStatus);
		paymentOrder.setUpdateTime(new Date());
		paymentOrder.setRealChannelOrderCode(realChannelOrdercode);
		paymentOrderRepository.saveAndFlush(paymentOrder);
		}catch(Exception e) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(Constss.RESP_CODE, Constss.FALIED);
			result.put(Constss.RESP_MESSAGE, "失败");
			result.put(Constss.RESULT, null);
			return result;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constss.RESP_CODE, Constss.SUCCESS);
		result.put(Constss.RESP_MESSAGE, "成功");
		result.put(Constss.RESULT, null);
		return result;
	}

}
