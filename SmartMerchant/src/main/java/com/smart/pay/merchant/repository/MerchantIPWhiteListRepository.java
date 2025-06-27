package com.smart.pay.merchant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantIPWhiteList;


@Repository
public interface MerchantIPWhiteListRepository extends JpaRepository<MerchantIPWhiteList, String>, JpaSpecificationExecutor<MerchantIPWhiteList> {

	
	 	@Modifying
	    @Query("delete from MerchantIPWhiteList ipwhitelist where ipwhitelist.id=:id")
	    void deleteMerchantIPWhiteListById(@Param("id") long id);
	
	 	
	 	
	 	@Query("from MerchantIPWhiteList ipwhitelist where ipwhitelist.merchantId=:merchant_id")
		List<MerchantIPWhiteList> queryMerchantIPWhiteListById(@Param("merchant_id") String merchantid);
	
	 	@Query("from MerchantIPWhiteList ipwhitelist where ipwhitelist.merchantId=:merchant_id and ipwhitelist.whitelist=:ip")
	 	Page<MerchantIPWhiteList> queryMerchantIPWhiteListByMerchantIdANDIp(@Param("merchant_id") String merchantid,@Param("ip") String ip,Pageable pageable);
	 	
	 	@Query("from MerchantIPWhiteList ipwhitelist where ipwhitelist.merchantId=:merchant_id ")
		Page<MerchantIPWhiteList> queryMerchantIPWhiteListByMerchantId(@Param("merchant_id") String merchantid,Pageable pageable);
	 	
	 	@Query("from MerchantIPWhiteList ipwhitelist where  ipwhitelist.whitelist=:ip")
	 	Page<MerchantIPWhiteList> queryMerchantIPWhiteListByIp(@Param("ip") String ip,Pageable pageable);
	 	
	 	@Query("from MerchantIPWhiteList ipwhitelist")
	 	Page<MerchantIPWhiteList> queryMerchantIPWhiteList(Pageable pageable);
	 	
}


