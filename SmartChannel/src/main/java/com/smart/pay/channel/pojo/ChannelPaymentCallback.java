package com.smart.pay.channel.pojo;

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

/**记录所有渠道异步回调的信息*/
@Entity
@Table(name = "t_channel_payment_callback")
public class ChannelPaymentCallback implements Serializable{



	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**渠道订单号*/
	@Column(name = "real_channel_order_code")
	private String realChannelOrderCode;
	
	
	/**平台订单号*/
	@Column(name = "order_code")
	private  String orderCode;
	
	/***/
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "real_amount")
	private BigDecimal realAmount;
	
	
	/**0 表示成功   1表示失败*/
	@Column(name = "status")
	private String status;
	
	/**返回失败状态， 错误描述*/
	@Column(name = "error_msg")
	private String errorMsg;
	
	
	
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





	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getRealAmount() {
		return realAmount;
	}


	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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


	public String getRealChannelOrderCode() {
		return realChannelOrderCode;
	}


	public void setRealChannelOrderCode(String realChannelOrderCode) {
		this.realChannelOrderCode = realChannelOrderCode;
	}
	
	
	
	
	
	
	
}
