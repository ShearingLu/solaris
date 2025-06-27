package com.smart.pay.clearing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.clearing.pojo.MerchantDayStatistics;

@Repository
public interface MerchantDayStatisticsRepository extends JpaRepository<MerchantDayStatistics, String>, JpaSpecificationExecutor<MerchantDayStatistics> {


	/**查询最近天的交易量**/
	@Query("select merchantDayStatistics from  MerchantDayStatistics merchantDayStatistics where merchantDayStatistics.merchantId=:merchantId and merchantDayStatistics.tradeDate >=:startDate and merchantDayStatistics.tradeDate <=:endDate order by merchantDayStatistics.tradeDate")
	List<MerchantDayStatistics> findMerchantDayStatistics(@Param("merchantId") String merchantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
}
