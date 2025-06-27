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

import com.smart.pay.merchant.business.MerchantManageBusiness;
import com.smart.pay.merchant.pojo.Merchant;
import com.smart.pay.merchant.pojo.MerchantAccount;
import com.smart.pay.merchant.pojo.MerchantChannel;
import com.smart.pay.merchant.repository.MerchantAccountRepository;
import com.smart.pay.merchant.repository.MerchantChannelRepository;
import com.smart.pay.merchant.repository.MerchantRepository;



@Service
public class MerchantManageBusinessImpl implements MerchantManageBusiness{

	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private MerchantAccountRepository merchantAccountRepository;
	
	@Autowired
	private MerchantChannelRepository merchantChannelRepository;
	
	@Transactional
	@Override
	public Merchant updateMerchant(Merchant merchant) {
		
		
		merchantRepository.saveAndFlush(merchant);
		return merchant;
	}
	

	
	
	@Transactional
	@Override
	public Merchant createMerchant(Merchant merchant) {
		
		
		merchantRepository.saveAndFlush(merchant);
		
		MerchantAccount merchantAccount = new MerchantAccount();
		
		merchantAccount.setAmount(BigDecimal.ZERO);
		merchantAccount.setCreateTime(new Date());
		merchantAccount.setFreezeAmount(BigDecimal.ZERO);
		merchantAccount.setMerchantId(merchant.getMerchantId());
		merchantAccount.setUpdateTime(new Date());
		merchantAccountRepository.saveAndFlush(merchantAccount);
		return merchant;
	}

	@Override
	public Merchant queryMerchantById(String merchantId) {
		return merchantRepository.findMerchantByMerchantId(merchantId);
	}

	public 	Page<Merchant>   queryMerchantByParentId(String parentId ,String merchantId,Pageable pageable){
		Page<Merchant> merchants;
		if(merchantId!=null&&merchantId.trim().length()>0){
			merchants= merchantRepository.queryMerchantByParentIdAndMerchantId(parentId, merchantId, pageable);
		}else{
			merchants=merchantRepository.queryMerchantByParentId(parentId, pageable);
		}
		return merchants;
	}


	@Override
	public Merchant queryMerchantByLoginid(String loginid, String loginpass) {
		
		return merchantRepository.findMerchantByLoginId(loginid, loginpass);
	}
	
	@Override
	public Merchant queryMerchantByLoginid(String loginid) {
		
		return merchantRepository.findMerchantByLoginId(loginid);
	}




	@Transactional
	@Override
	public MerchantChannel createMerchantChannel(MerchantChannel merchantChannel) {
		
		return merchantChannelRepository.saveAndFlush(merchantChannel);
	}



	@Cacheable(value="merchantchannel-key")
	@Override
	public List<MerchantChannel> queryAllMerchantChannels(String merchantId) {
		
		return merchantChannelRepository.findAllMerchantChannel(merchantId);
	}




	@Override
	public List<MerchantChannel>  queryAllMerchantChannelsByStatus(String merchantId, String status) {
		return merchantChannelRepository.findAllUsedMerchantChannel(merchantId, status);
	}



	@Transactional
	@Override
	public void closeOrOpenMerchantChannel(String merchantId, String channelCode, String status) {
		merchantChannelRepository.closeMerchantChannelStatus(merchantId, channelCode, status,  new Date());
	}


	@Override
	public Page<Merchant> queryAllMerchants(String merchant_id,String merAuthStatus,Pageable pageable) {
		Page<Merchant> merchants;
		if(merchant_id!=null&&merchant_id.trim().length()>0&&merAuthStatus!=null&&merAuthStatus.trim().length()>0){
			merchants = merchantRepository.queryMerchantByMerchantIdAndMerAuthStatus(merchant_id,merAuthStatus, pageable);
		}else if(merchant_id!=null&&merchant_id.trim().length()>0){
			merchants = merchantRepository.queryMerchantByMerchantId(merchant_id, pageable);
		}else if(merAuthStatus!=null&&merAuthStatus.trim().length()>0){
			merchants = merchantRepository.queryMerchantByMerAuthStatus(merAuthStatus, pageable);
		}else{
			merchants=merchantRepository.queryAllMerchants(pageable);
		}
		return merchants;
	}




	@Override
	public List<MerchantChannel> queryAllMerchantChannels() {
		return merchantChannelRepository.findAll();
	}




	@Override
	public List<Merchant> allAuthMerchant() {
		
		return merchantRepository.findAllAuthMerchants();
	}
}
