package com.smart.pay.timer.business.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smart.pay.timer.business.MerchantNotifyBusiness;
import com.smart.pay.timer.pojo.MerchantNotifyNextInterval;
import com.smart.pay.timer.pojo.MerchantNotifyRecord;
import com.smart.pay.timer.repository.MerchantNotifyNextIntervalRepository;
import com.smart.pay.timer.repository.MerchantNotifyRecordRepository;
import com.smart.pay.timer.util.NoticeConstants;

@Service
public class MerchantNotifyBusinessImpl implements MerchantNotifyBusiness{

	private static final Logger          LOG = LoggerFactory.getLogger(MerchantNotifyBusinessImpl.class);
	
	@Autowired
	private MerchantNotifyRecordRepository merchantNotifyRecordRepository;
	
	
	@Autowired
	private MerchantNotifyNextIntervalRepository httpNotifyIntervalRepository;
	
	@Transactional
	@Override
	public MerchantNotifyRecord createMerchantNotifyRecord(MerchantNotifyRecord notifyRecord) {
		
		return merchantNotifyRecordRepository.saveAndFlush(notifyRecord);
	}

	
	 @Override
	 public MerchantNotifyNextInterval getNextInterval(int index) {
	        return httpNotifyIntervalRepository.findHttpNotifyInterval(index);
	 }

	@Override
	public void callback() {
		 List<MerchantNotifyRecord> httpNotifys = merchantNotifyRecordRepository.findHttpNotifys(new Date());
	        for (MerchantNotifyRecord notify : httpNotifys) {
	        	LOG.info("start notify" + notify.getNotifyURL());
	            callBacking(notify);
	        }
		
	}
	
	
	@Async("myAsync")
    @Transactional
    public void callBacking(MerchantNotifyRecord notify) {

		HttpHeaders headers = new HttpHeaders();
	    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
	    headers.setContentType(type);
	    HttpEntity<String> entity = new HttpEntity<String>(notify.getParams(),headers);
	    try {
		    String notifyresult = new RestTemplate().postForObject(notify.getNotifyURL(), entity, String.class);
	
			if(notifyresult != null && !notifyresult.equalsIgnoreCase("success")) {
				LOG.info("responseCode ....................." + notifyresult);
	
	
	            /** 更新下一次的时间 */
	        	Calendar cal = Calendar.getInstance();
	            cal.setTime(notify.getNextCallTime());
	            MerchantNotifyNextInterval notifyInterval = getNextInterval(NoticeConstants.MAX_NOTIFY_COUNT - notify.getRemainCnt() + 1);
	            /** 减少次数 */
	            notify.setRemainCnt(notify.getRemainCnt() - 1);
	            cal.add(Calendar.MINUTE, notifyInterval.getIntervalTime());
	            notify.setNextCallTime(cal.getTime());
	
	        } else {
	            notify.setStatus(NoticeConstants.CALL_SUCCESS);
	        }
			
			
			 createMerchantNotifyRecord(notify);
	
			
			 
	    }catch(Exception e) {
	    	
	    	LOG.info("notifyException....................." + e.getMessage());
	    	
	    	/** 更新下一次的时间 */
        	Calendar cal = Calendar.getInstance();
            cal.setTime(notify.getNextCallTime());
            MerchantNotifyNextInterval notifyInterval = getNextInterval(NoticeConstants.MAX_NOTIFY_COUNT - notify.getRemainCnt() + 1);
            /** 减少次数 */
            notify.setRemainCnt(notify.getRemainCnt() - 1);
            cal.add(Calendar.MINUTE, notifyInterval.getIntervalTime());
            notify.setNextCallTime(cal.getTime());
	    	
            createMerchantNotifyRecord(notify);
	    	
	    }
	    
	    
	   
	    
	    
	    
	    
	}
	
	
	
	
	

}
