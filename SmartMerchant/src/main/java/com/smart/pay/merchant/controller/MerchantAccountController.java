package com.smart.pay.merchant.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.smart.pay.common.tools.DateUtil;
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.merchant.business.MerchantAccountBusiness;
import com.smart.pay.merchant.pojo.MerchantAccount;
import com.smart.pay.merchant.pojo.MerchantAccountHistory;

@Controller
@EnableAutoConfiguration
public class MerchantAccountController {

	
	@Autowired
	private MerchantAccountBusiness merAccountBusiness;
	
	
	/**查询商家的余额， **/
	/***根据商户的信息获取商户的基本信息**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/account/id")
	public @ResponseBody Object getMerchantAccount(HttpServletRequest request, @RequestParam("merchant_id") String merchantid) {
		
		MerchantAccount merchantAccount = merAccountBusiness.queryMerchantAccountByMerchantId(merchantid);
		
		if(merchantAccount != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAccount);
		}else {
			return ResultWrap.init(Constss.ERROR_MERCHANT_NOT_EXISTED, "商户账户不存在", "");
		}
	}
		

	
	/**查询商家的余额变动历史， 分页显示**/
	 @RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/account/history")
	    public @ResponseBody Object queryMerchantAccountHistory(HttpServletRequest request, //
	            @RequestParam(value = "merchant_id") String merchantId, //
	            @RequestParam(value = "start_time", required = false) String startTime, //
	            @RequestParam(value = "end_time", required = false) String endTime, //
	            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
	            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
	            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
	            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		 
		 
		 Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		 Date startDate  = null;
		 
		 Date endDate   = null;
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		 }
		 
		 
		 if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		 }
		 
		 
		 Page<MerchantAccountHistory>  pageHistory   = merAccountBusiness.pageMerchantAccountHistory(merchantId, startDate, endDate, pageable);
		 
		 
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageHistory);
	 }
	
	
	
	
	/**
	 * 新增一笔收单或分润
	 * 
	 * from_type    0  表示收款  2表示分润收入 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/collect")
	public @ResponseBody Object addCollectMoney(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid
			, @RequestParam("amount") String amount
			, @RequestParam("from_type") String fromType
			, @RequestParam("order_code") String ordercode
			) {
		
		MerchantAccount merchantAccount = merAccountBusiness.addCollectMoney(merchantid, new BigDecimal(amount), ordercode, fromType);
		
		if(merchantAccount != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAccount);
		}else {
			return ResultWrap.init(Constss.FALIED, "更新失败", "");
		}
	}
	
	
	/**
	 * 发起一笔提现， 并冻结相应的账户提现额度
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/withdraw/req")
	public @ResponseBody Object addNewWithdraw(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid
			, @RequestParam("amount") String amount
			, @RequestParam("order_code") String ordercode
			) {
		
		MerchantAccount merchantAccount = merAccountBusiness.addNewWithdraw(merchantid, new BigDecimal(amount), ordercode);
		
		if(merchantAccount != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAccount);
		}else {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	
	/***
	 * 提现失败解冻一笔提现, 并复原账户
	 * @param request
	 * @param merchantid
	 * @param amount
	 * @param ordercode
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/account/unfreeze")
	public @ResponseBody Object unfreezeAccount(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid
			, @RequestParam("amount") String amount
			, @RequestParam("order_code") String ordercode
			) {
		
		MerchantAccount merchantAccount = merAccountBusiness.unFreezeAmount(merchantid, new BigDecimal(amount), ordercode);
		
		if(merchantAccount != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAccount);
		}else {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	
	/**
	 * 提现成功解冻一笔提现， 并扣除用户账户
	 * @param request
	 * @param merchantid
	 * @param amount
	 * @param ordercode
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/smartmerchant/merchant/account/unfreezeandupdate")
	public @ResponseBody Object unFreezeAndUpdateAccount(HttpServletRequest request,
			@RequestParam("merchant_id") String merchantid
			, @RequestParam("amount") String amount
			, @RequestParam("order_code") String ordercode
			) {
		
		MerchantAccount merchantAccount = merAccountBusiness.unFreezeAndUpdateAccount(merchantid, new BigDecimal(amount), ordercode);
		
		if(merchantAccount != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", merchantAccount);
		}else {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
}

