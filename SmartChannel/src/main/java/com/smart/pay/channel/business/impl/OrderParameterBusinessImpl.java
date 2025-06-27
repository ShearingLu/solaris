package com.smart.pay.channel.business.impl;

import com.smart.pay.channel.business.OrderParameterBusiness;
import com.smart.pay.channel.pojo.OrderParameter;
import com.smart.pay.channel.repository.OrderParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderParameterBusinessImpl implements OrderParameterBusiness {

	@Autowired
	private OrderParameterRepository orderParameterRepository;
	
	@Override
	@Transactional
	public void save(OrderParameter orderParameter) {
		orderParameterRepository.saveAndFlush(orderParameter);
	}

	@Override
	public OrderParameter findByOrderCode(String orderCode) {
		return orderParameterRepository.findByOrderCode(orderCode);
	}

}
