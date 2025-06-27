package com.smart.pay.channel.business;

import com.smart.pay.channel.pojo.hq.HQERegion;
import com.smart.pay.channel.pojo.hq.HQXBindCard;
import com.smart.pay.channel.pojo.hq.HQXRegister;

import java.util.List;

public interface TopupPayChannelBusiness {
    HQXRegister getHQXRegisterByIdCard(String idCard);

    HQXBindCard getHQXBindCardByBankCard(String bankCard);

    void createHQXRegister(HQXRegister hqRegister);

    void createHQXBindCard(HQXBindCard hqxbind);

    HQXBindCard getHQXBindCardByOrderId(String dsorderid);

    List<HQERegion> getHQERegionByParentName(String cityName);
}
