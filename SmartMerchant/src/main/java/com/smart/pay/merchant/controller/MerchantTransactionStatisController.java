package com.smart.pay.merchant.controller;

import java.math.BigDecimal;
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
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.merchant.business.MerchantTransactionStatisBusiness;
import com.smart.pay.merchant.pojo.MerchantChannelTransactionStatis;

@Controller
@EnableAutoConfiguration
public class MerchantTransactionStatisController {

	
	@Autowired
	private MerchantTransactionStatisBusiness merchantTransactionStatisBusiness;
	
	/**创建或者更新商户的渠道交易统计*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/transactionstatis/create")
	public @ResponseBody Object createOrUpdateMerchantStatis(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid,
			@RequestParam("channel_code") String channelCode,
			@RequestParam("transaction_amount") BigDecimal transactionAmt,
			@RequestParam("statis_date") String statisDate
			) {
		
		MerchantChannelTransactionStatis  transactionStatis = merchantTransactionStatisBusiness.queryMerchantChannelTransactionStatis(merchantid, channelCode, statisDate);
		if(transactionStatis != null) {
			transactionStatis.setTransactionAmt(transactionAmt);
			transactionStatis.setUpdateTime(new Date());
		}else {
			transactionStatis = new MerchantChannelTransactionStatis();
			transactionStatis.setChannelCode(channelCode);
			transactionStatis.setCreateTime(new Date());
			transactionStatis.setMerchantId(merchantid);
			transactionStatis.setStatisDate(statisDate);
			transactionStatis.setUpdateTime(new Date());
			transactionStatis.setTransactionAmt(transactionAmt);
		}
		try {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantTransactionStatisBusiness.createMerchantChannelTransactionStatis(transactionStatis));
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败--"+e.getMessage(), "");
		}
		
	}
	
	
	/**创建或者更新商户的渠道交易统计*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/transactionstatis/query")
	public @ResponseBody Object queryMerchantStatis(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid,
			@RequestParam("channel_code") String channelCode,
			@RequestParam("statis_date") String statisDate
			) {
		
		
		try {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantTransactionStatisBusiness.queryMerchantChannelTransactionStatis(merchantid, channelCode, statisDate));
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败--"+e.getMessage(), "");
		}
	}
	
}
