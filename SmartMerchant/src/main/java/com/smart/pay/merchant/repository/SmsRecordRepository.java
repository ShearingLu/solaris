package com.smart.pay.merchant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.merchant.pojo.SmsRecord;

@Repository
public interface SmsRecordRepository extends JpaRepository<SmsRecord, String>, JpaSpecificationExecutor<SmsRecord> {

	
	/**根据手机号码， 获取商家的最新一条验证码**/
	@Query("select smsrecord from  SmsRecord smsrecord where smsrecord.phone=:phone order by smsrecord.createTime desc")
	List<SmsRecord> findLastestSmsRecord(@Param("phone") String phone);
	
}
