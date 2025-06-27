		package com.smart.pay.payment.repository;

import com.smart.pay.payment.pojo.RealChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealChannelRepository extends JpaRepository<RealChannel, String>, JpaSpecificationExecutor<RealChannel> {

	
	 @Query("select r from  RealChannel r where r.channelId=:realChannelCode and r.status=0")
	 RealChannel findRealChannelByrealChannelCode(@Param("realChannelCode") String realChannelCode);
	 
	 
	 @Query("select realChannel from  RealChannel realChannel where realChannel.status='0' and realChannel.realPayType=:realPayType and realChannel.lockStatus='0'")
	 List<RealChannel> findRealChannelsByType(@Param("realPayType") String realPayType);
	 
	 @Query("select realChannel from  RealChannel realChannel where realChannel.status='0' and realChannel.realPayType=:realPayType and  realChannel.realChannelCode=:realChannelCode")
	 List<RealChannel> findRealChannelByrealChannelCodeAndchannelType(@Param("realChannelCode") String  realChannelCode,@Param("realPayType") String realPayType);
	 
	 @Query("select realChannel from  RealChannel realChannel where realChannel.realChannelCode=:realChannelCode")
	 List<RealChannel> findRealChannelByrealchannelCode(@Param("realChannelCode") String realChannelCode);
	 
	 @Query("select realChannel from  RealChannel realChannel where realChannel.status='0'  and  realChannel.realPayType=:realPayType")
	 List<RealChannel> findRealChannelByrealChannelType(@Param("realPayType") String realPayType);
	 
	 @Modifying
	 @Query("delete from  RealChannel realChannel where realChannel.realChannelCode=:realChannelCode")
	 void delRealChannelByrealChannelCode(@Param("realChannelCode") String realChannelCode);
	 
}


