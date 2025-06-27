package com.smart.pay.channel.business.impl;

import com.smart.pay.channel.business.ChannelDetailBusiness;
import com.smart.pay.channel.pojo.ChannelDetail;
import com.smart.pay.channel.repository.ChannelDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelDetailBusinessImpl implements ChannelDetailBusiness {
	
	@Autowired
	private ChannelDetailRepository channelDetailRepository;

	@Override
	public ChannelDetail findByChannelTag(String channelTag) {
		return channelDetailRepository.findByChannelTag(channelTag);
	}

	@Override
	public List<ChannelDetail> findByChannelType(String channelType) {
		return channelDetailRepository.findByChannelType(channelType);
	}
}
