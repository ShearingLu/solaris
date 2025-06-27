package com.smart.pay.clearing.business;

import java.util.Date;
import java.util.List;

import com.smart.pay.clearing.pojo.EquipmentTrxStatistics;
import com.smart.pay.clearing.pojo.MerchantDayStatistics;
import com.smart.pay.clearing.pojo.MerchantMonthStatistics;

public interface MerchantTradeStatisticsBusiness {

	
	public MerchantDayStatistics createMerchantDayStatistics(MerchantDayStatistics merchantDayStatistics);
	
	public List<MerchantDayStatistics> queryMerchantDayStatisticss(String merchantId,  Date stateDate, Date endDate);
	
	
	public EquipmentTrxStatistics createEquipmentTrxStatistics(EquipmentTrxStatistics equipmentTrxStatistics);
	
	
	public EquipmentTrxStatistics queryEquipmentTrxStatistics(String equipmentTag, String trxDate);
	
	public MerchantMonthStatistics createMerchantMonthStatistics(MerchantMonthStatistics merchantMonthStatistics);
	
	
	public List<MerchantMonthStatistics> queryMerchantMonthStatisticss(String merchantId, int year, int startMonth, int endMonth);
	
}
