package com.smart.pay.channel.business.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.util.AlipayAPIClientFactory;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.ResultWrap;

/**
 * 支付宝转账请求类
 * @author Robin-QQ/WX:354476429
 * @date 2018年4月20日
 */
@Service
public class ALiToAccountPayPaymentRequestBusinessImpl implements PaymentRequestBusiness {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	
	@Override
	public Map<String, Object> paymentRequest(Map<String, Object> params) {
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		//实例化客户端
		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(paymentOrder.getRealChannelCode());
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		AlipayFundTransToaccountTransferModel accountModel = new AlipayFundTransToaccountTransferModel();
		
		accountModel.setOutBizNo("201804201705630271");
		accountModel.setPayeeType("ALIPAY_LOGONID");
		accountModel.setPayeeAccount("18879791271");
		accountModel.setAmount("0.01");
//		try {
//			accountModel.setRemark(URLEncoder.encode(paymentOrder.getProductName(), AlipayServiceEnvConstants.CHARSET));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			accountModel.setRemark("");
//		}
		AlipayFundTransToaccountTransferResponse response;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return ResultWrap.err(LOG, Constss.FALIED, "支付失败");
		}
		if(!response.isSuccess()){
//			System.out.println("调用成功");
			return ResultWrap.err(LOG, Constss.FALIED, "支付失败");
		}

		return ResultWrap.init(Constss.SUCCESS,"请求成功");
	}

}
