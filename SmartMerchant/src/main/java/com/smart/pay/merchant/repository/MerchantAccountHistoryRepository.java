package com.smart.pay.merchant.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantAccountHistory;

@Repository
public interface MerchantAccountHistoryRepository  extends JpaRepository<MerchantAccountHistory, String>, JpaSpecificationExecutor<MerchantAccountHistory> {

	
	 	@Query("select accountHistory from  MerchantAccountHistory accountHistory where accountHistory.merchantId=:merchantId")
	    Page<MerchantAccountHistory> findMerchantAccountHistoryByMerchantId(@Param("merchantId") String merchantId, Pageable able);
		
	
	 	@Query("select accountHistory from  MerchantAccountHistory accountHistory where accountHistory.merchantId=:merchantId and accountHistory.createTime>=:startTime")
	    Page<MerchantAccountHistory> findMerchantAccountHistoryByMerchantIdByStartTime(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, Pageable able);
		
	 	
	 	@Query("select accountHistory from  MerchantAccountHistory accountHistory where accountHistory.merchantId=:merchantId and accountHistory.createTime>=:startTime and accountHistory.createTime <=:endTime")
	    Page<MerchantAccountHistory> findMerchantAccountHistoryByMerchantIdByStartEndTime(@Param("merchantId") String merchantId, @Param("startTime") Date startTime,  @Param("endTime") Date endTime,  Pageable able);
		
}


