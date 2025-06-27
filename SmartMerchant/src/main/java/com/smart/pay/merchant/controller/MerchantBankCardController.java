package com.smart.pay.merchant.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.Md5Util;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.common.tools.TokenUtil;
import com.smart.pay.merchant.business.MerchantBankCardBusiness;
import com.smart.pay.merchant.business.MerchantManageBusiness;
import com.smart.pay.merchant.business.MerchantSecurityBusiness;
import com.smart.pay.merchant.pojo.Merchant;
import com.smart.pay.merchant.pojo.MerchantBankCard;
import com.smart.pay.merchant.pojo.MerchantChannel;
import com.smart.pay.merchant.pojo.MerchantInfo;
import com.smart.pay.merchant.pojo.MerchantKey;
import com.smart.pay.merchant.util.Util;

import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
public class MerchantBankCardController {

	
	@Autowired
	private MerchantManageBusiness manageBusiness;
	
	
	@Autowired
	private MerchantBankCardBusiness  merchantBankCardBusiness;
	
	
	@Autowired
	private Util util;
	
    @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/bank/new")
	public @ResponseBody Object AddMerchantBankCard(HttpServletRequest request, 
			@RequestParam(value = "merchant_name") String merchantName,//真实姓名
			
			@RequestParam(value = "card_no") String cardNo,//卡号
			
			@RequestParam(value = "phone") String phone,//预留手机号
			
			@RequestParam(value = "id_card") String idcard,// 身份证号
			
			@RequestParam(value = "bank_name",required = false) String bankName,//银行卡名字
			
			@RequestParam(value = "bank_code",required = false) String bankCode,//总行联行号
			
			@RequestParam(value = "branch_code",required = false) String branchCode,// 开户行联行号
			
			@RequestParam(value = "bank_brand",required = false) String bankBrand,//银行卡品牌 
			
			@RequestParam(value = "card_type",required = false) String cardType,// 卡的类型， 1借记卡，2信用卡 
			
			@RequestParam(value = "pri_or_pub",required = false) String priOrPub,//  0对私还1对公帐户
			
			@RequestParam(value = "nature",required = false) String nature,// 区别
			
			@RequestParam(value = "state", defaultValue = "0",required = false) String state// 使用状态 0启动 2关闭
		
			) {
    	 String token = request.getParameter("token");
		 String merchantId="";
		 try {
			 merchantId=TokenUtil.getUserId(token);
		} catch (Exception e1) {
			
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户不存在或信息不完整", null);
		}
    	
    	MerchantBankCard merchantBankCard = merchantBankCardBusiness.queryMerchantByMerchantIdAndBankCode(merchantId, bankCode);
		
		if(merchantBankCard ==null ) {
			merchantBankCard=new MerchantBankCard();
			merchantBankCard.setCreateTime(new Date());
		}
		merchantBankCard.setMerchantId(merchantId);
		merchantBankCard.setMerchantName(merchantName);
		merchantBankCard.setCardNo(cardNo);
		merchantBankCard.setIdcard(idcard);
		merchantBankCard.setPhone(phone);
		merchantBankCard.setState(state);
		merchantBankCard.setCardType(cardType);
		merchantBankCard.setIdDef("0");
		merchantBankCard.setState("0");
		merchantBankCard.setBankName(bankName);
		merchantBankCard.setBankBrand(bankBrand);
		merchantBankCard.setBankCode(bankCode);
		merchantBankCard.setBankName(bankName);
		merchantBankCard.setBranchCode(branchCode);
		merchantBankCard.setPriOrPub(priOrPub);
		merchantBankCard.setNature(nature);
		try {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantBankCardBusiness.createMerchantBankCard(merchantBankCard));
		} catch (Exception e) {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户不存在或信息不完整"+e.getMessage(), null);
		}
		
		
		
	}
	
	
    
    
	/***获取商户银行列表**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/bank/query")
	public @ResponseBody Object getMerchantBankCardByparent(HttpServletRequest request,
			@RequestParam(value = "card_no",required = false) String cardNo,//卡号
		   //商户编号
		   @RequestParam(value = "merchant_id",required = false) String merchantId,
		   //商户姓名
		   @RequestParam(value = "merchant_name",required = false) String merchantName,
		   
		   @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
           @RequestParam(value = "size", defaultValue = "20", required = false) int size,
           @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
           @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) {
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		
		Page<MerchantBankCard> MerchantBankCards = merchantBankCardBusiness.queryMerchantByMerchantId(merchantId, cardNo,merchantName, pageable);
		
		if(MerchantBankCards != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", MerchantBankCards);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "暂无商户数据", "");
		}
	}
	
	
	
	 
	/***设置默结算卡**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/bank/update/iddef")
	public @ResponseBody Object updateMerchantBankCard(HttpServletRequest request, 
			@RequestParam(value = "card_no") String cardNo,//卡号
			@RequestParam("merchant_id") String merchantId) {
		MerchantBankCard merchantBankCard = merchantBankCardBusiness.queryMerchantByMerchantIdAndBankCode(merchantId, cardNo);
		if(merchantBankCard != null) {
			merchantBankCardBusiness.UpdateMerchantByMerchantIdAndBankCodeSetidDef(merchantId, "0");
			merchantBankCard.setIdDef("1");
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantBankCardBusiness.createMerchantBankCard(merchantBankCard));
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户无默认卡", "");
		}
	}
	
	/***获取默结算卡**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/bank/query/iddef")
	public @ResponseBody Object getMerchantBankCard(HttpServletRequest request, 
			@RequestParam("merchant_id") String merchantId) {
		MerchantBankCard merchantBankCard = merchantBankCardBusiness.queryMerchantByMerchantIdAndidDef(merchantId, "1");
		if(merchantBankCard != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantBankCard);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户无默认卡", "");
		}
	}
	
	/***删除卡**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/bank/del/cardno")
	public @ResponseBody Object delMerchantBankCard(HttpServletRequest request, 
			@RequestParam(value = "card_no") String cardNo,//卡号
			@RequestParam("merchant_id") String merchantId) {
		MerchantBankCard merchantBankCard = merchantBankCardBusiness.queryMerchantByMerchantIdAndBankCode(merchantId, cardNo);
		if(merchantBankCard != null) {
			 merchantBankCardBusiness.deleteMerchantBankCard(merchantId, cardNo);
			return ResultWrap.init(Constss.SUCCESS, "成功", null);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "该卡不存在", "");
		}
	}
	
	
	

	
	
}
