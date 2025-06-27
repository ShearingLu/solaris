package com.smart.pay.payment.repository;

import com.smart.pay.payment.pojo.ChannelBindCardRoute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelBindCardRepository extends JpaRepository<ChannelBindCardRoute, String>, JpaSpecificationExecutor<ChannelBindCardRoute> {

    @Query("select c from ChannelBindCardRoute c where c.channelTag=:service")
    ChannelBindCardRoute queryByService(@Param("service") String service);
}
