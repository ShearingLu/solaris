package com.smart.pay.payment.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.pay.payment.business.PaymentRequestBusiness;
import com.smart.pay.payment.pojo.PaymentRequest;
import com.smart.pay.payment.repository.PaymentRequestRepository;

@Service
public class PaymentRequestBusinessImpl implements PaymentRequestBusiness{

	@Autowired
	private PaymentRequestRepository paymentRequestRepository;
	
	
	@Override
	public PaymentRequest createPaymentRequest(PaymentRequest request) {
		return paymentRequestRepository.saveAndFlush(request);
	}


	@Override
	public PaymentRequest queryPaymentRequestByMerchantIdAndOutTradeNo(String merchantId, String outTradeNo) {
		return paymentRequestRepository.findPaymentRequestByMerchantidAndOutradeno(merchantId, outTradeNo);
	}

	
	
	
}
