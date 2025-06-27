package com.smart.pay.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.smart.pay.payment.pojo.WithdrawCondition;

@Repository
public interface WithdrawConditionRepository extends JpaRepository<WithdrawCondition, String>, JpaSpecificationExecutor<WithdrawCondition> {



}
