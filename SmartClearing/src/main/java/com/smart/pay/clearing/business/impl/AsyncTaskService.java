package com.smart.pay.clearing.business.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.clearing.business.OrderBusiness;
import com.smart.pay.clearing.business.PaymentOrderDealBusiness;
import com.smart.pay.clearing.pojo.OnlinePaymentOrder;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.clearing.repository.OrderMarkupLogRepository;
import com.smart.pay.clearing.util.HttpApacheClientUtil;

import net.sf.json.JSONObject;

@Service
public class AsyncTaskService {

	private static final Logger               LOG = LoggerFactory.getLogger(PaymentOrderDealBusinessImpl.class);
	
	
	@Autowired
	private OrderMarkupLogRepository  orderMarkupLogRepository;
	
	@Autowired
	private PaymentOrderDealBusiness  paymentOrderDealBusiness;
	
	@Autowired
	private OrderBusiness  orderBusiness;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public int updateOnlinePaymentOrder(String alipayCode) {
		
		String  updateOnlineOrder	=	"update smartorderup.orderup t1 set t1.status = '1' where t1.alipay_code ='"+alipayCode+"'";
		int updateResult = jdbcTemplate.update(updateOnlineOrder);
		return updateResult;
	}
	
	
	public List<OnlinePaymentOrder>  getAllMarkupOrders(){
		String  selectOnlineOrder	=	"select t1.* from smartorderup.orderup t1 where t1.status = '0'";
		SimpleDateFormat sdfCT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<OnlinePaymentOrder> content = jdbcTemplate.query(selectOnlineOrder.toString(), new RowMapper<OnlinePaymentOrder>() {
			@Override
			public OnlinePaymentOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
				OnlinePaymentOrder onlineOrder = new OnlinePaymentOrder();
				onlineOrder.setAlipayCode(rs.getString("alipay_code"));
				onlineOrder.setOrderCode(rs.getString("order_code"));
				onlineOrder.setStatus(rs.getString("status"));
				onlineOrder.setTrxAmount(rs.getString("order_money"));
				try {
					onlineOrder.setTrxTime(sdfCT.parse(rs.getString("retime")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return onlineOrder;
			}
		});
		
		return content;
	}
	
	@Transactional
	@Async
	public void markupOrder2(OnlinePaymentOrder onlinepaymentOrder) {
		
		
		PaymentOrder  paymentOrder = orderBusiness.queryPaymentOrderBycode(onlinepaymentOrder.getOrderCode());
		
		if(paymentOrder != null && !paymentOrder.getOrderStatus().equalsIgnoreCase("1")) {
			paymentOrderDealBusiness.dealOrder(paymentOrder.getOrderCode(), "1", onlinepaymentOrder.getAlipayCode());
		
		}
		
		updateOnlinePaymentOrder(onlinepaymentOrder.getAlipayCode());
		
	}
	
	
	@Async
	public  void makeupOrder(PaymentOrder order) {
		
		
		if(order.getEquipmentTag() != null && !order.getEquipmentTag().equalsIgnoreCase("")) {
			
			
			
			String mobileURL = orderMarkupLogRepository.queryMobileUrl(order.getEquipmentTag());
			
			
			if(mobileURL != null && !mobileURL.equalsIgnoreCase("")) {
				
				
				String reqURL = "/getresult?trade_no="+order.getOrderCode();
				try {
					
					String responseURL = HttpApacheClientUtil.httpGetRequest(mobileURL+""+reqURL);
					JSONObject resultObj = JSONObject.fromObject(responseURL);
					if(!resultObj.containsKey("code")) {
						return;
					}
					
					String code = resultObj.getString("code");   //订单状态
					LOG.info("order status ...."+code);
					if(code.equalsIgnoreCase("0")) {
						return;
					}
					String money = resultObj.getString("money");  //金额
					LOG.info("money ...."+money);
					
					BigDecimal returnMoney = BigDecimal.ZERO;
			 		
			 		String[] nums = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
			 		String firstNum = money.substring(0, 1);
			 		boolean allNums = false;
			 		for(int i=0; i<nums.length; i++) {
			 			
			 			if(nums[i].equalsIgnoreCase(firstNum)) {
			 				allNums = true;
			 				break;
			 			}
			 			
			 		}
			 		
			 		
			 		if(allNums) {
			 			returnMoney = new BigDecimal(money);
			 		}else {
			 			
			 			returnMoney = new BigDecimal(money.substring(1));
			 		}
					
					
					String channelOrderNo =  resultObj.getString("no");  //第三方订单号
					
					if(code.equalsIgnoreCase("1")  && order.getAmount().compareTo(returnMoney) == 0 && order.getOrderStatus().equalsIgnoreCase("0")) {
						try {
							paymentOrderDealBusiness.dealOrder(order.getOrderCode(), "1", channelOrderNo);
							return;
						}catch(Exception e) {
							return;
						}
						
					}else {
						return;
					}
					
				}catch(Exception e) {
					LOG.info("设备链接异常........."+e.getMessage());
					/**直接标记当前设备异常**/
					return;
				}
				
			}else {
				
				return;
				
			}
				
			
			
			
			
		}else {
			
			return;
		}
		/**获取该手机的url*/
		
		
		/**发起调用， 获取返回信息，如果出现网络异常， 直接跑出去*/
		
		
		
		/**如果发现订单已经成功了， 直接补单操作*/
		
	
		
	}
	
	
	
}
