package com.smart.pay.clearing.business.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.clearing.business.MerchantTradeStatisticsBusiness;
import com.smart.pay.clearing.pojo.EquipmentTrxStatistics;
import com.smart.pay.clearing.pojo.MerchantDayStatistics;
import com.smart.pay.clearing.pojo.MerchantMonthStatistics;
import com.smart.pay.clearing.repository.EquipmentTrxStatisticsRepository;
import com.smart.pay.clearing.repository.MerchantDayStatisticsRepository;
import com.smart.pay.clearing.repository.MerchantMonthStatisticsRepository;

@Service
public class MerchantTradeStatisticsBusinessImpl implements MerchantTradeStatisticsBusiness{

	@Autowired
	private MerchantDayStatisticsRepository merchantDayStatisticsRepository;
	
	@Autowired
	private MerchantMonthStatisticsRepository merchantMonthStatisticsRepository;
	
	
	@Autowired
	private EquipmentTrxStatisticsRepository equipmentTrxStatisticsRepository;
	
	
	@Transactional
	@Override
	public MerchantDayStatistics createMerchantDayStatistics(MerchantDayStatistics merchantDayStatistics) {
		return merchantDayStatisticsRepository.saveAndFlush(merchantDayStatistics);
	}
	
	@Transactional
	@Override
	public EquipmentTrxStatistics createEquipmentTrxStatistics(EquipmentTrxStatistics equipmentTrxStatistics) {
		return equipmentTrxStatisticsRepository.saveAndFlush(equipmentTrxStatistics);
	}
	
	

	@Override
	public List<MerchantDayStatistics> queryMerchantDayStatisticss(String merchantId, Date startDate, Date endDate) {
		return merchantDayStatisticsRepository.findMerchantDayStatistics(merchantId, startDate, endDate);
	}

	@Transactional
	@Override
	public MerchantMonthStatistics createMerchantMonthStatistics(MerchantMonthStatistics merchantMonthStatistics) {
		return merchantMonthStatisticsRepository.saveAndFlush(merchantMonthStatistics);
	}

	@Override
	public List<MerchantMonthStatistics> queryMerchantMonthStatisticss(String merchantId, int year, int startMonth,
			int endMonth) {
		return merchantMonthStatisticsRepository.findMerchantMonthStatistics(merchantId, year, startMonth, endMonth);
	}

	@Override
	public EquipmentTrxStatistics queryEquipmentTrxStatistics(String equipmentTag, String trxDate) {
		
		return equipmentTrxStatisticsRepository.findEquipmentTrxStatistics(equipmentTag, trxDate);
	}
	
}
