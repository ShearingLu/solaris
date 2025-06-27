package com.smart.pay.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantChannelTransactionStatis;

@Repository
public interface MerchantChannelStatisRepository extends JpaRepository<MerchantChannelTransactionStatis, String>, JpaSpecificationExecutor<MerchantChannelTransactionStatis> {

	
	
	  @Query("select transactionStatis from  MerchantChannelTransactionStatis transactionStatis where transactionStatis.merchantId=:merchantId and transactionStatis.channelCode=:channelCode and transactionStatis.statisDate=:statisDate")
	  MerchantChannelTransactionStatis findMerchantChannelTransactionStatis(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode, @Param("statisDate") String statisDate);
	  
}
