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
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.clearing.business.OrderBusiness;
import com.smart.pay.clearing.pojo.OrderMarkupLog;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.clearing.repository.OrderMarkupLogRepository;
import com.smart.pay.clearing.repository.PaymentOrderRepository;

@Service
public  class OrderBusinessImpl implements OrderBusiness{

	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private OrderMarkupLogRepository  orderMarkupLogRepository;
	
	@Transactional
	@Override
	public PaymentOrder createPaymentOrder(PaymentOrder paymentOrder) {
		
		return paymentOrderRepository.saveAndFlush(paymentOrder);
	}



	@Override
	public PaymentOrder queryPaymentOrderBycode(String ordercode) {
		return paymentOrderRepository.findPaymentOrderBycode(ordercode);
	}



	@Override
	public PaymentOrder queryPaymentOrderByMercode(String merCode) {
		return paymentOrderRepository.findPaymentOrderBymerOrderCode(merCode);
	}



	@Override
	public PaymentOrder queryPaymentOrderByChannelcode(String channeOrdercode) {
		
		return paymentOrderRepository.findPaymentOrderByrealChannelOrderCode(channeOrdercode);
	}


	
	/**将未完成的订单拿出来**/
	@Override
	public List<PaymentOrder>  queryAllNoFinishOrders(Date startOrderTime){
		
		
		return paymentOrderRepository.findNoFinishPaymentOrders(startOrderTime);
	}
	

	@Override
	public Page<PaymentOrder> queryPaymentOrdersByMerchantId(String merchantid, String ordercode, String orderType,
			Date startTime, Date endTime, String orderstatus,   Pageable pageable) {
		
		if(ordercode != null && !ordercode.equalsIgnoreCase("")) {
			
			if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
				return paymentOrderRepository.findMerchantPaymentOrderBycode(merchantid, ordercode, orderType, pageable);
			}else {
				return paymentOrderRepository.findMerchantPaymentOrderBycode(ordercode, orderType, pageable);
			}
		}else {
			
			if(startTime == null) {
				
				if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
					if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
						return paymentOrderRepository.findPaymentOrderByMerchantidAndStatus(merchantid,  orderType, orderstatus,  pageable);
					}else {
						return paymentOrderRepository.findPaymentOrderByMerchantidAndStatus(orderType, orderstatus,  pageable);
					}
				}else {
					if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
						return paymentOrderRepository.findPaymentOrderByMerchantid(merchantid,  orderType, pageable);
					}else {
						return paymentOrderRepository.findPaymentOrderByOrderType(orderType, pageable);
					}
				}
				
				
			}else {
				
				if(endTime != null) {
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartEndDateAndStatus(merchantid, startTime, endTime, orderType,  orderstatus, pageable);
						}else {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartEndDateAndStatus(startTime, endTime, orderType,  orderstatus, pageable);
						}
					}else {
						if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartEndDate(merchantid, startTime, endTime, orderType, pageable);
						}else {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartEndDate(startTime, endTime, orderType, pageable);
						}
					}
					
				}else {
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartDateAndStatus(merchantid, startTime, orderType,  orderstatus,  pageable);
						}else {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartDateAndStatus(startTime, orderType,  orderstatus,  pageable);
						}
					}else {
						if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartDate(merchantid, startTime, orderType,  pageable);
						}else {
							return paymentOrderRepository.findPaymentOrderByMerchantidStartDate(startTime, orderType,  pageable);
						}
					}

				}
			}
		}
		
	}



	@Override
	public Page<PaymentOrder> queryPaymentOrdersByAgentid(String agentId, String ordercode, String orderType,
			Date startTime, Date endTime, String orderstatus, Pageable pageable) {
		
		if(ordercode != null && !ordercode.equalsIgnoreCase("")) {
			
			if(agentId != null && !agentId.equalsIgnoreCase("")) {
				return paymentOrderRepository.findAgentPaymentOrderBycode(agentId, ordercode, pageable);
			}else {
				return paymentOrderRepository.findPlatformPaymentOrderBycode(ordercode, pageable);
			}
		}else {
			
			if(startTime == null) {
				
				if(agentId != null && !agentId.equalsIgnoreCase("")) {
					
					
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						return paymentOrderRepository.findPaymentOrderByparentIdStatus(agentId, orderType, orderstatus, pageable);
					}else {
						return paymentOrderRepository.findPaymentOrderByparentId(agentId, orderType, pageable);
					}
				}else {
					
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						return paymentOrderRepository.findPlatformPaymentOrderByordertypeAndStatus(orderType,  orderstatus, pageable);
					}else {
						return paymentOrderRepository.findPlatformPaymentOrderByordertype(orderType, pageable);
					}
					
					
				}
			}else {
				
				if(endTime != null) {
					
					if(agentId != null && !agentId.equalsIgnoreCase("")) {
						
						if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByparentIdStartEndDateStatus(agentId, startTime, endTime, orderType, orderstatus, pageable);
						}else {
							return paymentOrderRepository.findPaymentOrderByparentIdStartEndDate(agentId, startTime, endTime, orderType,  pageable);
						}
						
					}else {
						
						
						if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
							return paymentOrderRepository.findPaymentOrderByStartEndDateAndStatus(startTime, endTime, orderType, orderstatus,  pageable);
						
						}else {
							return paymentOrderRepository.findPaymentOrderByStartEndDate(startTime, endTime, orderType, pageable);
						}
						
					
					}
				}else {
					
						if(agentId != null && !agentId.equalsIgnoreCase("")) {
							if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
								return paymentOrderRepository.findPaymentOrderByparentIdStartDateAndStatus(agentId, startTime, orderType, orderstatus, pageable);
							}else {
								return paymentOrderRepository.findPaymentOrderByparentIdStartDate(agentId, startTime, orderType, pageable);
							}
						}else {
							
							if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
								
								return paymentOrderRepository.findPaymentOrderByStartDateAndStatus(startTime, orderType, orderstatus,  pageable);
							}else {
								return paymentOrderRepository.findPaymentOrderByStartDate(startTime, orderType, pageable);
							}
							
						}
					
					
				}
			}
		}
	}



	@Override
	public BigDecimal querySumAmountByMerchantid(String merchantid, String orderType, String channelCode,  Date startTime, Date endTime) {
		
		
		if(endTime == null) {
			if(channelCode != null && !channelCode.equalsIgnoreCase("")) {
				
				return paymentOrderRepository.findSumAmountByMerchantidStartDateAndChannelCode(merchantid, startTime, orderType, channelCode);
			}else {
				return paymentOrderRepository.findSumAmountByMerchantidStartDate(merchantid, startTime, orderType);
			}
		}else{
			if(channelCode != null && !channelCode.equalsIgnoreCase("")) {
				
				return paymentOrderRepository.findSumAmountByMerchantidStartEndDateAndChannelcode(merchantid, startTime, endTime, orderType, channelCode);
			}else {
				return paymentOrderRepository.findSumAmountByMerchantidStartEndDate(merchantid, startTime, endTime, orderType);
			}
		}
	}



	@Override
	public BigDecimal querySumAmountByAgentId(String agentid, String orderType, Date startTime, Date endTime) {
		
		if(endTime == null) {
			
			return paymentOrderRepository.findSumAmountByAgentidStartDate(agentid, startTime, orderType);
			
		}else {
			
			return paymentOrderRepository.findSumAmountByAgentidStartEndDate(agentid, startTime, endTime, orderType);
		}
		
		
	}



	@Override
	public BigDecimal queryPlatformSumAmount(String orderType, Date startTime, Date endTime) {
		return paymentOrderRepository.findPlatformSumAmountByStartEndDate(startTime, endTime, orderType);
	}



	@Override
	public int queryPlatformTotalCnt(String orderType, Date startTime, Date endTime) {
		return paymentOrderRepository.findPlatformTotalCntByStartEndDate(startTime, endTime, orderType);
	}



	@Override
	public int queryTotalCntByMerchantid(String merchantid, String orderType, Date startTime, Date endTime) {
		
		return paymentOrderRepository.findTotalCntByMerchantidStartEndDate(merchantid, startTime, endTime, orderType);
	}



	@Override
	public Page<PaymentOrder> queryPlatformPaymentOrders(String ordercode, String orderType, Date startTime,
			Date endTime, String orderstatus, Pageable pageable) {
		if(ordercode != null && !ordercode.equalsIgnoreCase("")) {
			
			
			return paymentOrderRepository.findPlatformPaymentOrderBycode(ordercode, pageable);
			
		}else {
			
			if(startTime == null) {
				
				
				if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
					return paymentOrderRepository.findPlatformPaymentOrderByordertypeAndStatus(orderType,  orderstatus, pageable);
				}else {
					return paymentOrderRepository.findPlatformPaymentOrderByordertype(orderType, pageable);
				}
				
				
			}else {
				
				if(endTime != null) {
					
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						return paymentOrderRepository.findPaymentOrderByStartEndDateAndStatus(startTime, endTime, orderType, orderstatus,  pageable);
					
					}else {
						return paymentOrderRepository.findPaymentOrderByStartEndDate(startTime, endTime, orderType, pageable);
					}
				}else {
					if(orderstatus != null && !orderstatus.equalsIgnoreCase("")) {
						
						return paymentOrderRepository.findPaymentOrderByStartDateAndStatus(startTime, orderType, orderstatus,  pageable);
					}else {
						return paymentOrderRepository.findPaymentOrderByStartDate(startTime, orderType, pageable);
					}
				}
			}
		}
	}


	@Override
	public Map<String, Object> queryAgentsPaymentOrders(String ordercode, String orderType, Date startTime,
			Date endTime, String orderstatus, String agentid, String merchantId, String channelCode, String realChannelCode,
			Pageable pageable) {
		StringBuffer  totalSql	=	new StringBuffer("select count(*) as count,sum(po.amount) as sumAmount ,sum(real_amount) as sumRealAmout from t_payment_order po where 1=1 ");
		StringBuffer  dataSql	=	new StringBuffer("select * from t_payment_order po where 1=1 ");
		StringBuffer sqlData	=	new StringBuffer("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(ordercode!=null&&ordercode.trim().length()>0) {
			sqlData.append(" and (po.order_code='"+ordercode+"' or po.mer_order_code='"+ordercode+"' or po.real_channel_order_code='"+ordercode+"' ) ");
		}
		
		if(orderType!=null&&orderType.trim().length()>0) {
			sqlData.append(" and po.order_type='"+orderType+"'");
		}
		
		if(startTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')>='" + sdf.format(startTime) + "'");
		}
		
		if(endTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')<'" + sdf.format(endTime) + "'");
		}
		if(orderstatus!=null&&orderstatus.trim().length()>0) {
			sqlData.append(" and po.order_status='"+orderstatus+"'");
		}
		if(merchantId!=null&&merchantId.trim().length()>0) {
			sqlData.append(" and po.merchant_id='"+merchantId+"' ");
		}
		if(channelCode!=null&&channelCode.trim().length()>0) {
			sqlData.append(" and po.channel_code='"+channelCode+"'");
		}
		
		
		if(agentid !=null&& agentid.trim().length()>0) {
			sqlData.append(" and po.parent_id='"+agentid+"'");
		}
		
		if(realChannelCode!=null&&realChannelCode.trim().length()>0) {
			sqlData.append(" and po.real_channel_code='"+realChannelCode+"'");
		}
		totalSql.append(sqlData);
		dataSql.append(sqlData).append(" order by create_time desc limit "+pageable.getOffset()+","+pageable.getOffset()+pageable.getPageSize());
		
		Map<String, Object> total = jdbcTemplate.queryForMap(totalSql.toString());
		long count=Long.parseLong(total.get("count").toString()); 
		BigDecimal sumAmount	=	(BigDecimal) total.get("sumAmount") == null ? BigDecimal.ZERO : (BigDecimal) total.get("sumAmount");
		BigDecimal sumRealAmout	=	(BigDecimal) total.get("sumRealAmout");
		List<PaymentOrder> content = jdbcTemplate.query(dataSql.toString(), new RowMapper<PaymentOrder>() {
			@Override
			public PaymentOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
				PaymentOrder paymentOrder = new PaymentOrder();
				paymentOrder.setId(rs.getLong("id"));
				paymentOrder.setMerchantId(rs.getString("merchant_id"));
				paymentOrder.setParentId(rs.getString("parent_id"));
				paymentOrder.setMerchantName(rs.getString("merchant_name"));
				paymentOrder.setOrderCode(rs.getString("order_code"));
				paymentOrder.setMerOrderCode(rs.getString("mer_order_code")); 
				paymentOrder.setRealChannelOrderCode(rs.getString("real_channel_order_code")); 
				paymentOrder.setOrderStatus(rs.getString("order_status")); 
				paymentOrder.setOrderType(rs.getString("order_type")); 
				paymentOrder.setAmount(rs.getBigDecimal("amount")); 
				paymentOrder.setProductName(rs.getString("product_name")); 
				paymentOrder.setMerRate(rs.getBigDecimal("mer_rate")); 
				paymentOrder.setExtraFee(rs.getBigDecimal("extra_fee")); 
				paymentOrder.setRealAmount(rs.getBigDecimal("real_amount")); 
				paymentOrder.setChannelName(rs.getString("channel_name")); 
				paymentOrder.setChannelCode(rs.getString("channel_code")); 
				paymentOrder.setRealChannelCode(rs.getString("real_channel_code")); 
				paymentOrder.setRealChannelName(rs.getString("real_channel_name")); 
				paymentOrder.setRealChannelRate(rs.getBigDecimal("real_channel_rate")); 
				paymentOrder.setRealChannelExtraFee(rs.getBigDecimal("real_channel_extra_fee")); 
				paymentOrder.setNotifyURL(rs.getString("notify_url")); 
				paymentOrder.setEquipmentTag(rs.getString("equipment_tag"));
				paymentOrder.setAttach(rs.getString("attach")); 
				try {
					paymentOrder.setCreateTime(sdfCT.parse(rs.getString("create_time")));
					paymentOrder.setUpdateTime(sdfCT.parse(rs.getString("update_time"))); 
				} catch (ParseException e) {
					paymentOrder.setCreateTime(rs.getDate("create_time"));
				}  
				return paymentOrder;
			}
		});
		
		
		
		Page<PaymentOrder> paymentOrders=new PageImpl(content, pageable, count);
		Map<String,Object> pageOrders=new HashMap<String,Object>();
		pageOrders.put("content", paymentOrders.getContent());
		pageOrders.put("totalPages", paymentOrders.getTotalPages());
		pageOrders.put("totalElements", paymentOrders.getTotalElements());
		pageOrders.put("numberOfElements", paymentOrders.getNumberOfElements());
		pageOrders.put("size", paymentOrders.getSize());
		pageOrders.put("number", paymentOrders.getNumber());
		pageOrders.put("sumAmount",  sumAmount==null||sumAmount.equals("null")?"0.00":sumAmount);
		pageOrders.put("sumRealAmout",  sumRealAmout==null||sumRealAmout.equals("null")?"0.00":sumRealAmout);
		
		return pageOrders;
	}
	
	
	
	

	@Override
	public Map<String, Object> queryPlatformPaymentOrders(String ordercode, String orderType, Date startTime,
			Date endTime, String orderstatus, String merchantId, String channelCode, String realChannelCode,
			Pageable pageable) {
		StringBuffer  totalSql	=	new StringBuffer("select count(*) as count,sum(po.amount) as sumAmount ,sum(real_amount) as sumRealAmout from t_payment_order po where 1=1 ");
		StringBuffer  dataSql	=	new StringBuffer("select * from t_payment_order po where 1=1 ");
		StringBuffer sqlData	=	new StringBuffer("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(ordercode!=null&&ordercode.trim().length()>0) {
			sqlData.append(" and (po.order_code='"+ordercode+"' or po.mer_order_code='"+ordercode+"' or po.real_channel_order_code='"+ordercode+"' ) ");
		}
		
		if(orderType!=null&&orderType.trim().length()>0) {
			sqlData.append(" and po.order_type='"+orderType+"'");
		}
		
		if(startTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')>='" + sdf.format(startTime) + "'");
		}
		
		if(endTime!=null) {
			sqlData.append(" and date_format(create_time,'%Y-%m-%d')<'" + sdf.format(endTime) + "'");
		}
		if(orderstatus!=null&&orderstatus.trim().length()>0) {
			sqlData.append(" and po.order_status='"+orderstatus+"'");
		}
		if(merchantId!=null&&merchantId.trim().length()>0) {
			sqlData.append(" and po.merchant_id='"+merchantId+"' ");
		}
		if(channelCode!=null&&channelCode.trim().length()>0) {
			sqlData.append(" and po.channel_code='"+channelCode+"'");
		}
		if(realChannelCode!=null&&realChannelCode.trim().length()>0) {
			sqlData.append(" and po.real_channel_code='"+realChannelCode+"'");
		}
		totalSql.append(sqlData);
		dataSql.append(sqlData).append(" order by create_time desc limit "+pageable.getOffset()+","+pageable.getOffset()+pageable.getPageSize());
		
		Map<String, Object> total = jdbcTemplate.queryForMap(totalSql.toString());
		long count=Long.parseLong(total.get("count").toString()); 
		BigDecimal sumAmount	=	(BigDecimal) total.get("sumAmount") == null ? BigDecimal.ZERO : (BigDecimal) total.get("sumAmount");
		BigDecimal sumRealAmout	=	(BigDecimal) total.get("sumRealAmout");
		List<PaymentOrder> content = jdbcTemplate.query(dataSql.toString(), new RowMapper<PaymentOrder>() {
			@Override
			public PaymentOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
				PaymentOrder paymentOrder = new PaymentOrder();
				paymentOrder.setId(rs.getLong("id"));
				paymentOrder.setMerchantId(rs.getString("merchant_id"));
				paymentOrder.setParentId(rs.getString("parent_id"));
				paymentOrder.setMerchantName(rs.getString("merchant_name"));
				paymentOrder.setOrderCode(rs.getString("order_code"));
				paymentOrder.setMerOrderCode(rs.getString("mer_order_code")); 
				paymentOrder.setRealChannelOrderCode(rs.getString("real_channel_order_code")); 
				paymentOrder.setOrderStatus(rs.getString("order_status")); 
				paymentOrder.setOrderType(rs.getString("order_type")); 
				paymentOrder.setAmount(rs.getBigDecimal("amount")); 
				paymentOrder.setProductName(rs.getString("product_name")); 
				paymentOrder.setMerRate(rs.getBigDecimal("mer_rate")); 
				paymentOrder.setExtraFee(rs.getBigDecimal("extra_fee")); 
				paymentOrder.setRealAmount(rs.getBigDecimal("real_amount")); 
				paymentOrder.setChannelName(rs.getString("channel_name")); 
				paymentOrder.setChannelCode(rs.getString("channel_code")); 
				paymentOrder.setRealChannelCode(rs.getString("real_channel_code")); 
				paymentOrder.setRealChannelName(rs.getString("real_channel_name")); 
				paymentOrder.setRealChannelRate(rs.getBigDecimal("real_channel_rate")); 
				paymentOrder.setRealChannelExtraFee(rs.getBigDecimal("real_channel_extra_fee")); 
				paymentOrder.setNotifyURL(rs.getString("notify_url")); 
				paymentOrder.setEquipmentTag(rs.getString("equipment_tag"));
				paymentOrder.setAttach(rs.getString("attach")); 
				try {
					paymentOrder.setCreateTime(sdfCT.parse(rs.getString("create_time")));
					paymentOrder.setUpdateTime(sdfCT.parse(rs.getString("update_time"))); 
				} catch (ParseException e) {
					paymentOrder.setCreateTime(rs.getDate("create_time"));
				}  
				return paymentOrder;
			}
		});
		
		
		
		Page<PaymentOrder> paymentOrders=new PageImpl(content, pageable, count);
		Map<String,Object> pageOrders=new HashMap<String,Object>();
		pageOrders.put("content", paymentOrders.getContent());
		pageOrders.put("totalPages", paymentOrders.getTotalPages());
		pageOrders.put("totalElements", paymentOrders.getTotalElements());
		pageOrders.put("numberOfElements", paymentOrders.getNumberOfElements());
		pageOrders.put("size", paymentOrders.getSize());
		pageOrders.put("number", paymentOrders.getNumber());
		pageOrders.put("sumAmount",  sumAmount==null||sumAmount.equals("null")?"0.00":sumAmount);
		pageOrders.put("sumRealAmout",  sumRealAmout==null||sumRealAmout.equals("null")?"0.00":sumRealAmout);
		
		return pageOrders;
	}



	@Override
	public List<PaymentOrder> findPaymentOrdersByMerchantidStartEndDateAndStatus(String merchantid, 
			String orderType, Date startTime, Date endTime, String orderstatus) {
		return paymentOrderRepository.findPaymentOrdersByMerchantidStartEndDateAndStatus(merchantid,   startTime, endTime, orderType, orderstatus);
	}



	@Override
	public BigDecimal querySumAmountByEquipment(String equipmenttag, String orderType, Date startTime, Date endTime) {
		return paymentOrderRepository.findSumAmountByEquipmentTagStartEndDate(equipmenttag, startTime, endTime, orderType);
	}



	@Override
	public OrderMarkupLog queryLastestOrderMarkupLog() {
		
		return orderMarkupLogRepository.queryOrderMarkupLog();
	}


	@Transactional
	@Override
	public OrderMarkupLog saveOrderMarkupLog(OrderMarkupLog orderMarkupLog) {
		
		return orderMarkupLogRepository.saveAndFlush(orderMarkupLog);
	}


}
