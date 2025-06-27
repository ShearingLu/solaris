package com.smart.pay.timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.smart.pay.timer.pojo.MerchantNotifyNextInterval;

@Repository
public interface MerchantNotifyNextIntervalRepository extends JpaRepository<MerchantNotifyNextInterval, String>, JpaSpecificationExecutor<MerchantNotifyNextInterval> {

	
	 @Query("select httpNotifyInterval from  MerchantNotifyNextInterval httpNotifyInterval  where httpNotifyInterval.curIndex =:curIndex")
	 MerchantNotifyNextInterval findHttpNotifyInterval(@Param("curIndex") int curIndex);
	
}

