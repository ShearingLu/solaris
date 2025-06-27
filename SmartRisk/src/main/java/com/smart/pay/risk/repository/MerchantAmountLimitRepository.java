package com.smart.pay.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.risk.pojo.MerchantAmountLimit;

@Repository
public interface MerchantAmountLimitRepository extends JpaRepository<MerchantAmountLimit, String>, JpaSpecificationExecutor<MerchantAmountLimit> {

	
	 @Query("select merchantAmountLimit from  MerchantAmountLimit merchantAmountLimit where merchantAmountLimit.merchantId=:merchantId and merchantAmountLimit.channelCode=:channelCode")
	 MerchantAmountLimit findMerchantAmountLimitByMerchantId(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode);
	 
	 
	 
	 
	 @Query("select merchantAmountLimit from  MerchantAmountLimit merchantAmountLimit where merchantAmountLimit.merchantId=:merchantId and merchantAmountLimit.channelCode=:channelCode and  merchantAmountLimit.channelType=:channelType")
	 MerchantAmountLimit findMerchantAmountLimit(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode,@Param("channelType")String channelType);
	 
	 
	 @Query("select merchantAmountLimit from  MerchantAmountLimit merchantAmountLimit where merchantAmountLimit.merchantId=:merchantId")
	 List<MerchantAmountLimit> findMerchantAmountLimitByMerchantId(@Param("merchantId") String merchantId);
	
}
