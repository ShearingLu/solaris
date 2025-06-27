package com.smart.pay.merchant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String>, JpaSpecificationExecutor<Merchant> {

	
	
	 	@Query("select merchant from  Merchant merchant where merchant.merchantId=:merchantId")
	 	Merchant findMerchantByMerchantId(@Param("merchantId") String merchantId);
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.merAuthStatus='1' and  merchant.identity='2'")
	 	List<Merchant> findAllAuthMerchants();
	 	
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.parent=:parentId and merchant.merchantId=:merchantId")
	 	Page<Merchant> queryMerchantByParentIdAndMerchantId(@Param("parentId")String parentId ,@Param("merchantId") String merchantId,Pageable pageable);
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.parent=:parentId ")
	 	Page<Merchant> queryMerchantByParentId(@Param("parentId") String parentId ,Pageable pageable);
	 	
	 	@Query("select merchant from  Merchant merchant")
	 	Page<Merchant> queryAllMerchants(Pageable pageable);
	 	
		@Query("select merchant from  Merchant merchant where merchant.merchantId=:merchantId and merchant.merAuthStatus=:merAuthStatus")
		Page<Merchant> queryMerchantByMerchantIdAndMerAuthStatus(@Param("merchantId")String merchant_id,@Param("merAuthStatus")String merAuthStatus,Pageable pageable);
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.merchantId=:merchantId")
	 	Page<Merchant> queryMerchantByMerchantId(@Param("merchantId")String merchant_id,Pageable pageable);
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.merAuthStatus=:merAuthStatus")
	 	Page<Merchant> queryMerchantByMerAuthStatus(@Param("merAuthStatus")String merAuthStatus,Pageable pageable);
	 	
	 	@Query("select merchant from  Merchant merchant where merchant.loginId=:loginId and merchant.loginPass=:loginPass")
	 	Merchant findMerchantByLoginId(@Param("loginId") String loginId, @Param("loginPass") String loginPass);
	 	
		@Query("select merchant from  Merchant merchant where merchant.loginId=:loginId")
	 	Merchant findMerchantByLoginId(@Param("loginId") String loginId);
}

