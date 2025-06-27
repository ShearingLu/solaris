package com.smart.pay.merchant.business;

import com.smart.pay.merchant.pojo.MerchantUserInfo;

public interface MerchantUserInfoBusiness {

    MerchantUserInfo findByMerchantIdAndSubMerchantId(String merchantId, String subMerchantId,String channel_tag);

    void save(MerchantUserInfo merchantUserInfoNew);


}
