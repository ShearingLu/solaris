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

import com.smart.pay.payment.pojo.PaymentChannel;


@Repository
public interface PaymentChannelRepository extends JpaRepository<PaymentChannel, String>, JpaSpecificationExecutor<PaymentChannel> {

	
	
	 @Query("select paymentChannel from  PaymentChannel paymentChannel where paymentChannel.channelCode=:channelCode")
	 PaymentChannel findPaymentChannelByCode(@Param("channelCode") String channelCode);
	 
	 @Query("select paymentChannel from  PaymentChannel paymentChannel where paymentChannel.channelCode=:channelCode and paymentChannel.realPayType=:realPayType")
	 List<PaymentChannel> findPaymentChannelByChannelCodeAndchannelType(@Param("channelCode") String channelCode,@Param("realPayType") String realPayType);
	 
	 @Query("select paymentChannel from  PaymentChannel paymentChannel where paymentChannel.realPayType=:realPayType")
	 List<PaymentChannel> findPaymentChannelBychannelType(@Param("realPayType") String realPayType);
	 
	 @Query("select paymentChannel from  PaymentChannel paymentChannel where paymentChannel.channelCode=:channelCode ")
	 List<PaymentChannel> findPaymentChannelByChannelCode(@Param("channelCode") String channelCode);
	 
	 @Query("select paymentChannel from  PaymentChannel paymentChannel")
	 List<PaymentChannel> findPaymentChannel();
	 
	 @Modifying
	 @Query("delete from  PaymentChannel paymentChannel where paymentChannel.channelCode=:channelCode")
	 void deletePaymentChannelByCode(@Param("channelCode") String channelCode);
	
}

