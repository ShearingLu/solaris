package com.smart.pay.merchant.business;

import com.smart.pay.merchant.pojo.MerchantUserBindCard;

public interface MerchantUserBindCardBusiness {
    
    MerchantUserBindCard queryByMerchantIdAndbankCardAndChannelTag(String merchantId, String channelTag, String subMerchantId, String bankCard);

    void save(MerchantUserBindCard merchantUserBindCardNew);
}
