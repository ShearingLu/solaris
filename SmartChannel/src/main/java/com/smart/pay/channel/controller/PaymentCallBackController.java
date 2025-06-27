package com.smart.pay.channel.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.smart.pay.channel.business.ALiMerchantBusiness;
import com.smart.pay.channel.business.WXMerchantBusiness;
import com.smart.pay.channel.business.impl.BasePaymentRequest;
import com.smart.pay.channel.pojo.ALiMerchant;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.pojo.WXMerchant;
import com.smart.pay.channel.util.AlipayServiceEnvConstants;
import com.smart.pay.channel.wxpay.tools.WXPayConstants;
import com.smart.pay.channel.wxpay.tools.WXPayUtil;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.Md5Util;
import com.smart.pay.common.tools.ResultWrap;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@Controller
@EnableAutoConfiguration
public class PaymentCallBackController extends BasePaymentRequest {

		private final Logger LOG = LoggerFactory.getLogger(getClass());
		
		@Autowired
		private ALiMerchantBusiness aLiMerchantBusiness;
		
		@Autowired
		private WXMerchantBusiness wXMerchantBusiness;
	
	 	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/channel/test1/callback")
	    public @ResponseBody Object registerUser(HttpServletRequest request) {
			LOG.info("环球消费支付异步回调进来了======");

			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<String> keySet = parameterMap.keySet();
			for (String key : keySet) {
				String[] strings = parameterMap.get(key);
				for (String s : strings) {
					LOG.info(key + "=============" + s);
				}
			}
			String transtype = request.getParameter("transtype");
			String merchno = request.getParameter("merchno");
			String signType = request.getParameter("signType");
			String status = request.getParameter("status");
			String message = request.getParameter("message");
			String orderid = request.getParameter("orderid");
			String dsorderid = request.getParameter("dsorderid");
			String paytime = request.getParameter("paytime");
			LOG.info("回调参数====" + transtype + merchno + signType + status + message + orderid + dsorderid + paytime);
			LOG.info("第三方流水号======orderid" + orderid);
			LOG.info("订单===========dsorderid" + dsorderid);
	 		return null;
	 	}
	 	
	 	@RequestMapping(method = RequestMethod.GET, value = "/smartchannel/pay/success")
	    public String toPaySuccessHtml(HttpServletRequest request) {
	 		return "paysuccess";
	 	}
	 	
	 	/**
	 	 * 支付宝wap支付回调接口 
	 	 * @author Robin-QQ/WX:354476429 
	 	 * @date 2018年4月20日  
	 	 * @param request
	 	 * @return
	 	 */
	 	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/alipaywap/personal/callback")
	    public @ResponseBody Object aliPayPersonalWapNotify(HttpServletRequest request) {
	 		
	 		LOG.info("支付宝个人扫码回调进来了");
	 		
	 		String dt = request.getParameter("dt");
	 		LOG.info("dt.........."+dt);
	 		String mark = request.getParameter("mark");
	 		LOG.info("mark.........."+mark);
	 		String money = request.getParameter("money");
	 		LOG.info("money.........."+money);
	 		String no    = request.getParameter("no");
	 		LOG.info("no.........."+no);
	 		String type  = request.getParameter("type");
	 		LOG.info("type.........."+type);
	 		String signKey = "smartpay987654321";
	 		String compSign = Md5Util.getMD5(dt+mark+money+no+type+signKey);
	 		LOG.info("compSign.........."+compSign);
	 		String sign = request.getParameter("sign");
	 		LOG.info("sign.........."+sign);
	 		
	 		JSONObject resultJSONObject = this.getOrderByOrderCode(mark);
			
	 		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
				return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败");
			}
	 		BigDecimal returnMoney = BigDecimal.ZERO;
	 		
	 		String[] nums = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	 		String firstNum = money.substring(0, 1);
	 		boolean allNums = false;
	 		for(int i=0; i<nums.length; i++) {
	 			
	 			if(nums[i].equalsIgnoreCase(firstNum)) {
	 				allNums = true;
	 				break;
	 			}
	 			
	 		}
	 		
	 		
	 		if(allNums) {
	 			returnMoney = new BigDecimal(money);
	 		}else {
	 			
	 			returnMoney = new BigDecimal(money.substring(1));
	 		}
	 		
	 		
	 		
	 		
	 		
	 		
	 		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject.getJSONObject(Constss.RESULT), PaymentOrder.class);
			
	 		BigDecimal sysAmount = paymentOrder.getAmount();
	 		
	 		
	 		if(sysAmount.compareTo(returnMoney) != 0) {
	 			return ResultWrap.err(LOG, Constss.ERRRO_ORDER_HAS_CHECKED, "金额不匹配，交易无法处理");
	 		}
	 		
	 		
	 		if("1".equals(paymentOrder.getOrderStatus())) {
				return ResultWrap.err(LOG, Constss.ERRRO_ORDER_HAS_CHECKED, "订单已处理");
			}
			this.updateOrderByOrderCode(mark, "1", no);
	 		return "success";
	 	}
	 	
	 	
	 	
	 	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/alipaywap/callback")
	    public @ResponseBody Object aliPayWapNotify(HttpServletRequest request) {
	 		//获取支付宝POST过来反馈信息
	 		Map<String,String> params = new HashMap<String,String>();
	 		Map requestParams = request.getParameterMap();
	 		
	 		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	 		    String name = (String) iter.next();
	 		    String[] values = (String[]) requestParams.get(name);
	 		    String valueStr = "";
	 		    for (int i = 0; i < values.length; i++) {
	 		        valueStr = (i == values.length - 1) ? valueStr + values[i]
	 		                    : valueStr + values[i] + ",";
	 		  	}
	 		    //乱码解决，这段代码在出现乱码时使用。
//	 			try {
//					valueStr = new String(valueStr.getBytes("ISO-8859-1"), AlipayServiceEnvConstants.CHARSET);
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
	 			params.put(name, valueStr);
	 		}
	 		LOG.info("支付宝WAP回调进来了============params:"+params);
	 		
	 		String realChannelOrderCode = params.get("trade_no");
	 		String orderCode = params.get("out_trade_no");
	 		String amount = params.get("total_amount");
	 		String tradeStatus = params.get("trade_status");
	 		JSONObject resultJSONObject = this.getOrderByOrderCode(orderCode);
			
	 		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
				return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败");
			}
	 		
	 		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject.getJSONObject(Constss.RESULT), PaymentOrder.class);
	 		String realType = paymentOrder.getRealtype();
	 		
	 		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
	 		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
	 		ALiMerchant aLiMerchant = aLiMerchantBusiness.findALiMerchantByAppidAndrealType(params.get("app_id"), realType);
			boolean flag = false;
	 		if(aLiMerchant != null) {
 		 		try {
 					flag = AlipaySignature.rsaCheckV1(params,aLiMerchant.getaLiPublicKey(), AlipayServiceEnvConstants.CHARSET,"RSA2");
 				} catch (AlipayApiException e) {
 					e.printStackTrace();
 		 			return ResultWrap.err(LOG, Constss.ERROR_SIGN_NOVALID, "验签异常");
 				}
	 		}else{
	 			return ResultWrap.err(LOG, Constss.ERROR_SIGN_NOVALID, "验签失败,无支付宝密钥配置");
	 		}
	 		
	 		if(!flag) {
	 			return ResultWrap.err(LOG, Constss.ERROR_SIGN_NOVALID, "验签失败");
	 		}
	 		
	 		
	 		
			
			if(!"TRADE_SUCCESS".equalsIgnoreCase(tradeStatus) && !"TRADE_FINISHED".equalsIgnoreCase(tradeStatus)) {
				return ResultWrap.err(LOG, Constss.ORDER_FALIED, "非成功回调");
			}
			
			
			if(paymentOrder.getAmount().compareTo(new BigDecimal(amount)) != 0) {
				return ResultWrap.err(LOG, Constss.ERROR_AMOUNT_ERROR, "验证金额失败");
			}
			if("1".equals(paymentOrder.getOrderStatus())) {
				return ResultWrap.err(LOG, Constss.ERRRO_ORDER_HAS_CHECKED, "订单已处理");
			}
			this.updateOrderByOrderCode(orderCode, "1", realChannelOrderCode);
	 		return "SUCCESS";
	 	}

	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/wxpaywap/callback")
	public @ResponseBody Object wexinPayWapNotify(HttpServletRequest request) throws Exception {
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		/** 获取微信调用notify_url的返回XML信息 */
		String result = new String(outSteam.toByteArray(), "utf-8");
		Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
 		LOG.info("微信WAP回调进来了============resultMap:"+resultMap);
		if (!"SUCCESS".equals(resultMap.get("return_code"))) {
 			return ResultWrap.err(LOG, "非成功回调",resultMap.toString());
		}
		
		String orderCode = resultMap.get("out_trade_no");
		String realChannelOrderCode = resultMap.get("transaction_id");
		String resultCode = resultMap.get("result_code");
 		JSONObject resultJSONObject = this.getOrderByOrderCode(orderCode);
		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
			return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败");
		}
		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject.getJSONObject(Constss.RESULT), PaymentOrder.class);
		WXMerchant wxMerchant = wXMerchantBusiness.findByAppidAndRealType(resultMap.get("appid"),paymentOrder.getRealtype());
		
		if (wxMerchant == null) {
 			return ResultWrap.err(LOG, "获取微信资质失败",resultMap.toString());
		}
		
		boolean flag = false;
		flag = WXPayUtil.isSignatureValid(resultMap, wxMerchant.getPrivateKey(),WXPayConstants.SignType.HMACSHA256);
		if (!flag) {
 			return ResultWrap.err(LOG, Constss.ERROR_SIGN_NOVALID,resultMap.toString());
		}
		

		if (!"SUCCESS".equals(resultCode)) {
 			return ResultWrap.err(LOG, "非成功回调",resultMap.toString());
		}
		

		if("1".equals(paymentOrder.getOrderStatus())) {
			return ResultWrap.err(LOG, Constss.ERRRO_ORDER_HAS_CHECKED, "订单已处理");
		}
		this.updateOrderByOrderCode(orderCode, "1", realChannelOrderCode);
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("return_code", "SUCCESS");
		return WXPayUtil.mapToXml(returnMap);
	}
	
	
	/*public static void main(String[] args) {
		
		String money = "g10.11";
		BigDecimal returnMoney = BigDecimal.ZERO;
 		
 		String[] nums = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
 		String firstNum = money.substring(0, 1);
 		boolean allNums = false;
 		for(int i=0; i<nums.length; i++) {
 			
 			if(nums[i].equalsIgnoreCase(firstNum)) {
 				allNums = true;
 				break;
 			}
 			
 		}
 		
 		
 		if(allNums) {
 			returnMoney = new BigDecimal(money);
 		}else {
 			
 			returnMoney = new BigDecimal(money.substring(1));
 		}
 		
 		System.out.println(returnMoney);
		
	}
	*/
	
}
