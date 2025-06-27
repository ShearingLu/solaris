package com.smart.pay.channel.business.impl;

import com.smart.pay.channel.basechannel.kernel.annotation.ChannelMapping;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.business.TopupPayChannelBusiness;
import com.smart.pay.channel.controller.hqx.HQXpageRequest;
import com.smart.pay.channel.pojo.hq.HQXRegister;
import com.smart.pay.channel.pojo.PaymentOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@ChannelMapping(value = "HQX_DH")
@Service
public class HQXTopupPage extends BasePaymentRequest implements PaymentRequestBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(HQXTopupPage.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HQXpageRequest hqxPageRequest;

    @Value("${payment.ipAddress}")
    private String ipAddress;

    @Autowired
    private TopupPayChannelBusiness topupPayChannelBusiness;

    @Override
    public Map<String, Object> paymentRequest(Map<String, Object> params) throws Exception {

        PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
        LOG.info("环球X订单请求参数============"+paymentOrder);
        Map<String, Object> map = new HashMap<>();
        Map<String, String> maps = new HashMap<>();
        String orderCode = paymentOrder.getOrderCode();//订单号
        String orderType = paymentOrder.getRealtype();//订单类型 10 消费  11 还款
        String idCard = paymentOrder.getIdCard(); //身份证
        String bankCard = paymentOrder.getBankCard();//银行卡 信用卡
        String rate = paymentOrder.getMerRate().toString();//费率
        String extraFee = paymentOrder.getExtraFee().toString();//额外手续费
        LOG.info("qhx订单号========================"+orderCode);

//        HQXRegister hqRegister = topupPayChannelBusiness.getHQXRegisterByIdCard(idCard);

        if ("10".equals(orderType)) {
            LOG.info("根据判断进入消费任务======");
            // 判断用户是否修改费率或单笔手续费
//            if (!rate.equals(hqRegister.getRate()) || !extraFee.equals(hqRegister.getExtraFee())) {
//                maps = (Map<String, String>) hqxPageRequest.changeRate(idCard,bankCard, rate, extraFee);
//                if (!"000000".equals(maps.get("resp_code"))) {
//                    return map;
//                }
//            }
            // 用户进入消费任务
            map = (Map<String, Object>) hqxPageRequest.topay(orderCode);
        }

        if ("11".equals(orderType)) {
            // 用户进入还款任务
            map = (Map<String, Object>) hqxPageRequest.transfer(orderCode);
        }

        return map ;
    }

}
