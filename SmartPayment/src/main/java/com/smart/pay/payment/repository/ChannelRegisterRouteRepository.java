package com.smart.pay.payment.repository;

import com.smart.pay.payment.pojo.ChannelRegisterRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRegisterRouteRepository extends JpaRepository<ChannelRegisterRoute, String>, JpaSpecificationExecutor<ChannelRegisterRoute> {

    @Query("select c from ChannelRegisterRoute c where c.channelTag=:service")
    ChannelRegisterRoute queryRegisterUrlByService(@Param("service") String service);
}
