package com.smart.pay.risk.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.risk.business.RiskBusiness;
import com.smart.pay.risk.pojo.MerchantAmountLimit;
import com.smart.pay.risk.pojo.MerchantBlackList;


@Controller
@EnableAutoConfiguration
public class RiskController {

	@Autowired
	private RiskBusiness riskBusiness;
	
	
	
	/**设置商家限额/或更新商家限额**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantlimit/add")
	public @ResponseBody Object addMerchantLimit(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "day_limit") BigDecimal dayLimit,
			@RequestParam(value = "month_limit") BigDecimal monthLimit,
			@RequestParam(value = "channel_code") String channelCode,
			@RequestParam(value = "channel_type") String channelType,
			@RequestParam(value = "channel_name") String channelName
			) {
		
		 
		MerchantAmountLimit merchantAmountLimit=riskBusiness.queryMerchantAmountLimi(merchantId, channelCode,channelType);
		if(merchantAmountLimit==null){
			merchantAmountLimit = new MerchantAmountLimit();
			merchantAmountLimit.setCreateTime(new Date());
		}
		merchantAmountLimit.setChannelCode(channelCode);
		merchantAmountLimit.setChannelName(channelName);
		merchantAmountLimit.setDayLimit(dayLimit);
		merchantAmountLimit.setMerchantId(merchantId);
		merchantAmountLimit.setMonthLimit(monthLimit);
		merchantAmountLimit.setUpdateTime(new Date());
		merchantAmountLimit.setChannelType(channelType);
		try {
			riskBusiness.creatMerchantAmountLimit(merchantAmountLimit);
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAmountLimit);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	
		
	}
	
	
	/**查询商家限额*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantlimit/query")
	public @ResponseBody Object queryMerchantLimit(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "channel_code") String channelCode,
			@RequestParam(value = "channel_type") String channelType
			) {
		
		MerchantAmountLimit merchantAmountLimit = riskBusiness.queryMerchantAmountLimi(merchantId, channelCode,channelType);
		return ResultWrap.init(Constss.SUCCESS, "成功", merchantAmountLimit);	
	}
	/**查询商家所开通的通道*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantlimit/query/merchantid")
	public @ResponseBody Object queryMerchantLimit(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId
			) {
		List<MerchantAmountLimit> merchantAmountLimits = riskBusiness.queryMerchantAmountLimitAndMerchantid(merchantId);
		return ResultWrap.init(Constss.SUCCESS, "成功", merchantAmountLimits);	
	}
	
	/**新增商家的风控规则
	 * RiskType
	 * 0 标识没有交易权限
	 * 1表示没有登录权限
	 * 2表示没有提现权限 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantblacklist/add")
	public @ResponseBody Object addMerchantBlacklist(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "risk_type") String riskType
			) {
		
		MerchantBlackList merchantBlackList = new MerchantBlackList();
		
		merchantBlackList.setCreateTime(new Date());
		merchantBlackList.setMerchantId(merchantId);
		merchantBlackList.setRiskType(riskType);
		merchantBlackList.setUpdateTime(new Date());
		
		
		try {
			riskBusiness.createMerchantBlackList(merchantBlackList);
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantBlackList);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	/**删除商家的风控规则**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantblacklist/del")
	public @ResponseBody Object delMerchantBlacklist(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "risk_type") String riskType
			) {
		
		try {
			riskBusiness.deleteMerchantBlackList(merchantId, riskType);
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	
	
	/**批量查询所有商家的黑名单**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantblacklist/query")
    public @ResponseBody Object queryMerchantBlacklist(HttpServletRequest request, //
            @RequestParam(value = "merchant_id", required=false) String merchantId, 
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));

		Page<MerchantBlackList> blackList  = riskBusiness.pageQueryMerchantBlackList(merchantId, pageable);
		
		return ResultWrap.init(Constss.SUCCESS, "成功", blackList);
	}
	
	
	
	/***判断商家商家是否触发风控**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartrisk/merchantblacklist/merchantid")
	public @ResponseBody Object queryMerchantBlacklistByMerchantid(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "risk_type") String riskType
			) {
		
		MerchantBlackList merchantBlackList =  riskBusiness.queryMerchantBlackList(merchantId, riskType);
		return ResultWrap.init(Constss.SUCCESS, "成功", merchantBlackList);
	
	}
	
}
