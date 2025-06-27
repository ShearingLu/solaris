package com.smart.pay.timer.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.common.tools.DateUtil;
import com.smart.pay.timer.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
@EnableScheduling
public class MerchantTransactionStatisController {

	
	private static final Logger          LOG = LoggerFactory.getLogger(MerchantTransactionStatisController.class);
	
	
	
	@Autowired
	private Util util;
	
	
	/**每隔五分钟统计一下交易量**/
	@Scheduled(cron = "0 0/5 * * * ?")
    public void scheduler() {

	
	/***查询目前商户所有通道，然后循环查询每个通道的交易量， 然后再更新到数据库中间去 */
		URI uri = util.getServiceUrl("smartmerchant", "error url request!");
        String url = uri.toString() + "/smartmerchant/allmerchant/channel/query";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);

		JSONArray resultMerchantObj = resultObj.getJSONArray("result");
		if(!resultMerchantObj.isEmpty()) {
			for(int i=0; i<resultMerchantObj.size(); i++) {
				JSONObject tmpObject = resultMerchantObj.getJSONObject(i);
				
				String merchantId = tmpObject.getString("merchantId");
				String channelcode = tmpObject.getString("channelCode");
				
				SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
				
				String startDate = sdf.format(new Date());
				
				Date endDate = DateUtil.getTomorrow(new Date());
				
				String endDatestr = sdf.format(endDate);
				
				uri = util.getServiceUrl("smartclearing", "error url request!");
		        url = uri.toString() + "/smartclearing/merchant/sumamount/query";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        requestEntity.add("start_time", startDate);
		        requestEntity.add("end_time", endDatestr);
		        requestEntity.add("channel_code", channelcode);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
				resultObj = JSONObject.fromObject(result);
				String amount = resultObj.getString("result");
				
				
				uri = util.getServiceUrl("smartmerchant", "error url request!");
		        url = uri.toString() + "/smartmerchant/merchant/transactionstatis/create";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        requestEntity.add("transaction_amount", amount);
		        requestEntity.add("channel_code", channelcode);
		        requestEntity.add("statis_date", startDate);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
				
				
				
				
			}
			
			
			
		}
		
		
    }
	
	
	 @Scheduled(cron = "0 0/8 * * * ?")
	 public void dayEquipmentTradescheduler() {
		 LOG.info("设备统计交易开始");
		 
		 URI uri = util.getServiceUrl("smartclearing", "error url request!");
	     String url = uri.toString() + "/smartclearing/equipment/statis/day/update";
	     MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	     new RestTemplate().postForObject(url, requestEntity, String.class);
	     
	     LOG.info("设备统计交易结束");
	 }
	
	
	
	 /***每天零晨3点统计一下前一天的交易量*/
	 @Scheduled(cron = "0 0 3 * * ?")
	 public void dayTradescheduler() {
		 LOG.info("前一天统计交易开始");
		 
		 URI uri = util.getServiceUrl("smartclearing", "error url request!");
	     String url = uri.toString() + "/smartclearing/merchant/statis/day/update";
	     MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	     new RestTemplate().postForObject(url, requestEntity, String.class);
	     LOG.info("前一天统计交易结束");
	 }
	
	 
	 
	 /**每个月的一号凌晨3点开始批处理**/
	 @Scheduled(cron = " 0 0 3 1 * ?")
	 public void monthTradescheduler() {
		 LOG.info("每个月的一号凌晨3点开始");
		 URI uri = util.getServiceUrl("smartclearing", "error url request!");
	     String url = uri.toString() + "/smartclearing/merchant/statis/month/update";
	     MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	     new RestTemplate().postForObject(url, requestEntity, String.class);
	     LOG.info("每个月的一号凌晨3点结束");
	 }
}
