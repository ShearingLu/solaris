package com.smart.pay.payment.business.impl;

import com.smart.pay.payment.business.ChannelRegisterRouteBusiness;
import com.smart.pay.payment.pojo.ChannelRegisterRoute;
import com.smart.pay.payment.repository.ChannelRegisterRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelRegisterRouteBusinessImpl implements ChannelRegisterRouteBusiness {

    @Autowired
    private ChannelRegisterRouteRepository channelRegisterRouteBusinessRepository;

    @Override
    public ChannelRegisterRoute queryRegisterUrlByService(String service) {
        return channelRegisterRouteBusinessRepository.queryRegisterUrlByService(service);
    }
}
