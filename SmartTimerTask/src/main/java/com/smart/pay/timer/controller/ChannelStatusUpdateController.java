package com.smart.pay.timer.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.timer.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
@EnableScheduling
public class ChannelStatusUpdateController {

	@Autowired
	private Util util;
	
	/***将商户的路由状态更新可用或者不可用 每2分钟一次*/
	@Scheduled(cron = "0 0/2 * * * ?")
	public void scheduler() {
		
		/***
		 * 查询系统所有实际运行的通道
		 * 根据当前的运行状态更新路由中商户的状态
		 * */
		URI uri = util.getServiceUrl("smartpayment", "error url request!");
        String url = uri.toString() + "/smartpayment/pay/realchannel/all";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);
		JSONArray resultChannelObj = resultObj.getJSONArray("result");
		if(!resultChannelObj.isEmpty()) {
			for(int i=0; i<resultChannelObj.size(); i++) {
				
				JSONObject realObj  = resultChannelObj.getJSONObject(i);
				String realChannelCode = realObj.getString("realChannelCode");
				String status          = realObj.getString("status");
				uri = util.getServiceUrl("smartpayment", "error url request!");
		        url = uri.toString() + "/smartpayment/channelroute/status/update";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("real_channel_code", realChannelCode);
		        requestEntity.add("channel_status", status);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			}
		}
		
	}
	
	
}
