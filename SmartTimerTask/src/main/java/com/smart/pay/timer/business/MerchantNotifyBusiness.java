package com.smart.pay.timer.business;

import com.smart.pay.timer.pojo.MerchantNotifyNextInterval;
import com.smart.pay.timer.pojo.MerchantNotifyRecord;

public interface MerchantNotifyBusiness {

	
	public MerchantNotifyRecord createMerchantNotifyRecord(MerchantNotifyRecord notifyRecord);
	
	
	public void callback();
	
	
	public MerchantNotifyNextInterval getNextInterval(int index);
	
}
