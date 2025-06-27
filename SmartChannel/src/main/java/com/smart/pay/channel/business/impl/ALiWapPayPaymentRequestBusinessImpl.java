package com.smart.pay.channel.business.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.util.AlipayAPIClientFactory;
import com.smart.pay.channel.util.AlipayServiceEnvConstants;
import com.smart.pay.channel.util.ChannelConstss;
import com.smart.pay.channel.util.qrcode.QrCodeUtil;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.DateUtil;
import com.smart.pay.common.tools.ResultWrap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 支付宝wap支付请求类
 * @author Robin-QQ/WX:354476429
 * @date 2018年4月20日
 */
@Service
public class ALiWapPayPaymentRequestBusinessImpl extends BasePaymentRequest implements PaymentRequestBusiness {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	
	
	public static final String NOTIFY_URL = "/smartchannel/alipaywap/callback";
	
	/**
	 * 下游请求阿里wap支付接口地址
	 */
	public static final String ALI_WAP_PAY_URL = "/smartchannel/alipay/topup";
	
	/**
	 * 支付宝wap支付通道
	 * @author Robin-QQ/WX:354476429 
	 * @date 2018年4月20日  
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> paymentRequest(Map<String, Object> params) {
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		
		JSONObject channelDetilJSON = this.getChannelDetil(paymentOrder.getRealChannelCode());
		if (!Constss.SUCCESS.equals(channelDetilJSON.getString(Constss.RESP_CODE))) {
			return ResultWrap.init(Constss.FALIED, "无该渠道配置");
		}
		JSONArray resultJSONArray = channelDetilJSON.getJSONArray(Constss.RESULT);
		channelDetilJSON = (JSONObject) resultJSONArray.get(0);
		String realPayType = channelDetilJSON.getString("realPayType");
		String url = null;
		if (ChannelConstss.ALI_QRCODE_PAY_TYPE.equals(realPayType)) {
		
			url  = QrCodeUtil.qrOSSCodeEncode(paymentOrder.getOrderCode() + ".png", this.callBackIpAddress + this.ALI_WAP_PAY_URL + "?order_code="+paymentOrder.getOrderCode());
			
		}else if(ChannelConstss.ALI_WAP_TYPE.equals(realPayType)) {
			url = this.callBackIpAddress + this.ALI_WAP_PAY_URL + "?order_code="+paymentOrder.getOrderCode();
		}
		
		return ResultWrap.init(Constss.SUCCESS, "请求成功",url);
	}
	
	
	
	public Map<String, Object> aliRequest(Map<String, Object> params) {
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		//实例化客户端
		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(paymentOrder.getRealChannelCode());
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeWapPayModel wapModel = new AlipayTradeWapPayModel();
		wapModel.setBody("");
		try {
			wapModel.setSubject(URLEncoder.encode(paymentOrder.getProductName(), AlipayServiceEnvConstants.CHARSET));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			wapModel.setSubject("");
		}
		wapModel.setOutTradeNo(paymentOrder.getOrderCode());
		wapModel.setTotalAmount(paymentOrder.getAmount()+"");
		wapModel.setProductCode("QUICK_WAP_WAY");
		wapModel.setEnablePayChannels("balance,moneyFund,debitCardExpress,bankPay");
		alipayRequest.setBizModel(wapModel);
		String returnURL = paymentOrder.getReturnURL();
//		如果下游没有设置返回页面,则跳转全局支付成功页面
		alipayRequest.setReturnUrl(((returnURL==null||"".equals(returnURL))?this.callBackIpAddress+this.GLOBAL_RETURN_URL:returnURL));
		alipayRequest.setNotifyUrl(this.callBackIpAddress+NOTIFY_URL);//在公共参数中设置回跳和通知地址
		String from = "";
		try {
		    //这里和普通的接口调用不同，使用的是pageExecute
			from =  alipayClient.pageExecute(alipayRequest).getBody();
		    System.out.println(from);//就是orderString 可以直接给客户端请求，无需再做处理。
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResultWrap.err(LOG, Constss.FALIED, "支付失败");
		}
//		把跳转的代码放入Result中,返回给下游
		return ResultWrap.init(Constss.SUCCESS,"请求成功", from);
	}
	
}
