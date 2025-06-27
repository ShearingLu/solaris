package com.smart.pay.payment.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.pay.payment.business.ChannelBusiness;
import com.smart.pay.payment.pojo.PaymentChannel;
import com.smart.pay.payment.pojo.PaymentChannelRoute;
import com.smart.pay.payment.pojo.RealChannel;
import com.smart.pay.payment.pojo.SettlementObject;
import com.smart.pay.payment.repository.PaymentChannelRepository;
import com.smart.pay.payment.repository.PaymentChannelRouteRepository;
import com.smart.pay.payment.repository.RealChannelRepository;
import com.smart.pay.payment.repository.SettlementObjectRepository;

@Service
public class ChannelBusinessImpl implements ChannelBusiness{

	@Autowired
	private PaymentChannelRepository paymentChannelRepository;
	
	
	@Autowired
	private PaymentChannelRouteRepository paymentChannelRouteRepository;
	
	
	@Autowired
	private RealChannelRepository realChannelRepository;

	
	
	@Autowired
	private SettlementObjectRepository settlementObjectRepository;

	@Transactional
	@Override
	public PaymentChannel createPaymentChannel(PaymentChannel channel) {
		return paymentChannelRepository.saveAndFlush(channel);
	}


	@Override
	public List<PaymentChannel> queryAllPaymentChannels() {
		return paymentChannelRepository.findAll();
	}

	@Override
	public List<PaymentChannel> queryAllPCPaymentChannels(String channelCode,String channelType) {
		
		 List<PaymentChannel> pcs;
		if(channelCode!=null&&channelCode.trim().length()>0&&channelType!=null&&channelType.trim().length()>0){
			pcs= paymentChannelRepository.findPaymentChannelByChannelCodeAndchannelType(channelCode,channelType);
		}else if(channelCode!=null&&channelCode.trim().length()>0){
			pcs= paymentChannelRepository.findPaymentChannelByChannelCode(channelCode);
		}else if(channelType!=null&&channelType.trim().length()>0){
			pcs= paymentChannelRepository.findPaymentChannelBychannelType(channelType);
		}else{
			pcs= paymentChannelRepository.findAll();
		}
		
		return pcs;
	}
	
	@Override
	public PaymentChannel queryPaymentChannelByChannelcode(String channelCode) {
		return paymentChannelRepository.findPaymentChannelByCode(channelCode);
	}

	
	@Transactional
	@Override
	public void delPaymentChannelByChannelcode(String channelcode) {
		paymentChannelRepository.deletePaymentChannelByCode(channelcode);
	}


	@Override
	public List<PaymentChannelRoute> queryPaymentChannelRouteByChannelCode(String merchantId, String channelCode) {
		return paymentChannelRouteRepository.findPaymentChannelRouteByCode(merchantId, channelCode);
	}


	@Override
	public RealChannel queryRealChannel(String realChannelCode) {
		return realChannelRepository.findRealChannelByrealChannelCode(realChannelCode);
	}


	@Transactional
	@Override
	public RealChannel saveRealChannel(RealChannel realChannel) {
		
		return realChannelRepository.saveAndFlush(realChannel);
	}

	
	@Transactional
	@Override
	public void delRealChannel(String realchannelcode) {
		realChannelRepository.delRealChannelByrealChannelCode(realchannelcode);
	}


	@Override
	public List<RealChannel> queryAllRealchannels() {
		
		return realChannelRepository.findAll();
	}
	@Override
	public List<RealChannel> queryAllPCPaymentRealChannels(String realChannelCode, String channelType) {
		List<RealChannel> realChannels;
		if(realChannelCode!=null&& realChannelCode.trim().length()>0&&channelType!=null&& channelType.trim().length()>0) {
			realChannels=realChannelRepository.findRealChannelByrealChannelCodeAndchannelType(realChannelCode,channelType);
		}else if(realChannelCode!=null&& realChannelCode.trim().length()>0) {
			realChannels=realChannelRepository.findRealChannelByrealchannelCode(realChannelCode);
		}else if(channelType!=null&& channelType.trim().length()>0) {
			realChannels=realChannelRepository.findRealChannelByrealChannelType(channelType);
		}else {
			realChannels=realChannelRepository.findAll();
			
		}
		
		return realChannels;
	}

	@Transactional
	@Override
	public PaymentChannelRoute createPaymentChannelRoute(PaymentChannelRoute route) {
		
		return paymentChannelRouteRepository.saveAndFlush(route);
	}

	@Transactional
	@Override
	public void delPaymentChannelRoute(long id) {
		paymentChannelRouteRepository.delPaymentChannelRouteById(id);
	}


	@Override
	public Page<PaymentChannelRoute> pagePaymentChannelRoute(String merchantId, Pageable pageable) {
		
		if(merchantId != null && !merchantId.equalsIgnoreCase("")) {
			
			return paymentChannelRouteRepository.findPaymentChannelRouteByMerchantid(merchantId, pageable);
			
		}else {
			
			return paymentChannelRouteRepository.findPaymentChannelRoute(pageable);
		
		}
		
	}


	@Override
	public List<SettlementObject> queryAllSettlementObject() {
		
		return settlementObjectRepository.findAll();

	}


	@Transactional
	@Override
	public SettlementObject addNewSettlementObject(SettlementObject settlementObject) {
		return settlementObjectRepository.saveAndFlush(settlementObject);
	}

	@Transactional
	@Override
	public void delSettlementObject(String settlementid) {
		settlementObjectRepository.delSettlementObject(settlementid);
	}

	@Transactional
	@Override
	public void updateChannelRouteStatus(String realchannelcode, String status) {
		paymentChannelRouteRepository.updateChannelRouteStatus(status, realchannelcode);
	}


	@Override
	public List<RealChannel> queryRealChannelsByType(String realPayType) {
		
		return realChannelRepository.findRealChannelsByType(realPayType);
	}


	


	
}
