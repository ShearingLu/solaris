package com.smart.pay.merchant.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.merchant.pojo.MerchantIPWhiteList;
import com.smart.pay.merchant.pojo.MerchantKey;

public interface MerchantSecurityBusiness {

	
	public MerchantIPWhiteList createMerchantIPWhiteList(MerchantIPWhiteList ipwhiteList);
	
	public List<MerchantIPWhiteList>  queryMerchantIPWhiteListByMerchantid(String merchantid);
	
	public Page<MerchantIPWhiteList> queryMerchantIPWhiteListByPrent(String merchantid,String ip,Pageable pageable);
	
	public void delMerchantIPWhiteList(long id);
	
	
	public MerchantKey  queryMerchantKey(String merchantid, String keyType);
	
	
	public MerchantKey  createMerchantKey(MerchantKey merchantKey);
	
}
