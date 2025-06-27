package com.smart.pay.channel.util.yzf;

import com.smart.pay.channel.util.hqx.HashMapConver;
import com.smart.pay.channel.util.hqx.HttpUtils;
import com.smart.pay.channel.util.hqx.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public abstract class YZFAbstractChannel {

	private static final Logger log = LoggerFactory.getLogger(YZFAbstractChannel.class.getSimpleName());

	//private final String merchno="sl2018080218221"; // 填写你们自己的商户号
	private final String platMerchant="shbyt2019071016"; // 填写你们自己的商户号
    //支付产品序列号
    private final String payKey="0100";
    //银⾏卡私密信息密⽂
    private final String secretContent="40000103";
    //签名
    private final String sign="40000103";


    private final String productType="40000103";

	public String transcode="050";

	public String postUrl="http://pay.huanqiuhuiju.com/authsys/api/large/channel/pay/execute.do";

	public String certType="";

	public YZFAbstractChannel(String transcode, String postUrl,String certType) {
		
		this.transcode = transcode;
		this.postUrl=postUrl;
		this.certType =certType;
	}

	public Map<String,String> allRequestMethod(Map<String,String> map){

//		if(map.get("transtype")!=null){
//			map.put("transcode","902");  //交易码
//		}else {
//			map.put("transcode","050");  //交易码
//		}

		map.put("outTradeNo",new Date().getTime()+""); //唯一值，交易唯一
		System.out.println("outTradeNo"+new Date().getTime()+"");

		map.put("platMerchant", platMerchant); //商户号

        map.put("payKey", payKey); //商户号

        Map orderbymap =  HashMapConver.getOrderByMap();
		
	    orderbymap.putAll(map);			
	    
	    byte[] response =  HashMapConver.getSign(orderbymap,sign);
	    
	    try {
			if(map.get("transtype")!=null){
			//postUrl="http://pay.huanqiuhuiju.com/authsys/api/channel/pay/execute.do";
			postUrl="http://pay.huanqiuhuiju.com/authsys/api/auth/execute.do";
			}else{
				postUrl="http://pay.huanqiuhuiju.com/authsys/api/channel/pay/execute.do";
			}
		
	      String result = HttpUtils.post(postUrl, response); //发送post请求

	      System.out.println("返回参数："+result);
	      
	      Map<String ,String> resultMap = JsonUtil.jsonToMap(result);
	      
	      return resultMap;
	      
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return null;
	    
	}
	public Map<String,Object> allRequestMethodquery(Map<String,String> map){

		if(map.get("transtype")!=null){
			map.put("transcode","902");  //交易码
		}else {
			map.put("transcode","050");  //交易码
		}

		map.put("ordersn",new Date().getTime()+""); //唯一值，交易唯一
		System.out.println("ordersn"+new Date().getTime()+"");


		map.put("platMerchant", platMerchant); //商户号

		Map orderbymap =  HashMapConver.getOrderByMap();

		orderbymap.putAll(map);

		byte[] response =  HashMapConver.getSign(orderbymap,sign);

		try {
			if(map.get("transtype")!=null){
				//postUrl="http://pay.huanqiuhuiju.com/authsys/api/channel/pay/execute.do";
				postUrl="http://pay.huanqiuhuiju.com/authsys/api/auth/execute.do";
			}else{
				postUrl="http://pay.huanqiuhuiju.com/authsys/api/channel/pay/execute.do";
			}

			String result = HttpUtils.post(postUrl, response); //发送post请求

			System.out.println("返回参数："+result);

			Map<String ,Object> resultMap = JsonUtil.jsonToMap(result);

			return resultMap;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
	
}
