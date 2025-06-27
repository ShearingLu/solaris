package com.smart.pay.channel.util.yzf;

import com.smart.pay.channel.util.hqx.AbstractChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 小额聚合
 * <p>
 * 实际调用的接口在抽象类里面。
 */

public class YZFSmartRepayChannel extends YZFAbstractChannel {

    public static final String postUrl = "http://47.99.58.100:28520/bigpay-web-gateway/quickPay/initPay"; // 请求地址

    public static final String productType = "40000103";// 产品类型

    public static final String certType ="IDENTITY";//证件类型 固定值：身份证

    private static final Logger LOG = LoggerFactory.getLogger(YZFSmartRepayChannel.class);

    public YZFSmartRepayChannel() {

        super(productType, postUrl,certType);
        // TODO Auto-generated constructor stub
    }
}
