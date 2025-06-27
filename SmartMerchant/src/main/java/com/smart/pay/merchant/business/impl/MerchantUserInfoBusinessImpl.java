package com.smart.pay.merchant.business.impl;

import com.smart.pay.merchant.business.MerchantUserInfoBusiness;
import com.smart.pay.merchant.pojo.MerchantUserInfo;
import com.smart.pay.merchant.repository.MerchantUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MerchantUserInfoBusinessImpl implements MerchantUserInfoBusiness {

    @Autowired
    private MerchantUserInfoRepository merchantUserInfoRepository;
    @Override
    public MerchantUserInfo findByMerchantIdAndSubMerchantId(String merchantId, String subMerchantId,String channel_tag) {
        return merchantUserInfoRepository.findByMerchantIdAndSubMerchantId(merchantId,subMerchantId,channel_tag);
    }

    @Transactional
    @Override
    public void save(MerchantUserInfo merchantUserInfoNew) {
        merchantUserInfoRepository.save(merchantUserInfoNew);
    }

}
