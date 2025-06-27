package com.smart.pay.channel.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.channel.pojo.MobileEquipment;
import com.smart.pay.channel.pojo.PersonalURL;

public interface MobileManageBusiness {

	
	
	/**获取当前所有设备*/
	public Page<MobileEquipment>  queryAllMobileEquipments(Pageable pageable);
	
	 
	public List<MobileEquipment> findAllEquipments();
	
	/***创建或更新一个设备*/
	public MobileEquipment  createOrUpdate(MobileEquipment mobileEquipment);
	
	public List<MobileEquipment> queryExpiredEquipment();
	
	

	/**根据设备标号获取一个设备*/
	public MobileEquipment  queryMobileEquipmentByNo(String equipmentNo);
	
	
	/**根据设备标识获取一个设备*/
	public MobileEquipment  queryMobileEquipmentByTag(String equipmentTag);
	
	/**删除一个设备**/
	public void delMobileEquipmentByequipmentNo(String equipmentNo);
	
	/**获取一个可用的设备*/
	public List<MobileEquipment> queryCanUseMobileEquipment();
	
	/**获取所有在线的设备*/
	public List<MobileEquipment> queryAllOnlineEquipment();
	
	
	
	/***存储一个用户支付链接*/
	public PersonalURL  savePersonalURL(PersonalURL personalURL);
	
	
	public PersonalURL  queryPersonalURL(String sign);
	
	
}
