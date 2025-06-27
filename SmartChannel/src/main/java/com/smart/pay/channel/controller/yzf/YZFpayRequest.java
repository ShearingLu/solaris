package com.smart.pay.channel.controller.yzf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.pay.channel.basechannel.BaseChannel;
import com.smart.pay.channel.basechannel.kernel.annotation.ChannelAPIMapping;
import com.smart.pay.channel.basechannel.kernel.annotation.ChannelMapping;
import com.smart.pay.channel.business.TopupPayChannelBusiness;
import com.smart.pay.channel.business.yzf.YXEBusiness;
import com.smart.pay.channel.common.ChannelUtils;
import com.smart.pay.channel.config.RedisUtil;
import com.smart.pay.channel.controller.hqx.HQXpageRequest;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.pojo.PaymentRequestParameter;
import com.smart.pay.channel.pojo.hq.HQERegion;
import com.smart.pay.channel.pojo.yzf.YXEAddress;
import com.smart.pay.channel.pojo.yzf.YXEBankBin;
import com.smart.pay.channel.util.hqx.HashMapConver;
import com.smart.pay.channel.util.hqx.SmartRepayChannel;
import com.smart.pay.channel.util.yzf.JiFuPayMsg;
import com.smart.pay.channel.util.yzf.MerchantApiUtil;
import com.smart.pay.channel.util.yzf.SimpleHttpUtils;
import com.smart.pay.channel.util.yzf.YZFSmartRepayChannel;
import com.smart.pay.common.tools.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件名: byt_router_new
 * 包名: com.smart.pay.channel.controller.YZF
 * 说明:
 */
@ChannelMapping(value = "YZF_PAY")
@Controller
@EnableAutoConfiguration
public class YZFpayRequest extends BaseChannel {

    private static final Logger LOG = LoggerFactory.getLogger(HQXpageRequest.class);

    private final String payKey ="ff604dec92344462b985a3ec02c1ad4a";// 商户支付Key

    private final String paySecret ="5cc8fe60d0814a63a79f2eac7a96afc6";// 签名

    private final String platMerchant ="DEV88882019111310001145";// 平台商户

    private final String URL ="http://47.99.58.100:28520";// 请求地址 （生产地址）
//    @Value("${payment.ipAddress}")
//    private  String payKey ;// 商户支付Key
//    @Value("${payment.ipAddress}")
//    private  String paySecret ;// 签名
//    @Value("${payment.ipAddress}")
//    private  String platMerchant ;// 平台商户
//    @Value("${payment.ipAddress}")
//    private  String URL ;// 请求地址 （生产地址）

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TopupPayChannelBusiness topupPayChannelBusiness;

    @Value("${payment.ipAddress}")
    private String ip;

    @Autowired
    YXEBusiness yxeBusiness;



    @Autowired
    private RestTemplate restTemplate;

    // 飏支付支付请求
    @RequestMapping(method = RequestMethod.POST, value = "/smartchannel/topup/yzf/topay")
    @ResponseBody
    public Object toPay(@RequestParam(value = "ordercode") String orderCode) {

        Map map = new HashMap();
        LOG.info("进入飏支付消费======订单号：" + orderCode);
        // 查询订单信息
        PaymentOrder prp = redisUtil.getPaymentRequestParameter(orderCode);

        LOG.info("进入飏支付消费======订单信息：" + prp.toString());
        String idCard = prp.getIdCard();
        String bankCard = prp.getBankCard();
        BigDecimal amount = prp.getRealAmount();
        BigDecimal rate = prp.getMerRate();
        String extra = prp.getCity();// //消费计划|福建省-泉州市
        String cityName = extra.substring(extra.indexOf("-") + 1);
//        //去掉‘市’
//        String city=cityName.substring(0,cityName.length());
//        String provinceName = extra.substring(extra.indexOf("|") + 1,extra.indexOf("-"));
//        //去掉‘省’
//        String province =provinceName.substring(0,provinceName.length());

        // 获取落地城市（消费的城市）
        YXEAddress yxeAddress = yxeBusiness.getYXEAddressByCityName(cityName);
        if (yxeAddress == null) {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "暂时不支持该城市");
            return map;
        }
        String cityCode = yxeAddress.getId().toString(); // 城市
        String provinceCode = cityCode.substring(0, 2) + "0000"; // 省份
        YXEBankBin yxeBankBinByBankName = yxeBusiness.getYXEBankBinByBankName(prp.getBankName());

        if(null==yxeBankBinByBankName){
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "暂时不支持该银行！");
            return map;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("platMerchant", platMerchant); // 平台商户 平台提供
        paramMap.put("payKey", payKey); // 商户支付Key
        paramMap.put("productType", "40000103"); // 产品类型 固定值
        paramMap.put("quotaType", "SMALL_AMOUNT"); // 额度类型 当该值为空时，表示不区分大小额
        paramMap.put("orderPrice", amount); // 支付金额(元)
        paramMap.put("feeRate", rate); // 支付费率  费率为0.5%，则填写0.005即可
        paramMap.put("outTradeNo", orderCode); // 支付订单号
        paramMap.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 订单时间
        paramMap.put("notifyUrl", ip+"/smartchannel/topup/yzf/payNotifyUrl"); // 后台异步通知地址
        paramMap.put("productName", "个体商户"); // 不能为空，自由填写商品名称即可

        paramMap.put("province", provinceCode); // 省份
        paramMap.put("city", cityCode); // 城市
        // 银行卡私密信息
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("bankAccountType", "PRIVATE_CREDIT_ACCOUNT"); // 银行卡类型 PRIVATE_CREDIT_ACCOUNT 对私信用卡  只支持信用卡
        objectMap.put("phoneNo", prp.getPhone()); // 手机号 必填

        objectMap.put("bankCode",yxeBankBinByBankName.getBankCode()); // 银行编码
        objectMap.put("bankAccountName", prp.getUserName()); // 账户姓名 必填
        objectMap.put("bankAccountNo", prp.getBankCard()); // 信用卡号 必填
        objectMap.put("certType", "IDENTITY"); // 证件类型 IDENTITY 身份证
        objectMap.put("certNo", prp.getIdCard()); // 身份证号 必填
        objectMap.put("cvn2", prp.getSecurityCode()); // CVN2 必填
        objectMap.put("expDate", prp.getExpiredTime()); // MMYY 必填
        String secretContent = MerchantApiUtil.aesEncode(objectMap, paySecret);
        paramMap.put("secretContent", secretContent); // 加密密文
        paramMap.put("sign", MerchantApiUtil.getSign(paramMap, paySecret)); // 签名

        LOG.info("飏支付消费请求报文Map:" + paramMap);

        String url = URL + "/bigpay-web-gateway/quickPay/initPay";
        String payResult = SimpleHttpUtils.httpPost(url, paramMap);


        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(payResult);
        LOG.info("飏支付消费返回信息" + payResult);
        Object code = jsonObject.get("resultCode"); // 返回码
        Object payMessage = jsonObject.get("payMessage"); // 请求结果(请求成功时)
        Object errMsg = jsonObject.get("errMsg"); // 错误信息(请求失败时)
        Object payStatus = jsonObject.get("payStatus");
        if (!"0000".equals(code)) {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付消费请求失败，原因" + errMsg);
            return map;
        }
        if ("SUCCESS".equals(payStatus)) {


            map.put(CommonConstants.RESP_CODE, "999998");
            map.put(CommonConstants.RESP_MESSAGE, "支付成功，等待银行扣款");
            return map;
        } else if ("PAYING".equals(payStatus)) {
            Map<String, Object> rersultMap =null;
            try {
                Thread.sleep(5000);
                rersultMap = this.payQuery(orderCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(CommonConstants.SUCCESS.equals(rersultMap.get(CommonConstants.RESP_CODE))){
                //成功

                map.put(CommonConstants.RESP_CODE, "999998");
                map.put(CommonConstants.RESP_MESSAGE, "支付成功，等待银行扣款");
            }else if(CommonConstants.FALIED.equals(rersultMap.get(CommonConstants.RESP_CODE))){
                map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
                map.put(CommonConstants.RESP_MESSAGE, errMsg);
                return map;
            }
            map.put(CommonConstants.RESP_CODE, "999998");
            map.put(CommonConstants.RESP_MESSAGE, "支付处理中，等待银行扣款");
            return map;
        } else {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, errMsg);
            return map;
        }
    }



//    // 飏支付消费回调
//    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},
//            value = "/smartchannel/topup/yzf/payNotifyUrl")
//    @ResponseBody
//    public String payNotifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try {
//            LOG.info("飏支付消费回调======");
//            String orderPrice = request.getParameter("orderPrice");
//            String orderTime = request.getParameter("orderTime");
//            String outTradeNo = request.getParameter("outTradeNo");
//            String payKey = request.getParameter("payKey");
//            String product_name = request.getParameter("product Name");
//            String sign = request.getParameter("sign");
//            String successTime = request.getParameter("successTime");
//            String tradeStatus = request.getParameter("tradeStatus");
//
//            PaymentOrder prp = redisUtil.getPaymentRequestParameter(outTradeNo);
//            if (prp == null) {
//                LOG.info("飏支付消费回调{}不存在", orderPrice);
//            }
//            String channelTag = prp.getChannelTag();
//            String ipAddress = prp.getIpAddress();
//
//            if ("SUCCESS".equals(tradeStatus)) {
//
//                LOG.info("飏支付消费回调======消费成功" + prp);
//
//                RestTemplate restTemplate = new RestTemplate();
//
//                String url = prp.getIpAddress() + "/v1.0/creditcardmanager/update/taskstatus/by/ordercode";
//                MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
//                requestEntity.add("orderCode", outTradeNo);
//
//                String result = null;
//                JSONObject jsonObject;
//                JSONObject resultObj;
//                try {
//                    result = restTemplate.postForObject(url, requestEntity, String.class);
//                    LOG.info("RESULT================" + result);
//                    jsonObject = JSONObject.fromObject(result);
//                    resultObj = jsonObject.getJSONObject("result");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LOG.error("", e);
//                }
//
//                url = prp.getIpAddress() + ChannelUtils.getCallBackUrl(prp.getIpAddress());
//
//                requestEntity = new LinkedMultiValueMap<String, String>();
//                requestEntity.add("status", "1");
//                requestEntity.add("order_code", outTradeNo);
//                try {
//                    result = restTemplate.postForObject(url, requestEntity, String.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LOG.error("", e);
//                }
//
//                LOG.info("订单状态修改成功===================" + outTradeNo + "====================" + result);
//
//                LOG.info("订单已支付!");
//
//                return "SUCCESS";
//
//            } else if ("PAYING".equals(tradeStatus)) {
//                LOG.info("交易处理中!");
//
//                return "SUCCESS";
//            } else {
//
//                LOG.info("交易处理失败!");
//                addOrderCauseOfFailure(outTradeNo, request.toString(), ipAddress);
//
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            LOG.info("飏支付消费回调======异常" + e);
//            return "FAIL";
//        }
//
//    }


    public Map<String, Object> payQuery(String orderCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        LOG.info("进入飏支付消费结果查询======订单号：" + orderCode);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("platMerchant", platMerchant); // 平台商户 平台提供
        paramMap.put("payKey", payKey); // 商户支付Key
        paramMap.put("outTradeNo", orderCode); // 原交易订单号
        // 签名及生成请求API的方法
        String sign = MerchantApiUtil.getSign(paramMap, paySecret);
        paramMap.put("sign", sign); // 签名
        LOG.info("进入飏支付消费结果查询请求原文" + paramMap.toString());
        String url = URL + "/bigpay-web-gateway/query/singleOrder";
        String payResult = SimpleHttpUtils.httpPost(url, paramMap);
        LOG.info("进入飏支付消费结果查询结果" + payResult);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(payResult);
        Object resultCode = jsonObject.get("resultCode"); // 返回码
        Object errMsg = jsonObject.get("errMsg"); // 错误信息(请求失败时)
        Object orderStatus = jsonObject.get("orderStatus");
        if (!"0000".equals(resultCode)) {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付消费请求失败");
            return map;
        }
        if ("SUCCESS".equals(orderStatus)) {
            Object trxNo = jsonObject.get("trxNo");
            LOG.info("订单执行成功==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.SUCCESS);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付消费支付成功");
            return map;
        } else if ("PAYING".equals(orderStatus)) {
            LOG.info("订单执行失败==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付消费支付失败");
            return map;
        } else if ("WAITING_PAYMENT".equals(orderStatus)) {
            LOG.info("订单处理中==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.WAIT_CHECK);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付消费处理中，等待银行扣款");
            return map;
        } else {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, errMsg);
            return map;
        }
    }



    /**
     * 开始代付
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/smartchannel/topup/yzf/transfer")
    public Object transfer(@RequestParam(value = "orderCode") String orderCode) throws Exception {
        LOG.info("开始进入还款========================");

        Map map = new HashMap();
        LOG.info("进入飏支付还款======订单号：" + orderCode);
        PaymentOrder prp = redisUtil.getPaymentRequestParameter(orderCode);
        LOG.info("进入飏支付还款======订信息：" + prp);
        String idCard = prp.getIdCard();
        String bankCard = prp.getBankCard();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("platMerchant", platMerchant);// 平台商户 平台提供
        paramMap.put("payKey", payKey);
        paramMap.put("quotaType", "SMALL_AMOUNT ");// 额度类型 当该值为空时，表示不区分大小额
        paramMap.put("proxyType", "T0"); // 交易类型 固定值
        paramMap.put("productType", "QUICKPAY"); // QUICKPAY 固定值

        paramMap.put("outTradeNo", orderCode); // 商户T0出款订单号
        paramMap.put("orderPrice", prp.getRealAmount()); // 订单金额, 单位:元
        paramMap.put("settFee", prp.getExtraFee()); // 结算手续费

        paramMap.put("bankAccountType", "PRIVATE_CREDIT_ACCOUNT"); // 收款银行卡类型
        paramMap.put("certNo", prp.getIdCard()); // 身份证号 必填
        paramMap.put("phoneNo", prp.getPhone());  // 手机号 必填
        paramMap.put("receiverName", prp.getUserName());// 收款人姓名 必填
        paramMap.put("certType", "IDENTITY"); // 收款人证件类型 固定值
        paramMap.put("receiverAccountNo", prp.getBankCard()); // 收款人银行卡号 必填

        paramMap.put("bankClearNo", "265464"); // 代付清算行号  可随意填，但不能为空
        paramMap.put("bankBranchNo", "613123"); // 代付开户行支行行号   可随意填，但不能为空
        paramMap.put("bankName", "中国工商银行"); // 开户行名称  可随意填，但不能为空
        paramMap.put("bankCode", "ICBC"); // 银行编码   可随意填，但不能为空
        paramMap.put("bankBranchName", "中国建设银行杭州支行"); // 代付开户行支行名称  可随意填，但不能为空
        paramMap.put("province", "浙江"); // 开户省份  可随意填，但不能为空
        paramMap.put("city", "杭州"); // 开户城市  可随意填，但不能为空

        paramMap.put("openCardId",prp.getBindId()); //绑卡返回的openCardId 必填

        JiFuPayMsg msg = new JiFuPayMsg();
        paramMap.put("extendParam", JSON.toJSONString(msg));// 扩展参数

        LOG.info("进入飏支付还款请求原文：" + paramMap);
        String sign = MerchantApiUtil.getSign(paramMap, paySecret);
        paramMap.put("sign", sign);// 签名

        String url = URL + "/bigpay-web-gateway/accountProxyPay/initPay";
        String payResult = SimpleHttpUtils.httpPost(url, paramMap);

        LOG.info("进入飏支付还款返回信息：" + payResult);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(payResult);
        Object resultCode = jsonObject.get("resultCode");//返回码
        Object errMsg = jsonObject.get("resMsg");//错误信息(请求失败时)
        Object remitStatus = jsonObject.get("remitStatus");
        if (!"0000".equals(resultCode)) {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付还款请求失败，原因" + errMsg);
            return map;
        }
        if ("REMIT_SUCCESS".equals(remitStatus)) {
            map.put(CommonConstants.RESP_CODE, "999998");
            map.put(CommonConstants.RESP_MESSAGE, "结算成功，等待银行出款");
            return map;
        } else if ("REMITTING".equals(remitStatus)) {

            map.put(CommonConstants.RESP_CODE, "999998");
            map.put(CommonConstants.RESP_MESSAGE, "结算处理中，等待银行出款");
            return map;
        } else {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, errMsg);
            return map;
        }

    }

    // 飏支付还款结果查询

    public Map reppayQuery( String orderCode) {
        Map map = new HashMap();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("payKey", payKey); // 商户支付Key
        paramMap.put("outTradeNo", orderCode);//原交易订单号
        paramMap.put("platMerchant", platMerchant);//平台商户  平台提供
        /////签名及生成请求API的方法///
        String sign = MerchantApiUtil.getSign(paramMap, paySecret);
        paramMap.put("sign", sign);
        String url = URL + "/bigpay-web-gateway/proxyPayQuery/query";
        String payResult = SimpleHttpUtils.httpPost(url, paramMap);
        LOG.info("进入飏支付还款返回信息：" + payResult);
        JSONObject jsonObject = JSON.parseObject(payResult);
        Object resultCode = jsonObject.get("resultCode");//返回码
        Object errMsg = jsonObject.get("errMsg");//错误信息(请求失败时)
        Object remitStatus = jsonObject.get("remitStatus");
        if (!"0000".equals(resultCode)) {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付还款请求失败");
            return map;
        }
        if ("REMIT_SUCCESS".equals(remitStatus)) {
            LOG.info("订单执行成功==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.SUCCESS);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付还款支付成功");
            return map;
        } else if ("REMIT_FAIL".equals(remitStatus)) {
            LOG.info("订单执行失败==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付还款支付失败");
            return map;
        } else if ("REMITTING".equals(remitStatus)) {
            LOG.info("订单处理中==================");
            map.put(CommonConstants.RESP_CODE, CommonConstants.WAIT_CHECK);
            map.put(CommonConstants.RESP_MESSAGE, "飏支付还款处理中，等待银行打款");
            return map;
        } else {
            map.put(CommonConstants.RESP_CODE, CommonConstants.FALIED);
            map.put(CommonConstants.RESP_MESSAGE, errMsg);
            return map;
        }
    }



}
