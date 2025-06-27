package com.smart.pay.clearing.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.clearing.pojo.ProfitRecord;

public interface ProfitManageBusiness {

	
	
	public Page<ProfitRecord> queryProfitOrdersByAgentid(String agentId, String orderCode,  Date startTime, Date endTime, Pageable pageable);
	
	public Map<String, Object>  queryProfitOrdersByAgentid(String agentId, String orderCode,  Date startTime, Date endTime,String channelCode ,String realChannelCode, Pageable pageable);
	
	public Map<String, Object>  queryProfitOrdersByAgentid(String agentId, String orderMerchantId, String orderCode,  Date startTime, Date endTime,String channelCode ,String realChannelCode, Pageable pageable);
	
	
	
	public BigDecimal  queryProfitSumAmountByAgentId(String agentid, Date startTime, Date endTime);
}
