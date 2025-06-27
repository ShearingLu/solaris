package com.smart.pay.channel.business.impl;

import com.smart.pay.channel.business.TopupPayChannelBusiness;
import com.smart.pay.channel.pojo.hq.HQERegion;
import com.smart.pay.channel.pojo.hq.HQXBindCard;
import com.smart.pay.channel.pojo.hq.HQXRegister;
import com.smart.pay.channel.repository.hqx.HQERegionRepository;
import com.smart.pay.channel.repository.hqx.HQXBindCardRepository;
import com.smart.pay.channel.repository.hqx.HQXRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class TopupPayChannelBusinessImpl implements TopupPayChannelBusiness {

    @Autowired
    private HQXBindCardRepository hqxBindCardRepository;

    @Autowired
    private HQXRegisterRepository hqxRegisterRepository;

    @Autowired
    private HQERegionRepository hqeRegionRepository;

    @Autowired
    private EntityManager em;

    @Override
    public HQXRegister getHQXRegisterByIdCard(String idCard) {
        return null;
    }

    @Override
    public HQXBindCard getHQXBindCardByBankCard(String bankCard) {
        em.clear();
        HQXBindCard hqxBindCard = hqxBindCardRepository.getHQXBindCardByBankCard(bankCard);
        return hqxBindCard;
    }

    @Override
    public void createHQXRegister(HQXRegister hqRegister) {
        hqxRegisterRepository.saveAndFlush(hqRegister);
    }

    @Override
    public void createHQXBindCard(HQXBindCard hqxbind) {
        hqxBindCardRepository.saveAndFlush(hqxbind);
    }

    @Override
    public HQXBindCard getHQXBindCardByOrderId(String dsorderid) {
        return hqxBindCardRepository.getHQXBindCardByOrderId(dsorderid);
    }

    @Override
    public List<HQERegion> getHQERegionByParentName(String cityName) {
        em.clear();
        List<HQERegion> result = hqeRegionRepository.getHQERegionByParentName(cityName);
        return result;
    }
}
