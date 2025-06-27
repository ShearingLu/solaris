package com.smart.pay.payment.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.payment.pojo.PaymentChannel;
import com.smart.pay.payment.pojo.PaymentChannelRoute;
import com.smart.pay.payment.pojo.RealChannel;
import com.smart.pay.payment.pojo.SettlementObject;

public interface ChannelBusiness {

	
	public PaymentChannel  createPaymentChannel(PaymentChannel channel);
	
	
	public List<PaymentChannel>  queryAllPaymentChannels();
	
	public List<PaymentChannel>  queryAllPCPaymentChannels(String channelCode,String channelType);
	
	public PaymentChannel queryPaymentChannelByChannelcode(String channelCode);
	
	
	public List<PaymentChannelRoute> queryPaymentChannelRouteByChannelCode(String merchantId, String channelCode);
	
	public void updateChannelRouteStatus(String realchannelcode, String status);
	
	
	public PaymentChannelRoute createPaymentChannelRoute(PaymentChannelRoute route);
	
	
	public Page<PaymentChannelRoute> pagePaymentChannelRoute(String merchantId, Pageable pageable);
	
	
	public void delPaymentChannelRoute(long id);
	
	public void delPaymentChannelByChannelcode(String channelcode);
	
	
	public RealChannel queryRealChannel(String realChannelCode);
	
	
	public RealChannel saveRealChannel(RealChannel realChannel);
	
	
	public List<RealChannel>  queryRealChannelsByType(String realPayType);
	
	
	public void delRealChannel(String realchannelcode);
	
	
	public List<RealChannel> queryAllRealchannels();
	
	public List<RealChannel> queryAllPCPaymentRealChannels(String realChannelCode,String channelType);
	
	
	public List<SettlementObject> queryAllSettlementObject();
	
	public SettlementObject addNewSettlementObject(SettlementObject settlementObject);
	
	
	public void delSettlementObject(String settlementid);
}
