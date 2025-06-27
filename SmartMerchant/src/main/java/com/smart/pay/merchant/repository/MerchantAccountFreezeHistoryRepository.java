package com.smart.pay.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.MerchantAccountFreezeHistory;

@Repository
public interface MerchantAccountFreezeHistoryRepository extends JpaRepository<MerchantAccountFreezeHistory, String>, JpaSpecificationExecutor<MerchantAccountFreezeHistory> {

	

}
