package com.smart.pay.merchant.business.impl;

import com.smart.pay.merchant.business.MerchantUserBindCardBusiness;
import com.smart.pay.merchant.pojo.MerchantUserBindCard;
import com.smart.pay.merchant.repository.MerchantUserBindCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserBindCardBusinessImpl implements MerchantUserBindCardBusiness {

    @Autowired
    private MerchantUserBindCardRepository merchantUserBindCardRepository;
    @Override
    public MerchantUserBindCard queryByMerchantIdAndbankCardAndChannelTag(String merchantId, String channelTag, String subMerchantId, String bankCard) {
        return merchantUserBindCardRepository.queryByMerchantIdAndbankCardAndChannelTag(merchantId,channelTag,subMerchantId,bankCard);
    }

    @Override
    public void save(MerchantUserBindCard merchantUserBindCardNew) {
        merchantUserBindCardRepository.save(merchantUserBindCardNew);
    }
}
