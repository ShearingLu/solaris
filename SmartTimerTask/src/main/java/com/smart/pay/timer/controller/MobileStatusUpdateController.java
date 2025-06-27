package com.smart.pay.timer.controller;

import java.net.URI;

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

import com.smart.pay.timer.util.HttpApacheClientUtil;
import com.smart.pay.timer.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
@EnableScheduling
public class MobileStatusUpdateController {

	private static final Logger          LOG = LoggerFactory.getLogger(MobileStatusUpdateController.class);
	
	
	@Autowired
	private Util util;
	
	@Scheduled(cron = "0/10 * * * * ?")
	public void scheduler() {
		LOG.info("设备状态更新开始");
		URI uri = util.getServiceUrl("smartchannel", "error url request!");
        String url = uri.toString() + "/smartchannel/mobile/status/update";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        new RestTemplate().postForObject(url, requestEntity, String.class);
        LOG.info("设备状态更新结束");
		
	}
	
	
	@Scheduled(cron = "0 0/3 * * * ?")
	public void scheduleOrder() {
		LOG.info("自动补单机制开始");
		URI uri = util.getServiceUrl("smartclearing", "error url request!");
        String url = uri.toString() + "/smartclearing/order/markup/auto";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        new RestTemplate().postForObject(url, requestEntity, String.class);
        LOG.info("自动补单机制结束");
	}
	
	
	
	@Scheduled(cron = "0 0/2 * * * ?")
	public void onlinePaymentOrder() {
		LOG.info("账单自动补单机制开始");
		URI uri = util.getServiceUrl("smartclearing", "error url request!");
        String url = uri.toString() + "/smartclearing/order/markup/online";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        new RestTemplate().postForObject(url, requestEntity, String.class);
        LOG.info("账单补单机制结束");
	}
	
	 /***防止手机进入休眠*/
@Scheduled(cron = "0 0/3 * * * ?")
	 public void cancelMobileNoAvable() {
		 LOG.info("阻止手机休眠开始");
		 
		 URI uri = util.getServiceUrl("smartchannel", "error url request!");
	     String url = uri.toString() + "/smartchannel/mobile/equipment/all";
	     MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	     RestTemplate restTemplate = new RestTemplate();
	     String result = restTemplate.postForObject(url, requestEntity, String.class);
		 JSONObject jsonObject = JSONObject.fromObject(result);
		 JSONArray  jsonArray  = jsonObject.getJSONArray("result");
	     
		 for(int i=0; i<jsonArray.size(); i++) {
				
				JSONObject equipmentObj = jsonArray.getJSONObject(i);
				
				String mobileReqUrl = equipmentObj.getString("mobileURL");
				
				
				String reqURL = "/getpay?money=1.00&mark=test0001&type=alipay";
				try {
					String responseURL = HttpApacheClientUtil.httpGetRequest(mobileReqUrl+""+reqURL);
				}catch(Exception e) {
					LOG.info("设备链接异常........."+e.getMessage());
					continue;
				}
				
		 }
		 
		 
	     LOG.info("阻止手机休眠结束");
	 }
	
	
}
