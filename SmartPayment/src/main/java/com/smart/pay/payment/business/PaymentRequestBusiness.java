package com.smart.pay.payment.business;

import com.smart.pay.payment.pojo.PaymentRequest;

public interface PaymentRequestBusiness {

	
	
	public PaymentRequest createPaymentRequest(PaymentRequest request);
	
	
	
	public PaymentRequest queryPaymentRequestByMerchantIdAndOutTradeNo(String merchantId, String outTradeNo);
	
	
}
