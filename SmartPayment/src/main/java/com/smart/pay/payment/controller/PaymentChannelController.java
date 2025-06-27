package com.smart.pay.payment.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.smart.pay.common.tools.ResultWrap;
import com.smart.pay.payment.business.ChannelBusiness;
import com.smart.pay.payment.pojo.PaymentChannel;
import com.smart.pay.payment.pojo.PaymentChannelRoute;
import com.smart.pay.payment.pojo.RealChannel;

@Controller
@EnableAutoConfiguration
public class PaymentChannelController {

	
	
	@Autowired
	private ChannelBusiness channelBusiness;
	
	/**支付通道查询**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/channel/all")
	public @ResponseBody Object queryAllPaymentChannel(HttpServletRequest request) {
		
		
		List<PaymentChannel> channels = channelBusiness.queryAllPaymentChannels();
		
		return ResultWrap.init(Constss.SUCCESS, "成功",channels);
	}
	
	/**外放通道查询
	 * pc
	 * 	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/platform/pay/channel/all")
	public @ResponseBody Object queryAllPCPaymentChannel(HttpServletRequest request,
			//外放通道
			@RequestParam(value = "channel_code",  required = false) String channelCode,
			//类型
			@RequestParam(value = "channel_type", required = false) String channelType
			) {
		
		List<PaymentChannel>  channels = channelBusiness.queryAllPCPaymentChannels(channelCode,channelType);
		
		return ResultWrap.init(Constss.SUCCESS, "成功",channels);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/channel/code")
	public @ResponseBody Object queryPaymentChannelByChannelcode(HttpServletRequest request,
			@RequestParam(value = "channel_code") String channelCode) {
	
		
		PaymentChannel paymentChannel = channelBusiness.queryPaymentChannelByChannelcode(channelCode);
		
		if(paymentChannel != null) {
			
			return ResultWrap.init(Constss.SUCCESS, "成功", paymentChannel);
		}else {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	
	/**支付通道信息删除**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/channel/del")
	public @ResponseBody Object deletePaymentChannel(HttpServletRequest request,
			@RequestParam(value = "channel_code") String channelCode
			) {
		
		try {
			channelBusiness.delPaymentChannelByChannelcode(channelCode);
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			
			return ResultWrap.init(Constss.FALIED, "失败", "");
			
		}
		
	}
	
	
	
	/**支付通道信息添加更新**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/channel/new")
	public @ResponseBody Object updatePaymentChannel(HttpServletRequest request,
			@RequestParam(value = "channel_code") String channelCode,
			@RequestParam(value = "channel_name") String channelName,
			@RequestParam(value = "channel_type") String channelType,
			@RequestParam(value = "real_pay_type", required=false, defaultValue="0") String realPayType
			) {
		
		
		PaymentChannel paymentChannel  =channelBusiness.queryPaymentChannelByChannelcode(channelCode);
		if(paymentChannel==null){
			 paymentChannel  = new PaymentChannel();
			 paymentChannel.setChannelCode(channelCode);
			 paymentChannel.setChannelName(channelName);
			 paymentChannel.setChannelType(channelType);
			 paymentChannel.setCreateTime(new Date());
			 paymentChannel.setUpdateTime(new Date());
			 paymentChannel.setRealPayType(realPayType);
		}else{
			paymentChannel.setChannelCode(channelCode);
			paymentChannel.setChannelName(channelName);
			paymentChannel.setChannelType(channelType);
			 paymentChannel.setRealPayType(realPayType);
			paymentChannel.setUpdateTime(new Date());
		}
		
		try {
			channelBusiness.createPaymentChannel(paymentChannel);
		
			return ResultWrap.init(Constss.SUCCESS, "成功", paymentChannel);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	
	}
	
	
	
	/**
	 * 新增/更新实际支付的渠道
	 * 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/channelroute/status/update")
	public @ResponseBody Object updateChannelRouteStatus(HttpServletRequest request,
			@RequestParam(value = "real_channel_code") String realChannelCode,
			@RequestParam(value = "channel_status") String channelStatus
			) {
		
		try {
			channelBusiness.updateChannelRouteStatus(realChannelCode, channelStatus);
		
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	}
	
	
	/**
	 * 新增/更新实际支付的渠道
	 * 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/realchannel/new")
	public @ResponseBody Object addRealChannel(HttpServletRequest request,
			@RequestParam(value = "real_channel_code") String realChannelCode,
			@RequestParam(value = "real_channel_name") String realChannelName,
			@RequestParam(value = "rate") BigDecimal rate,
			@RequestParam(value = "extra_fee", required=false, defaultValue="0") BigDecimal extraFee,
			@RequestParam(value = "channel_status", required=false, defaultValue="0") String channelStatus,
			@RequestParam(value = "settlement_id", required=false) String settlementid,
			@RequestParam(value = "ratio", required=false, defaultValue="1") int ratio,
			@RequestParam(value = "real_pay_type", required=false, defaultValue="0") String realPayType
			) {
		
		RealChannel realChannel = channelBusiness.queryRealChannel(realChannelCode);
		if(realChannel == null) {
			
			realChannel = new RealChannel();
			realChannel.setCreateTime(new Date());
			realChannel.setExtraFee(extraFee);
			realChannel.setRate(rate);
			realChannel.setRealChannelCode(realChannelCode);
			realChannel.setRealChannelName(realChannelName);
			realChannel.setStatus(channelStatus);
			realChannel.setSettlementid(settlementid);
			realChannel.setRatio(ratio);
			realChannel.setRealPayType(realPayType);
			realChannel.setUpdateTime(new Date());
			
		}else {
			
			realChannel.setExtraFee(extraFee);
			realChannel.setRate(rate);
			realChannel.setRealChannelCode(realChannelCode);
			realChannel.setRealChannelName(realChannelName);
			realChannel.setStatus(channelStatus);
			realChannel.setSettlementid(settlementid);
			realChannel.setRatio(ratio);
			realChannel.setRealPayType(realPayType);
			realChannel.setUpdateTime(new Date());
			
		}
		
		
		try {
			channelBusiness.saveRealChannel(realChannel);
			return ResultWrap.init(Constss.SUCCESS, "成功", realChannel);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
		
	
	}
	
	
	
	/**支付通道信息删除**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/realchannel/del")
	public @ResponseBody Object deletePaymentRealChannel(HttpServletRequest request,
			@RequestParam(value = "real_channel_code") String realChannelCode
			) {
		
		try {
			channelBusiness.delRealChannel(realChannelCode);
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			
			return ResultWrap.init(Constss.FALIED, "失败", "");
			
		}
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/realchannel/all")
	public @ResponseBody Object queryAllPaymentRealChannel(HttpServletRequest request
			) {
	
		List<RealChannel> channels = channelBusiness.queryAllRealchannels();
		
		return ResultWrap.init(Constss.SUCCESS, "成功",channels);
	}
	
	/**上游通道查询
	 * pc
	 * 	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/platform/pay/realchannel/all")
	public @ResponseBody Object queryAllPCPaymentRealChannel(HttpServletRequest request,
			//上游通道
			@RequestParam(value = "real_channel_code",  required = false) String realChannelCode,
			//类型
			@RequestParam(value = "channel_type", required = false) String channelType
			) {
		
		List<RealChannel>  channels = channelBusiness.queryAllPCPaymentRealChannels(realChannelCode,channelType);
		
		return ResultWrap.init(Constss.SUCCESS, "成功",channels);
	}
	
	
	
	/**查询所有支付通道的路由**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/route/query")
    public @ResponseBody Object queryChannelRoute(HttpServletRequest request, //
            @RequestParam(value = "merchant_id", required=false) String merchantId, 
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));

		Page<PaymentChannelRoute> channelRoutes  = channelBusiness.pagePaymentChannelRoute(merchantId, pageable);
		
		return ResultWrap.init(Constss.SUCCESS, "成功", channelRoutes);
		
	}
	
	
	
	
	/**新增或者更新商户的通道到支付通道的路由**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/route/new")
    public @ResponseBody Object addChannelRoute(HttpServletRequest request, //
            @RequestParam(value = "merchant_id") String merchantId, 
            @RequestParam(value = "channel_code") String channelCode, 
            @RequestParam(value = "channel_name", required=false) String channelName, 
            @RequestParam(value = "real_channel_code") String realChannelCode, 
            @RequestParam(value = "real_channel_name", required=false) String realChannelName,
            @RequestParam(value = "ratio", required=false, defaultValue="1") int ratio
            
           ) throws Exception{
		
		
		List<PaymentChannelRoute> paymentChannelRoutes = channelBusiness.queryPaymentChannelRouteByChannelCode(merchantId, channelCode);
		PaymentChannelRoute paymentChannelRoute = null;
		if(paymentChannelRoutes == null || paymentChannelRoutes.size() == 0) {
			
			
			paymentChannelRoute = new PaymentChannelRoute();
			paymentChannelRoute.setChannelCode(channelCode);
			paymentChannelRoute.setChannelName(channelName);
			paymentChannelRoute.setCreateTime(new Date());
			paymentChannelRoute.setMerchantId(merchantId);
			paymentChannelRoute.setRealChannelCode(realChannelCode);
			paymentChannelRoute.setRealChannelName(realChannelName);
			paymentChannelRoute.setStatus("0");
			paymentChannelRoute.setUpdateTime(new Date());
			paymentChannelRoute.setRatio(ratio);
		}else {
			paymentChannelRoute = paymentChannelRoutes.get(0);
			paymentChannelRoute.setChannelCode(channelCode);
			paymentChannelRoute.setChannelName(channelName);
			paymentChannelRoute.setMerchantId(merchantId);
			paymentChannelRoute.setRealChannelCode(realChannelCode);
			paymentChannelRoute.setRealChannelName(realChannelName);
			paymentChannelRoute.setUpdateTime(new Date());
			paymentChannelRoute.setStatus("0");
			paymentChannelRoute.setRatio(ratio);
			
		}
		
		try {
			channelBusiness.createPaymentChannelRoute(paymentChannelRoute);
			return ResultWrap.init(Constss.SUCCESS, "成功", paymentChannelRoute);
		}catch(Exception e) {
			return ResultWrap.init(Constss.FALIED, "失败", "");
		}
	}
	
	
	
	/**删除支付通道的路由**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartpayment/pay/route/del")
    public @ResponseBody Object delChannelRoute(HttpServletRequest request, //
            @RequestParam(value = "id") long id
           ) throws Exception{		
		
		try {
			channelBusiness.delPaymentChannelRoute(id);
			return ResultWrap.init(Constss.SUCCESS, "成功", "");
		}catch(Exception e) {
			
			return ResultWrap.init(Constss.FALIED, "失败", "");
			
		}
	}
	
	
	
	
	
}
