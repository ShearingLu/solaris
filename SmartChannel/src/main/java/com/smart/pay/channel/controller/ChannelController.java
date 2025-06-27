package com.smart.pay.channel.controller;

import com.google.common.collect.Maps;
import com.smart.pay.channel.basechannel.kernel.ChannelHandler;
import com.smart.pay.common.tools.CommonConstants;
import com.smart.pay.common.tools.ResultWrap;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChannelController {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private ChannelHandler channelHandler;

    @RequestMapping(value = "/m2.0/shield/pay/gateway", method = RequestMethod.POST)
    public @ResponseBody
    Object
    payGateway(@RequestBody Map<String, Object> params) {
        LOG.info(params.get("requestid") + ".type[" + params.get("orderType") + "]" + ".type[" + params.get("orderCode") + "]." +
                "trigger.shield.begin!===========================支付通道映射：通道标识" + params.get("channelTag") + " 通道方法：" + params.get("methodOps"));

        String channeltag = (String) params.get("channelTag");
        String methodDO = (String) params.get("methodOps");
        Map<String, Object> topupRequestMap = null;
        try {
            topupRequestMap = channelHandler.handle(channeltag, methodDO, params);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultWrap.err(LOG, CommonConstants.FALIED, "请求支付失败");
        }

        return topupRequestMap;
    }



    @RequestMapping(value = "/m2.0/shield/ops/get/isBindCard", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> isBindCard(@RequestBody Map<String,Object> map) {
        Map<String,Object> data = Maps.newHashMap();
        String channelTag = map.get("channelTag").toString();
        data.put("channelTag",channelTag);
        data.put("methodOps","isBindCard");
/*        Map<String,Object> param = Maps.newHashMap();
        param.put("bankCard",bankCard);
        param.put("idCard",idCard);
        param.put("phone",phone);
        param.put("bankName",bankName);
        param.put("userName",userName);
        param.put("expiredTime",expiredTime);
        param.put("securityCode",securityCode);
        param.put("cardType",cardType);*/
        data.put("data",map);
        Map<String, Object> topupRequestMap = (Map<String, Object>) this.payGateway(data);
        return topupRequestMap;
    }

}