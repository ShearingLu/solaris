package com.smart.pay.payment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.payment.pojo.PaymentChannelRoute;

@Repository
public interface PaymentChannelRouteRepository extends JpaRepository<PaymentChannelRoute, String>, JpaSpecificationExecutor<PaymentChannelRoute> {

	
	 @Query("select paymentChannelRoute from  PaymentChannelRoute paymentChannelRoute where paymentChannelRoute.merchantId=:merchantId and paymentChannelRoute.channelCode=:channelCode and paymentChannelRoute.status='0'")
	 List<PaymentChannelRoute> findPaymentChannelRouteByCode(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode);
	 
	
	 
	 @Query("select paymentChannelRoute from  PaymentChannelRoute paymentChannelRoute where paymentChannelRoute.merchantId=:merchantId")
	 Page<PaymentChannelRoute> findPaymentChannelRouteByMerchantid(@Param("merchantId") String merchantId, Pageable pageable);
	 
	 
	 @Query("select paymentChannelRoute from  PaymentChannelRoute paymentChannelRoute")
	 Page<PaymentChannelRoute> findPaymentChannelRoute(Pageable pageable);
	 
	 
	 
	 @Modifying
	 @Query("delete from  PaymentChannelRoute paymentChannelRoute where paymentChannelRoute.id=:id")
	 void delPaymentChannelRouteById(@Param("id") long id);
	 
	 
	 @Modifying
	 @Query("update PaymentChannelRoute paymentChannelRoute set paymentChannelRoute.status=:status where paymentChannelRoute.realChannelCode=:realChannelCode")
	 void updateChannelRouteStatus(@Param("status") String status, @Param("realChannelCode") String realChannelCode);
	 
	 
}
