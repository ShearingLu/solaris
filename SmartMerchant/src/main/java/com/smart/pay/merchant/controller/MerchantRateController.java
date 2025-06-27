package com.smart.pay.merchant.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.common.tools.TokenUtil;
import com.smart.pay.merchant.business.MerchantManageBusiness;
import com.smart.pay.merchant.business.MerchantRateBusiness;
import com.smart.pay.merchant.pojo.Merchant;
import com.smart.pay.merchant.pojo.MerchantRate;

@Controller
@EnableAutoConfiguration
public class MerchantRateController {

	
	@Autowired
	private MerchantRateBusiness merchantRateBusiness;
	
	@Autowired
	private MerchantManageBusiness manageBusiness;
	
	/**查询商户或者代理商费率**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/rate/id")
	public @ResponseBody Object getMerchantRate(HttpServletRequest request, @RequestParam("merchant_id") String merchantid,
			 @RequestParam("channel_code") String channelCode) {
		
		
		MerchantRate merchantRate = merchantRateBusiness.queryMerchantRate(merchantid, channelCode);
		
		if(merchantRate != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantRate);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_RATE_NOT_EXISTED, "没有设置费率", null);
		}
		
		
	}
	
	/**查询商户或者代理商费率**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/rate/merchantid")
	public @ResponseBody Object getMerchantRate(HttpServletRequest request, 
			@RequestParam("merchant_id") String merchantid) {
		
		List<MerchantRate> merchantRates = merchantRateBusiness.queryMerchantRate(merchantid);
		
		if(merchantRates != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantRates);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_RATE_NOT_EXISTED, "没有设置费率", null);
		}
		
		
	}
	
		
     /**新增或者修改一个商户费率, 可以是代理商/商户的 **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/rate/new")
	public @ResponseBody Object addMerchantRate(HttpServletRequest request, @RequestParam("merchant_id") String merchantid,
			 @RequestParam("channel_code") String channelCode,  
			 @RequestParam(value="channel_name", required=false) String channelName,
			 @RequestParam("rate") String rate
			 ,  @RequestParam(value="extra_fee", required=false, defaultValue="0") String extraFee) {
		
		 String token = request.getParameter("token");
		 String prent_id="";
		 try {
			prent_id=TokenUtil.getUserId(token);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 /**
		  * 校验
		  * start
		  * **/
		 Merchant merchant = manageBusiness.queryMerchantById(merchantid);
		 //判断修改人是不是直属上级
		 if(merchant==null||!merchant.getParent().equals(prent_id)){
			 return ResultWrap.init(Constss.FALIED, "费率生成失败--非直属上级无权更改费率", "");
		 }
		 //判断费率小于1
		 if(new BigDecimal(rate).compareTo(new BigDecimal("1"))==1){
			 return ResultWrap.init(Constss.FALIED, "费率生成失败--费率不能>1", "");
		}
		 
		 //判断是否大于上级费率
		 MerchantRate merchantRate = merchantRateBusiness.queryMerchantRate(merchantid, channelCode);
		 
		 MerchantRate prentRate = merchantRateBusiness.queryMerchantRate(prent_id, channelCode);
		if(prentRate==null||prentRate.getRate().compareTo(new BigDecimal(rate))==1){
			 return ResultWrap.init(Constss.FALIED, prentRate==null?"费率生成失败--您本身没有该通道费率请联系您的推荐人进行确认":"费率生成失败--费率不能低于您所拥有费率"+prentRate.getRate(), "");
		}
		if(prentRate==null||prentRate.getExtraFee().compareTo(new BigDecimal(extraFee))==1){
			 return ResultWrap.init(Constss.FALIED, prentRate==null?"费率生成失败--您本身没有该通道费率请联系您的推荐人进行确认":"费率生成失败--额外费用不能低于您所拥有额外费用"+prentRate.getRate(), "");
		}
		 /**
		  * 校验
		  * end
		  * **/
		if(merchantRate == null) {
			
			merchantRate  = new MerchantRate();
			merchantRate.setChannelCode(channelCode);
			merchantRate.setChannelName(channelName);
			merchantRate.setCreateTime(new Date());
			merchantRate.setExtraFee(extraFee == null || extraFee.equalsIgnoreCase("") ? BigDecimal.ZERO : new BigDecimal(extraFee));
			merchantRate.setMerchantId(merchantid);
			merchantRate.setRate(new BigDecimal(rate));
			merchantRate.setUpdateTime(new Date());
		}else {
			
			
			merchantRate.setExtraFee(extraFee == null || extraFee.equalsIgnoreCase("") ? BigDecimal.ZERO : new BigDecimal(extraFee));
			merchantRate.setRate(new BigDecimal(rate));
			merchantRate.setUpdateTime(new Date());
			
			
		}
		
		
		try {
			merchantRateBusiness.createMerchantRate(merchantRate);
			return ResultWrap.init(Constss.SUCCESS, "费率生成成功", merchantRate);
		}catch(Exception e) {
					
			return ResultWrap.init(Constss.FALIED, "费率生成失败--"+e.getMessage(), "");
			
		}
	
	}
	
	
	
}
