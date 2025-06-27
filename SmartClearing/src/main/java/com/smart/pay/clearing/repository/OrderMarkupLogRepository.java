package com.smart.pay.clearing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.smart.pay.clearing.pojo.OrderMarkupLog;

@Repository
public interface OrderMarkupLogRepository  extends JpaRepository<OrderMarkupLog, String>, JpaSpecificationExecutor<OrderMarkupLog> {

	
	
	 @Query(value = "select * from t_order_markup_log  order by statistics_date desc  limit 0,1;", nativeQuery = true)
	 OrderMarkupLog queryOrderMarkupLog();
	  


	 @Query(value = "SELECT t.mobile_url FROM smartchannel.t_mobile_equipment t WHERE t.equipment_tag = :equipmentTag", nativeQuery = true)
	 String queryMobileUrl(@Param("equipmentTag") String equipmentTag);



}