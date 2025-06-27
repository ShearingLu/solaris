package com.smart.pay.merchant.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.SmsBusiness;
import com.smart.pay.merchant.pojo.SmsRecord;
import com.smart.pay.merchant.repository.SmsRecordRepository;

@Service
public class SmsBusinessImpl implements SmsBusiness{

	@Autowired
	private SmsRecordRepository smsRecordRepository;
	
	
	@Transactional
	@Override
	public SmsRecord createSmsRecord(SmsRecord record) {
		
		return smsRecordRepository.saveAndFlush(record);
	}

	@Override
	public SmsRecord queryLastestSmsByPhone(String phone) {
		
		List<SmsRecord> smsRecords = smsRecordRepository.findLastestSmsRecord(phone);
		
		if(smsRecords != null  && smsRecords.size() > 0 ) {
			return smsRecords.get(0);
		}else {
			return null;
		}
		
	}

	
	
	
}
