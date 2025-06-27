package com.smart.pay.channel.business.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.util.ChannelConstss;
import com.smart.pay.channel.util.qrcode.QrCodeUtil;
import com.smart.pay.channel.wxpay.tools.WXPay;
import com.smart.pay.channel.wxpay.tools.WXPayAPIClientFactory;
import com.smart.pay.channel.wxpay.tools.WXPayConfigImpl;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.IPUtils;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.common.tools.UUIDGenerator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class WXWapPayPaymentRequestBusinessImpl extends BasePaymentRequest implements PaymentRequestBusiness {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	public static final String NOTIFY_URL = "/smartchannel/wxpaywap/callback";
	
	
	public static final String WEXIN_WAP_PAY_URL = "/smartchannel/wexinpay/topup";
	
	@Autowired
	private WXPayAPIClientFactory wXPayAPIClientFactory;
	
	@Override
	public Map<String, Object> paymentRequest(Map<String, Object> params) {
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		HttpServletRequest request =  (HttpServletRequest) params.get("request");
		JSONObject channelDetilJSON = this.getChannelDetil(paymentOrder.getRealChannelCode());
		if (!Constss.SUCCESS.equals(channelDetilJSON.getString(Constss.RESP_CODE))) {
			return ResultWrap.init(Constss.FALIED, "无该渠道配置");
		}
		JSONArray resultJSONArray = channelDetilJSON.getJSONArray(Constss.RESULT);
		channelDetilJSON = (JSONObject) resultJSONArray.get(0);
		String realPayType = channelDetilJSON.getString("realPayType");
		BigDecimal amount = paymentOrder.getAmount();
		String url = null;

		if (ChannelConstss.WX_WAP_PAY_TYPE.equals(realPayType)) {
			
			url = this.callBackIpAddress + this.WEXIN_WAP_PAY_URL + "?order_code="+paymentOrder.getOrderCode();
			
			
			//	return this.wexinRequest(params);
			
		}else if (ChannelConstss.WX_QRCODE_PAY_TYPE.equals(realPayType)) {
			WXPayConfigImpl config = wXPayAPIClientFactory.getWXPayConfigImpl(paymentOrder.getRealChannelCode());
	        WXPay wxpay = null;
			try {
				wxpay = new WXPay(config);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new RuntimeException("初始化微信配置异常!");
			}
	        Map<String, String> data = new HashMap<String, String>();
	        data.put("body", paymentOrder.getProductName());
	        data.put("out_trade_no", paymentOrder.getOrderCode());
	        data.put("fee_type", "CNY");
	        data.put("total_fee", amount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
	        data.put("spbill_create_ip", request.getLocalAddr());
	        data.put("notify_url", this.callBackIpAddress+NOTIFY_URL);
	        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
	        data.put("product_id", "01");
	        data.put("limit_pay", "no_credit");
	        data.put("nonce_str", UUIDGenerator.getUUID());
	        Map<String, String> resp = null;
	        try {
	            resp = wxpay.unifiedOrder(data);
	            System.out.println(resp);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("请求微信支付异常!");
	        }
	        String returnCode = resp.get("return_code");
	        if ("SUCCESS".equals(returnCode)) {
		        String resultCode = resp.get("result_code");
		        if ("SUCCESS".equals(resultCode)) {
		        	String codeUrl = resp.get("code_url");
		        	url = QrCodeUtil.qrOSSCodeEncode(paymentOrder.getOrderCode() + ".png", codeUrl);
				}
			}
		}
		
		if (url == null || "".equals(url)) {
			return ResultWrap.init(Constss.FALIED, "请求失败");
		}
		
		return ResultWrap.init(Constss.SUCCESS, "请求成功", url);
	}
	
	public Map<String, Object> wexinRequest(Map<String, Object> params){
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		HttpServletRequest request =  (HttpServletRequest) params.get("request");
		WXPayConfigImpl config = wXPayAPIClientFactory.getWXPayConfigImpl(paymentOrder.getRealChannelCode());
		BigDecimal amount = paymentOrder.getAmount();
		String returnURL = paymentOrder.getReturnURL();
		returnURL = this.callBackIpAddress+this.GLOBAL_RETURN_URL;

		
        WXPay wxpay = null;
		String url = null;

		try {
			wxpay = new WXPay(config);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException("初始化微信配置异常!");
		}
		String remoteIP = this.getRemoteIP(request);
		System.out.println("remoteIP====" + remoteIP);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", paymentOrder.getProductName());
        data.put("out_trade_no", paymentOrder.getOrderCode());
        data.put("fee_type", "CNY");
        data.put("total_fee", amount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        data.put("spbill_create_ip",remoteIP);
        data.put("notify_url", this.callBackIpAddress+NOTIFY_URL);
        data.put("trade_type", "MWEB");  // 此处指定为H5支付
        data.put("product_id", "01");
        data.put("limit_pay", "no_credit");
        data.put("nonce_str", UUIDGenerator.getUUID());
        
        System.out.println(data);
        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求微信支付异常!");
        }
        String returnCode = resp.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
	        String resultCode = resp.get("result_code");
	        if ("SUCCESS".equals(resultCode)) {
	        	url = resp.get("mweb_url");
	    		String prepay_id = url.substring(url.indexOf("prepay_id="),url.indexOf("&")).replace("prepay_id=", "");
	    		String packages = url.substring(url.indexOf("package="),url.length()).replace("package=", "");
	        	url = "<form name=\"punchout_form\" method=\"GET\" action=\""+url+"\"><input type=\"hidden\" name=\"prepay_id\" value=\""+prepay_id+"\"><input type=\"hidden\" name=\"package\" value=\""+packages+"\"></form><script>document.forms[0].submit();</script>";
	        	System.out.println("url..............."+url);
	        }
		}
        
    	if (url == null || "".equals(url)) {
    		url = "<form name=\"punchout_form\" method=\"GET\" action=\""+returnURL+"\"></form><script>document.forms[0].submit();</script>";
    	}
    	
		return ResultWrap.init(Constss.SUCCESS, "请求成功", url);
	}
	
    public String getRemoteIP(HttpServletRequest request) {  
		String ipAddress = request.getHeader("x-forwarded-for");  
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("Proxy-Client-ipAddress");  
        }  
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("WL-Proxy-Client-ipAddress");  
        }  
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getRemoteAddr();  
        }
        String[] strs = ipAddress.split(",");
        if (strs.length > 0) {
        	ipAddress = strs[0];
		}
        return ipAddress.trim();  
    }  

}
