package com.smart.pay.timer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.pay.timer.business.MerchantNotifyBusiness;
import com.smart.pay.timer.pojo.MerchantNotifyRecord;

import net.sf.json.JSONObject;


@Controller
@EnableAutoConfiguration
@EnableScheduling
public class MerchantNotifyController {

	
		@Autowired
		private MerchantNotifyBusiness merchantNotifyBusiness;

		
		
		@Scheduled(cron = "0/10 * * * * ?")
	    public void scheduler() {
			merchantNotifyBusiness.callback();

	    }
	
	
		
		/**新增一条未未回调成功记录**/
		@Async("myAsync")
		@RequestMapping(method = RequestMethod.POST, value = "/smarttimer/callback/add")
		public @ResponseBody Object addCallbackRecord(HttpServletRequest request,
				@RequestParam(value = "merchant_id") String merchantid,
				@RequestParam(value = "notify_url") String notifyurl,
				@RequestParam(value = "call_params") String callparams
				) {
			
			MerchantNotifyRecord merchantNotifyRecord = new MerchantNotifyRecord();
			merchantNotifyRecord.setCreateTime(new Date());
			merchantNotifyRecord.setMerchantId(merchantid);
			merchantNotifyRecord.setNotifyURL(notifyurl);
			merchantNotifyRecord.setParams(callparams);
			merchantNotifyRecord.setStatus("1");
			merchantNotifyRecord.setRemainCnt(24);
			merchantNotifyRecord.setNextCallTime(new Date());
			merchantNotifyRecord.setCreateTime(new Date());
			merchantNotifyBusiness.createMerchantNotifyRecord(merchantNotifyRecord);
			
			return null;
		}
		
		
		
		@RequestMapping(method = RequestMethod.POST, value = "/smarttimer/merchant/test")
		public @ResponseBody Object testNotify(HttpServletRequest request
				) {
			 BufferedReader br;  
			    StringBuilder sb = null;  
			    String reqBody = null;  
			    try {  
			        br = new BufferedReader(new InputStreamReader(  
			                request.getInputStream()));  
			        String line = null;  
			        sb = new StringBuilder();  
			        while ((line = br.readLine()) != null) {  
			            sb.append(line);  
			        }  
			        reqBody = URLDecoder.decode(sb.toString(), "UTF-8"); 
			        
			        System.out.println(reqBody);
			        
			        JSONObject jsonObject = JSONObject.fromObject(reqBody);
			        
			        
			        System.out.println("transaction_id"+ jsonObject.getString("transaction_id"));
			        
			        
			       
			       // System.out.println("JsonReq reqBody>>>>>" + reqBody);  
			        return reqBody;  
			    } catch (IOException e) {  
			        // TODO Auto-generated catch block  
			        e.printStackTrace();  
			        return "jsonerror";  
			    }  
			
			//return "success";
		}
		
		
}
