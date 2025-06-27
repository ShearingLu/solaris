package com.smart.pay.payment.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;



/**如果目标的路由不可用。 有限选择低费率可用的， 同时兼顾通道的交易量*/
@Entity
@Table(name = "t_payment_channel_route")
public class PaymentChannelRoute implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**商家id*/
	@Column(name = "merchant_id")
	private String merchantId;
	
	
	/**交易通道*/
	@Column(name = "channel_code")
	private String channelCode;
	
	
	/**交易通道名字*/
	@Column(name = "channel_name")
	private String channelName;
	
	
	/**实际通道*/
	@Column(name = "real_channel_code")
	private String realChannelCode;
	
	
	/**
	 * 商家走固定路由的时候， 通道交易量的分配比率
	 * 
	 * 为大于的1整数， 数字越大， 证明出现的概率越高
	 * 默认是1
	 * */
	@Column(name = "ratio")
	private int ratio = 1;
	
	
	
	
	/**实际通道名字**/
	@Column(name = "real_channel_name")
	private String realChannelName;
	
	/**
	 * 0 表示可用
	 * 1表示不可用
	 * */
	@Column(name = "status")
	private String status;
	
	
	
	
	
	
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


	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}


	public String getChannelCode() {
		return channelCode;
	}


	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}


	public String getChannelName() {
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getRealChannelCode() {
		return realChannelCode;
	}


	public void setRealChannelCode(String realChannelCode) {
		this.realChannelCode = realChannelCode;
	}


	public String getRealChannelName() {
		return realChannelName;
	}


	public void setRealChannelName(String realChannelName) {
		this.realChannelName = realChannelName;
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

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}




	public int getRatio() {
		return ratio;
	}


	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	
	
	
}
