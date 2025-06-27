package com.smart.pay.merchant.repository;

import com.smart.pay.merchant.pojo.MerchantUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantUserInfoRepository extends JpaRepository<MerchantUserInfo, String>, JpaSpecificationExecutor<MerchantUserInfo> {

    @Query("select m from MerchantUserInfo m where m.merchantId=:merchantId and m.subMerchantId=:subMerchantId and m.channelTag=:channel_tag")
    MerchantUserInfo findByMerchantIdAndSubMerchantId(@Param("merchantId") String merchantId, @Param("subMerchantId") String subMerchantId,@Param("channel_tag") String channel_tag);
}
