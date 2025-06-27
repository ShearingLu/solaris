package com.smart.pay.clearing.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.smart.pay.clearing.pojo.MerchantMonthStatistics;

@Repository
public interface MerchantMonthStatisticsRepository extends JpaRepository<MerchantMonthStatistics, String>, JpaSpecificationExecutor<MerchantMonthStatistics> {

	
	/**查询最近天的交易量**/
	@Query("select merchantMonthStatistics from  MerchantMonthStatistics merchantMonthStatistics where merchantMonthStatistics.merchantId=:merchantId and merchantMonthStatistics.year =:year and merchantMonthStatistics.month >=:startMonth and merchantMonthStatistics.month <=:endMonth order by merchantMonthStatistics.month")
	List<MerchantMonthStatistics> findMerchantMonthStatistics(@Param("merchantId") String merchantId, @Param("year") int year, @Param("startMonth") int startMonth, @Param("endMonth") int endMonth);
	
	
}
