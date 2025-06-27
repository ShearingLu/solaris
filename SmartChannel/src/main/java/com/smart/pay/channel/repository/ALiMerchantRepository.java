package com.smart.pay.channel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.channel.pojo.ALiMerchant;

@Repository
public interface ALiMerchantRepository extends JpaRepository<ALiMerchant, String>, JpaSpecificationExecutor<ALiMerchant>{

	ALiMerchant findByRealChannelCode(String channelTag);

	ALiMerchant findByAppid(String appid);

	
	@Query("select alimerchant from  ALiMerchant alimerchant where alimerchant.appid=:appid and alimerchant.realtype=:realtype")
	ALiMerchant findALiMerchantByAppidAndrealType(@Param("appid") String appid, @Param("realtype") String realtype);
	
}
