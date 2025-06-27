package com.smart.pay.channel.business;

import com.smart.pay.channel.pojo.OrderParameter;

public interface OrderParameterBusiness {
    void save(OrderParameter orderParameter);

    OrderParameter findByOrderCode(String orderCode);
}
