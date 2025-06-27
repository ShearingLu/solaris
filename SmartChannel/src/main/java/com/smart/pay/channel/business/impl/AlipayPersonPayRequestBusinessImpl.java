package com.smart.pay.channel.business.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.channel.business.MobileManageBusiness;
import com.smart.pay.channel.business.PaymentRequestBusiness;
import com.smart.pay.channel.pojo.MobileEquipment;
import com.smart.pay.channel.pojo.PaymentOrder;
import com.smart.pay.channel.pojo.PersonalURL;
import com.smart.pay.channel.util.ChannelConstss;
import com.smart.pay.channel.util.HttpApacheClientUtil;
import com.smart.pay.channel.util.LotteryUtil;
import com.smart.pay.channel.util.Util;
import com.smart.pay.channel.util.qrcode.QrCodeUtil;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;

import net.sf.json.JSONObject;

@Service
public class AlipayPersonPayRequestBusinessImpl extends BasePaymentRequest implements PaymentRequestBusiness {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MobileManageBusiness  mobileManageBusiness;
	
	@Value("${callback.ipAddress}")
	private String ipAddress;
	
	@Autowired
	private Util util;
	
	@Override
	public Map<String, Object> paymentRequest(Map<String, Object> params) {
		
		PaymentOrder paymentOrder = (PaymentOrder) params.get("paymentOrder");
		
		/**获取一个可用手机**/
		List<MobileEquipment>   mobileEquipments = mobileManageBusiness.queryCanUseMobileEquipment();
		if(mobileEquipments == null){   //暂时无可用设备
			LOG.info("暂时没有可用设备11");
			return ResultWrap.init(Constss.FALIED, "服务器繁忙，稍后重试", ipAddress+"/smartchannel/personal/sysbusy");
		
		}else {
			
			//给设备发送推送消息。  并将该设备状态置为已被暂用
			String responseURL = "";
			//
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<MobileEquipment>  allCanUseEquipments  = new ArrayList<MobileEquipment>();
			
			
			for(MobileEquipment mobileEquipmenttmp :mobileEquipments) {
				//mobileEquipment = mobileEquipmenttmp;
				BigDecimal dayLimit = mobileEquipmenttmp.getDaylimit();
				/**判断当前的交易量， 如果超出了最大交易量， 目前最多是10万一天*/
				URI uri = util.getServiceUrl("smartclearing", "error url request!");
		        String equipmenturl = uri.toString() + "/smartclearing/equipment/trxamount";
		        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("trx_date", sdf.format(new Date()));
		        requestEntity.add("equipment_tag", mobileEquipmenttmp.getEquipmentTag());
		        String result = new RestTemplate().postForObject(equipmenturl, requestEntity, String.class);
				JSONObject resultObj = JSONObject.fromObject(result);
				
				JSONObject resultJson  = resultObj.getJSONObject("result");
				
				if(!resultJson.isNullObject()  && dayLimit != null) {
				
			        String totalAmountAmout = resultJson.getString("totalTradeAmt");
			        LOG.info(mobileEquipmenttmp.getEquipmentTag()+"设备交易的交易量为"+totalAmountAmout);
			        
			        if(new BigDecimal(totalAmountAmout).compareTo(dayLimit) > 0) {
			        	continue;
			        }else {
			        	allCanUseEquipments.add(mobileEquipmenttmp);
			        }
				}else {
					allCanUseEquipments.add(mobileEquipmenttmp);
				}
				
				/*String url = mobileEquipmenttmp.getMobileURL();
				String reqURL = "/getpay?money="+paymentOrder.getAmount()+"&mark="+paymentOrder.getOrderCode()+"&type=alipay";
				try {
					responseURL = HttpApacheClientUtil.httpGetRequest(url+""+reqURL);
					break;
				}catch(Exception e) {
					LOG.info("设备链接异常........."+e.getMessage());
					*//**直接标记当前设备异常**//*
					mobileEquipment.setEnable("0");
					mobileEquipment.setExtra1("当前设备异常");
					mobileManageBusiness.createOrUpdate(mobileEquipment);
					continue;
				}*/
			}

			
			if(allCanUseEquipments == null || allCanUseEquipments.size() == 0){   //暂时无可用设备
				LOG.info("暂时没有可用设备22");
				return ResultWrap.init(Constss.FALIED, "服务器繁忙，稍后重试", ipAddress+"/smartchannel/personal/sysbusy");
			}
			
			MobileEquipment  mobileEquipment  = null;
			if(allCanUseEquipments.size() == 1) {
				mobileEquipment = allCanUseEquipments.get(0);
			}else {
				int x=0;
				Map<Integer, Object>  indexEquipments  = new HashMap<Integer, Object>();
				List<Double> indexs = new ArrayList<Double>();  
				for(MobileEquipment tempEquipment : allCanUseEquipments) {
					
					indexs.add(new Double(tempEquipment.getRatio()));
					indexEquipments.put(x, tempEquipment);
					x++;
				}
				
				LotteryUtil ll = new LotteryUtil(indexs);  
				int index = ll.randomColunmIndex();  
				if(index < 0) {
					return ResultWrap.init(Constss.FALIED, "服务器繁忙，稍后重试", ipAddress+"/smartchannel/personal/sysbusy");
				}	
				mobileEquipment = (MobileEquipment)indexEquipments.get(index);
			}
			
			
			String reqURL = "/getpay?money="+paymentOrder.getAmount()+"&mark="+paymentOrder.getOrderCode()+"&type=alipay";
			try {
				responseURL = HttpApacheClientUtil.httpGetRequest(mobileEquipment.getMobileURL()+""+reqURL);
			}catch(Exception e) {
				LOG.info("设备链接异常........."+e.getMessage());
				/**直接标记当前设备异常**/
				mobileEquipment.setEnable("0");
				mobileEquipment.setExtra1("当前设备异常");
				mobileManageBusiness.createOrUpdate(mobileEquipment);
			}
			
			
			
			JSONObject jsonResult  = JSONObject.fromObject(responseURL);
			if(jsonResult.containsKey("payurl")) {
				mobileEquipment.setTakeupStatus("1");  //表示该设备已经被占用
				Calendar cal = Calendar.getInstance();
				mobileEquipment.setTakeupTime(cal.getTime());
				cal.add(Calendar.MINUTE, 1);
				mobileEquipment.setReleaseTime(cal.getTime());
				mobileManageBusiness.createOrUpdate(mobileEquipment);
				String payurl = jsonResult.getString("payurl");
				
				
				/***将手机的设备号更新到订单表里面*/
				URI uri = util.getServiceUrl("smartclearing", "error url request!");
		        String url = uri.toString() + "/smartclearing/order/update/equipmenttag";
		        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
		        requestEntity.add("order_code", paymentOrder.getOrderCode());
		        requestEntity.add("equipment_tag", mobileEquipment.getEquipmentTag());
		        new RestTemplate().postForObject(url, requestEntity, String.class);
				
				
				long curTime = System.currentTimeMillis();
				String sign = RandomUtils.generateNumString(15)+"";
				
				PersonalURL personalURL = new PersonalURL();
				personalURL.setPersonalURL(payurl);
				personalURL.setSign(sign);
				personalURL.setStartTime(curTime+"");
				personalURL.setIsUse("0");
				mobileManageBusiness.savePersonalURL(personalURL);
				payurl = ipAddress+"/smartchannel/personal/request?trx_url="+payurl+"&sign="+sign;
		        
				
				String tmpPayurl = payurl;
				if (ChannelConstss.ALI_PERSONAL_CODE.equalsIgnoreCase(paymentOrder.getRealChannelCode())) {
					payurl  = QrCodeUtil.qrOSSCodeEncode(paymentOrder.getOrderCode() + ".png", tmpPayurl);
				}
				
				
				
				return ResultWrap.init("000000", "成功", payurl);
			}else {
				return ResultWrap.init(Constss.FALIED, "服务器繁忙，稍后重试", ipAddress+"/smartchannel/personal/sysbusy");
				
			}
		}
		
	}

}
