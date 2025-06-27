package com.smart.pay.merchant.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.IPUtils;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.merchant.business.MerchantSecurityBusiness;
import com.smart.pay.merchant.pojo.MerchantIPWhiteList;
import com.smart.pay.merchant.pojo.MerchantKey;

@Controller
@EnableAutoConfiguration
public class MerchantSecurityController {

	
	@Autowired
	private MerchantSecurityBusiness  merchantSecurityBusiness;
	
	/**新增外放商家的服务器白名单**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/whitelist")
	public @ResponseBody Object addMerchantIPAddress(HttpServletRequest request, @RequestParam("merchant_id") String merchantid,
			@RequestParam("ip_address") String ipaddress) {
		
		MerchantIPWhiteList  ipWhiteList = new MerchantIPWhiteList();
		ipWhiteList.setCreateTime(new Date());
		ipWhiteList.setUpdateTime(new Date());
		ipWhiteList.setMerchantId(merchantid);
		
		if(IPUtils.matches(ipaddress)) {
			ipWhiteList.setWhitelist(ipaddress);
		}else {
			return ResultWrap.init(Constss.FALIED, "ip参数无效", "");
		}
		
		
		
		
		
		
		try {
			merchantSecurityBusiness.createMerchantIPWhiteList(ipWhiteList);
			return ResultWrap.init(Constss.SUCCESS, "成功", ipWhiteList);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "创建失败--"+e.getMessage(), "");
			
		}

		
	}
	
	
	
	/**删除外放商家的白名单**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/whitelist/del")
	public @ResponseBody Object delMerchantIPAddress(HttpServletRequest request, @RequestParam("id") long id) {
		
		
		
		try {
			merchantSecurityBusiness.delMerchantIPWhiteList(id);
			return ResultWrap.init(Constss.SUCCESS, "删除成功", "");
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "删除失败--"+e.getMessage(), "");
			
		}

		
	}
	
	
	/**查询外方商家的白名单*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/whitelist/query")
	public @ResponseBody Object queryMerchantIPAddress(HttpServletRequest request, @RequestParam("merchant_id") String merchantid) {
	
	
		try {
			List<MerchantIPWhiteList>  ipwhitelist = merchantSecurityBusiness.queryMerchantIPWhiteListByMerchantid(merchantid);
			return ResultWrap.init(Constss.SUCCESS, "成功", ipwhitelist);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败--"+e.getMessage(), "");
			
		}
	
	}
	
	/**查询外方商家的白名单*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/whitelist/platform/query")
	public @ResponseBody Object queryMerchantIPAddressPrent(HttpServletRequest request, 
				@RequestParam(value ="merchant_id", required = false) String merchantid,
				@RequestParam(value ="ip", required = false) String ip,
			 	@RequestParam(value = "page", defaultValue = "0", required = false) int page, //
	            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
	            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
	            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) {
	
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		try {
			Page<MerchantIPWhiteList>  ipwhitelist = merchantSecurityBusiness.queryMerchantIPWhiteListByPrent(merchantid,ip,pageable);
			return ResultWrap.init(Constss.SUCCESS, "成功", ipwhitelist);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败--"+e.getMessage(), "");
			
		}
	
	}
	
	
	

	/**给外方商家生成一个md5密钥**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/key/new")
	public @ResponseBody Object newMerchantKey(HttpServletRequest request, @RequestParam("merchant_id") String merchantid, @RequestParam("key_type") String keytype) {
	
		
		/**首先判断商户的密钥是否存在**/
		MerchantKey  merchantKey = merchantSecurityBusiness.queryMerchantKey(merchantid, keytype);
	
		if(merchantKey == null) {
			merchantKey  = new MerchantKey();
			
			merchantKey.setCreateTime(new Date());
			merchantKey.setKeyType(keytype);
			merchantKey.setKey(RandomUtils.generateNewLowerString(32));
			merchantKey.setMerchantId(merchantid);
			merchantKey.setPubKey("");
			merchantKey.setUpdateTime(new Date());
			
			
			
		}else {
			
			
			merchantKey.setKey(RandomUtils.generateNewLowerString(32));
			merchantKey.setPubKey("");
			merchantKey.setUpdateTime(new Date());
			
			
			
		}
		
		try {
			merchantSecurityBusiness.createMerchantKey(merchantKey);
			return ResultWrap.init(Constss.SUCCESS, "密钥生成成功", merchantKey);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "密钥生成失败--"+e.getMessage(), "");
		}
		
	}
	
	
	/**查询外方商家当前的密钥*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/key/query")
	public @ResponseBody Object queryMerchantKey(HttpServletRequest request, @RequestParam("merchant_id") String merchantid, @RequestParam("key_type") String keytype) {
	
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			MerchantKey  merchantKey = merchantSecurityBusiness.queryMerchantKey(merchantid, keytype);
			if(merchantKey==null){
				map= ResultWrap.init(Constss.FALIED, "失败--暂无数据", "");
			}else{
				map= ResultWrap.init(Constss.SUCCESS, "成功", merchantKey);
			}
		}catch(Exception e) {
			map= ResultWrap.init(Constss.FALIED, "失败--"+e.getMessage(), "");
		}
		return map;
	}
	
	
}
