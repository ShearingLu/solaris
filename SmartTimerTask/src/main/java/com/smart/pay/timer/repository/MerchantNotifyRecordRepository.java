package com.smart.pay.timer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.timer.pojo.MerchantNotifyRecord;

@Repository
public interface MerchantNotifyRecordRepository extends JpaRepository<MerchantNotifyRecord, String>, JpaSpecificationExecutor<MerchantNotifyRecord> {

	
	@Query("select httpNotify from  MerchantNotifyRecord httpNotify where httpNotify.status='1' and httpNotify.remainCnt > 0 and httpNotify.nextCallTime <= :curDate ")
    List<MerchantNotifyRecord> findHttpNotifys(@Param("curDate") Date curDate);
	
	
}

