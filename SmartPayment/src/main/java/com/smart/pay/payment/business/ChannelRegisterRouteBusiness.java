package com.smart.pay.payment.business;

import com.smart.pay.payment.pojo.ChannelRegisterRoute;

public interface ChannelRegisterRouteBusiness {

    ChannelRegisterRoute queryRegisterUrlByService(String service);
}
