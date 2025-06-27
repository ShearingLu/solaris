package com.smart.pay.channel.basechannel.kernel;


import com.google.common.collect.Maps;
import com.smart.pay.channel.basechannel.kernel.annotation.ChannelAPIMapping;
import com.smart.pay.common.tools.CommonConstants;
import com.smart.pay.common.tools.ResultWrap;
import groovy.util.logging.Slf4j;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
@Slf4j
public class AbstractShieldService implements ShieldService {

    @Override
    @ChannelAPIMapping(value = "genOrderCode")
    public Map<String, Object> genOrderCode(String orderCode) {
        return ResultWrap.init("200", "获取通道订单号成功", orderCode);
    }

    @Override
    @ChannelAPIMapping(value = "getRealAmountModel")
    public Map<String, Object> getRealAmountModel(Map<String, Object> params) {
        return ResultWrap.init("200", "通道金额转换模式", BigDecimal.ROUND_HALF_DOWN);
    }

    @Override
    @ChannelAPIMapping(value = "isBindCard")
    public Map<String, Object> isBindCard(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    @ChannelAPIMapping(value = "isBindCard")
    public Map<String, Object> isNeedBindCard(Map<String, Object> params) throws Exception {
        return null;
    }


}
