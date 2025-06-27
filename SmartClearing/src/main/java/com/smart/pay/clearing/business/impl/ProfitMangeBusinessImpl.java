package com.smart.pay.clearing.business.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.smart.pay.clearing.business.ProfitManageBusiness;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.clearing.pojo.ProfitRecord;
import com.smart.pay.clearing.repository.PlatformProfitRecordRepository;


@Service
public class ProfitMangeBusinessImpl implements ProfitManageBusiness{

	
	@Autowired
	private PlatformProfitRecordRepository platformProfitRecordRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**分页获取代理商，平台分润订单明细*/
	public Page<ProfitRecord> queryProfitOrdersByAgentid(String agentId, String orderCode,  Date startTime, Date endTime, Pageable pageable){
	
		if(orderCode != null && !orderCode.equalsIgnoreCase("")) {
			
			return platformProfitRecordRepository.findProfitOrderByparentIdBycode(agentId, orderCode, pageable);
			
		}else {
			
			if(startTime == null) {
				
				return platformProfitRecordRepository.findProfitOrderByparentId(agentId, pageable);
					
			}else {
				
				if(endTime != null) {
					
					
					return platformProfitRecordRepository.findProfitOrderByparentIdStartEndDate(agentId, startTime, endTime, pageable);
					
				}else {
					
					
					return platformProfitRecordRepository.findProfitOrderByparentIdStartDate(agentId, startTime, pageable);
					
				}
				
			}
		}

	}
	
	
	/**按时间统计代理商某个阶段的分润*/
	public BigDecimal  queryProfitSumAmountByAgentId(String agentid, Date startTime, Date endTime) {
		
		if(endTime == null) {
			
			return platformProfitRecordRepository.findProfitSumAmountByAgentidStartDate(agentid, startTime);
			
		}else {
			
			return platformProfitRecordRepository.findProfitSumAmountByAgentidStartEndDate(agentid, startTime, endTime);
			
		}

	}


	@Override
	public Map<String, Object>  queryProfitOrdersByAgentid(String agentId, String ordercode, Date startTime, Date endTime,
			 String channelCode, String realChannelCode, Pageable pageable) {
		StringBuffer  totalSql	=	new StringBuffer("select count(*) as count,sum(amount) as sumAmount ,sum(own_income) as sumOwnIncome from t_profit_record pr where 1=1 ");
		StringBuffer  dataSql	=	new StringBuffer("select * from t_profit_record pr where 1=1 ");
		StringBuffer sqlData	=	new StringBuffer("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdfCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(ordercode!=null&&ordercode.trim().length()>0) {
			sqlData.append(" and pr.order_code='"+ordercode+"' ");
		}
		if(agentId!=null&&agentId.trim().length()>0) {
			sqlData.append(" and pr.merchant_id='"+agentId+"' ");
		}
		if(startTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')>='" + sdf.format(startTime) + "'");
		}
		
		if(endTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')<'" + sdf.format(endTime) + "'");
		}
		if(channelCode!=null&&channelCode.trim().length()>0) {
			sqlData.append(" and pr.channel_code='"+channelCode+"' ");
		}
		if(realChannelCode!=null&&realChannelCode.trim().length()>0) {
			sqlData.append(" and pr.real_channel_code='"+realChannelCode+"' ");
		}
		totalSql.append(sqlData);
		dataSql.append(sqlData).append(" order by create_time desc limit "+pageable.getOffset()+","+pageable.getOffset()+pageable.getPageSize());
		
		Map<String, Object> total = jdbcTemplate.queryForMap(totalSql.toString());
		long count=Long.parseLong(total.get("count").toString()); 
		BigDecimal sumAmount	=	(BigDecimal) total.get("sumAmount");
		BigDecimal sumOwnIncome	=	(BigDecimal) total.get("sumOwnIncome");
		List<ProfitRecord> content = jdbcTemplate.query(dataSql.toString(), new RowMapper<ProfitRecord>() {
			@Override
			public ProfitRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfitRecord paymentOrder = new ProfitRecord();
				paymentOrder.setId(rs.getLong("id"));
				paymentOrder.setMerchantId(rs.getString("merchant_id"));
				paymentOrder.setMerchantName(rs.getString("merchant_name"));
				paymentOrder.setOrderCode(rs.getString("order_code"));
				paymentOrder.setChannelName(rs.getString("channel_name")); 
				paymentOrder.setChannelCode(rs.getString("channel_code")); 
				paymentOrder.setRealChannelCode(rs.getString("real_channel_code")); 
				paymentOrder.setRealChannelName(rs.getString("real_channel_name")); 
				paymentOrder.setAmount(rs.getBigDecimal("amount")); 
				paymentOrder.setMerRate(rs.getBigDecimal("mer_rate")); 
				paymentOrder.setMerExtraFee(rs.getBigDecimal("mer_extra_fee")); 
				paymentOrder.setOwnRate(rs.getBigDecimal("own_rate")); 
				paymentOrder.setOwnExtraFee(rs.getBigDecimal("own_extra_fee")); 
				paymentOrder.setOwnIncome(rs.getBigDecimal("own_income")); 
				try {
					paymentOrder.setCreateTime(sdfCT.parse(rs.getString("create_time")));
				} catch (ParseException e) {
					paymentOrder.setCreateTime(rs.getDate("create_time"));
				} 
				return paymentOrder;
			}
		});
		
		
		
		Page<ProfitRecord> paymentOrders=new PageImpl(content, pageable, count);
		Map<String,Object> pageOrders=new HashMap<String,Object>();
		pageOrders.put("content", paymentOrders.getContent());
		pageOrders.put("totalPages", paymentOrders.getTotalPages());
		pageOrders.put("totalElements", paymentOrders.getTotalElements());
		pageOrders.put("numberOfElements", paymentOrders.getNumberOfElements());
		pageOrders.put("size", paymentOrders.getSize());
		pageOrders.put("number", paymentOrders.getNumber());
		pageOrders.put("sumAmount", sumAmount==null||sumAmount.equals("null")?"0.00":sumAmount);
		pageOrders.put("sumOwnIncome",sumOwnIncome==null||sumOwnIncome.equals("null")?"0.00":sumOwnIncome );
		
		return pageOrders;
		
	}


	@Override
	public Map<String, Object> queryProfitOrdersByAgentid(String agentId, String orderMerchantId, String orderCode,
			Date startTime, Date endTime, String channelCode, String realChannelCode, Pageable pageable) {
		StringBuffer  totalSql	=	new StringBuffer("select count(*) as count,sum(amount) as sumAmount ,sum(own_income) as sumOwnIncome from t_profit_record pr where 1=1 ");
		StringBuffer  dataSql	=	new StringBuffer("select * from t_profit_record pr where 1=1 ");
		StringBuffer sqlData	=	new StringBuffer("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdfCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(orderCode!=null&&orderCode.trim().length()>0) {
			sqlData.append(" and pr.order_code='"+orderCode+"' ");
		}
		
		
		if(orderMerchantId!=null&&orderMerchantId.trim().length()>0) {
			sqlData.append(" and pr.order_merchant_id='"+orderMerchantId+"' ");
		}
		
		if(agentId!=null&&agentId.trim().length()>0) {
			sqlData.append(" and pr.merchant_id='"+agentId+"' ");
		}
		if(startTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')>='" + sdf.format(startTime) + "'");
		}
		
		if(endTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')<'" + sdf.format(endTime) + "'");
		}
		if(channelCode!=null&&channelCode.trim().length()>0) {
			sqlData.append(" and pr.channel_code='"+channelCode+"' ");
		}
		if(realChannelCode!=null&&realChannelCode.trim().length()>0) {
			sqlData.append(" and pr.real_channel_code='"+realChannelCode+"' ");
		}
		totalSql.append(sqlData);
		dataSql.append(sqlData).append(" order by create_time desc limit "+pageable.getOffset()+","+pageable.getOffset()+pageable.getPageSize());
		
		Map<String, Object> total = jdbcTemplate.queryForMap(totalSql.toString());
		long count=Long.parseLong(total.get("count").toString()); 
		BigDecimal sumAmount	=	(BigDecimal) total.get("sumAmount");
		BigDecimal sumOwnIncome	=	(BigDecimal) total.get("sumOwnIncome");
		List<ProfitRecord> content = jdbcTemplate.query(dataSql.toString(), new RowMapper<ProfitRecord>() {
			@Override
			public ProfitRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfitRecord paymentOrder = new ProfitRecord();
				paymentOrder.setId(rs.getLong("id"));
				paymentOrder.setMerchantId(rs.getString("merchant_id"));
				paymentOrder.setMerchantName(rs.getString("merchant_name"));
				paymentOrder.setOrderCode(rs.getString("order_code"));
				paymentOrder.setChannelName(rs.getString("channel_name")); 
				paymentOrder.setChannelCode(rs.getString("channel_code")); 
				paymentOrder.setRealChannelCode(rs.getString("real_channel_code")); 
				paymentOrder.setRealChannelName(rs.getString("real_channel_name")); 
				paymentOrder.setAmount(rs.getBigDecimal("amount")); 
				paymentOrder.setMerRate(rs.getBigDecimal("mer_rate")); 
				paymentOrder.setMerExtraFee(rs.getBigDecimal("mer_extra_fee")); 
				paymentOrder.setOwnRate(rs.getBigDecimal("own_rate")); 
				paymentOrder.setOwnExtraFee(rs.getBigDecimal("own_extra_fee")); 
				paymentOrder.setOwnIncome(rs.getBigDecimal("own_income")); 
				try {
					paymentOrder.setCreateTime(sdfCT.parse(rs.getString("create_time")));
				} catch (ParseException e) {
					paymentOrder.setCreateTime(rs.getDate("create_time"));
				} 
				return paymentOrder;
			}
		});
		
		
		
		Page<ProfitRecord> paymentOrders=new PageImpl(content, pageable, count);
		Map<String,Object> pageOrders=new HashMap<String,Object>();
		pageOrders.put("content", paymentOrders.getContent());
		pageOrders.put("totalPages", paymentOrders.getTotalPages());
		pageOrders.put("totalElements", paymentOrders.getTotalElements());
		pageOrders.put("numberOfElements", paymentOrders.getNumberOfElements());
		pageOrders.put("size", paymentOrders.getSize());
		pageOrders.put("number", paymentOrders.getNumber());
		pageOrders.put("sumAmount", sumAmount==null||sumAmount.equals("null")?"0.00":sumAmount);
		pageOrders.put("sumOwnIncome",sumOwnIncome==null||sumOwnIncome.equals("null")?"0.00":sumOwnIncome );
		
		return pageOrders;
		
	}
	
	
}
