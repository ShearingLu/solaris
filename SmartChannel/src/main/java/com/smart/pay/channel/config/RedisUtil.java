package com.smart.pay.channel.config;

import com.smart.pay.channel.business.OrderParameterBusiness;
import com.smart.pay.channel.pojo.OrderParameter;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.pojo.PaymentRequestParameter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private OrderParameterBusiness orderParameterBusiness;
	
	public void savePaymentRequestParameter(String key, PaymentOrder value){
		redisTemplate.opsForValue().set(key, value,60*60, TimeUnit.SECONDS);
		
		JSONObject orderJson = JSONObject.fromObject(value);
		OrderParameter orderParameter = new OrderParameter();
		orderParameter.setOrderCode(key);
		orderParameter.setOrderJson(orderJson.toString());
		orderParameterBusiness.save(orderParameter);
	}
	
	public PaymentOrder getPaymentRequestParameter(String orderCode){
		PaymentOrder result = (PaymentOrder) redisTemplate.opsForValue().get(orderCode);
		if (result == null) {
			OrderParameter orderParameter = orderParameterBusiness.findByOrderCode(orderCode);
			if (orderParameter == null) {
				return result;
			}
			String orderJson = orderParameter.getOrderJson();
			JSONObject jsonObject = JSONObject.fromObject(orderJson);
			return (PaymentOrder) JSONObject.toBean(jsonObject, PaymentOrder.class);
		}else {
			return result;
		}
	}
//	/**
//	 * 写入缓存
//	 *
//	 * @param key
//	 * @param value
//	 * @return
//	 */
//	public boolean set(String key, KQRegister value) {
//		boolean result = false;
//		try {
//			redisTemplate.opsForValue().set(key, value, 60 * 2, TimeUnit.SECONDS);
//			result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * 读取缓存
//	 *
//	 * @param key
//	 * @return
//	 */
//	public List<KQRegister> getKq(String key) {
//		List<KQRegister> result = (List<KQRegister>) redisTemplate.opsForValue().get(key);
//		if (result == null) {
//			return null;
//		}
//		return result;
//	}
	
}
