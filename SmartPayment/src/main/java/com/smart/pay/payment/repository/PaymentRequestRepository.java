package com.smart.pay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.payment.pojo.PaymentRequest;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, String>, JpaSpecificationExecutor<PaymentRequest> {

	
	 @Query("select paymentRequest from  PaymentRequest paymentRequest where paymentRequest.merchantId=:merchantId and paymentRequest.outTradeNo=:outTradeNo")
	 PaymentRequest findPaymentRequestByMerchantidAndOutradeno(@Param("merchantId") String merchantId, @Param("outTradeNo") String outTradeNo);
	 
	
}
