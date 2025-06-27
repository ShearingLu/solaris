package com.smart.pay.risk.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.risk.business.RiskBusiness;
import com.smart.pay.risk.pojo.MerchantAmountLimit;
import com.smart.pay.risk.pojo.MerchantBlackList;
import com.smart.pay.risk.repository.MerchantAmountLimitRepository;
import com.smart.pay.risk.repository.MerchantBlackListRepository;



@Service
public class RiskBusinessImpl implements RiskBusiness{

	
	@Autowired
	private MerchantAmountLimitRepository amoutLimitRepository;
	
	
	@Autowired
	private MerchantBlackListRepository   merchantBlackListRepository;
	
	
	@Transactional
	@Override
	public MerchantAmountLimit creatMerchantAmountLimit(MerchantAmountLimit merchantAmountLimit) {
		
		return amoutLimitRepository.saveAndFlush(merchantAmountLimit);
	}

	@Override
	public MerchantAmountLimit queryMerchantAmountLimit(String merchantid, String channelcode) {
		
		return amoutLimitRepository.findMerchantAmountLimitByMerchantId(merchantid, channelcode);
	}
	
	@Override
	public MerchantAmountLimit queryMerchantAmountLimi(String merchantid, String channelcode, String channelType) {
		
		return amoutLimitRepository.findMerchantAmountLimit(merchantid, channelcode,channelType);
	}
	
	@Override
	public List<MerchantAmountLimit> queryMerchantAmountLimitAndMerchantid(String merchantid){
		return amoutLimitRepository.findMerchantAmountLimitByMerchantId(merchantid);
	}

	@Transactional
	@Override
	public MerchantBlackList createMerchantBlackList(MerchantBlackList merchantBlackList) {
		return merchantBlackListRepository.saveAndFlush(merchantBlackList);
	}

	@Transactional
	@Override
	public void deleteMerchantBlackList(String merchantid, String riskType) {
		merchantBlackListRepository.deleteMerchantBlackListByMerchantId(merchantid, riskType);
	}

	@Override
	public MerchantBlackList queryMerchantBlackList(String merchantid, String riskType) {
		
		return merchantBlackListRepository.findMerchantBlackListByMerchantId(merchantid, riskType);
	}

	@Override
	public Page<MerchantBlackList> pageQueryMerchantBlackList(String merchantid, Pageable pageable) {
		
		if(merchantid != null && !merchantid.equalsIgnoreCase("")) {
			return merchantBlackListRepository.pageMerchantBlackListByMerchantId(merchantid, pageable);
		}else {
			return merchantBlackListRepository.pageMerchantBlackList(pageable);
		}
		
	}


}
