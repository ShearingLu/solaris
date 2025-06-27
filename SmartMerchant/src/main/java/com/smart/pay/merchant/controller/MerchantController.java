package com.smart.pay.merchant.controller;

import com.smart.pay.common.tools.*;
import com.smart.pay.merchant.business.*;
import com.smart.pay.merchant.pojo.*;
import com.smart.pay.merchant.util.SmsUtil;
import com.smart.pay.merchant.util.Util;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.*;

@Controller
@EnableAutoConfiguration
public class MerchantController {
	private static final Logger LOG = LoggerFactory.getLogger(MerchantController.class);
	
	@Autowired
	private MerchantManageBusiness manageBusiness;
	
	
	@Autowired
	private MerchantSecurityBusiness  merchantSecurityBusiness;
	
	
	@Autowired
	private SmsBusiness smsBusiness;
	
	@Autowired
	private Util util;
	
	@Autowired
	private MerchantUserInfoBusiness merchantUserInfoBusiness;

	@Autowired
	private MerchantUserBindCardBusiness merchantUserBindCardBusiness;

	@RequestMapping(method = RequestMethod.GET, value = "/smartmerchant/merchant/monitor")
	public @ResponseBody Object monitor(HttpServletRequest request) {
		return "success";
		
	}
	
	
    @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/login")
	public @ResponseBody Object loginSys(HttpServletRequest request, @RequestParam("login_id") String loginId
			, @RequestParam("login_pass") String loginPass) {



		LOG.info("login_id======"+loginId);
		LOG.info("login_pass======"+loginPass);
    	
		Merchant merchant = manageBusiness.queryMerchantByLoginid(loginId, Md5Util.getMD5(loginPass));
		MerchantInfo merchantInfo=new MerchantInfo();
		
		if(merchant != null) {
			
			URI uri = util.getServiceUrl("smartrisk", "error url request!");
	        String url = uri.toString() + "/smartrisk/merchantblacklist/merchantid";
	        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
	        requestEntity.add("merchant_id", merchant.getMerchantId());
	        requestEntity.add("risk_type", "1");// 0 标识没有交易权限 1表示没有登录权限 2表示没有提现权限 
	        String result = new RestTemplate().postForObject(url, requestEntity, String.class);
	        JSONObject resultObj = JSONObject.fromObject(result);
	        JSONObject resultObject= resultObj.getJSONObject("result");
			if(!resultObject.isEmpty()) {
				return ResultWrap.init(Constss.FALIED, "没有登录权限", "");
			}
			
			
			
			String token = TokenUtil.createToken(merchant.getMerchantId());
			BeanUtils.copyProperties(merchant,merchantInfo);
			merchantInfo.setToken(token);
			merchantInfo.setPayPass(null);
			merchantInfo.setLoginPass(null);
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantInfo);
		}else {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
		
		
		
	}
	
    
    @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/updatesettleperoid")
   	public @ResponseBody Object updateMerchantSettlePeroid(HttpServletRequest request, @RequestParam("merchant_id") String merchantid, @RequestParam("settle_peroid") String settleperoid) {
   		
    	Merchant merchant = manageBusiness.queryMerchantById(merchantid);
   		
   		merchant.setSettlePeroid(Integer.parseInt(settleperoid));
   		try {
   			manageBusiness.updateMerchant(merchant);
   			return ResultWrap.init(Constss.SUCCESS, "成功", merchant);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "更新失败", null);
		}
    }
    
	
    @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/info")
	public @ResponseBody Object getMerchantInfo(HttpServletRequest request, @RequestParam("merchant_id") String merchantid, @RequestParam("key_type") String keyType) {
		
		Merchant merchant = manageBusiness.queryMerchantById(merchantid);
		
		MerchantKey  merchantKey = merchantSecurityBusiness.queryMerchantKey(merchantid, keyType);
		
		
		
		
		if(merchant != null && merchantKey != null) {
			
			MerchantInfo merchantInfo  = new MerchantInfo();
			merchantInfo.setContactEmail(merchant.getContactEmail());
			merchantInfo.setContactPhone(merchant.getContactPhone());
			merchantInfo.setIdentity(merchant.getIdentity());
			merchantInfo.setKey(merchantKey.getKey());
			merchantInfo.setKeyType(merchantKey.getKeyType());
			merchantInfo.setLoginId(merchant.getLoginId());
			merchantInfo.setLoginPass(merchant.getLoginPass());
			merchantInfo.setMerAuthStatus(merchant.getMerAuthStatus());
			merchantInfo.setMerBusinessLicense(merchant.getMerBusinessLicense());
			merchantInfo.setMerchantId(merchant.getMerchantId());
			merchantInfo.setMerchantName(merchant.getMerchantName());
			merchantInfo.setParent(merchant.getParent());
			merchantInfo.setPayPass(merchant.getPayPass());
			merchantInfo.setPubKey(merchantKey.getPubKey());
			
			
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantInfo);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户不存在或信息不完整", null);
		}
	}
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/allauthmerchant")
	public @ResponseBody Object getMerchantAllAuthMerchant(HttpServletRequest request) {
    	
    	List<Merchant> merchants = manageBusiness.allAuthMerchant();
    	
    	return ResultWrap.init(Constss.SUCCESS, "成功", merchants);
    }
    
    
	/***根据商户编号获取子商户信息**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/parent/id")
	public @ResponseBody Object getMerchantByparent(HttpServletRequest request,
		   //上级商户号
		   @RequestParam("parent_id") String parentId,
		   //商户编号
		   @RequestParam(value = "merchant_id",required = false) String merchantId,
		   
		   @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
           @RequestParam(value = "size", defaultValue = "20", required = false) int size,
           @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
           @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) {
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		
		Page<Merchant> merchants = manageBusiness.queryMerchantByParentId(parentId, merchantId,pageable);
		
		
		if(merchants != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchants);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "暂无商户数据", "");
		}
	}
	
	
	/***根据商户编号获取子商户信息**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/platform/all")
	public @ResponseBody Object getAllMerchantByparent(HttpServletRequest request,
		//商户编号
			@RequestParam(value = "merchant_id",required = false) String merchantId,
			@RequestParam(value = "mer_auth_status",required = false) String merAuthStatus,
		   @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
           @RequestParam(value = "size", defaultValue = "20", required = false) int size,
           @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
           @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) {
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		
		Page<Merchant> merchants = manageBusiness.queryAllMerchants(merchantId,merAuthStatus,pageable);
		
		if(merchants != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchants);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "暂无商户数据", "");
		}
	}
	
	 
		/***根据商户的信息获取商户的基本信息**/
		@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/id")
		public @ResponseBody Object getMerchant(HttpServletRequest request, @RequestParam("merchant_id") String merchantid) {
			
			Merchant merchant = manageBusiness.queryMerchantById(merchantid);
			
			
			if(merchant != null) {
				return ResultWrap.init(Constss.SUCCESS, "成功", merchant);
			}else {
				return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户不存在", "");
			}
		}
	
	
	/**认证商户*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/auth")
	public @ResponseBody Object authMerchant(HttpServletRequest request, @RequestParam("merchant_id") String merchantid
			, @RequestParam(value="auth_status", defaultValue="1") String authstatus) {
		

		try {
			Merchant merchant = manageBusiness.queryMerchantById(merchantid);
			merchant.setMerAuthStatus(authstatus);
			
			
			
			manageBusiness.updateMerchant(merchant);
		
			return ResultWrap.init(Constss.SUCCESS, "认证成功", merchant);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "认证失败--"+e.getMessage(), "");
			
		}
		
	}
		
	/**更新商户的密码*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/updatepass")
	public @ResponseBody Object updateMerchantPass(HttpServletRequest request, 
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="old_pass") String oldpass
			, @RequestParam(value="new_pass") String newpass) {
		
		Merchant tmpMerchant = manageBusiness.queryMerchantById(merchantId);
		
		Merchant merchant = manageBusiness.queryMerchantByLoginid(tmpMerchant.getLoginId(), Md5Util.getMD5(oldpass));
		if(merchant == null) {
			return ResultWrap.init(Constss.FALIED, "密码不正确", "");
		}else {
			
			tmpMerchant.setLoginPass(Md5Util.getMD5(newpass));
			manageBusiness.updateMerchant(tmpMerchant);
			return ResultWrap.init(Constss.SUCCESS, "更新成功", "");
		}

	}
	
	
	
	/***发送短信， 根据商家的手机号码*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/sendcode")
	public @ResponseBody Object sendValidcode(HttpServletRequest request, 
			@RequestParam(value="merchant_id") String merchantId) {
		Merchant tmpMerchant = manageBusiness.queryMerchantById(merchantId);
		
		String validcode = RandomUtils.generateNumString(4);
		
		SmsRecord smsrecord = new SmsRecord();
		smsrecord.setPhone(tmpMerchant.getLoginId());
		smsrecord.setValidcode(validcode);
		smsrecord.setCreateTime(new Date());
		smsBusiness.createSmsRecord(smsrecord);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", validcode);
		try {
			SmsUtil.sendMsg(tmpMerchant.getLoginId(), params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ResultWrap.init(Constss.SUCCESS, "发送成功", "");
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/updatepaypass")
	public @ResponseBody Object updateMerchantPayPass(HttpServletRequest request, 
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="valid_code") String validcode,
			@RequestParam(value="new_pass") String newpass) {
		
		Merchant tmpMerchant = manageBusiness.queryMerchantById(merchantId);
		
		
		String lastestvalidcode = smsBusiness.queryLastestSmsByPhone(tmpMerchant.getLoginId()).getValidcode();
		
		tmpMerchant.setPayPass(Md5Util.getMD5(newpass));
		if(validcode.equalsIgnoreCase(lastestvalidcode)) {   //验证码成功
			manageBusiness.updateMerchant(tmpMerchant);
			return ResultWrap.init(Constss.SUCCESS, "更新成功", "");
		}else {
			return ResultWrap.init(Constss.FALIED, "验证码失败", "");
		}
		
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/valid/paypass")
	public @ResponseBody Object validpass(HttpServletRequest request, 
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="paypass") String  paypass) {
		
		Merchant tmpMerchant = manageBusiness.queryMerchantById(merchantId);
		if(tmpMerchant.getPayPass().equalsIgnoreCase(Md5Util.getMD5(paypass))) {
			
			
			return ResultWrap.init(Constss.SUCCESS, "支付密码正确", "");
		}else {
			return ResultWrap.init(Constss.FALIED, "支付密码失败", "");
		}
		
	
		
	}
	
	
	
	
	
	
	/**创建一个商户**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/new")
	public @ResponseBody Object createMerchant(HttpServletRequest request, @RequestParam("merchant_name") String merchantname
			, @RequestParam("contact_email") String contactEmail, @RequestParam("contact_phone") String contactPhone
			, @RequestParam(value="login_pass", defaultValue="123456", required=false) String loginPass
			, @RequestParam(value="pay_pass", defaultValue="123456", required=false) String payPass
			, @RequestParam(value="identity", defaultValue="2", required=false) String identity
			, @RequestParam(value="parent_id", defaultValue="888888", required=false) String parentId) {
		
		Merchant merchant = manageBusiness.queryMerchantByLoginid(contactPhone);
		try {
		if(merchant!=null){
			return ResultWrap.init(Constss.FALIED, "创建失败--"+contactPhone+"已在其他账户绑定", "");
		}else{
			merchant=new  Merchant();
			merchant.setContactEmail(contactEmail);
			merchant.setMerchantId(RandomUtils.generateNumString(10));
			merchant.setContactPhone(contactPhone);
			merchant.setCreateTime(new Date());
			merchant.setIdentity(identity);
			merchant.setLoginId(contactPhone);
			merchant.setLoginPass(Md5Util.getMD5(loginPass));
			merchant.setMerAuthStatus("0");
			merchant.setMerBusinessLicense("");
			merchant.setMerchantName(merchantname);
			merchant.setParent(parentId);
			merchant.setPayPass(Md5Util.getMD5(payPass));
			merchant.setUpdateTime(new Date());
			manageBusiness.createMerchant(merchant);
		}
		return ResultWrap.init(Constss.SUCCESS, "成功", merchant);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "创建失败--"+e.getMessage(), "");
			
		}
		
	}
	
	
	
	
	
	
	/**新增一个商家通道**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/channel/new")
	public @ResponseBody Object addMerchantChannel(HttpServletRequest request, 
			@RequestParam("merchant_id") String merchantid, 
			@RequestParam(value="merchant_name") String merchantName,
			@RequestParam("channel_code") String channelCode, 
			@RequestParam(value="channel_name") String channelname
			) {
		
		MerchantChannel merchantChannel = new MerchantChannel();
		merchantChannel.setChannelCode(channelCode);
		merchantChannel.setChannelName(channelname);
		merchantChannel.setMerchantId(merchantid);
		merchantChannel.setMerchantName(merchantName);
		merchantChannel.setStatus("1"); //标识可用
		merchantChannel.setIsAllowFreeSel("0"); //默认允许自动灵活选择通道
		merchantChannel.setCreateTime(new Date());
		merchantChannel.setUpdateTime(new Date());
		try {
		manageBusiness.createMerchantChannel(merchantChannel);
		return ResultWrap.init(Constss.SUCCESS, "成功", merchantChannel);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "创建失败--"+e.getMessage(), "");
			
		}
		
	}
	
	
	/**查询商家所有外方通道*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/channel/query")
	public @ResponseBody Object queryAllMerchantChannel(HttpServletRequest request, 
			@RequestParam("merchant_id") String merchantid, 
			@RequestParam(value="status", defaultValue="") String status
			) {
		
		List<MerchantChannel> merchantChannels = new ArrayList<MerchantChannel>();

		try {
			if(status != null && !status.equalsIgnoreCase("")) {
				merchantChannels = manageBusiness.queryAllMerchantChannelsByStatus(merchantid, status);
			}else {
				merchantChannels = manageBusiness.queryAllMerchantChannels(merchantid);
			}
			
			return ResultWrap.init(Constss.SUCCESS, "查询成功", merchantChannels);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "查询失败--"+e.getMessage(), null);
			
		}
		
	}
	
	
	/**查询平台给商户配置的所有外放通道**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/allmerchant/channel/query")
	public @ResponseBody Object queryAllMerchantChannel(HttpServletRequest request
			) {
		
		try {
			
			return ResultWrap.init(Constss.SUCCESS, "查询成功", manageBusiness.queryAllMerchantChannels());
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "查询失败--"+e.getMessage(), null);
			
		}
		
	}
	
	
	
	
	/**更新一个商家的外方通道**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/channel/update")
	public @ResponseBody Object updateMerchantChannel(HttpServletRequest request, 
			@RequestParam("merchant_id") String merchantid, 
			@RequestParam("channel_code") String channelCode, 
			@RequestParam(value="status") String status
			) {
	
		try {
			manageBusiness.closeOrOpenMerchantChannel(merchantid, channelCode, status);
			return ResultWrap.init(Constss.SUCCESS, "更新成功", "");
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "更新失败--"+e.getMessage(), null);
			
		}
		
	}
	//将子商户进件成功数据存入数据库
	@RequestMapping(method =RequestMethod.POST,value="/smartmerchant/merchant/user/info/save")
	@ResponseBody
	public Object saveMerchantUserRegister(
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="channel_tag") String channel_tag,
			@RequestParam(value="phone",required = false) String phone,
			@RequestParam(value="id_card",required = false) String idCard,
			@RequestParam(value="sub_merchant_id") String subMerchantId,
			@RequestParam(value="sub_rate",required = false) String subRate,
			@RequestParam(value="sub_extra_fee",required = false) String subExtraFee
	){
		MerchantUserInfo merchantUserInfo=merchantUserInfoBusiness.findByMerchantIdAndSubMerchantId(merchantId,subMerchantId,channel_tag);
		//进件
		//将进件成功的用户数据存入数据库
		if(merchantUserInfo==null){
			LOG.info("新进用户========merchantId==="+merchantId+"====phone==="+phone+"=====subMerchantId===="+subMerchantId);
			MerchantUserInfo merchantUserInfoNew=new MerchantUserInfo();
			merchantUserInfoNew.setChannelTag(channel_tag);
			merchantUserInfoNew.setMerchantId(merchantId);
			merchantUserInfoNew.setPhone(phone);
			merchantUserInfoNew.setIdCard(idCard);
			merchantUserInfoNew.setSubMerchantId(subMerchantId);
			merchantUserInfoNew.setSubRate(new BigDecimal(subRate));
			merchantUserInfoNew.setSubExtraFee(new BigDecimal(subExtraFee));
			merchantUserInfoNew.setStatus(1);
			merchantUserInfoNew.setCreateTime(new Date());
			merchantUserInfoNew.setUpdate_time(new Date());
			merchantUserInfoBusiness.save(merchantUserInfoNew);
		}else{//修改进件费率
			LOG.info("========merchantId==="+merchantId+"====phone==="+phone+"=====subMerchantId===="+subMerchantId);
			merchantUserInfo.setSubRate(new BigDecimal(subRate));
			merchantUserInfo.setSubExtraFee(new BigDecimal(subExtraFee));
			merchantUserInfo.setUpdate_time(new Date());
			merchantUserInfo.setStatus(1);
			merchantUserInfoBusiness.save(merchantUserInfo);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST,value="/smartmerchant/merchant/user/info/query")
	@ResponseBody
	public Object queryUserInfoBySubMerchantId(
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="sub_merchant_id") String subMerchantId,
			@RequestParam(value="channel_tag") String channelTag
	){
		MerchantUserInfo merchantUserInfo=merchantUserInfoBusiness.findByMerchantIdAndSubMerchantId(merchantId,subMerchantId,channelTag);
		if(merchantUserInfo!=null){
			return ResultWrap.init(Constss.SUCCESS, "查询成功", merchantUserInfo);
		}
		return ResultWrap.init(Constss.FALIED, "查询失败", null);
	}

	//将子商户绑卡成功数据存入数据库
	@RequestMapping(method =RequestMethod.POST,value="/smartmerchant/merchant/user/bindcard/save")
	@ResponseBody
	public Object saveMerchantUserBindId(
			@RequestParam(value="merchant_id") String merchantId,
			@RequestParam(value="channel_tag") String channelTag,
			@RequestParam(value="id_card",required = false) String idCard,
			@RequestParam(value="sub_merchant_id") String subMerchantId,
			@RequestParam(value="bindId") String bindId,
			@RequestParam(value="bankCard") String bankCard
	){
		MerchantUserBindCard merchantUserBindCard=merchantUserBindCardBusiness.queryByMerchantIdAndbankCardAndChannelTag(merchantId,channelTag,subMerchantId,bankCard);
		if(merchantUserBindCard==null){
			MerchantUserBindCard merchantUserBindCardNew=new MerchantUserBindCard();
			merchantUserBindCardNew.setBankCard(bankCard);
			merchantUserBindCardNew.setMerchant_id(merchantId);
			merchantUserBindCardNew.setSubMerchantId(subMerchantId);
			merchantUserBindCardNew.setIdCard(idCard);
			merchantUserBindCardNew.setChannelTag(channelTag);
			merchantUserBindCardNew.setBindId(bindId);
			merchantUserBindCardNew.setStatus(1);
			merchantUserBindCardNew.setCreateTime(new Date());
			merchantUserBindCardBusiness.save(merchantUserBindCardNew);
		}else{
			merchantUserBindCard.setBindId(bindId);
			merchantUserBindCard.setStatus(1);
			merchantUserBindCard.setCreateTime(new Date());
			merchantUserBindCardBusiness.save(merchantUserBindCard);
		}
		return null;
	}
}
