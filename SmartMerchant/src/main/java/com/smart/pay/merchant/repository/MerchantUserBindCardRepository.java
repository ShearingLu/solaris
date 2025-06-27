package com.smart.pay.merchant.repository;

import com.smart.pay.merchant.pojo.MerchantUserBindCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantUserBindCardRepository extends JpaRepository<MerchantUserBindCard, String>, JpaSpecificationExecutor<MerchantUserBindCard> {

    @Query("select m from MerchantUserBindCard m where m.merchant_id=:merchantId and m.subMerchantId=:subMerchantId and m.channelTag=:channelTag and m.bankCard=:bankCard")
    MerchantUserBindCard queryByMerchantIdAndbankCardAndChannelTag(@Param("merchantId") String merchantId, @Param("channelTag") String channelTag, @Param("subMerchantId") String subMerchantId, @Param("bankCard") String bankCard);
}
