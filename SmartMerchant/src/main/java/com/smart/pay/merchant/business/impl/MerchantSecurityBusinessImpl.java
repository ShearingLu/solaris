package com.smart.pay.merchant.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.MerchantSecurityBusiness;
import com.smart.pay.merchant.pojo.MerchantIPWhiteList;
import com.smart.pay.merchant.pojo.MerchantKey;
import com.smart.pay.merchant.repository.MerchantIPWhiteListRepository;
import com.smart.pay.merchant.repository.MerchantKeyRepository;


@Service
public class MerchantSecurityBusinessImpl implements MerchantSecurityBusiness {

	@Autowired
	private MerchantIPWhiteListRepository merchantIpWhiteListRepository;
	
	
	@Autowired
	private MerchantKeyRepository  merchantKeyRepository;
	
	
	
	@Transactional
	@Override
	public MerchantIPWhiteList createMerchantIPWhiteList(MerchantIPWhiteList ipwhiteList) {
		return merchantIpWhiteListRepository.saveAndFlush(ipwhiteList);
	}

	@Transactional
	@Override
	public void delMerchantIPWhiteList(long id) {
		merchantIpWhiteListRepository.deleteMerchantIPWhiteListById(id);
	}



	@Override
	public List<MerchantIPWhiteList> queryMerchantIPWhiteListByMerchantid(String merchantid) {
		return merchantIpWhiteListRepository.queryMerchantIPWhiteListById(merchantid);
	}
	
	@Override
	public Page<MerchantIPWhiteList> queryMerchantIPWhiteListByPrent(String merchantid,String ip,Pageable pageable){
		Page<MerchantIPWhiteList> MerchantIPWhiteLists = null;
		if(merchantid!=null&&merchantid.trim().length()>0&&ip!=null&&ip.trim().length()>0){
			MerchantIPWhiteLists=merchantIpWhiteListRepository.queryMerchantIPWhiteListByMerchantIdANDIp(merchantid, ip, pageable);
		}else if(merchantid!=null&&merchantid.trim().length()>0){
			MerchantIPWhiteLists=merchantIpWhiteListRepository.queryMerchantIPWhiteListByMerchantId(merchantid,  pageable);
			
		}else if(ip!=null&&ip.trim().length()>0){
			MerchantIPWhiteLists=merchantIpWhiteListRepository.queryMerchantIPWhiteListByIp(ip,  pageable);
			
		}else{
			MerchantIPWhiteLists=merchantIpWhiteListRepository.queryMerchantIPWhiteList( pageable);
		}
		
		return MerchantIPWhiteLists;
		
	}

	@Override
	public MerchantKey queryMerchantKey(String merchantid, String keyType) {

		return merchantKeyRepository.findMerchantKeyByMerchantId(merchantid, keyType);
	}



	@Transactional
	@Override
	public MerchantKey createMerchantKey(MerchantKey merchantKey) {

		
		
		
		return merchantKeyRepository.saveAndFlush(merchantKey);
	}

}
