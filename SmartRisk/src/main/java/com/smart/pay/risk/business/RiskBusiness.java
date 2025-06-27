package com.smart.pay.risk.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.risk.pojo.MerchantAmountLimit;
import com.smart.pay.risk.pojo.MerchantBlackList;

public interface RiskBusiness{

	
	public MerchantAmountLimit creatMerchantAmountLimit(MerchantAmountLimit merchantAmountLimit);
	
	public MerchantAmountLimit queryMerchantAmountLimit(String merchantid, String channelcode);
	
	public MerchantAmountLimit queryMerchantAmountLimi(String merchantid, String channelcode,String channelType);
	
	public List<MerchantAmountLimit> queryMerchantAmountLimitAndMerchantid(String merchantid);
	
	public MerchantBlackList createMerchantBlackList(MerchantBlackList merchantBlackList);
	
	
	public void deleteMerchantBlackList(String merchantid,  String riskType);
	
	
	public MerchantBlackList queryMerchantBlackList(String merchantid, String riskType);

	
	Page<MerchantBlackList> pageQueryMerchantBlackList(String merchantid, Pageable pageable);
	 
}
