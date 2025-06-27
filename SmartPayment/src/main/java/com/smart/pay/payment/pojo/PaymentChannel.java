package com.smart.pay.payment.pojo;

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


/**商户可以选择的支付通道*/
@Entity
@Table(name = "t_payment_channel")
public class PaymentChannel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	/**商家接入的通道**/
	@Column(name = "channel_name")
	private String channelName;
	
	/**商家接入的通道code, 支付接口的时候提交， 唯一*/
	@Column(name = "channel_code")
	private String channelCode;
	
	/**
	 * 通道类型
	 * 0是收单  1是提现
	 * */
	@Column(name = "channel_type")
	private String channelType;
	
	
	
	/***通道类别
	 * 
	 * 0 =  支付宝wap
	 * 1 =  微信wap
	 * 
	 * 2 =  支付宝扫码
	 * 
	 * 3 =  微信扫码
	 * 
	 * 4 =  微信公众号支付
	 * 
	 * 5 = 快捷扫码
	 * 
	 * 
	 * */
	/**
	 * */
	@Column(name = "real_pay_type")
	private String realPayType;
	
	@Column(name = "min_limit")
	private BigDecimal minlimit;
	
	
	@Column(name = "max_limit")
	private BigDecimal maxlimit;
	
	
	@Column(name = "create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date              createTime;
	
	@Column(name = "update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getChannelName() {
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getChannelCode() {
		return channelCode;
	}


	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}


	public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getRealPayType() {
		return realPayType;
	}


	public void setRealPayType(String realPayType) {
		this.realPayType = realPayType;
	}


	public BigDecimal getMinlimit() {
		return minlimit;
	}


	public void setMinlimit(BigDecimal minlimit) {
		this.minlimit = minlimit;
	}


	public BigDecimal getMaxlimit() {
		return maxlimit;
	}


	public void setMaxlimit(BigDecimal maxlimit) {
		this.maxlimit = maxlimit;
	}


	
	
	
	
	
	
}
