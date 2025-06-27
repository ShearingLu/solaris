package com.smart.pay.merchant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantRate;



@Repository
public interface MerchantRateRepository extends JpaRepository<MerchantRate, String>, JpaSpecificationExecutor<MerchantRate> {

	
	@Query("select merchantRate from  MerchantRate merchantRate where merchantRate.merchantId=:merchantId and merchantRate.channelCode=:channelCode")
	MerchantRate findMerchantRateByMerchantId(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode);
	
	@Query("select merchantRate from  MerchantRate merchantRate where merchantRate.merchantId=:merchantId")
	List<MerchantRate> findMerchantRateByMerchantId(@Param("merchantId") String merchantId);
	
	@Modifying
    @Query("delete from  MerchantRate merchantRate where merchantRate.merchantId=:merchantId and merchantRate.channelCode=:channelCode")
    void deleteMerchantRateByMerchantId(@Param("merchantId") String merchantId, @Param("channelCode") String channelCode);
	
}

