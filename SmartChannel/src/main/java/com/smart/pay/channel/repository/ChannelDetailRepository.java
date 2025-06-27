package com.smart.pay.channel.repository;

import com.smart.pay.channel.pojo.ChannelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelDetailRepository extends JpaRepository<ChannelDetail, Long>, JpaSpecificationExecutor<ChannelDetail> {

	ChannelDetail findByChannelTag(String channelTag);
	
	List<ChannelDetail>  findByChannelType(String channelType);

}
