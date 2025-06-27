package com.smart.pay.channel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.channel.pojo.PersonalURL;

@Repository
public interface PersonalURLRepository extends JpaRepository<PersonalURL, Long>,JpaSpecificationExecutor<PersonalURL>{

	
	 @Query("select personalURL from  PersonalURL personalURL where personalURL.sign=:sign")
	 PersonalURL findPersonalURLBySign(@Param("sign") String sign);
	  
	

}
