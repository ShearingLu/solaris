package com.smart.pay.channel.basechannel.kernel;

import java.util.Map;

public interface ShieldService {
    /**
     * params：默认map中会传入key：orderCode的订单号
     * 如果需要通道提供自定义订单号规则，则重新生成订单号，然后将新的订单号返回
     * @param orderCode
     * @return
     */
    public Map<String, Object> genOrderCode(String orderCode);

    /**
     * 按通道自定义金额"四舍五入"模式
     *
    */
    public Map<String, Object> getRealAmountModel(Map<String, Object> params);


    /**
     *  userId
     *  channelTag
     *  bankCard
     *  idCard
     *  phone
     *  bankName
     *  userName
     *  expiredTime
     *  cardType
     *  securityCode
     *  extraFee
     *  rate
     *  dbankName
     *  dphone
     *  dbankCard
     */
    public Map<String, Object> isBindCard(Map<String, Object> params) throws Exception;


    Map<String,Object> isNeedBindCard(Map<String, Object> params) throws Exception;

}
