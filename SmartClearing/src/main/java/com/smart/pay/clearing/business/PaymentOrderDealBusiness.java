package com.smart.pay.clearing.business;

import java.util.Map;

import com.smart.pay.clearing.pojo.PaymentOrder;

public interface PaymentOrderDealBusiness {

	
	public Map<String, Object> dealOrder(String ordercode, String orderStatus, String realChannelOrdercode);
	
	
	public void createNotifyRecord(String merchantid, String notifyurl, String callparams);
	
	
	public void handCallBack(PaymentOrder paymentOrder);
	
}
