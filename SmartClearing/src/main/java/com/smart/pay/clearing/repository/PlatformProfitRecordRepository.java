package com.smart.pay.clearing.repository;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.clearing.pojo.ProfitRecord;


@Repository
public interface PlatformProfitRecordRepository  extends JpaRepository<ProfitRecord, String>, JpaSpecificationExecutor<ProfitRecord> {

	  @Query("select profitRecord from  ProfitRecord profitRecord where profitRecord.merchantId=:merchantId")
	  Page<ProfitRecord>  findProfitOrderByparentId(@Param("merchantId") String merchantId, Pageable pageAble);
	  
	  @Query("select profitRecord from  ProfitRecord profitRecord where profitRecord.merchantId=:merchantId and profitRecord.createTime>=:startTime")
	  Page<ProfitRecord>  findProfitOrderByparentIdStartDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, Pageable pageAble);
	  
	  @Query("select profitRecord from  ProfitRecord profitRecord where profitRecord.merchantId=:merchantId and profitRecord.createTime>=:startTime and profitRecord.createTime<:endTime")
	  Page<ProfitRecord>  findProfitOrderByparentIdStartEndDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable pageAble);

	  @Query("select profitRecord from  ProfitRecord profitRecord where profitRecord.merchantId=:merchantId and profitRecord.orderCode=:orderCode")
	  Page<ProfitRecord>  findProfitOrderByparentIdBycode(@Param("merchantId") String merchantId, @Param("orderCode") String orderCode, Pageable pageAble);
	
	
	  @Query("select sum(profitRecord.ownIncome) from   ProfitRecord profitRecord where profitRecord.merchantId=:merchantId and profitRecord.createTime>=:startTime")
	  BigDecimal  findProfitSumAmountByAgentidStartDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime);
	  
	  @Query("select sum(profitRecord.ownIncome) from   ProfitRecord profitRecord where profitRecord.merchantId=:merchantId and profitRecord.createTime>=:startTime and profitRecord.createTime<:endTime")
	  BigDecimal  findProfitSumAmountByAgentidStartEndDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
	  
	  
}
