package com.smart.pay.merchant.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantAccount;

@Repository
public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, String>, JpaSpecificationExecutor<MerchantAccount> {


    @Query("select account from  MerchantAccount account where account.merchantId=:merchantId")
    MerchantAccount findMerchantAccountByMerchantId(@Param("merchantId") String merchantId);
	
    
    
    /**先锁定账户*/
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)  
    @Query("select account from  MerchantAccount account where account.merchantId=:merchantId")
    MerchantAccount findMerchantAccountByMerchantIdLock(@Param("merchantId") String merchantId);
    
}

