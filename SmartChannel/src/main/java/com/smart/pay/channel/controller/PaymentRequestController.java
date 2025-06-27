package com.smart.pay.channel.controller;

import com.smart.pay.channel.business.MobileManageBusiness;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.business.impl.ALiWapPayPaymentRequestBusinessImpl;
import com.smart.pay.channel.business.impl.BasePaymentRequest;
import com.smart.pay.channel.business.impl.PaymentRequestFactory;
import com.smart.pay.channel.business.impl.WXWapPayPaymentRequestBusinessImpl;
import com.smart.pay.channel.config.RedisUtil;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.pojo.PersonalURL;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.ResultWrap;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class PaymentRequestController extends BasePaymentRequest{

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaymentRequestFactory paymentRequestFactory;

	@Autowired
	private MobileManageBusiness mobileManageBusiness;

	@Autowired
	private RedisUtil redisUtil;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/smartchannel/personal/sysbusy")
	public String sysbusy(HttpServletRequest request,HttpServletResponse response) {
		return "nosys";
	}
	
	
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/smartchannel/personal/request")
	public String personalRequest(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="trx_url") String trxURL,
			@RequestParam(value="sign") String sign
			) {
		
		PersonalURL personalURL = mobileManageBusiness.queryPersonalURL(sign);
		if(personalURL.getIsUse().equalsIgnoreCase("1")) {
			return "timeexpired";
		}
		
		
		if(personalURL == null) {
			return "timeexpired";
		}else {
			
			long curTime = System.currentTimeMillis();
			long priTime = Long.parseLong(personalURL.getStartTime());
			long differTime = curTime-priTime;
			if(differTime > 60000) {   //超过1分钟那么支付就失效
				return "timeexpired";
			}else {
				
				/***将该设备状态变成已经使用*/
				personalURL.setIsUse("1");
				mobileManageBusiness.savePersonalURL(personalURL);
				return "redirect:"+trxURL;
				
			}
			
		}
		
		
		
		
		
	}
	//支付入口
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/topup/request")
	public @ResponseBody Object topupRequest(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="order_code")String orderCode,
			@RequestParam(value="data")	String data
			) throws Exception {
		
		JSONObject resultJSONObject;
		try {
			resultJSONObject = this.getOrderByOrderCode(orderCode);
			LOG.info("订单参数============="+resultJSONObject);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败");
		}

		LOG.info("=====data=====:" + data);

		JSONObject parameterJSON = null;
		try {
			parameterJSON = JSONObject.fromObject(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultWrap.err(LOG, "999999", "JSON校验失败!");
		}

		String extra = "";
		try {
			extra = parameterJSON.getString("extra");
			parameterJSON.discard("extra");


		} catch (Exception e1) {

		}

		PaymentOrder bean = (PaymentOrder) JSONObject.toBean(parameterJSON, PaymentOrder.class);
		LOG.info("value=========="+bean);
		bean.setExtra(extra);
//		redisUtil.savePaymentRequestParameter(orderCode, bean);

//		//两个json合并
//		resultJSONObject=combineJson(resultJSONObject, parameterJSON);

		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
			return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败", resultJSONObject);
		}
		resultJSONObject = resultJSONObject.getJSONObject(Constss.RESULT);
//		将获取的JSON订单转换为model
		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject, PaymentOrder.class);
		LOG.info("请求订单================"+paymentOrder);
		PaymentRequestBusiness paymentRequestBusiness = paymentRequestFactory.getTopChannelRequestImpl(paymentOrder.getRealChannelCode());
		if(paymentRequestBusiness == null) {
			return ResultWrap.err(LOG, Constss.FALIED, "获取通道失败");
		}

		//用户身份证号码
		paymentOrder.setIdCard(bean.getIdCard());
		paymentOrder.setBankCard(bean.getBankCard());
		paymentOrder.setUserName(bean.getUserName());
		paymentOrder.setPhone(bean.getPhone());
		//请求ip
		paymentOrder.setIpAddress(bean.getIpAddress());
		paymentOrder.setExtra(bean.getExtra());
		paymentOrder.setCity(bean.getCity());
		paymentOrder.setSubMerchantNo(bean.getSubMerchantNo());
		paymentOrder.setBindId(bean.getBindId());
		paymentOrder.setCity(bean.getCity());
        paymentOrder.setSecurityCode(bean.getSecurityCode());
        paymentOrder.setExpiredTime(bean.getExpiredTime());
        paymentOrder.setCity(bean.getCity());
        paymentOrder.setBankName(bean.getBankName());
        LOG.info("订单参数============="+paymentOrder);
		//将订单请求参数存入redis
		redisUtil.savePaymentRequestParameter(orderCode, paymentOrder);
		Map<String,Object> params = new HashMap<>();
		params.put("paymentOrder", paymentOrder);
		params.put("request", request);
		Map<String, Object> paymentRequestResult = paymentRequestBusiness.paymentRequest(params);
		return paymentRequestResult;
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/smartchannel/alipay/topup")
	public @ResponseBody Object aLiPayTopUp(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="order_code")String orderCode
			) {
		JSONObject resultJSONObject = this.getOrderByOrderCode(orderCode);
		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
			return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败", resultJSONObject);
		}
		resultJSONObject = resultJSONObject.getJSONObject(Constss.RESULT);
//		将获取的JSON订单转换为model
		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject, PaymentOrder.class);
		ALiWapPayPaymentRequestBusinessImpl paymentRequestBusiness = (ALiWapPayPaymentRequestBusinessImpl) paymentRequestFactory.getTopChannelRequestImpl(paymentOrder.getRealChannelCode());
		if(paymentRequestBusiness == null) {
			return ResultWrap.err(LOG, Constss.FALIED, "获取通道失败");
		}
		Map<String,Object> params = new HashMap<>();
		params.put("paymentOrder", paymentOrder);
		Map<String, Object> paymentRequestResult = paymentRequestBusiness.aliRequest(params);
		
		if(paymentRequestResult != null && paymentRequestResult.containsKey(Constss.RESP_CODE)) {
			if(Constss.SUCCESS.equals(paymentRequestResult.get(Constss.RESP_CODE))) {
				try {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().println(paymentRequestResult.get(Constss.RESULT));
					response.getWriter().flush();
					response.getWriter().close(); 
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}
		return paymentRequestResult;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/smartchannel/wexinpay/topup")
	public @ResponseBody Object wexinPayTopUp(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="order_code")String orderCode
			) throws  IOException{
		JSONObject resultJSONObject = this.getOrderByOrderCode(orderCode);
		if(resultJSONObject == null || !resultJSONObject.containsKey(Constss.RESP_CODE) || Constss.FALIED.equals(resultJSONObject.getString(Constss.RESP_CODE))) {
			return ResultWrap.err(LOG, Constss.ORDER_FALIED, "获取订单失败", resultJSONObject);
		}
		resultJSONObject = resultJSONObject.getJSONObject(Constss.RESULT);
		//		将获取的JSON订单转换为model
		PaymentOrder paymentOrder = (PaymentOrder) JSONObject.toBean(resultJSONObject, PaymentOrder.class);
		WXWapPayPaymentRequestBusinessImpl paymentRequestBusiness = (WXWapPayPaymentRequestBusinessImpl) paymentRequestFactory.getTopChannelRequestImpl(paymentOrder.getRealChannelCode());
		if(paymentRequestBusiness == null) {
			return ResultWrap.err(LOG, Constss.FALIED, "获取通道失败");
		}
		Map<String,Object> params = new HashMap<>();
		params.put("paymentOrder", paymentOrder);
		params.put("request", request);
		Map<String, Object> paymentRequestResult = paymentRequestBusiness.wexinRequest(params);
		if(paymentRequestResult != null && paymentRequestResult.containsKey(Constss.RESP_CODE)) {
			if(Constss.SUCCESS.equals(paymentRequestResult.get(Constss.RESP_CODE))) {
				try {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().println(paymentRequestResult.get(Constss.RESULT));
					response.getWriter().flush();
					response.getWriter().close(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}
		return paymentRequestResult;
		
	}

	@SuppressWarnings("unchecked")
	private JSONObject combineJson(JSONObject srcObj, JSONObject addObj) throws JSONException {

		Iterator<String> itKeys1 = addObj.keys();
		String key, value;
		while(itKeys1.hasNext()){
			key = itKeys1.next();
			value = addObj.optString(key);

			srcObj.put(key, value);
		}
		return srcObj;
	}

}
