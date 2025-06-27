package com.smart.pay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.payment.pojo.SettlementObject;


@Repository
public interface SettlementObjectRepository extends JpaRepository<SettlementObject, String>, JpaSpecificationExecutor<SettlementObject> {


	 @Modifying
	 @Query("delete from  SettlementObject settleObject where settleObject.settlementid=:settlementid")
	 void delSettlementObject(@Param("settlementid") String settlementid);
}
