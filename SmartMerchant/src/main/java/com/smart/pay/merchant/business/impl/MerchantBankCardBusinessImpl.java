package com.smart.pay.merchant.business.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.merchant.business.MerchantBankCardBusiness;
import com.smart.pay.merchant.business.MerchantManageBusiness;
import com.smart.pay.merchant.pojo.Merchant;
import com.smart.pay.merchant.pojo.MerchantAccount;
import com.smart.pay.merchant.pojo.MerchantBankCard;
import com.smart.pay.merchant.pojo.MerchantChannel;
import com.smart.pay.merchant.repository.MerchantAccountRepository;
import com.smart.pay.merchant.repository.MerchantBankCardRepository;
import com.smart.pay.merchant.repository.MerchantChannelRepository;
import com.smart.pay.merchant.repository.MerchantRepository;



@Service
public class MerchantBankCardBusinessImpl implements MerchantBankCardBusiness{

	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private MerchantBankCardRepository merchantBankCardRepository;

	@Transactional
	@Override
	public MerchantBankCard createMerchantBankCard(MerchantBankCard merchantBankCard) {
		
		return merchantBankCardRepository.save(merchantBankCard);
	}

	@Override
	public MerchantBankCard queryMerchantByMerchantIdAndBankCode(String merchantId, String bankCode) {
		
		return merchantBankCardRepository.queryMerchantBankCardByMerchantIdAndBankCode(merchantId, bankCode);
	}
	
	@Override
	public MerchantBankCard  queryMerchantByMerchantIdAndidDef(String merchantId,String idDef){
		
		return merchantBankCardRepository.queryMerchantBankCardByMerchantIdAndidDef(merchantId, idDef);
	}
	
	
	@Transactional
	@Override
	public void  UpdateMerchantByMerchantIdAndBankCodeSetidDef(String merchantId,String idDef){
		
		 merchantBankCardRepository.UpdateMerchantByMerchantIdAndBankCodeSetidDef(merchantId, idDef);
	}

	@Override
	public Page<MerchantBankCard> queryMerchantByMerchantId(String merchantId, String bankCode,String merchantName, Pageable pageable) {
		Page<MerchantBankCard> merchantBankCards=null;
		
		if(merchantId!=null&&merchantId.trim().length()>0&&bankCode!=null&&bankCode.trim().length()>0&&merchantName!=null&&merchantName.trim().length()>0) {
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByMerchantIdAndBankCodeAndMerchantName(merchantId, bankCode,merchantName, pageable);
		}else  if(bankCode!=null&&bankCode.trim().length()>0&&merchantName!=null&&merchantName.trim().length()>0) {
			
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByBankCodeAndMerchantName( bankCode,merchantName, pageable);
		}else  if(merchantId!=null&&merchantId.trim().length()>0&&merchantName!=null&&merchantName.trim().length()>0) {
			
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByMerchantIdAndMerchantName( merchantId,merchantName, pageable);
		}else  if(merchantId!=null&&merchantId.trim().length()>0&&bankCode!=null&&bankCode.trim().length()>0){
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByMerchantIdAndBankCode(merchantId, bankCode, pageable);
		}else if(merchantId!=null&&merchantId.trim().length()>0){
			
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByMerchantId(merchantId, pageable);
		}else if(bankCode!=null&&bankCode.trim().length()>0){
			
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardByBankCode(bankCode, pageable);
		}else if(merchantName!=null&&merchantName.trim().length()>0){
			
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCardBymerchantName(merchantName, pageable);
		}else{
			merchantBankCards=merchantBankCardRepository.queryMerchantBankCard( pageable);
		}
		
		return merchantBankCards;
	}

	@Transactional
	@Override
	public void deleteMerchantBankCard(String merchantid, String bankCode) {
		merchantBankCardRepository.deleteMerchantBankCardByMerchantIdAndBankCode(merchantid, bankCode);
	}
	
	
	
}
