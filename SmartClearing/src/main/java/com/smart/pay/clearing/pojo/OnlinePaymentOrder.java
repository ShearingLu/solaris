package com.smart.pay.clearing.pojo;

import java.io.Serializable;
import java.util.Date;

public class OnlinePaymentOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private String orderCode;
	private String alipayCode;
	private Date trxTime;
	private String trxAmount;
	private String status;
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getAlipayCode() {
		return alipayCode;
	}
	public void setAlipayCode(String alipayCode) {
		this.alipayCode = alipayCode;
	}
	public Date getTrxTime() {
		return trxTime;
	}
	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
	}
	public String getTrxAmount() {
		return trxAmount;
	}
	public void setTrxAmount(String trxAmount) {
		this.trxAmount = trxAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
