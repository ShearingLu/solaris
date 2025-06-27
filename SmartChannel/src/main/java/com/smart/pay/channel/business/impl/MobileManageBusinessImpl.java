package com.smart.pay.channel.business.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.channel.business.MobileManageBusiness;
import com.smart.pay.channel.pojo.MobileEquipment;
import com.smart.pay.channel.pojo.PersonalURL;
import com.smart.pay.channel.repository.MobileEquipmentRepository;
import com.smart.pay.channel.repository.PersonalURLRepository;


@Service
public class MobileManageBusinessImpl  implements MobileManageBusiness{

	
	@Autowired
	private MobileEquipmentRepository mobileEquipmentRepository;

	@Autowired
	private PersonalURLRepository  personalURLRepository;
	
	
	@Override
	public Page<MobileEquipment> queryAllMobileEquipments(Pageable pageable) {
		return mobileEquipmentRepository.pageUserMobileEquipment(pageable);
	}

	
	@Transactional
	@Override
	public MobileEquipment createOrUpdate(MobileEquipment mobileEquipment) {
		return (MobileEquipment)mobileEquipmentRepository.saveAndFlush(mobileEquipment);
	}

	@Override
	public MobileEquipment queryMobileEquipmentByNo(String equipmentNo) {
		return mobileEquipmentRepository.findMobileEquipmentByEquipmentNo(equipmentNo);
	}

	@Transactional
	@Override
	public void delMobileEquipmentByequipmentNo(String equipmentNo) {
		mobileEquipmentRepository.delMobileEquipmentByEquipmentNo(equipmentNo);
	}

	@Override
	public List<MobileEquipment> queryCanUseMobileEquipment() {
		List<MobileEquipment> mobileEuipments = mobileEquipmentRepository.findAllCanUserMobileEquipment();
		
		return mobileEuipments;
	}


	@Override
	public MobileEquipment queryMobileEquipmentByTag(String equipmentTag) {
		
		return mobileEquipmentRepository.findMobileEquipmentByEquipmentTag(equipmentTag);
	}


	@Override
	public List<MobileEquipment> queryExpiredEquipment() {
		
		return mobileEquipmentRepository.findAllExpiredMobileEquipment(new Date());
	}


	@Transactional
	@Override
	public PersonalURL savePersonalURL(PersonalURL personalURL) {
		
		return personalURLRepository.saveAndFlush(personalURL);
	}


	@Override
	public PersonalURL queryPersonalURL(String sign) {
		
		return personalURLRepository.findPersonalURLBySign(sign);
	}


	@Override
	public List<MobileEquipment> findAllEquipments() {
		return mobileEquipmentRepository.findAll();
	}


	@Override
	public List<MobileEquipment> queryAllOnlineEquipment() {
		// TODO Auto-generated method stub
		return mobileEquipmentRepository.findAllEnableMobileEquipment();
	}

}
