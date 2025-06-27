package com.smart.pay.channel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.smart.pay.channel.pojo.WXMerchant;
import com.smart.pay.channel.wxpay.tools.WXPayConfigImpl;

@Repository
public interface WXMerchantRepository  extends JpaRepository<WXMerchant, Long>,JpaSpecificationExecutor<WXMerchant>{

	WXMerchant findByRealChannelCode(String channelTag);

	WXMerchant findByAppid(String appid);

	WXMerchant findByAppidAndRealType(String appid, String realtype);

}
