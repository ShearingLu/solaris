package com.smart.pay.channel.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.channel.pojo.MobileEquipment;


@Repository
public interface MobileEquipmentRepository extends JpaRepository<MobileEquipment, String>, JpaSpecificationExecutor<MobileEquipment>{

	
	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment where mobileEquipment.enable='1' and mobileEquipment.takeupStatus='0'")
	  List<MobileEquipment> findAllCanUserMobileEquipment();
	  
	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment where mobileEquipment.enable='1'")
	  List<MobileEquipment> findAllEnableMobileEquipment();
	  
	  
	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment where  mobileEquipment.takeupStatus='1' and mobileEquipment.releaseTime <=:curtime")
	  List<MobileEquipment> findAllExpiredMobileEquipment(@Param("curtime") Date curtime);
	  

	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment")
	  Page<MobileEquipment> pageUserMobileEquipment(Pageable pageable);
	
	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment where mobileEquipment.equipmentNo=:equipmentNo")
	  MobileEquipment findMobileEquipmentByEquipmentNo(@Param("equipmentNo") String equipmentNo);
	  
	  
	  @Query("select mobileEquipment from  MobileEquipment mobileEquipment where mobileEquipment.equipmentTag=:equipmentTag")
	  MobileEquipment findMobileEquipmentByEquipmentTag(@Param("equipmentTag") String equipmentTag);

	  @Modifying
	  @Query("delete from  MobileEquipment mobileEquipment where mobileEquipment.equipmentNo=:equipmentNo")
	  void delMobileEquipmentByEquipmentNo(@Param("equipmentNo") String equipmentNo);
}
