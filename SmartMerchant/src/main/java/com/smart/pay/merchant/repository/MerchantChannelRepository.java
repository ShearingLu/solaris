package com.smart.pay.merchant.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantChannel;

@Repository
public interface MerchantChannelRepository  extends JpaRepository<MerchantChannel, String>, JpaSpecificationExecutor<MerchantChannel> {

	
	  @Query("select merchantChannel from  MerchantChannel merchantChannel where merchantChannel.merchantId=:merchantId and merchantChannel.status=:status")
	  List<MerchantChannel> findAllUsedMerchantChannel(@Param("merchantId") String merchantId, @Param("status") String status);
	
	  
	  @Query("select merchantChannel from  MerchantChannel merchantChannel where merchantChannel.merchantId=:merchantId")
	  List<MerchantChannel> findAllMerchantChannel(@Param("merchantId") String merchantId);
	  
	  
	  @Modifying
	  @Query("update  MerchantChannel merchantChannel set merchantChannel.status=:status, merchantChannel.updateTime=:updateTime where merchantChannel.merchantId=:merchantId and merchantChannel.channelCode=:channelCode")
	  void closeMerchantChannelStatus(@Param("merchantId") String merchantId,  @Param("channelCode") String channelCode, @Param("status") String status, @Param("updateTime") Date updateTime);
}

