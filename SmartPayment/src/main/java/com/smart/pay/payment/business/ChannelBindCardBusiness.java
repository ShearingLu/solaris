package com.smart.pay.payment.business;

import com.smart.pay.payment.pojo.ChannelBindCardRoute;

public interface ChannelBindCardBusiness {
    ChannelBindCardRoute queryByService(String service);
}
