package com.smart.pay.channel.repository;

import com.smart.pay.channel.pojo.OrderParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderParameterRepository extends JpaRepository<OrderParameter, Long>, JpaSpecificationExecutor<OrderParameter> {

	OrderParameter findByOrderCode(String orderCode);

}
