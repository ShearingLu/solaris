package com.smart.pay.payment.business.impl;

import com.smart.pay.payment.business.ChannelBindCardBusiness;
import com.smart.pay.payment.pojo.ChannelBindCardRoute;
import com.smart.pay.payment.repository.ChannelBindCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelBindCardBusinessIImpl implements ChannelBindCardBusiness {

    @Autowired
    private ChannelBindCardRepository channelBindCardBusinessIRepository;
    @Override
    public ChannelBindCardRoute queryByService(String service) {
        return channelBindCardBusinessIRepository.queryByService(service);
    }
}
