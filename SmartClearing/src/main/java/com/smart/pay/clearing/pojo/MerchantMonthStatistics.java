package com.smart.pay.clearing.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "t_merchant_month_statistics")
public class MerchantMonthStatistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Column(name = "merchant_id")
	private String merchantId;
	
	@Column(name = "trade_year")
	private int year;
	
	@Column(name = "trade_month")
	private int month;
	
	@Column(name = "total_trade_amt")
	private BigDecimal totalTradeAmt;
	
	@Column(name = "statis_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date statisTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public BigDecimal getTotalTradeAmt() {
		return totalTradeAmt;
	}

	public void setTotalTradeAmt(BigDecimal totalTradeAmt) {
		this.totalTradeAmt = totalTradeAmt;
	}

	public Date getStatisTime() {
		return statisTime;
	}

	public void setStatisTime(Date statisTime) {
		this.statisTime = statisTime;
	}
	
	
	
	
	
}
