package com.smart.pay.clearing.business;

import java.util.Map;

public interface WithdrawOrderDealBusiness {

	
	public Map<String, Object> dealOrder(String ordercode, String orderStatus, String realChannelOrdercode);
	
	
}
