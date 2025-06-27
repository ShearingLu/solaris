package com.smart.pay.risk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.risk.pojo.MerchantBlackList;

@Repository
public interface MerchantBlackListRepository extends JpaRepository<MerchantBlackList, String>, JpaSpecificationExecutor<MerchantBlackList> {

	
	@Query("select merchantBlackList from  MerchantBlackList merchantBlackList where merchantBlackList.merchantId=:merchantId and merchantBlackList.riskType=:riskType")
	MerchantBlackList findMerchantBlackListByMerchantId(@Param("merchantId") String merchantId, @Param("riskType") String riskType);
	
	
	
	@Modifying
	@Query("delete from  MerchantBlackList merchantBlackList where merchantBlackList.merchantId=:merchantId and merchantBlackList.riskType=:riskType")
	void deleteMerchantBlackListByMerchantId(@Param("merchantId") String merchantId, @Param("riskType") String riskType);
	
	
	
	@Query("select merchantBlackList from  MerchantBlackList merchantBlackList where merchantBlackList.merchantId=:merchantId")
	Page<MerchantBlackList> pageMerchantBlackListByMerchantId(@Param("merchantId") String merchantId, Pageable pageable);
	
	
	@Query("select merchantBlackList from  MerchantBlackList merchantBlackList")
	Page<MerchantBlackList> pageMerchantBlackList(Pageable pageable);
	
	
	
}

