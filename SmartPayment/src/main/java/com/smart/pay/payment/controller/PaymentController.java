package com.smart.pay.payment.controller;

import com.smart.pay.common.tools.*;
import com.smart.pay.payment.business.*;
import com.smart.pay.payment.pojo.*;
import com.smart.pay.payment.util.ErrorCodeMsg;
import com.smart.pay.payment.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class PaymentController {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	private Util util;

	@Autowired
	private PaymentRequestBusiness paymentRequestBusiness;
	
	@Autowired
	private ChannelBusiness channelBusiness;
	
	@Autowired
	private WithdrawBusiness withdrawBusiness;

	@Autowired
	private ChannelRegisterRouteBusiness channelRegisterRouteBusiness;

	@Autowired
	private ChannelBindCardBusiness channelBindCardBusiness;

	/**支付查询接口*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/order/query")
	public @ResponseBody Object orderquery(HttpServletRequest request,
			@RequestParam(value = "version") String version,
			@RequestParam(value = "charset") String charset,
			@RequestParam(value = "sign_type") String signType,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "out_trade_no", required=false) String outTradeNo,
			@RequestParam(value = "transaction_id", required=false) String transactionId,
			@RequestParam(value = "nonce_str") String nonceStr,
			@RequestParam(value = "sign") String sign
			) {
		/**查询商家的key**/
		URI uri = util.getServiceUrl("smartmerchant", "error url request!");
        String url = uri.toString() + "/smartmerchant/merchant/info";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantId);
        requestEntity.add("key_type", signType == null || signType.equalsIgnoreCase("") ? "MD5" :  signType);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);

		JSONObject resultMerchantObj = resultObj.getJSONObject("result");
		if(resultMerchantObj.isEmpty()) {

				return getResponseResultNoBiz(ErrorCodeMsg.MERCHANTNOEXIST);
		}
		
		String key = resultMerchantObj.getString("key");
		
		/**验证签名**/
		Map<String, String> signParams = new HashMap<String, String>();
		signParams.put("version", version);
		signParams.put("charset",charset);
		signParams.put("sign_type", signType);
		signParams.put("merchant_id",merchantId);
		if(outTradeNo != null && !outTradeNo.equalsIgnoreCase("")) {
			signParams.put("out_trade_no", outTradeNo);
		}
		if(transactionId != null && !transactionId.equalsIgnoreCase("")) {
			signParams.put("transaction_id", transactionId);
		}
		signParams.put("nonce_str",nonceStr);
		signParams.put("sign", sign);
		boolean isOk = SignUtils.checkParam(signParams, key);
		if(!isOk) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.SIGNERROR);
		}
		
		
		/**验证参数*/
		/**版本号是否正确*/
		if(!version.equalsIgnoreCase("1.0")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.VERSIONERROR);
		}
		
		/**chartset是否正确*/
		if(!charset.equalsIgnoreCase("UTF-8")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.CHARSETERROR);
		}
	
		/**sign_type是否正确**/
		if(!signType.equalsIgnoreCase("MD5")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.SIGNTYPEERROR);
		}
		
		
		if((outTradeNo == null || outTradeNo.equalsIgnoreCase(""))
				&& (transactionId == null || transactionId.equalsIgnoreCase(""))) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.NOTENOUGHERROR);
		}		
		
		
		if(transactionId != null && !transactionId.equalsIgnoreCase("")) {
			
			
			uri = util.getServiceUrl("smartclearing", "error url request!");
	        url = uri.toString() + "/smartclearing/transaction/order/query";
	        requestEntity = new LinkedMultiValueMap<String, String>();
	        requestEntity.add("order_code", transactionId);
	        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			resultObj = JSONObject.fromObject(result);
			if(resultObj.getString(Constss.RESP_CODE).equalsIgnoreCase(Constss.SUCCESS)) {
					
				resultMerchantObj = resultObj.getJSONObject("result");
					
					
				return 	returnQueryOrderInfo(resultMerchantObj, key);
					
			}else {
				return getResponseResult(merchantId, key, ErrorCodeMsg.ORDERNOTEXISTERROR);
			}
			
		}else{
			
			
			uri = util.getServiceUrl("smartclearing", "error url request!");
	        url = uri.toString() + "/smartclearing/transaction/merorder/query";
	        requestEntity = new LinkedMultiValueMap<String, String>();
	        requestEntity.add("merchant_code", outTradeNo);
	        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			resultObj = JSONObject.fromObject(result);
			if(resultObj.getString(Constss.RESP_CODE).equalsIgnoreCase(Constss.SUCCESS)) {
					
				resultMerchantObj = resultObj.getJSONObject("result");
				return 	returnQueryOrderInfo(resultMerchantObj, key);
					
			}else {
				return getResponseResult(merchantId, key, ErrorCodeMsg.MERORDERNOTEXISTERROR);
			}
			
		}
		
		
		
	}
	
	
	public Map<String, String> returnQueryOrderInfo(JSONObject resultMerchantObj, String key) {
		
		Map<String, String> resultParams = new HashMap<String, String>();
		resultParams.put("version", "1.0");
		resultParams.put("charset", "UTF-8");
		resultParams.put("sign_type", "MD5");
		resultParams.put("status", "0");
		resultParams.put("result_code", "0");
		resultParams.put("merchant_id", resultMerchantObj.getString("merchantId"));
		resultParams.put("nonce_str", String.valueOf(new Date().getTime()));
		resultParams.put("trade_state", resultMerchantObj.getString("orderStatus"));
		if(resultMerchantObj.getString("orderStatus").equalsIgnoreCase("1")) {

			resultParams.put("trade_type", resultMerchantObj.getString("channelCode"));
			resultParams.put("transaction_id", resultMerchantObj.getString("orderCode"));
			resultParams.put("out_transaction_id", resultMerchantObj.getString("realChannelOrderCode"));
			resultParams.put("out_trade_no", resultMerchantObj.getString("merOrderCode"));
			resultParams.put("total_amount", resultMerchantObj.getString("amount"));
			resultParams.put("real_amount", resultMerchantObj.getString("amount"));
			resultParams.put("fee_type", "CNY");

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date tempDate = null;
			try {
				tempDate = sdf1.parse(resultMerchantObj.getString("updateTime"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			resultParams.put("time_end", sdf.format(tempDate));

			if(resultMerchantObj.getString("attach") != null && !resultMerchantObj.getString("attach").equalsIgnoreCase("") && !resultMerchantObj.getString("attach").equalsIgnoreCase("null")) {
				resultParams.put("attach", resultMerchantObj.getString("attach"));
			}
		}
		
		Map<String,String> params = SignUtils.paraFilter(resultParams);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        resultParams.put("sign", sign);
		
		
		return resultParams;
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/withdraw/condition/new")
	public @ResponseBody Object addwithdrawcondition(HttpServletRequest request,
			@RequestParam(value = "single_limit", defaultValue="0") String singleLimit,
			@RequestParam(value = "single_min_limit", defaultValue="0") String singleMinLimit,
			@RequestParam(value = "day_limit", defaultValue="0") String daylimit,
			@RequestParam(value = "outer_fee", defaultValue="0") String outerfee
			) {
		
		WithdrawCondition withdrawCondition = new WithdrawCondition();
		withdrawCondition.setDayLimit(new BigDecimal(daylimit));
		withdrawCondition.setSingleMinLimit(new BigDecimal(singleMinLimit));
		withdrawCondition.setSingleLimit(new BigDecimal(singleLimit));
		withdrawCondition.setOuterFee(new BigDecimal(outerfee));
		withdrawCondition = withdrawBusiness.createOrUpdateWithdrawCondition(withdrawCondition);
		return ResultWrap.init(Constss.SUCCESS, "增加或更新成功", withdrawCondition);
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/withdraw/condition/query")
	public @ResponseBody Object querywithdrawcondition(HttpServletRequest request
			) {
		return ResultWrap.init(Constss.SUCCESS, "查询成功", withdrawBusiness.queryWithdrawCondtion());
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/withdraw")
	public @ResponseBody Object withdraw(HttpServletRequest request,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "amount") String amount,
			@RequestParam(value = "card_no", required=false) String cardNo,
			@RequestParam(value = "card_name", required=false) String cardName
			) {
		/**验证商家是否有交易权限**/
		URI uri = util.getServiceUrl("smartrisk", "error url request!");
		String url = uri.toString() + "/smartrisk/merchantblacklist/merchantid";
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantId);
        requestEntity.add("risk_type", "2");// 0 标识没有交易权限 1表示没有登录权限 2表示没有提现权限 
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
        JSONObject resultObj = JSONObject.fromObject(result);
        JSONObject resultObject= resultObj.getJSONObject("result");
		if(!resultObject.isEmpty()) {
			return ResultWrap.init(Constss.FALIED, "没有提现权限不足", "");
		}
		if(!resultObject.isEmpty()) {
			return ResultWrap.init(Constss.FALIED, "没有提现权限不足", "");
		}
		WithdrawCondition withdrawCondition = withdrawBusiness.queryWithdrawCondtion();
		/**通道代码块*/
		synchronized(this){
			
			/***验证商户的余额是否充足*/
			uri = util.getServiceUrl("smartmerchant", "error url request!");
	        url = uri.toString() + "/smartmerchant/merchant/account/id";
	        requestEntity = new LinkedMultiValueMap<String, String>();
	        requestEntity.add("merchant_id", merchantId);
	        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			resultObj = JSONObject.fromObject(result);
			JSONObject accountObj  = resultObj.getJSONObject("result");
			if(accountObj.isEmpty()) {
				return ResultWrap.init(Constss.FALIED, "失败", "");
			}else {
				
				String balance = accountObj.getString("amount");
				
				
				
				if(withdrawCondition.getSingleLimit() != null && withdrawCondition.getSingleLimit().compareTo(BigDecimal.ZERO) > 0) {
					if(new BigDecimal(amount).compareTo(withdrawCondition.getSingleLimit()) > 0) {
						return ResultWrap.init(Constss.FALIED, "单笔最大提现金额为"+withdrawCondition.getSingleLimit(), "");
					}
				}
				
				if(withdrawCondition.getSingleMinLimit() != null && withdrawCondition.getSingleMinLimit().compareTo(BigDecimal.ZERO) > 0) {
					if(new BigDecimal(amount).compareTo(withdrawCondition.getSingleMinLimit()) < 0) {
						return ResultWrap.init(Constss.FALIED, "单笔最小提现金额为"+withdrawCondition.getSingleMinLimit(), "");
					}
				}
				
				
				
				/**判断日限制**/
				if(withdrawCondition.getDayLimit() != null && withdrawCondition.getDayLimit().compareTo(BigDecimal.ZERO) > 0) {
					
					
					uri = util.getServiceUrl("smartclearing", "error url request!");
			        url = uri.toString() + "/smartclearing/merchant/sumamount/query";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        
			        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
					
					String startDate = sdf.format(new Date());
					
					Date endDate = DateUtil.getTomorrow(new Date());
					
					String endDatestr = sdf.format(endDate);
			        
			        requestEntity.add("start_time", startDate);
			        requestEntity.add("end_time", endDatestr);
			        requestEntity.add("order_type", "1");
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
					resultObj = JSONObject.fromObject(result);
					String daytradeamount = resultObj.getString("result");
					
					if(new BigDecimal(amount).add(new BigDecimal(daytradeamount)).compareTo(withdrawCondition.getDayLimit()) > 0) {
						return ResultWrap.init(Constss.FALIED, "单日最大体现金额为"+withdrawCondition.getDayLimit(), "");
					}
					
				}
				
				
				if(new BigDecimal(amount).compareTo(new BigDecimal(balance)) > 0) {
					return ResultWrap.init(Constss.FALIED, "余额不足", "");
				}
				
				uri = util.getServiceUrl("smartmerchant", "error url request!");
		        url = uri.toString() + "/smartmerchant/merchant/id";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
		        resultObj = JSONObject.fromObject(result);
				String merchantname = resultObj.getJSONObject("result").getString("merchantName");
				String parent = resultObj.getJSONObject("result").getString("parent");
		        String settlementPeroid = resultObj.getJSONObject("result").getString("settlePeroid");
				
		        String bankcardid = "";
		        String bankcardname = "";
		        if(cardNo != null && !cardNo.equalsIgnoreCase("")
		        		&& cardName != null && !cardName.equalsIgnoreCase("")) {
		        	
		        	bankcardid = cardNo;
		        	bankcardname = cardName;
		        
		        }else {
		       
			        uri = util.getServiceUrl("smartmerchant", "error url request!");
			        url = uri.toString() + "/smartmerchant/merchant/bank/query/iddef";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			        resultObj = JSONObject.fromObject(result);
			       
			        if(!resultObj.getJSONObject("result").isEmpty()) {
			        	bankcardid = resultObj.getJSONObject("result").getString("cardNo");
			        	bankcardname = resultObj.getJSONObject("result").getString("merchantName");
			        	
			        }
		        
		        }
				/***创建订单*/
				/**生成订单**/
				uri = util.getServiceUrl("smartclearing", "error url request!");
		        url = uri.toString() + "/smartclearing/order/withdraw/new";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        requestEntity.add("merchant_name", merchantname);
		        requestEntity.add("parent_id", parent);
		        requestEntity.add("amount", amount);
		        requestEntity.add("extra_fee", withdrawCondition.getOuterFee() == null ? "0":withdrawCondition.getOuterFee().toString());
		        requestEntity.add("settle_peroid", settlementPeroid);
		        requestEntity.add("attach", bankcardid+" "+bankcardname);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
				JSONObject withdrawOrderObj = JSONObject.fromObject(result);
				if(withdrawOrderObj.getString("resp_code").equalsIgnoreCase(Constss.SUCCESS)) {
					
					/***冻结用户账户余额**/
					uri = util.getServiceUrl("smartmerchant", "error url request!");
			        url = uri.toString() + "/smartmerchant/merchant/withdraw/req";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        requestEntity.add("order_code", withdrawOrderObj.getJSONObject("result").getString("orderCode"));
			        requestEntity.add("amount", amount);
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			        return ResultWrap.init(Constss.SUCCESS, "提现成功", "");
				}else {
					return ResultWrap.init(Constss.FALIED, "创建订单失败", "");
				}
				
				
				
			}
			
		
		}
		
		
	}
	
	
	/***
	 * authstatus 为 1 表示通过
	 * 
	 * authstatus 为2表示拒绝
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/withdraw/auth")
	public @ResponseBody Object authwithdraw(HttpServletRequest request,
			@RequestParam(value = "order_code") String ordercode,
			@RequestParam(value = "auth_status") String authorreject
			) {
		
		
		
		
		/**更新订单**/
		URI uri = util.getServiceUrl("smartclearing", "error url request!");
        String url = uri.toString() + "/smartclearing/order/update";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("order_code", ordercode);
        requestEntity.add("order_status", authorreject);
        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
        JSONObject resultObj = JSONObject.fromObject(result);
		
        if(resultObj.getString("resp_code").equalsIgnoreCase(Constss.SUCCESS)) {
        	 return ResultWrap.init(Constss.SUCCESS, "更新成功", "");
        }else {
        	return ResultWrap.init(Constss.FALIED, "更新失败", "");
        }

	}
	
	
	
	
	/**发起提现**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/gateway/withdraw")
	public @ResponseBody Object withdrawindex(HttpServletRequest request,
			@RequestParam(value = "version") String version,
			@RequestParam(value = "charset") String charset,
			@RequestParam(value = "sign_type") String signType,
			@RequestParam(value = "merchant_id") String merchantId,
			@RequestParam(value = "card_no") String cardno,
			@RequestParam(value = "user_name") String username,
			@RequestParam(value = "amount") String amount,
			@RequestParam(value = "merchant_order_code") String merchantOrderCode,
			@RequestParam(value = "nonce_str") String nonceStr,
			@RequestParam(value = "sign") String sign
			) {
		
		/**查询商家的key**/
		URI uri = util.getServiceUrl("smartmerchant", "error url request!");
        String url = uri.toString() + "/smartmerchant/merchant/info";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantId);
        requestEntity.add("key_type", signType == null || signType.equalsIgnoreCase("") ? "MD5" :  signType);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);

		JSONObject resultMerchantObj = resultObj.getJSONObject("result");
		if(resultMerchantObj.isEmpty()) {

				return getResponseResultNoBiz(ErrorCodeMsg.MERCHANTNOEXIST);
		}
		
		String key = resultMerchantObj.getString("key");
		
		/**验证签名**/
		Map<String, String> signParams = new HashMap<String, String>();
		signParams.put("version", version);
		signParams.put("charset",charset);
		signParams.put("sign_type", signType);
		signParams.put("amount", amount);
		signParams.put("merchant_id",merchantId);
		signParams.put("nonce_str",nonceStr);
		signParams.put("card_no",cardno);
		signParams.put("user_name",username);
		signParams.put("merchant_order_code",merchantOrderCode);
		signParams.put("sign", sign);
		boolean isOk = SignUtils.checkParam(signParams, key);
		if(!isOk) {
			//return getResponseResult(merchantId, key, ErrorCodeMsg.SIGNERROR);
			return getResponseResultNoBiz(ErrorCodeMsg.SIGNERROR);
		}
		
		
		/**验证参数*/
		/**版本号是否正确*/
		if(!version.equalsIgnoreCase("1.0")) {
			return getResponseResultNoBiz(ErrorCodeMsg.VERSIONERROR);
		}
		
		/**chartset是否正确*/
		if(!charset.equalsIgnoreCase("UTF-8")) {
			return getResponseResultNoBiz(ErrorCodeMsg.CHARSETERROR);
		}
	
		/**sign_type是否正确**/
		if(!signType.equalsIgnoreCase("MD5")) {
			return getResponseResultNoBiz(ErrorCodeMsg.SIGNTYPEERROR);
		}
		
		
		/**验证商家是否有交易权限**/
		uri = util.getServiceUrl("smartrisk", "error url request!");
		url = uri.toString() + "/smartrisk/merchantblacklist/merchantid";
		requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantId);
        requestEntity.add("risk_type", "2");// 0 标识没有交易权限 1表示没有登录权限 2表示没有提现权限 
        result = new RestTemplate().postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        JSONObject resultObject= resultObj.getJSONObject("result");
		if(!resultObject.isEmpty()) {
			return getResponseResultNoBiz(ErrorCodeMsg.NOPRIVILEDGE);
		}
		
		
		WithdrawCondition withdrawCondition = withdrawBusiness.queryWithdrawCondtion();
		/**通道代码块*/
		synchronized(this){
			
			/***验证商户的余额是否充足*/
			uri = util.getServiceUrl("smartmerchant", "error url request!");
	        url = uri.toString() + "/smartmerchant/merchant/account/id";
	        requestEntity = new LinkedMultiValueMap<String, String>();
	        requestEntity.add("merchant_id", merchantId);
	        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			resultObj = JSONObject.fromObject(result);
			JSONObject accountObj  = resultObj.getJSONObject("result");
			if(accountObj.isEmpty()) {
				return getResponseResultNoBiz(ErrorCodeMsg.MERCHANTNOEXIST);
			}else {
				
				String balance = accountObj.getString("amount");
				
				if(withdrawCondition.getSingleLimit() != null && withdrawCondition.getSingleLimit().compareTo(BigDecimal.ZERO) > 0) {
					if(new BigDecimal(amount).compareTo(withdrawCondition.getSingleLimit()) > 0) {
						//return ResultWrap.init(Constss.FALIED, "单笔最大提现金额为"+withdrawCondition.getSingleLimit(), "");
						return getResponseResultNoBiz(ErrorCodeMsg.OUTOFAMOUNTRANGE);
					}
				}
				
				if(withdrawCondition.getSingleMinLimit() != null && withdrawCondition.getSingleMinLimit().compareTo(BigDecimal.ZERO) > 0) {
					if(new BigDecimal(amount).compareTo(withdrawCondition.getSingleMinLimit()) < 0) {
						return getResponseResultNoBiz(ErrorCodeMsg.OUTOFAMOUNTRANGE);
					}
				}
				
				
				
				/**判断日限制**/
				if(withdrawCondition.getDayLimit() != null && withdrawCondition.getDayLimit().compareTo(BigDecimal.ZERO) > 0) {
					
					
					uri = util.getServiceUrl("smartclearing", "error url request!");
			        url = uri.toString() + "/smartclearing/merchant/sumamount/query";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        
			        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
					
					String startDate = sdf.format(new Date());
					
					Date endDate = DateUtil.getTomorrow(new Date());
					
					String endDatestr = sdf.format(endDate);
			        
			        requestEntity.add("start_time", startDate);
			        requestEntity.add("end_time", endDatestr);
			        requestEntity.add("order_type", "1");
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
					resultObj = JSONObject.fromObject(result);
					String daytradeamount = resultObj.getString("result");
					
					if(new BigDecimal(amount).add(new BigDecimal(daytradeamount)).compareTo(withdrawCondition.getDayLimit()) > 0) {
						return getResponseResultNoBiz(ErrorCodeMsg.OUTOFDAYAMOUNTRANGE);
					}
					
				}
				
				
				if(new BigDecimal(amount).compareTo(new BigDecimal(balance)) > 0) {
					return getResponseResultNoBiz(ErrorCodeMsg.OUTOFUSERBALANCE);
				}
				
				uri = util.getServiceUrl("smartmerchant", "error url request!");
		        url = uri.toString() + "/smartmerchant/merchant/id";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
		        resultObj = JSONObject.fromObject(result);
				String merchantname = resultObj.getJSONObject("result").getString("merchantName");
				String parent = resultObj.getJSONObject("result").getString("parent");
		        String settlementPeroid = resultObj.getJSONObject("result").getString("settlePeroid");
				
		        String bankcardid = "";
		        String bankcardname = "";
		        if(cardno != null && !cardno.equalsIgnoreCase("")
		        		&& username != null && !username.equalsIgnoreCase("")) {
		        	
		        	bankcardid = cardno;
		        	bankcardname = username;
		        
		        }else {
		       
			        uri = util.getServiceUrl("smartmerchant", "error url request!");
			        url = uri.toString() + "/smartmerchant/merchant/bank/query/iddef";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
			        resultObj = JSONObject.fromObject(result);
			       
			        if(!resultObj.getJSONObject("result").isEmpty()) {
			        	bankcardid = resultObj.getJSONObject("result").getString("cardNo");
			        	bankcardname = resultObj.getJSONObject("result").getString("merchantName");
			        	
			        }
		        
		        }
				
		        
		        String ordercode = RandomUtils.generateNewLowerString(20);
		        /***冻结用户账户余额**/
				uri = util.getServiceUrl("smartmerchant", "error url request!");
		        url = uri.toString() + "/smartmerchant/merchant/withdraw/req";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", merchantId);
		        requestEntity.add("order_code", ordercode);
		        requestEntity.add("amount", amount);
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
		        JSONObject withdrawfreeresult = JSONObject.fromObject(result);
		        if(withdrawfreeresult.getString("resp_code").equalsIgnoreCase(Constss.SUCCESS)) {
					/***创建订单*/
					/**生成订单**/
					uri = util.getServiceUrl("smartclearing", "error url request!");
			        url = uri.toString() + "/smartclearing/order/withdraw/new";
			        requestEntity = new LinkedMultiValueMap<String, String>();
			        requestEntity.add("merchant_id", merchantId);
			        requestEntity.add("merchant_name", merchantname);
			        requestEntity.add("parent_id", parent);
			        requestEntity.add("amount", amount);
			        requestEntity.add("order_code", ordercode);
			        requestEntity.add("extra_fee", withdrawCondition.getOuterFee() == null ? "0":withdrawCondition.getOuterFee().toString());
			        requestEntity.add("settle_peroid", settlementPeroid);
			        requestEntity.add("attach", bankcardid+":"+bankcardname); 
			        requestEntity.add("merchant_order_code", merchantOrderCode);
			        result = new RestTemplate().postForObject(url, requestEntity, String.class);
					JSONObject withdrawOrderObj = JSONObject.fromObject(result);
					if(withdrawOrderObj.getString("resp_code").equalsIgnoreCase(Constss.SUCCESS)) {
						return getWithdrawSuccessResponseResult(merchantId, key, merchantOrderCode);
					}else {
						return getResponseResultNoBiz(ErrorCodeMsg.NONAMESYSERROR);
					}
		        }else {
		        	return getResponseResultNoBiz(ErrorCodeMsg.NONAMESYSERROR);
		        }
			
				
			}
	
		}
	
	}
	/**支付请求
	 * 
	 * 1: 将支付请求存入到数据库中间
	 * 
	 * 
	 * 2: 验证签名
	 * 
	 * 3: 判断白名单
	 * 
	 * 
	 * 4:
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/gateway")
	public @ResponseBody Object payIndex(HttpServletRequest request,
			@RequestParam(value = "tradeCode", required=false) String service,  //通道号 01环球
			@RequestParam(value = "methodName", required = false) String tradeCode, //交易码 01 register 02 bindCard 10 pay 11 withDraw
			@RequestParam(value="phone",required = false) String phone,//手机号
			@RequestParam(value="idCardNo",required = false) String idCard,//身份证号
			@RequestParam(value="bankCardNumber",required = false) String bankCardNumber,//银行卡号
			@RequestParam(value="bankAccountName",required = false) String bankAccountName,//银行卡姓名
			@RequestParam(value="bankName",required = false) String bankName,//银行名称
			@RequestParam(value="rate",required = false) String rate,//费率
			@RequestParam(value="extraFee",required = false) String extraFee,//手续费
			@RequestParam(value="securityCode",required = false) String securityCode,//CVV码
			@RequestParam(value="expiredTime",required = false) String expiredTime,//信用卡有效期 YY/MM
			@RequestParam(value="subMerchantNo"	,required = false) String subMerchantNo,//终端子商户号
			@RequestParam(value="bindId",required = false) String bindId,//用户通道绑卡id
			@RequestParam(value="city",required = false) String city,//落地城市
			@RequestParam(value = "version", required=false) String version,
			@RequestParam(value = "charset", required=false) String charset,
			@RequestParam(value = "sign_type", required=false) String signType,
			@RequestParam(value = "merchanId", required=false) String merchantId,
			@RequestParam(value = "outTradeNo", required=false) String outTradeNo, //下游商户订单号
			@RequestParam(value = "nonceStr", required=false) String nonceStr,
			@RequestParam(value = "goodsDesc", required=false) String goodsDesc,
			@RequestParam(value = "attach", required=false) String attach,
			@RequestParam(value = "totalAmount", required=false) String totalAmount,
			@RequestParam(value = "notifyUrl", required=false) String notifyURL,
			@RequestParam(value = "returnUrl", required=false) String returnURL,
			@RequestParam(value = "sign", required=false) String sign
			) {
		
		if(service == null || service.equalsIgnoreCase("") || version == null || version.equalsIgnoreCase("") 
			|| charset == null || charset.equalsIgnoreCase("")
			|| signType == null || signType.equalsIgnoreCase("")
			|| merchantId == null || merchantId.equalsIgnoreCase("")
			|| outTradeNo == null || outTradeNo.equalsIgnoreCase("")
			|| nonceStr == null || nonceStr.equalsIgnoreCase("")
			|| goodsDesc == null || goodsDesc.equalsIgnoreCase("")
		    || notifyURL == null || notifyURL.equalsIgnoreCase("")
		    || sign == null || sign.equalsIgnoreCase("")){
			return getResponseResultNoBiz(ErrorCodeMsg.PARAMSMUST);
		}
		Map map=new HashMap<>();

		com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
		jsonObject.put("tradeCode",service);
		jsonObject.put("methodName",tradeCode);
		jsonObject.put("phone",phone);
		jsonObject.put("idCard",idCard);
		jsonObject.put("bankCardNumber",bankCardNumber);
		jsonObject.put("bankAccountName",bankAccountName);
		jsonObject.put("city",city);
		jsonObject.put("version",version);
		jsonObject.put("charset",charset);
		jsonObject.put("signType",signType);
		jsonObject.put("merchantId",merchantId);
		jsonObject.put("outTradeNo",outTradeNo);
		jsonObject.put("goodsDesc",goodsDesc);
		jsonObject.put("totalAmount",totalAmount);
		jsonObject.put("notifyURL",notifyURL);
		jsonObject.put("returnURL",returnURL);
		jsonObject.put("sign",sign);
		jsonObject.put("subMerchantNo",subMerchantNo);
		jsonObject.put("bindId",bindId);
		LOG.info("请求参数=============="+jsonObject);
		
		
		/**查询商家的key**/
		URI uri = util.getServiceUrl("smartmerchant", "error url request!");
        String url = uri.toString() + "/smartmerchant/merchant/info";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", merchantId);
        requestEntity.add("key_type", signType);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject resultObj = JSONObject.fromObject(result);

		JSONObject resultMerchantObj = resultObj.getJSONObject("result");
		if(resultMerchantObj.isEmpty()) {

				return getResponseResultNoBiz(ErrorCodeMsg.MERCHANTNOEXIST);
		}
		
				
		
		String key = resultMerchantObj.getString("key");
		String merchantname = resultMerchantObj.getString("merchantName");
		String parentId     = resultMerchantObj.getString("parent");
		
		/****/
		PaymentRequest tempRequest = paymentRequestBusiness.queryPaymentRequestByMerchantIdAndOutTradeNo(merchantId, outTradeNo);
		if(tempRequest != null) {
			return getResponseResult(tempRequest.getMerchantId(), key, ErrorCodeMsg.REQUESTHASEXIST);
		}
		
		/**将用户的请求加入进来*/
		PaymentRequest paymentRequest = savePaymentRequest(service, version, charset, signType, merchantId, outTradeNo, nonceStr, goodsDesc, attach, totalAmount, notifyURL, returnURL, sign);
		
		
		Map<String, String> signParms = new HashMap<String, String>();
		signParms.put("tradeCode", service);
		signParms.put("version", version);
		signParms.put("charset", charset);
		signParms.put("sign_type", signType);
		signParms.put("merchant_id", merchantId);
		signParms.put("out_trade_no", outTradeNo);
		signParms.put("nonce_str", nonceStr);
		signParms.put("goods_desc", goodsDesc);
		signParms.put("total_amount", totalAmount);
		signParms.put("notify_url", notifyURL);
		signParms.put("sign", sign);
		if(returnURL != null && !returnURL.equalsIgnoreCase("")) {
			signParms.put("return_url", returnURL);
		}
		
		if(attach != null && !attach.equalsIgnoreCase("")) {
			signParms.put("attach", attach);
		}
		
		
		
//		/**首先验证签名*/
//		if(!SignUtils.checkParam(signParms, key)) {
//			return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.SIGNERROR);
//		}

		//根据通道号获取通道标识
		RealChannel realChannel=channelBusiness.queryRealChannel(service);
		if(realChannel==null){
			return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.CHANNELNOTOPENERROR);
		}
		service=realChannel.getRealChannelCode();
		LOG.info("通道标识============="+service);
		/**根据当前的请求接口，获取商家通道 **/
		PaymentChannel paymentChannel = channelBusiness.queryPaymentChannelByChannelcode(realChannel.getRealChannelCode());



//        if(new BigDecimal(totalAmount).compareTo(paymentChannel.getMaxlimit()) > 0  || new BigDecimal(totalAmount).compareTo(paymentChannel.getMinlimit()) < 0) {
//
//            return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.MAXLIMIT);
//
//        }


			
		/**判断白名单*/
		String remoteIP  = IPUtils.getRemoteIP(request);

		LOG.info(paymentRequest.getMerchantId()+"...........remoteIP............"+remoteIP);
		
		uri = util.getServiceUrl("smartmerchant", "error url request!");
        url = uri.toString() + "/smartmerchant/merchant/whitelist/query";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        restTemplate = new RestTemplate();
        result = restTemplate.postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        JSONArray resultArray = resultObj.getJSONArray("result");
		if(resultArray.isEmpty()) {
	        
	        return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.NOTWHITELIST);
		
		}else {
			boolean isWhiteList =false;
			for(int i=0; i<resultArray.size(); i++) {
				JSONObject tempObj = resultArray.getJSONObject(i);
				if(remoteIP.contains(tempObj.getString("whitelist"))) {
					isWhiteList = true;
					break;
				}
			}
			if(!isWhiteList) {
				 return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.NOTWHITELIST);
			}
		}


		/**判断商家是否开通了此通道*/
		uri = util.getServiceUrl("smartmerchant", "error url request!");
        url = uri.toString() + "/smartmerchant/merchant/channel/query";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        requestEntity.add("status", "1");   //查询所有可用通道
        restTemplate = new RestTemplate();
        result = restTemplate.postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        resultArray = resultObj.getJSONArray("result");
        
        
        String channelName = "";
        String isAllowFreeSel = "";
        if(resultArray.isEmpty()) {
        	 return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.SERVICENOSUPPORTERROR);
        }else {
        	
        	boolean isOpenService = false;
        	for(int n=0; n<resultArray.size(); n++) {
        		JSONObject tmpMerchantChannelObj  = resultArray.optJSONObject(n);
        		if(tmpMerchantChannelObj.getString("channelCode").equalsIgnoreCase(service)) {
        			
        			channelName = tmpMerchantChannelObj.getString("channelName");
        			isAllowFreeSel = tmpMerchantChannelObj.getString("isAllowFreeSel");
        			isOpenService = true;
        			break;
        		}
        		
        	}
        	
        	if(!isOpenService) {
        		 return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.SERVICENOSUPPORTERROR);
        	}
        	
        }

        
        
		/*List<PaymentChannel> channels = channelBusiness.queryAllPaymentChannels();
		boolean isExistService = false;
		for(PaymentChannel paymentChannel : channels) {
			if(service.equalsIgnoreCase(paymentChannel.getChannelCode())) {
				isExistService = true;
				break;
			}
		}
		
		if(!isExistService) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.SERVICENOSUPPORTERROR);
		}*/
		
		
		
		/**验证参数*/
		/**版本号是否正确*/
		if(!version.equalsIgnoreCase("1.0")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.VERSIONERROR);
		}
		
		/**chartset是否正确*/
		if(!charset.equalsIgnoreCase("UTF-8")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.CHARSETERROR);
		}
	
		/**sign_type是否正确**/
		if(!signType.equalsIgnoreCase("MD5")) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.SIGNTYPEERROR);
		}

		//消费及还款操作，验证金额
		if("pay".equals(tradeCode)||"withDraw".equals(tradeCode)){
            boolean isAmount = Tools.checkAmount(totalAmount);
            if(!isAmount) {
                return getResponseResult(merchantId, key, ErrorCodeMsg.AMOUTERROR);
            }
        }

		
		
		/**验证商家是否有交易权限**/
		uri = util.getServiceUrl("smartrisk", "error url request!");
        url = uri.toString() + "/smartrisk/merchantblacklist/merchantid";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        requestEntity.add("risk_type", "0");// 0 标识没有交易权限 1表示没有登录权限 2表示没有提现权限
        result = new RestTemplate().postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        JSONObject resultObject= resultObj.getJSONObject("result");
		if(!resultObject.isEmpty()) {
			return getResponseResult(merchantId, key, ErrorCodeMsg.NOPAYAUTHERROR);
		}
		
		
		
		/**判断此商户是否有交易限额的限制**/
		uri = util.getServiceUrl("smartrisk", "error url request!");
        url = uri.toString() + "/smartrisk/merchantlimit/query";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        requestEntity.add("channel_code", service);
        requestEntity.add("channel_type", "0");
        result = new RestTemplate().postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        resultObject= resultObj.getJSONObject("result");
		if(!resultObject.isEmpty()) {
			
			String dayLimit  = resultObject.getString("dayLimit");
			if(new BigDecimal(dayLimit).compareTo(BigDecimal.ZERO) > 0) {   //表示系统有限制
				
				SimpleDateFormat sdfDf = new SimpleDateFormat("yyyy-MM-dd");
				/***查询当前系统已经统计的今天的交易总量**/
				uri = util.getServiceUrl("smartmerchant", "error url request!");
		        url = uri.toString() + "/smartmerchant/merchant/transactionstatis/query";
		        requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
		        requestEntity.add("channel_code", service);
		        requestEntity.add("statis_date", sdfDf.format(new Date()));
		        result = new RestTemplate().postForObject(url, requestEntity, String.class);
		        resultObj = JSONObject.fromObject(result);
		        resultObject= resultObj.getJSONObject("result");
				String transactionAmt = resultObject.getString("transactionAmt");
				
				if(new BigDecimal(transactionAmt).compareTo(new BigDecimal(dayLimit)) >=0) {
					return getResponseResult(merchantId, key, ErrorCodeMsg.MERCHANTAMOUTLIMITERROR);
				}
			}
			
		}

		//根据商户号查询可执行通道


		//交易码register 表示商户进件
		if("register".equals(tradeCode)){
			//根据通道查询注册接口地址
            LOG.info("进件操作=================phone==="+phone);
			ChannelRegisterRoute channelRegisterRoute=channelRegisterRouteBusiness.queryRegisterUrlByService(service);
			String channelRegisterUrl=channelRegisterRoute.getRegisterUrl();
			restTemplate=new RestTemplate();
			uri = util.getServiceUrl("smartchannel", "error url request!");
			url = uri.toString() + channelRegisterUrl;
			MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
			params.add("merchant_id",merchantId);
			params.add("bankCard",bankCardNumber);
			params.add("idCard",idCard);
			params.add("phone",phone);
			params.add("userName",bankAccountName);
			params.add("rate",rate);
			params.add("extraFee",extraFee);
			LOG.info("进件请求参数======"+params);
			try {
				String entranceResult=restTemplate.postForObject(url,params,String.class);
				com.alibaba.fastjson.JSONObject json=com.alibaba.fastjson.JSONObject.parseObject(entranceResult);
				String code=json.getString("resp_code");
				String message=json.getString("resp_message");
				com.alibaba.fastjson.JSONObject registerJsonObject=json.getJSONObject("result");
				if(!CommonConstants.SUCCESS.equals(code)){
					map.clear();
					map.put(CommonConstants.RESP_CODE,CommonConstants.FALIED);
					map.put(CommonConstants.RESP_MESSAGE,message);
					return map;
				}else {
                    map.clear();
                    map.put(CommonConstants.RESP_CODE,CommonConstants.SUCCESS);
                    map.put(CommonConstants.RESP_MESSAGE,message);
                    return map;
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.clear();
				map.put(CommonConstants.RESP_CODE,CommonConstants.FALIED);
				map.put(CommonConstants.RESP_MESSAGE,"网络波动异常");
				return map;
			}
		}else if("bindCard".equals(tradeCode)){//交易码bindCard 表示绑卡交易
			//根据通道查询绑卡接口地址
			ChannelBindCardRoute channelBindCardRoute=channelBindCardBusiness.queryByService(service);
			String bindCardUrl=channelBindCardRoute.getBindCardUrl();
			restTemplate=new RestTemplate();
			uri = util.getServiceUrl("smartchannel", "error url request!");
			url = uri.toString() + bindCardUrl;
			MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
			params.add("merchant_id",merchantId);
			params.add("bankCard",bankCardNumber);
			params.add("idCard",idCard);
			params.add("phone",phone);
			params.add("userName",bankAccountName);
			params.add("securityCode",securityCode);
			params.add("expiredTime",expiredTime);
			params.add("subMerchantNo",subMerchantNo); //进件子商户号
			params.add("dsorderid",outTradeNo);//商户订单号
			LOG.info("绑卡请求参数======"+params);
			try {
				String entranceResult=restTemplate.postForObject(url,params,String.class);
				com.alibaba.fastjson.JSONObject json=com.alibaba.fastjson.JSONObject.parseObject(entranceResult);
				String code=json.getString("resp_code");
				String message=json.getString("resp_message");
				String bindResult=json.getString("result");
				if(!CommonConstants.SUCCESS.equals(code)){
					map.clear();
					map.put(CommonConstants.RESP_CODE,CommonConstants.FALIED);
					map.put(CommonConstants.RESP_MESSAGE,message);
					map.put(CommonConstants.RESULT,bindResult);
					return map;
				}
				map.clear();
				map.put(CommonConstants.RESP_CODE,code);
				map.put(CommonConstants.RESP_MESSAGE,message);
				map.put(CommonConstants.RESULT,bindResult);
				LOG.info("绑卡返回信息=========="+map);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				map.clear();
				map.put(CommonConstants.RESP_CODE,CommonConstants.FALIED);
				map.put(CommonConstants.RESP_MESSAGE,"网络波动异常");
				return map;
			}
		}


		
//		/**获取路由*/
//		List<PaymentChannelRoute>  channelRoutes = channelBusiness.queryPaymentChannelRouteByChannelCode(paymentRequest.getMerchantId(), paymentRequest.getService());
//
//		String realChannelCode = "";
//		String realChannelName = "";
//
//		if(channelRoutes == null || channelRoutes.size() == 0) {
//
//			/***判断能不能启动第二选项。 如果不能， 直接抛出当前的通道不可用*/
//			if(isAllowFreeSel.equalsIgnoreCase("0")) {
//
//				/**获取当前所有可用的同类型的通道**/
//				List<RealChannel> realChannels = channelBusiness.queryRealChannelsByType(paymentChannel.getRealPayType());
//
//				if(realChannels == null || realChannels.size() == 0) {
//					return getResponseResult(merchantId, key, ErrorCodeMsg.CHANNELNOTOPENERROR);
//				}else {
//
//					if(realChannels.size() == 1) {
//
//						realChannelCode  = realChannels.get(0).getRealChannelCode();
//						realChannelName  = realChannels.get(0).getRealChannelName();
//
//
//					}else {
//						int x=0;
//						Map<Integer, String>  indexRealCodes  = new HashMap<Integer, String>();
//						Map<Integer, String>  indexRealNames  = new HashMap<Integer, String>();
//						List<Double> indexs = new ArrayList<Double>();
//						for(RealChannel realChannel : realChannels) {
//
//
//							indexs.add(new Double(realChannel.getRatio()));
//							indexRealCodes.put(x, realChannel.getRealChannelCode());
//							indexRealNames.put(x, realChannel.getRealChannelName());
//							x++;
//						}
//
//						LotteryUtil ll = new LotteryUtil(indexs);
//						int index = ll.randomColunmIndex();
//						if(index < 0) {
//							return getResponseResult(merchantId, key, ErrorCodeMsg.CHANNELNOTOPENERROR);
//						}
//						realChannelCode = indexRealCodes.get(index);
//						realChannelName = indexRealNames.get(index);
//					}
//
//
//				}
//
//
//
//			}else {
//				return getResponseResult(merchantId, key, ErrorCodeMsg.CHANNELNOTOPENERROR);
//			}
//
//
//		}else {
//
//
//			if(channelRoutes.size() == 1) {  //表示只有一家， 那么只能用此通道了
//
//				realChannelCode  = channelRoutes.get(0).getRealChannelCode();
//				realChannelName  = channelRoutes.get(0).getRealChannelName();
//
//			}else {
//
//				/**根据比率根据一定的概率获取通道*/
//				int x=0;
//				List<Double> indexs = new ArrayList<Double>();
//				Map<Integer, PaymentChannelRoute>  indexPayChannelRoute = new HashMap<Integer, PaymentChannelRoute>();
//				for(PaymentChannelRoute route : channelRoutes) {
//					indexs.add(new Double(route.getRatio()));
//					indexPayChannelRoute.put(x, route);
//					x++;
//				}
//
//				LotteryUtil ll = new LotteryUtil(indexs);
//				int index = ll.randomColunmIndex();
//
//				if(index < 0) {
//					return getResponseResult(merchantId, key, ErrorCodeMsg.CHANNELNOTOPENERROR);
//				}
//
//				PaymentChannelRoute tmpRoute = indexPayChannelRoute.get(index);
//				realChannelCode  = tmpRoute.getRealChannelCode();
//				realChannelName  = tmpRoute.getRealChannelName();
//
//			}
//
//
//		}
//
//
//		/**获取实际通道*/
//		RealChannel realChannel = channelBusiness.queryRealChannel(realChannelCode);
		
		/**获取商家费率**/
		uri = util.getServiceUrl("smartmerchant", "error url request!");
        url = uri.toString() + "/smartmerchant/merchant/rate/id";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        requestEntity.add("channel_code", service);
        restTemplate = new RestTemplate();
        result = restTemplate.postForObject(url, requestEntity, String.class);
        resultObj = JSONObject.fromObject(result);
        JSONObject merchantRateObj = resultObj.getJSONObject("result");
        
        if(merchantRateObj.isNullObject()) {
        	
        	return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.MERCHANTNORATE);
        }
        //查询子商户费率及手续费存入订单
		uri = util.getServiceUrl("smartmerchant", "error url request!");
		url = uri.toString() + "/smartmerchant/merchant/user/info/query";
		requestEntity = new LinkedMultiValueMap<String, String>();
		requestEntity.add("merchant_id",merchantId);
		requestEntity.add("sub_merchant_id",subMerchantNo);
		requestEntity.add("channel_tag",service);
		result=restTemplate.postForObject(url,requestEntity,String.class);
		com.alibaba.fastjson.JSONObject jsonObject1= com.alibaba.fastjson.JSONObject.parseObject(result);
		com.alibaba.fastjson.JSONObject subMerchantInfo=jsonObject1.getJSONObject("result");
		String subRate=subMerchantInfo.getString("subRate");//子商户费率
		String subExtraFee=subMerchantInfo.getString("subExtraFee");//子商户手续费

		/**生成订单**/
		uri = util.getServiceUrl("smartclearing", "error url request!");
        url = uri.toString() + "/smartclearing/order/new";
        requestEntity = new LinkedMultiValueMap<String, String>();
        requestEntity.add("merchant_id", paymentRequest.getMerchantId());
        requestEntity.add("merchant_name", merchantname);
        requestEntity.add("parent_id", parentId);
        requestEntity.add("attach", attach);
        requestEntity.add("mer_rate", merchantRateObj.getString("rate"));
        requestEntity.add("extra_fee", merchantRateObj.getString("extraFee"));
        requestEntity.add("mer_order_code", paymentRequest.getOutTradeNo());
        requestEntity.add("order_type", "0");//0表示收单
        requestEntity.add("amount", paymentRequest.getTotalAmount());
        requestEntity.add("product_name", "");
        requestEntity.add("channel_code", service);
        requestEntity.add("channel_name", channelName);
        requestEntity.add("real_channel_code", service);
        requestEntity.add("real_channel_name", channelName);
        requestEntity.add("notify_url", paymentRequest.getNotifyURL());
        requestEntity.add("return_url", paymentRequest.getReturnURL());
        requestEntity.add("real_channel_rate", subRate);//终端用户费率
        requestEntity.add("real_channel_extra_fee", subExtraFee);//终端用户手续费
		String realType="";
		if("pay".equals(tradeCode)){
			realType="10";
		}else{
			realType="11";
		}
        requestEntity.add("real_type", realType);//订单类型，10为消费，11为还款
		requestEntity.add("phone", phone);//手机号
		requestEntity.add("idCard", idCard);//身份证号码
		requestEntity.add("bankCardNumber", bankCardNumber);//银行卡号
		requestEntity.add("bankAccountName", bankAccountName);//银行卡用户姓名

        restTemplate = new RestTemplate();
        result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject paymentOrderObj = JSONObject.fromObject(result);
		
		LOG.info("index:"+IPUtils.getRemoteIP(request));
		/**根据不同的支付服务， 路由不同的支付接口**/
        uri = util.getServiceUrl("smartchannel", "error url request!");
        url = uri.toString() + "/smartchannel/topup/request";
        requestEntity = new LinkedMultiValueMap<String, String>();
        String orderCode=paymentOrderObj.getJSONObject("result").getString("orderCode");
        requestEntity.add("order_code", orderCode);
        //JSON传入需要数据
        jsonObject.put("orderCode",orderCode);
		jsonObject.put("amount",totalAmount);
		jsonObject.put("bankCard",bankCardNumber);
		jsonObject.put("extra",orderCode);
		jsonObject.put("ipAddress",remoteIP);

        jsonObject.put("phone",phone);//手机号
        jsonObject.put("idCard",idCard);//身份证号
        jsonObject.put("bankCardNumber",bankCardNumber);//银行卡号
        jsonObject.put("bankName",bankName);//银行名称
        jsonObject.put("bankAccountName",bankAccountName);//银行卡姓名
        jsonObject.put("securityCode",securityCode);//CVV码
        jsonObject.put("expiredTime",expiredTime);//信用卡有效期 YY/MM
        jsonObject.put("bindId",bindId);
        jsonObject.put("city",city);


        requestEntity.add("data", jsonObject.toString());
        result = new RestTemplate().postForObject(url, requestEntity, String.class);
		JSONObject urlObj = JSONObject.fromObject(result);
		if(urlObj.getString("resp_code").equalsIgnoreCase(Constss.SUCCESS)) {
			return getSuccessResponseResult(paymentRequest.getMerchantId(), key, urlObj.getString("result"));
		}else {
			return getResponseResult(paymentRequest.getMerchantId(), key, ErrorCodeMsg.CHANNELERROR);
		}
		
	}
	
	
	
	public boolean validParam(HttpServletRequest request) {
		
		
		
		
		/**版本号是否正确*/
		if(!request.getParameter("version").equalsIgnoreCase("1.0")) {
			return false;

		}
		
		/**chartset是否正确*/
		if(!request.getParameter("charset").equalsIgnoreCase("UTF-8")) {
			return false;

		}
		
		
		/**service是否存在**/
		String service = request.getParameter("service");
		List<PaymentChannel> channels = channelBusiness.queryAllPaymentChannels();
		boolean isExistService = false;
		for(PaymentChannel paymentChannel : channels) {
			if(service.equalsIgnoreCase(paymentChannel.getChannelCode())) {
				isExistService = true;
				break;
			}
		}
		
		if(!isExistService) {
			return false;
		}
		
		
		/**sign_type是否正确**/
		if(!request.getParameter("sign_type").equalsIgnoreCase("MD5")) {
			return false;

		}
		
		
		/**金额格式是否正确，支持两位小数*/
		boolean isAmount = Tools.checkAmount(request.getParameter("total_amount"));
		if(!isAmount) {
			return false;
		}
		
		
		return true;
	}
	
	
	public boolean isSignOk(HttpServletRequest request, String key) {
		
		boolean isOk = false;
		
		
		Map<String, String> map = CommonUtil.getParameterMap(request);
		
		
		
		isOk = SignUtils.checkParam(map, key);
		
		return isOk;
		
	}
	
	
	
	
	
	
	
	/**存储请求到数据库*/
	public PaymentRequest savePaymentRequest(String service, String version, String charset, String signType, String merchantId, String outTradeNo, String nonceStr, String goodsDesc, String attach, String totalAmount, String notifyURL, String returnURL, String sign) {
		
		
		PaymentRequest paymentRequest = new PaymentRequest();
		
		paymentRequest.setMerchantId(merchantId);	
		/**版本号 非必传*/
		paymentRequest.setVersion(version);
		/**字符集 非必传*/
		paymentRequest.setCharset(charset);
		paymentRequest.setNotifyURL(notifyURL);
		/***非必传*/
		paymentRequest.setReturnURL(returnURL);
		paymentRequest.setOutTradeNo(outTradeNo);
		
		/**支付包 H5, API,  微信 H5, API  扫码等支付方式
		 * 
		 * pay.alipay.wap
		 * pay.alipay.api
		 * pay.alipay.scancode
		 * pay.weixin.wap
		 * pay.weixin.scancode
		 * pay.weixin.api
		 * 
		 * */
		paymentRequest.setService(service);
		/***验证签名
		 * 默认是MD5
		 * 非必传
		 */
		paymentRequest.setSignType(signType);
		paymentRequest.setTotalAmount(totalAmount);
		
		/**商品描述*/
		paymentRequest.setGoodsDesc(goodsDesc);
		
		
		/**额外参数
		 * 非必传
		 * */
		paymentRequest.setAttach(attach);
		
		
		/**随机字符串**/
		paymentRequest.setNonceStr(nonceStr);
		
		
		/**签名*/
		paymentRequest.setSign(sign);
		paymentRequest.setCreateTime(new Date());
		paymentRequest.setUpdateTime(new Date());
		paymentRequestBusiness.createPaymentRequest(paymentRequest);
		return paymentRequest;
	}
	
	public Map<String, String> getResponseResultNoBiz(ErrorCodeMsg errorCodeMsg) {

		Map<String, String> result = new HashMap<String, String>();
		
		
		ResponseResult responseResult = new ResponseResult();
		
		responseResult.setSign_type("MD5");
		responseResult.setVersion("1.0");
		responseResult.setStatus(errorCodeMsg.getErrorCode());
		responseResult.setCharset("UTF-8");
		responseResult.setMessage(errorCodeMsg.getErrormsg());
		
		result.put("version", responseResult.getVersion());
		result.put("charset", responseResult.getCharset());
		result.put("sign_type", responseResult.getSign_type());
		result.put("status", responseResult.getStatus());
		result.put("message", responseResult.getMessage());
		
        
        return result;
		
		
	}
	
	
	public  Map<String, String> getResponseResult(String merchantId, String key, ErrorCodeMsg errorCodeMsg) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setErr_code(errorCodeMsg.getErrorCode());
		responseResult.setErr_msg(errorCodeMsg.getErrormsg());
		responseResult.setMerchant_id(merchantId);
		responseResult.setSign_type("MD5");
		//responseResult.setSign(sign);
		responseResult.setNonce_str(String.valueOf(new Date().getTime()));
		responseResult.setResult_code("0000");
		responseResult.setVersion("1.0");
		responseResult.setStatus("0");
		responseResult.setCharset("UTF-8");
		
		
		Map<String, String>  signMap = new HashMap<String, String>();
		signMap.put("version", responseResult.getVersion());
		signMap.put("charset", responseResult.getCharset());
		signMap.put("sign_type", responseResult.getSign_type());
		signMap.put("status", responseResult.getStatus());	
		signMap.put("merchant_id", responseResult.getMerchant_id());
		signMap.put("result_code", responseResult.getResult_code());
		signMap.put("nonce_str", responseResult.getNonce_str());
		signMap.put("err_code", responseResult.getErr_code());
		signMap.put("err_msg", responseResult.getErr_msg());
		
		Map<String,String> params = SignUtils.paraFilter(signMap);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        responseResult.setSign(sign);
        
        
        Map<String, String>  resultparams = new HashMap<String, String>();
        resultparams.put("version", responseResult.getVersion());
        resultparams.put("charset", responseResult.getCharset());
        resultparams.put("sign_type", responseResult.getSign_type());
        resultparams.put("status", responseResult.getStatus());
        resultparams.put("merchant_id", responseResult.getMerchant_id());
        resultparams.put("result_code", responseResult.getResult_code());
        resultparams.put("nonce_str", responseResult.getNonce_str());
        resultparams.put("err_code", responseResult.getErr_code());
        resultparams.put("err_msg", responseResult.getErr_msg());
        resultparams.put("sign", responseResult.getSign());
        
        return resultparams;
		
		
	}
	
	
	
	public Map<String, String> getWithdrawSuccessResponseResult(String merchantId, String key, String result) {
		WithdrawResponseResult responseResult = new WithdrawResponseResult();
		responseResult.setMerchant_id(merchantId);
		responseResult.setSign_type("MD5");
		//responseResult.setSign(sign);
		responseResult.setNonce_str(String.valueOf(new Date().getTime()));
		responseResult.setResult_code("0");
		responseResult.setVersion("1.0");
		responseResult.setStatus("0");
		responseResult.setCharset("UTF-8");
		//responseResult.setPay_info(result);
		
		Map<String, String>  signMap = new HashMap<String, String>();
		signMap.put("version", responseResult.getVersion());
		signMap.put("charset", responseResult.getCharset());
		signMap.put("sign_type", responseResult.getSign_type());
		signMap.put("status", responseResult.getStatus());	
		signMap.put("merchant_id", responseResult.getMerchant_id());
		signMap.put("result_code", responseResult.getResult_code());
		signMap.put("nonce_str", responseResult.getNonce_str());
		signMap.put("merchant_order_code", result);
		/*signMap.put("err_code", responseResult.getErr_code());
		signMap.put("err_msg", responseResult.getErr_msg());*/
		//signMap.put("pay_info", responseResult.getPay_info());
		Map<String,String> params = SignUtils.paraFilter(signMap);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        responseResult.setSign(sign);
        
        
        Map<String, String>  resultparams = new HashMap<String, String>();
        resultparams.put("version", responseResult.getVersion());
        resultparams.put("charset", responseResult.getCharset());
        resultparams.put("sign_type", responseResult.getSign_type());
        resultparams.put("status", responseResult.getStatus());
        resultparams.put("merchant_id", responseResult.getMerchant_id());
        resultparams.put("result_code", responseResult.getResult_code());
        resultparams.put("nonce_str", responseResult.getNonce_str());
        resultparams.put("merchant_order_code", result);
        resultparams.put("sign", responseResult.getSign());
        //resultparams.put("pay_info", responseResult.getPay_info());
        
        
        return resultparams;
		
		
	}
	
	
	
	public Map<String, String> getSuccessResponseResult(String merchantId, String key, String result) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setMerchant_id(merchantId);
		responseResult.setSign_type("MD5");
		//responseResult.setSign(sign);
		responseResult.setNonce_str(String.valueOf(new Date().getTime()));
		responseResult.setResult_code("0");
		responseResult.setVersion("1.0");
		responseResult.setStatus("0");
		responseResult.setCharset("UTF-8");
		responseResult.setPay_info(result);
		
		Map<String, String>  signMap = new HashMap<String, String>();
		signMap.put("version", responseResult.getVersion());
		signMap.put("charset", responseResult.getCharset());
		signMap.put("sign_type", responseResult.getSign_type());
		signMap.put("status", responseResult.getStatus());	
		signMap.put("merchant_id", responseResult.getMerchant_id());
		signMap.put("result_code", responseResult.getResult_code());
		signMap.put("nonce_str", responseResult.getNonce_str());
		/*signMap.put("err_code", responseResult.getErr_code());
		signMap.put("err_msg", responseResult.getErr_msg());*/
		signMap.put("pay_info", responseResult.getPay_info());
		Map<String,String> params = SignUtils.paraFilter(signMap);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        responseResult.setSign(sign);
        
        
        Map<String, String>  resultparams = new HashMap<String, String>();
        resultparams.put("version", responseResult.getVersion());
        resultparams.put("charset", responseResult.getCharset());
        resultparams.put("sign_type", responseResult.getSign_type());
        resultparams.put("status", responseResult.getStatus());
        resultparams.put("merchant_id", responseResult.getMerchant_id());
        resultparams.put("result_code", responseResult.getResult_code());
        resultparams.put("nonce_str", responseResult.getNonce_str());
        resultparams.put("sign", responseResult.getSign());
        resultparams.put("pay_info", responseResult.getPay_info());
        
        
        return resultparams;
		
		
	}


//	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/gateway/channel/regi")
//	public Object registerIndex(){
//
//	}
	
	
}
