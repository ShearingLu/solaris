package com.smart.pay.clearing.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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

import com.smart.pay.clearing.business.ProfitManageBusiness;
import com.smart.pay.clearing.pojo.ProfitRecord;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.DateUtil;
import com.smart.pay.common.tools.ResultWrap;

@Controller
@EnableAutoConfiguration
public class ProfitController {

	
	@Autowired
	private ProfitManageBusiness profitManageBusiness;
	
	/**代理商总平台查询分润明细查询
	 * 
	 * 订单号
	 * 
	 * 时间
	 * 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/profit/history")
    public @ResponseBody Object queryProfitHistory(HttpServletRequest request, //
            @RequestParam(value = "platform_id", required = false) String platformid, //
            @RequestParam(value = "order_code", required = false) String orderCode, //
            @RequestParam(value = "start_time", required = false) String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime, //
            @RequestParam(value = "channel_code", required = false) String channelCode, //外放通道
            @RequestParam(value = "real_channel_code", required = false) String realChannelCode, //上游通道
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
//		 Page<ProfitRecord>  pageProfitRecords   = profitManageBusiness.queryProfitOrdersByAgentid(platformid, orderCode, startDate, endDate, pageable);
		Map<String, Object>   pageProfitRecords   = profitManageBusiness.queryProfitOrdersByAgentid(platformid, orderCode, startDate, endDate,channelCode,realChannelCode, pageable);
		 
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageProfitRecords);
	}
	
	
	
	/**代理商查询分润明细查询
	 * 并可以根据产生分润的商户进行查询
	 * 订单号
	 * 
	 * 时间
	 * 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/agent/profit/history")
    public @ResponseBody Object queryAgentProfitHistory(HttpServletRequest request, //
            @RequestParam(value = "agent_id") String agentId, //
            @RequestParam(value = "order_merchant_id", required = false) String orderMerchantId, //
            @RequestParam(value = "order_code", required = false) String orderCode, //
            @RequestParam(value = "start_time", required = false) String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime, //
            @RequestParam(value = "channel_code", required = false) String channelCode, //外放通道
            @RequestParam(value = "real_channel_code", required = false) String realChannelCode, //上游通道
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
//		 Page<ProfitRecord>  pageProfitRecords   = profitManageBusiness.queryProfitOrdersByAgentid(platformid, orderCode, startDate, endDate, pageable);
		Map<String, Object>   pageProfitRecords   = profitManageBusiness.queryProfitOrdersByAgentid(agentId, orderMerchantId,  orderCode, startDate, endDate,channelCode,realChannelCode, pageable);
		 
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageProfitRecords);
	}
	
	


	/**代理商/总平台按时间统计查询利润接口**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/profit/sumamount")
    public @ResponseBody Object queryMerchantSumAmount(HttpServletRequest request, //
            @RequestParam(value = "platform_id") String platformid, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime
            ) throws Exception{
		
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		
		
		
		BigDecimal totalSumAmount = profitManageBusiness.queryProfitSumAmountByAgentId(platformid, startDate, endDate);
		
		if(totalSumAmount == null) {
			totalSumAmount = BigDecimal.ZERO;
		}
		
		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalSumAmount);
	}
	
	
	
	
}
