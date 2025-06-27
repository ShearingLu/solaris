package com.smart.pay.payment.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.payment.business.ChannelBusiness;
import com.smart.pay.payment.pojo.SettlementObject;

@Controller
@EnableAutoConfiguration
public class SettlementObjectController {

	
	@Autowired
	private ChannelBusiness channelBusiness;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/settlementobj/query")
	public @ResponseBody Object queryAllSettlementObject(HttpServletRequest request
			) {
		
		
		try {
		
			return ResultWrap.init(Constss.SUCCESS, "成功", channelBusiness.queryAllSettlementObject());
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/settlementobj/new")
	public @ResponseBody Object addNewSettlementObject(HttpServletRequest request,
			@RequestParam(value = "settlement_name") String settlementName,
			@RequestParam(value = "contact") String contact
			) {
		
		
		try {
			
			
			SettlementObject  settlementObject = new SettlementObject();
			settlementObject.setContact(contact);
			settlementObject.setCreateTime(new Date());
			settlementObject.setSettlementid(RandomUtils.generateString(5));
			settlementObject.setSettlementName(settlementName);
			settlementObject.setUpdateTime(new Date());
		
			return ResultWrap.init(Constss.SUCCESS, "成功", channelBusiness.addNewSettlementObject(settlementObject));
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/settlementobj/del")
	public @ResponseBody Object delSettlementObject(HttpServletRequest request,
			@RequestParam(value = "settlement_id") String settlementId
			) {
		
		
		try {
			channelBusiness.delSettlementObject(settlementId);
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	}
	
	
}
