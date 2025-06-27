package com.smart.pay.clearing.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.clearing.pojo.OrderMarkupLog;
import com.smart.pay.clearing.pojo.PaymentOrder;

public interface OrderBusiness {

	
	
	/**生成一笔订单**/
	public PaymentOrder createPaymentOrder(PaymentOrder paymentOrder);
	
	
	/**根据订单号查询一笔订单*/
	public PaymentOrder queryPaymentOrderBycode(String ordercode);
	
	/**拿到未完成的订单*/
	public List<PaymentOrder>  queryAllNoFinishOrders(Date startOrderTime);
	
	/**根据商家订单号查询一笔订单**/
	public PaymentOrder queryPaymentOrderByMercode(String ordercode);
	
	
	/**渠道订单号查询一笔订单**/
	public PaymentOrder queryPaymentOrderByChannelcode(String channeOrdercode);
	
	/**查询统计最新的订单*/
	public OrderMarkupLog queryLastestOrderMarkupLog();
	
	
	/***存储待补的订单信息*/
	public OrderMarkupLog saveOrderMarkupLog(OrderMarkupLog orderMarkupLog);
	
	
	
	
	
	/**分页获取商户订单**/
	public Page<PaymentOrder> queryPaymentOrdersByMerchantId(String merchantid, String ordercode, String orderType,  Date startTime, Date endTime,  String orderstatus,  Pageable pageable);
	
	 
	public List<PaymentOrder>    findPaymentOrdersByMerchantidStartEndDateAndStatus(String merchantid, 
			String orderType, Date startTime, Date endTime, String orderstatus);
	
	
	/**分页获取代理商，平台订单*/
	public Page<PaymentOrder> queryPaymentOrdersByAgentid(String agentId, String ordercode, String orderType, Date startTime, Date endTime,  String orderstatus, Pageable pageable);
	
	
	/**分页获取代理商，平台订单*/
	public Page<PaymentOrder> queryPlatformPaymentOrders(String ordercode, String orderType, Date startTime, Date endTime, String orderstatus,  Pageable pageable);
	
	/**分页获取代理商，平台订单*/
	public Map<String, Object> queryPlatformPaymentOrders(String ordercode, String orderType, Date startTime, Date endTime, String orderstatus,String merchantId,String channelCode,String realChannelCode,  Pageable pageable);
	
	/***代理商分页订单查询*/
	public Map<String, Object> queryAgentsPaymentOrders(String ordercode, String orderType, Date startTime,
			Date endTime, String orderstatus, String agentid, String merchantId, String channelCode, String realChannelCode,
			Pageable pageable);
	
	
	/**按时间统计商家某个阶段的交易量*/
	public BigDecimal  querySumAmountByMerchantid(String merchantid,  String orderType, String channelCode,  Date startTime, Date endTime);
	
	
	/**按某个设备的交易量进行统计*/
	public BigDecimal querySumAmountByEquipment(String equipmenttag, String orderType,  Date startTime, Date endTime);
	
	/**按时间统计商家某个阶段的交易笔数*/
	public int queryTotalCntByMerchantid(String merchantid,  String orderType,  Date startTime, Date endTime);
	
	
	/**按时间统计代理商某个阶段的交易量*/
	public BigDecimal  querySumAmountByAgentId(String agentid, String orderType,  Date startTime, Date endTime);
	
	
	/**按时间统计平台某个阶段的交易量*/
	public BigDecimal  queryPlatformSumAmount(String orderType,  Date startTime, Date endTime);
	
	
	/**按时间统计平台某个阶段总的成交笔数*/
	public int queryPlatformTotalCnt(String orderType,  Date startTime, Date endTime);
	
	
	
	
}
