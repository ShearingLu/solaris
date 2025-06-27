package com.smart.pay.channel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.smart.pay.channel.pojo.ChannelPaymentCallback;

@Repository
public interface ChannelPaymentCallbackRepository extends JpaRepository<ChannelPaymentCallback, String>, JpaSpecificationExecutor<ChannelPaymentCallback> {

}

