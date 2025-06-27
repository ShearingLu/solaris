package com.smart.pay.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantKey;

@Repository
public interface MerchantKeyRepository extends JpaRepository<MerchantKey, String>, JpaSpecificationExecutor<MerchantKey> {

	
	
	@Query("select merchantKey from  MerchantKey merchantKey where merchantKey.merchantId=:merchantId and merchantKey.keyType=:keyType")
	MerchantKey findMerchantKeyByMerchantId(@Param("merchantId") String merchantId, @Param("keyType") String keyType);
	
}

