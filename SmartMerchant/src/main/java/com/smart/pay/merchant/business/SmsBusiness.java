package com.smart.pay.merchant.business;

import com.smart.pay.merchant.pojo.SmsRecord;

public interface SmsBusiness {

	
	
	/**创建一条短信记录**/
	public SmsRecord createSmsRecord(SmsRecord record);
	
	
	
	/**获取当前用户最新一条短信记录*/
	public SmsRecord queryLastestSmsByPhone(String phone);
	
}
