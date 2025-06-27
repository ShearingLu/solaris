package com.smart.pay.channel.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.pay.channel.business.MobileManageBusiness;
import com.smart.pay.channel.business.impl.BasePaymentRequest;
import com.smart.pay.channel.pojo.MobileEquipment;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;

@Controller
@EnableAutoConfiguration
public class MobileEquipmentManageController extends BasePaymentRequest {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	
	@Autowired
	private MobileManageBusiness  mobileManageBusiness;
	
	
	/**新增一个设备信息*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/create")
    public @ResponseBody Object createOrUpdateMobileEquipment(HttpServletRequest request,
    		@RequestParam(value="equipment_tag")String equipmentTag,
    		@RequestParam(value="brand", required=false)String brand,
    		@RequestParam(value="sys_version", required=false)String sysVersion,
    		@RequestParam(value="mobile_url", required=false)String mobileurl,
    		@RequestParam(value="extra_1", required=false)String extra1,
    		@RequestParam(value="extra_2", required=false)String extra2,
    		@RequestParam(value="extra_3", required=false)String extra3,
    		@RequestParam(value="extra_4", required=false)String extra4,
    		@RequestParam(value="extra_5", required=false)String extra5
    		) {
 		
		MobileEquipment  mobileEquipment = new MobileEquipment();
		
		mobileEquipment   = mobileManageBusiness.queryMobileEquipmentByTag(equipmentTag);
		if(mobileEquipment == null) {
			
			mobileEquipment  = new MobileEquipment();
			mobileEquipment.setBrand(brand);
			mobileEquipment.setCreateTime(new Date());
			mobileEquipment.setEnable("1");  //默认可用
			mobileEquipment.setEquipmentNo(RandomUtils.generateString(6));
			mobileEquipment.setEquipmentTag(equipmentTag);
			mobileEquipment.setMobileURL(mobileurl);
			mobileEquipment.setExtra1(extra1);
			mobileEquipment.setExtra2(extra2);
			mobileEquipment.setExtra3(extra3);
			mobileEquipment.setExtra4(extra4);
			mobileEquipment.setExtra5(extra5);
			mobileEquipment.setUpdateTime(new Date());
			mobileEquipment.setSysVersion(sysVersion);
			mobileEquipment.setTakeupStatus("0");
			mobileEquipment  = mobileManageBusiness.createOrUpdate(mobileEquipment);
		}else {
			mobileEquipment.setBrand(brand);
			mobileEquipment.setEnable("1");  //默认可用
			mobileEquipment.setEquipmentTag(equipmentTag);
			mobileEquipment.setExtra1(extra1);
			mobileEquipment.setExtra2(extra2);
			mobileEquipment.setExtra3(extra3);
			mobileEquipment.setExtra4(extra4);
			mobileEquipment.setMobileURL(mobileurl);
			mobileEquipment.setExtra5(extra5);
			mobileEquipment.setUpdateTime(new Date());
			mobileEquipment.setSysVersion(sysVersion);
			mobileEquipment  = mobileManageBusiness.createOrUpdate(mobileEquipment);
		}
		
		return ResultWrap.init("000000", "成功", mobileEquipment);
 	}
	
	
	/**根据设备的no获取一个设备信息**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/query/no")
    public @ResponseBody Object queryMobileEquipmentByNo(HttpServletRequest request,
    		@RequestParam(value="equipment_tag")String equipmentTag) {
		MobileEquipment  mobileEquipment = mobileManageBusiness.queryMobileEquipmentByTag(equipmentTag);
		
		return ResultWrap.init("000000", "成功", mobileEquipment);
 	}
	
	
	/**分页查询所有设备 **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/page/query")
    public @ResponseBody Object pageMobileEquipment(HttpServletRequest request,
    		 @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
             @RequestParam(value = "size", defaultValue = "20", required = false) int size,
             @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
             @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) {
 		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		Page<MobileEquipment>   pageResult = mobileManageBusiness.queryAllMobileEquipments(pageable);
		return ResultWrap.init("000000", "成功", pageResult);
 	}
	
	/**分页查询所有设备 **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/equipment/all")
    public @ResponseBody Object allMobileEquipment(HttpServletRequest request) {
 		
		List<MobileEquipment>   mobileEquipments = mobileManageBusiness.queryAllOnlineEquipment();
		return ResultWrap.init("000000", "成功", mobileEquipments);
 	}
	
	/**使用一个设备发起支付**/
	/*@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/payment/req")
    public @ResponseBody Object paymentMobileEquipment(HttpServletRequest request) {
		
		*//**获取一个有用设备**//*
		MobileEquipment   mobileEquipment = mobileManageBusiness.queryCanUseMobileEquipment();
		if(mobileEquipment == null){   //暂时无可用设备
			return ResultWrap.init("999999", "暂无可用通道", null);
		}else {
			
			//给设备发送推送消息。  并将该设备状态置为已被暂用
			String url = mobileEquipment.getMobileURL();
			
			//String reqURL = "/getpay?money="++"&mark="++"&type=alipay";
			
			mobileEquipment.setTakeupStatus("1");  //表示该设备已经被占用
			Calendar cal = Calendar.getInstance();
			mobileEquipment.setTakeupTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			mobileEquipment.setReleaseTime(cal.getTime());
			mobileManageBusiness.createOrUpdate(mobileEquipment);
			
			return ResultWrap.init("000000", "成功", url);
		}
 	}*/
	
	/***将当前已经失效的设备直接职位可用*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/status/update")
    public @ResponseBody Object paymentMobileEquipment(HttpServletRequest request) {
		
		LOG.info("将手机的状态复位开始");
		List<MobileEquipment>   mobileEquipments = mobileManageBusiness.queryExpiredEquipment();
		for(MobileEquipment mobileEquipment : mobileEquipments) {
			
			mobileEquipment.setTakeupStatus("0");  //表示该设备已经被释放
			mobileEquipment.setTakeupTime(null);
			mobileEquipment.setReleaseTime(null);
			mobileManageBusiness.createOrUpdate(mobileEquipment);
			
			//return ResultWrap.init("000000", "成功", url);
		}
		LOG.info("手机的状态复位成功");

		
		return null;
 	}
	
	
	
	
	/**设备回调服务器传递信息**/
	/*@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/payment/response")
    public @ResponseBody Object responseMobileEquipment(HttpServletRequest request,
    		@RequestParam(value="resp_url")String respURL,
    		@RequestParam(value="mobile_tag")String mobiletag
    		) {
 		
		
		
		
		
 		return null;
 	}*/
	
	
	
	/**释放一个设备*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/payment/release")
    public @ResponseBody Object releaseMobileEquipment(HttpServletRequest request,
    		@RequestParam(value="equipment_tag")String equipmenttag
    		) {
 		
		MobileEquipment  mobileEquipment = mobileManageBusiness.queryMobileEquipmentByTag(equipmenttag);
		
		mobileEquipment.setTakeupStatus("0");
		mobileEquipment.setReleaseTime(null);
		mobileEquipment.setTakeupTime(null);
		mobileManageBusiness.createOrUpdate(mobileEquipment);
		return ResultWrap.init("000000", "成功");
 	}
	
	/***通知系统该设备异常**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/equipment/error")
    public @ResponseBody Object notifyMobileEquipmentError(HttpServletRequest request,
    		@RequestParam(value="equipment_tag")String equipmenttag,
    		@RequestParam(value="error_msg")String errormsg
    		) {
 		
		MobileEquipment  mobileEquipment = mobileManageBusiness.queryMobileEquipmentByTag(equipmenttag);
		mobileEquipment.setEnable("0");
		mobileEquipment.setExtra1(errormsg);
		mobileManageBusiness.createOrUpdate(mobileEquipment);
		return ResultWrap.init("000000", "成功");
 	}
	
	
	
	/**删除一个设备*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartchannel/mobile/payment/del")
    public @ResponseBody Object delMobileEquipment(HttpServletRequest request,
    		@RequestParam(value="equipment_tag")String equipmentTAG
    		) {
 		
		
		MobileEquipment  mobileEquipment = mobileManageBusiness.queryMobileEquipmentByTag(equipmentTAG);
		mobileEquipment.setEnable("0");
		mobileEquipment.setUpdateTime(new Date());
		mobileEquipment = mobileManageBusiness.createOrUpdate(mobileEquipment);
		
		return ResultWrap.init("000000", "成功", mobileEquipment);
 	}
}
