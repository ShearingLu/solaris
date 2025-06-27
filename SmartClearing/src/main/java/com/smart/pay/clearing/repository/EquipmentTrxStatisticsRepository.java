package com.smart.pay.clearing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.clearing.pojo.EquipmentTrxStatistics;


@Repository
public interface EquipmentTrxStatisticsRepository extends JpaRepository<EquipmentTrxStatistics, String>, JpaSpecificationExecutor<EquipmentTrxStatistics> {


	/**查询最近天的交易量**/
	@Query("select equipmentTrxStatistics from  EquipmentTrxStatistics equipmentTrxStatistics where equipmentTrxStatistics.equipmentTag=:equipmentTag and equipmentTrxStatistics.tradeDate =:tradeDate")
	EquipmentTrxStatistics findEquipmentTrxStatistics(@Param("equipmentTag") String equipmentTag, @Param("tradeDate") String tradeDate);
	
	
	
}
