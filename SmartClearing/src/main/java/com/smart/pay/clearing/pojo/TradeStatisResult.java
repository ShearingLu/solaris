package com.smart.pay.clearing.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TradeStatisResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String merchantId;
	
	
	/**
	 * 如果是日期， 那就访问dd/MM;
	 * 
	 * 如果是月份就访问  yyyy/MM
	 * */
	private String statisTime;
	
	
	
	private BigDecimal tradeAmount;



	public String getMerchantId() {
		return merchantId;
	}



	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}



	public String getStatisTime() {
		return statisTime;
	}



	public void setStatisTime(String statisTime) {
		this.statisTime = statisTime;
	}



	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}



	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	
	
	
	
}
