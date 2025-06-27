package com.smart.pay.merchant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantBankCard;

@Repository
public interface MerchantBankCardRepository extends JpaRepository<MerchantBankCard, String>, JpaSpecificationExecutor<MerchantBankCard> {

	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.cardNo=:bankCode")
	public MerchantBankCard queryMerchantBankCardByMerchantIdAndBankCode(@Param("merchantId")String merchantId, @Param("bankCode")String bankCode);
	
	
	@Modifying
	@Query("delete from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.cardNo=:bankCode")
	public void deleteMerchantBankCardByMerchantIdAndBankCode(@Param("merchantId")String merchantId, @Param("bankCode")String bankCode);
	
	
	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.idDef=:idDef  and mbc.state='0'")
	public MerchantBankCard  queryMerchantBankCardByMerchantIdAndidDef(@Param("merchantId")String merchantId, @Param("idDef")String idDef);
	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.cardNo=:bankCode and mbc.state='0' and mbc.merchantName like %:merchantName% ")
	public Page<MerchantBankCard>  queryMerchantBankCardByMerchantIdAndBankCodeAndMerchantName(@Param("merchantId")String merchantId, @Param("bankCode")String bankCode, @Param("merchantName")String merchantName,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where  mbc.cardNo=:bankCode and mbc.merchantName like %:merchantName%  and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardByBankCodeAndMerchantName(@Param("bankCode")String bankCode, @Param("merchantName")String merchantName,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and  mbc.merchantName like %:merchantName%  and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardByMerchantIdAndMerchantName(@Param("merchantId")String merchantId, @Param("merchantName")String merchantName,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.cardNo=:bankCode and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardByMerchantIdAndBankCode(@Param("merchantId")String merchantId, @Param("bankCode")String bankCode,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where mbc.merchantId=:merchantId and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardByMerchantId(@Param("merchantId")String merchantId, Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where mbc.cardNo=:bankCode and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardByBankCode(@Param("bankCode")String bankCode,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc where  mbc.merchantName like %:merchantName%  and mbc.state='0'")
	public Page<MerchantBankCard>  queryMerchantBankCardBymerchantName(@Param("merchantName")String merchantName,Pageable pageable);
	
	@Query("from  MerchantBankCard mbc ")
	public Page<MerchantBankCard>  queryMerchantBankCard(Pageable pageable);
	
	@Modifying
	@Query("update MerchantBankCard set idDef=:idDef where merchantId=:merchantId")
	void  UpdateMerchantByMerchantIdAndBankCodeSetidDef(@Param("merchantId")String merchantId,@Param("idDef")String idDef);
	
}


