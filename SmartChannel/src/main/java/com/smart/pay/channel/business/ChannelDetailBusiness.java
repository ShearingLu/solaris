package com.smart.pay.channel.business;

import com.smart.pay.channel.pojo.ChannelDetail;

import java.util.List;

public interface ChannelDetailBusiness {

	ChannelDetail findByChannelTag(String channelTag);
	
	List<ChannelDetail> findByChannelType(String channelType);

}
