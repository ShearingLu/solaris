package com.smart.pay.clearing.business.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.clearing.business.PaymentOrderDealBusiness;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.clearing.pojo.ProfitRecord;
import com.smart.pay.clearing.repository.PaymentOrderRepository;
import com.smart.pay.clearing.repository.PlatformProfitRecordRepository;
import com.smart.pay.clearing.util.Util;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.MD5;
import com.smart.pay.common.tools.SignUtils;

import net.sf.json.JSONObject;

@Service
public class PaymentOrderDealBusinessImpl implements PaymentOrderDealBusiness{
	
	
	private static final Logger               LOG = LoggerFactory.getLogger(PaymentOrderDealBusinessImpl.class);
	
	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	
	
	@Autowired
	private PlatformProfitRecordRepository  profitRecordRepository;
	
	@Autowired
	private Util util;
	

	
	@Transactional
	@Override
	public Map<String, Object> dealOrder(String ordercode, String orderStatus, String realChannelOrdercode) {
		LOG.info("进入回调处理了"+ordercode);
		try {
		PaymentOrder paymentOrder =  paymentOrderRepository.findPaymentOrderBycode(ordercode);
		/***/
		if(orderStatus.equalsIgnoreCase("1")) {
			
			 String merchantid  = paymentOrder.getMerchantId();
			 
			 /**给商户添加一笔收款**/
			 URI uri = util.getServiceUrl("smartmerchant", "error url request!");
	         String url = uri.toString() + "/smartmerchant/merchant/collect";
	         MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	         requestEntity.add("merchant_id", merchantid);
	         requestEntity.add("amount", paymentOrder.getRealAmount().toString());
	         requestEntity.add("from_type", "0");
	         requestEntity.add("order_code", ordercode);
	         RestTemplate restTemplate = new RestTemplate();
	         String result = restTemplate.postForObject(url, requestEntity, String.class);
			 
			 
			 String parentId    = paymentOrder.getParentId();
			 BigDecimal channelRate = paymentOrder.getMerRate();
			 BigDecimal extraFee    = paymentOrder.getExtraFee(); 
			 String channelCode = paymentOrder.getChannelCode();
			 String channelName = paymentOrder.getChannelName();
			 BigDecimal amount = paymentOrder.getAmount();
			 while(true) {
				 
					 uri = util.getServiceUrl("smartmerchant", "error url request!");
			         url = uri.toString() + "/smartmerchant/merchant/id";
			         requestEntity = new LinkedMultiValueMap<String, String>();
			         requestEntity.add("merchant_id", parentId);
			         restTemplate = new RestTemplate();
			         result = restTemplate.postForObject(url, requestEntity, String.class);
			         LOG.info("RESULT================" + result);
			         JSONObject jsonObject = JSONObject.fromObject(result);
			         JSONObject resultObj = jsonObject.getJSONObject("result");
					 String merchantname = resultObj.getString("merchantName");
					 String newParentId  = resultObj.getString("parent");
					 
					 if(!parentId.equalsIgnoreCase("999999")) {
						 LOG.info("进入普通用户处理"+parentId);
						 uri = util.getServiceUrl("smartmerchant", "error url request!");
				         url = uri.toString() + "/smartmerchant/merchant/rate/id";
				         requestEntity = new LinkedMultiValueMap<String, String>();
				         requestEntity.add("merchant_id", parentId);
				         requestEntity.add("channel_code", channelCode);
				         restTemplate = new RestTemplate();
				         result = restTemplate.postForObject(url, requestEntity, String.class);
				         LOG.info("RESULT================" + result);
				         jsonObject = JSONObject.fromObject(result);
				         resultObj = jsonObject.getJSONObject("result");
				         
				         if(!resultObj.isEmpty()) {
					        	
				        	 String curRate = resultObj.getString("rate");
				        	 String curextraFee = resultObj.getString("extraFee");
				        	
				        	 BigDecimal profit = BigDecimal.ZERO;
				        	 if(new BigDecimal(curRate).compareTo(channelRate) < 0 ) {
				        		 
				        		 profit = amount.multiply(channelRate.subtract(new BigDecimal(curRate)));
				        		 
				        	 }
				        	 
				        	 
				        	 if(new BigDecimal(curextraFee).compareTo(extraFee) < 0 ) {
				        		 profit = profit.add(extraFee.subtract(new BigDecimal(curextraFee)));
				        	 }
				        	 
				        	 if(profit.compareTo(BigDecimal.ZERO) > 0) {
					        	 /***产生一笔分润*/
					        	 ProfitRecord profitRecord = new ProfitRecord();
					        	 profitRecord.setAmount(amount);
					        	 profitRecord.setChannelCode(channelCode);
					        	 profitRecord.setChannelName(channelName);
					        	 profitRecord.setCreateTime(new Date());
					        	 profitRecord.setMerchantId(parentId);
					        	 profitRecord.setMerchantName(merchantname);
					        	 profitRecord.setMerExtraFee(extraFee);
					        	 profitRecord.setMerRate(channelRate);
					        	 profitRecord.setOrderCode(ordercode);
					        	 profitRecord.setOrderMerchantId(paymentOrder.getMerchantId());
					        	 profitRecord.setOwnExtraFee(new BigDecimal(curextraFee));
					        	 profitRecord.setOwnRate(new BigDecimal(curRate));
					        	 profitRecord.setOwnIncome(profit);
					        	 profitRecord.setRealChannelCode(paymentOrder.getRealChannelCode());
					        	 profitRecord.setRealChannelName(paymentOrder.getRealChannelName());
					        	 profitRecordRepository.saveAndFlush(profitRecord);
					        
					        	 /**更新账户**/
					        	 uri = util.getServiceUrl("smartmerchant", "error url request!");
						         url = uri.toString() + "/smartmerchant/merchant/collect";
						         requestEntity = new LinkedMultiValueMap<String, String>();
						         requestEntity.add("merchant_id", parentId);
						         requestEntity.add("amount", profit.toString());
						         requestEntity.add("from_type", "0");     //0  表示收款  2表示分润收入 
						         requestEntity.add("order_code", ordercode);
						         restTemplate = new RestTemplate();
						         result = restTemplate.postForObject(url, requestEntity, String.class);
				        	 
				        	 }
				        	 
				        	 channelRate = new BigDecimal(curRate);
				        	 extraFee  = new BigDecimal(curextraFee);
				         
				         }
				         
					 }else {
						 
						 LOG.info("进入超级用户处理");
						 String curRate = paymentOrder.getRealChannelRate().toString();
			        	 String curextraFee = paymentOrder.getRealChannelExtraFee().toString();
			        	
			        	 BigDecimal profit = BigDecimal.ZERO;
			        	 if(new BigDecimal(curRate).compareTo(channelRate) < 0 ) {
			        		 
			        		 profit = amount.multiply(channelRate.subtract(new BigDecimal(curRate)));
			        		 
			        	 }
			        	 
			        	 
			        	 if(new BigDecimal(curextraFee).compareTo(extraFee) < 0 ) {
			        		 profit = profit.add(extraFee.subtract(new BigDecimal(curextraFee)));
			        	 }
			        	 
			        	 if(profit.compareTo(BigDecimal.ZERO) > 0) {
				        	 /***产生一笔分润*/
				        	 ProfitRecord profitRecord = new ProfitRecord();
				        	 profitRecord.setAmount(amount);
				        	 profitRecord.setChannelCode(channelCode);
				        	 profitRecord.setChannelName(channelName);
				        	 profitRecord.setCreateTime(new Date());
				        	 profitRecord.setMerchantId(parentId);
				        	 profitRecord.setMerchantName(merchantname);
				        	 profitRecord.setMerExtraFee(extraFee);
				        	 profitRecord.setMerRate(channelRate);
				        	 profitRecord.setOrderCode(ordercode);
				        	 profitRecord.setOrderMerchantId(paymentOrder.getMerchantId());
				        	 profitRecord.setOwnExtraFee(new BigDecimal(curextraFee));
				        	 profitRecord.setOwnRate(new BigDecimal(curRate));
				        	 profitRecord.setOwnIncome(profit);
				        	 profitRecord.setRealChannelCode(paymentOrder.getRealChannelCode());
				        	 profitRecord.setRealChannelName(paymentOrder.getRealChannelName());
				        	 profitRecordRepository.saveAndFlush(profitRecord);
				        
				        	 /**更新账户**/
				        	 uri = util.getServiceUrl("smartmerchant", "error url request!");
					         url = uri.toString() + "/smartmerchant/merchant/collect";
					         requestEntity = new LinkedMultiValueMap<String, String>();
					         requestEntity.add("merchant_id", parentId);
					         requestEntity.add("amount", profit.toString());
					         requestEntity.add("from_type", "0");     //0  表示收款  2表示分润收入 
					         requestEntity.add("order_code", ordercode);
					         restTemplate = new RestTemplate();
					         result = restTemplate.postForObject(url, requestEntity, String.class);
			        	 
			        	 }
			        	 
			        	 channelRate = new BigDecimal(curRate);
			        	 extraFee  = new BigDecimal(curextraFee);
						 
						 
					 }
			         
			         
			         
			         if(parentId != null && !parentId.equalsIgnoreCase("999999")) {  //还没有到贴牌
			        	 parentId = newParentId;
			        	 continue;
			         }else {
			        	 break;
			        	 
			         }
			 }	
		}
		
		
		paymentOrder.setOrderStatus(orderStatus);
		paymentOrder.setUpdateTime(new Date());
		paymentOrder.setRealChannelOrderCode(realChannelOrdercode);
		paymentOrderRepository.saveAndFlush(paymentOrder);
		LOG.info("回调订单处理完成");
		/**
		 * 朝数据添加一笔回调记录
		 * 
		 * **/
		if(paymentOrder.getMerOrderCode() != null && !paymentOrder.getMerOrderCode().equalsIgnoreCase("")
				
				&& paymentOrder.getNotifyURL() != null &&  !paymentOrder.getNotifyURL().equalsIgnoreCase("")
				) {
			
			/**直接回调商家， 如果超时活着么有接受到正常的响应， 存入数据库中间去 **/
			doNotify(paymentOrder);
			
		}
		}catch(Exception e) {
			LOG.info("回调处理异常"+e.getMessage());
			e.printStackTrace();
			
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constss.RESP_CODE, Constss.SUCCESS);
		result.put(Constss.RESP_MESSAGE, "成功");
		result.put(Constss.RESULT, null);
		return result;
	}
	
	
	
	public   void doNotify(PaymentOrder paymentOrder) {
		
		Map<String, String> resultParams = new HashMap<String, String>();
		resultParams.put("version", "1.0");
		resultParams.put("charset", "UTF-8");
		resultParams.put("sign_type", "MD5");
		resultParams.put("status", "0");
		resultParams.put("result_code", "0");
		resultParams.put("merchant_id", paymentOrder.getMerchantId());
		resultParams.put("nonce_str", String.valueOf(new Date().getTime()));
		resultParams.put("trade_type", paymentOrder.getChannelCode());
		resultParams.put("pay_result", "0");
		resultParams.put("transaction_id", paymentOrder.getOrderCode());
		resultParams.put("out_transaction_id", paymentOrder.getRealChannelOrderCode());
		resultParams.put("out_trade_no", paymentOrder.getMerOrderCode());
		resultParams.put("total_amount", paymentOrder.getAmount().toString());
		resultParams.put("real_amount", paymentOrder.getAmount().toString());
		resultParams.put("fee_type", "CNY");
		if(paymentOrder.getAttach() != null && !paymentOrder.getAttach().equalsIgnoreCase("")) {
			resultParams.put("attach", paymentOrder.getAttach());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		resultParams.put("time_end", sdf.format(new Date()));
		Map<String,String> params = SignUtils.paraFilter(resultParams);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        
        
        URI uri = util.getServiceUrl("smartmerchant", "error url request!");
        String url = uri.toString() + "/smartmerchant/merchant/info";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentOrder.getMerchantId());
        requestEntity.add("key_type", "MD5");
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);

		JSONObject resultMerchantObj = resultObj.getJSONObject("result");
		String key = resultMerchantObj.getString("key");
        
        
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        resultParams.put("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
	    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
	    headers.setContentType(type);
	    JSONObject jsonObject = JSONObject.fromObject(resultParams);
	    String requestJson = jsonObject.toString();
	    HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	    try {
		    String notifyresult = new RestTemplate().postForObject(paymentOrder.getNotifyURL(), entity, String.class);
		    System.out.println(result);
			if(notifyresult != null && !notifyresult.equalsIgnoreCase("success")) {
				saveCallBack(paymentOrder.getMerchantId(), paymentOrder.getNotifyURL(), requestJson);
				
			}
		
	    
	    }catch(Exception e) {
			
	    	saveCallBack(paymentOrder.getMerchantId(), paymentOrder.getNotifyURL(), requestJson);
	    	LOG.info("callback exception==========="+e.getMessage());
		}
		
	}
	
	
	public void saveCallBack(String merchantid,  String notifyURL, String jsonParams) {
		/*URI uri = util.getServiceUrl("smarttimer", "error url request!");
        String url = uri.toString() + "/smarttimer/callback/add";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantid);
        requestEntity.add("notify_url", notifyURL);
        requestEntity.add("call_params", jsonParams);
        new RestTemplate().postForObject(url, requestEntity, String.class);*/
		
		createNotifyRecord(merchantid, notifyURL, jsonParams);
		
	}



	@Transactional
	@Override
	public void createNotifyRecord(String merchantid, String notifyurl, String callparams) {
		// TODO Auto-generated method stub
		paymentOrderRepository.insertTimers(merchantid, "1", notifyurl, callparams, 24, new Date(), new Date());
	}
	
	
	/*public static void main(String[] args) {
		
		Map<String, String> resultParams = new HashMap<String, String>();
		resultParams.put("version", "1.0");
		resultParams.put("charset", "UTF-8");
		resultParams.put("sign_type", "MD5");
		resultParams.put("status", "0");
		resultParams.put("result_code", "0");
		resultParams.put("merchant_id", "7612634878");
		resultParams.put("nonce_str", "1527757388493");
		resultParams.put("trade_type", "pay.alipay.wappay");
		resultParams.put("pay_result", "0");
		resultParams.put("transaction_id", "sgvoh7xxxmtiegor2c8f");
		resultParams.put("out_transaction_id", "2018053121001004940511025078");
		resultParams.put("out_trade_no", "PY20180531165951ZT8FP");
		resultParams.put("total_amount", "155.00");
		resultParams.put("real_amount", "151.90");
		resultParams.put("fee_type", "CNY");
		
		
		resultParams.put("time_end", "20180531170308");
		Map<String,String> params = SignUtils.paraFilter(resultParams);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        
        System.out.println(preStr);
       
		String key = "niqspupk7m9is1s3b6arv5st37wwtvfp";
        
        
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
		System.out.println(sign);
		
	}
*/

	@Transactional
	@Override
	public void handCallBack(PaymentOrder paymentOrder) {
		doNotify(paymentOrder);
	}

}
