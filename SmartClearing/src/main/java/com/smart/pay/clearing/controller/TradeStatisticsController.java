package com.smart.pay.clearing.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.smart.pay.clearing.business.MerchantTradeStatisticsBusiness;
import com.smart.pay.clearing.business.OrderBusiness;
import com.smart.pay.clearing.pojo.EquipmentTrxStatistics;
import com.smart.pay.clearing.pojo.MerchantDayStatistics;
import com.smart.pay.clearing.pojo.MerchantMonthStatistics;
import com.smart.pay.clearing.pojo.TradeStatisResult;
import com.smart.pay.clearing.util.Util;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.ResultWrap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
public class TradeStatisticsController {

	private static final Logger    LOG = LoggerFactory.getLogger(TradeStatisticsController.class); 
	
	
	@Autowired
	private MerchantTradeStatisticsBusiness merTradeStatisticsBusiness;
	
	@Autowired
	private OrderBusiness orderBusiness;
	
	
	@Autowired
	private Util util;
	
	
	/***获取当前tag，日期的交易量*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/equipment/trxamount")
    public @ResponseBody Object findEquipmentAmountByTagAndDate(HttpServletRequest request, //
    		@RequestParam(value = "equipment_tag") String equipmentTag, //
            @RequestParam(value = "trx_date") String trxDate  
            ) throws Exception{
		
		EquipmentTrxStatistics  equipmentTrxStatistics = merTradeStatisticsBusiness.queryEquipmentTrxStatistics(equipmentTag, trxDate);
		
		 return ResultWrap.init(Constss.SUCCESS, "成功", equipmentTrxStatistics);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/equipment/statis/day/update")
    public @ResponseBody Object updateEquipmentDayTradeStatistics(HttpServletRequest request //
            ) throws Exception{
		LOG.info("交易统计开始");
		URI uri = util.getServiceUrl("smartchannel", "error url request!");
        String url = uri.toString() + "/smartchannel/mobile/equipment/all";
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, requestEntity, String.class);
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONArray  jsonArray  = jsonObject.getJSONArray("result");
		
		/**昨天的开始时间*/
		Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
		
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
		
		/**昨天的结束时间**/
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.add(Calendar.DATE, 1);
        endcalendar.set(Calendar.HOUR_OF_DAY, 0);
        endcalendar.set(Calendar.MINUTE, 0);
        endcalendar.set(Calendar.SECOND, 0);
        Date endTime = endcalendar.getTime();
        
        
		
		for(int i=0; i<jsonArray.size(); i++) {
			
			JSONObject equipmentObj = jsonArray.getJSONObject(i);
			
			String equipmentTag = equipmentObj.getString("equipmentTag");
			BigDecimal totalAmount = orderBusiness.querySumAmountByEquipment(equipmentTag, "0", startTime, endTime);
			
			/**判断交易量**/
			EquipmentTrxStatistics equipmentTrxStatistics = merTradeStatisticsBusiness.queryEquipmentTrxStatistics(equipmentTag, simpleDateFormat.format(startTime));
			if(equipmentTrxStatistics != null) {
				equipmentTrxStatistics.setTotalTradeAmt(totalAmount == null ? BigDecimal.ZERO : totalAmount);
				merTradeStatisticsBusiness.createEquipmentTrxStatistics(equipmentTrxStatistics);
			}else {
				equipmentTrxStatistics = new EquipmentTrxStatistics();
				
				equipmentTrxStatistics.setEquipmentTag(equipmentTag);
				equipmentTrxStatistics.setTradeDate(simpleDateFormat.format(startTime));
				equipmentTrxStatistics.setTotalTradeAmt(totalAmount == null ? BigDecimal.ZERO : totalAmount);
				merTradeStatisticsBusiness.createEquipmentTrxStatistics(equipmentTrxStatistics);
			}
			
			
			
			
		}
		
		LOG.info("交易统计结束");
		return null;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/statis/day/update")
    public @ResponseBody Object updateMerchantDayTradeStatistics(HttpServletRequest request //
            ) throws Exception{
		
		try {
			URI uri = util.getServiceUrl("smartmerchant", "error url request!");
	        String url = uri.toString() + "/smartmerchant/merchant/allauthmerchant";
	        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.postForObject(url, requestEntity, String.class);
			JSONObject jsonObject = JSONObject.fromObject(result);
			JSONArray  jsonArray  = jsonObject.getJSONArray("result");
			
			
			/**昨天的开始时间*/
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        Date startTime = calendar.getTime();
			
			
			/**昨天的结束时间**/
	        Calendar endcalendar = Calendar.getInstance();
	        endcalendar.set(Calendar.HOUR_OF_DAY, 0);
	        endcalendar.set(Calendar.MINUTE, 0);
	        endcalendar.set(Calendar.SECOND, 0);
	        Date endTime = endcalendar.getTime();
	        
	        
			
			for(int i=0; i<jsonArray.size(); i++) {
				
				JSONObject merchantObj = jsonArray.getJSONObject(i);
				
				String merchantid = merchantObj.getString("merchantId");
				
				
				
				BigDecimal totalAmount = orderBusiness.querySumAmountByMerchantid(merchantid, "0", "", startTime, endTime);
				
				MerchantDayStatistics merchantDayStatistics = new MerchantDayStatistics();
				
				merchantDayStatistics.setMerchantId(merchantid);
				merchantDayStatistics.setTradeDate(startTime);
				merchantDayStatistics.setStatisTime(new Date());
				merchantDayStatistics.setTotalTradeAmt(totalAmount == null ? BigDecimal.ZERO : totalAmount);
				merTradeStatisticsBusiness.createMerchantDayStatistics(merchantDayStatistics);
				
				
			}
			LOG.info("日统计成功");
		}catch(Exception e) {
			
			e.printStackTrace();
			LOG.info("日统计失败");
		}
		return null;
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/statis/month/update")
    public @ResponseBody Object updateMerchantMonthTradeStatistics(HttpServletRequest request //
            ) throws Exception{
		
		try {
			
			URI uri = util.getServiceUrl("smartmerchant", "error url request!");
	        String url = uri.toString() + "/smartmerchant/merchant/allauthmerchant";
	        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.postForObject(url, requestEntity, String.class);
			JSONObject jsonObject = JSONObject.fromObject(result);
			JSONArray  jsonArray  = jsonObject.getJSONArray("result");
			
			
			/**昨天的开始时间*/
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        Date startTime = calendar.getTime();
			
			
			/**昨天的结束时间**/
	        Calendar endcalendar = Calendar.getInstance();
	        endcalendar.set(Calendar.HOUR_OF_DAY, 0);
	        endcalendar.set(Calendar.MINUTE, 0);
	        endcalendar.set(Calendar.SECOND, 0);
	        Date endTime = endcalendar.getTime();
	        
	        
			
			for(int i=0; i<jsonArray.size(); i++) {
				
				JSONObject merchantObj = jsonArray.getJSONObject(i);
				
				String merchantid = merchantObj.getString("merchantId");
				
				
				
				BigDecimal totalAmount = orderBusiness.querySumAmountByMerchantid(merchantid, "0", "", startTime, endTime);
				
				MerchantMonthStatistics merchantMonthStatistics = new MerchantMonthStatistics();
				
				merchantMonthStatistics.setMerchantId(merchantid);
				merchantMonthStatistics.setYear(calendar.get(Calendar.YEAR));
				merchantMonthStatistics.setMonth(calendar.get(Calendar.MONTH)+1);
				merchantMonthStatistics.setStatisTime(new Date());
				merchantMonthStatistics.setTotalTradeAmt(totalAmount == null ? BigDecimal.ZERO : totalAmount);
				merTradeStatisticsBusiness.createMerchantMonthStatistics(merchantMonthStatistics);
				
				
			}
			LOG.info("月统计成功");
		}catch(Exception e) {
			
			e.printStackTrace();
			LOG.info("月统计失败");
		}
		return null;
		
		
	}
	
	
	
	/**
	 * 
	 * 统计过去的走势
	 * statisType 为  0 按天统计
	 * peroid 当statisType 为0 ，表示统计的天数
	 * 
	 * statisType 为1 表示按月统计
	 * 
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/statis/lastest")
    public @ResponseBody Object queryMerchantTradeStatistics(HttpServletRequest request, //
            @RequestParam(value = "merchant_id") String merchantId, //
            @RequestParam(value = "statistics_type") String statisType        
            ) throws Exception{
		
		List<TradeStatisResult>  tradeStatisResult = new ArrayList<TradeStatisResult>();
		
		if(statisType.equalsIgnoreCase("0")) {  //按天统计
			
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, -1);
			Date resultDate = ca.getTime(); // 结果  
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			String endDatestr = sdf.format(resultDate);  
			
			Date endDate = sdf.parse(endDatestr);
			
			Calendar ca1 = Calendar.getInstance();
			ca1.add(Calendar.DATE, -30);
			Date startResultDate = ca1.getTime(); // 结果  
			String startDatestr = sdf.format(startResultDate);  
			
			Date startDate = sdf.parse(startDatestr);
			
			
			List<MerchantDayStatistics> merchantDayStatistics  = merTradeStatisticsBusiness.queryMerchantDayStatisticss(merchantId, startDate, endDate);
			SimpleDateFormat ddMM  = new SimpleDateFormat("dd/MM");
			for(MerchantDayStatistics  statis : merchantDayStatistics) {
				
				TradeStatisResult  tradeStatisResultTmp = new TradeStatisResult();
				tradeStatisResultTmp.setMerchantId(statis.getMerchantId());
				tradeStatisResultTmp.setTradeAmount(statis.getTotalTradeAmt());
				tradeStatisResultTmp.setStatisTime(ddMM.format(statis.getTradeDate()));
				tradeStatisResult.add(tradeStatisResultTmp);
				
			}
			
			
		}else {
			
			Calendar c = Calendar.getInstance();
			
			int year = c.get(Calendar.YEAR);
			
			int month = c.get(Calendar.MONTH) + 1;
			
			
			int startMonth = 1;
			int endMonth  = 0;
			if((month -1) <=0 ) {
				endMonth = 1;
			}else {
				endMonth = (month -1);
			}
			
			
			List<MerchantMonthStatistics> merchantMonthStatistics = merTradeStatisticsBusiness.queryMerchantMonthStatisticss(merchantId, year, startMonth, endMonth);
			for(MerchantMonthStatistics  statis : merchantMonthStatistics) {
				
				TradeStatisResult  tradeStatisResultTmp = new TradeStatisResult();
				tradeStatisResultTmp.setMerchantId(statis.getMerchantId());
				tradeStatisResultTmp.setTradeAmount(statis.getTotalTradeAmt());
				tradeStatisResultTmp.setStatisTime(statis.getYear()+"/"+statis.getMonth());
				tradeStatisResult.add(tradeStatisResultTmp);
				
			}
		} 
		
		
		 return ResultWrap.init(Constss.SUCCESS, "成功", tradeStatisResult);
		
	}
	
}
