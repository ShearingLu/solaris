package com.smart.pay.merchant.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.merchant.pojo.MerchantBankCard;

public interface MerchantBankCardBusiness {

	
	/**添加银行卡*/
	public MerchantBankCard  createMerchantBankCard(MerchantBankCard merchantBankCard);
	
	
	public MerchantBankCard  queryMerchantByMerchantIdAndBankCode(String merchantId,String bankCode);
	
	
	public void deleteMerchantBankCard(String merchantid, String bankCode);
	
	public void  UpdateMerchantByMerchantIdAndBankCodeSetidDef(String merchantId,String idDef);
	
	public  MerchantBankCard queryMerchantByMerchantIdAndidDef(String merchantId,String idDef);
	
	public 	Page<MerchantBankCard>   queryMerchantByMerchantId(String merchantId ,String bankCode,String merchantName,Pageable pageable);
	
}
