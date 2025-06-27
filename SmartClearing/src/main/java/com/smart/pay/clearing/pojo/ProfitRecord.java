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


/**如果从代理商开始的， 那就需要给上级发分润*/

@Entity
@Table(name = "t_profit_record")
public class ProfitRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	/**平台订单号*/
	@Column(name = "order_code")
	private String orderCode;
	
	
	/**交易商家**/
	@Column(name = "order_merchant_id")
	private String orderMerchantId;
	
	/**商家id*/
	@Column(name = "merchant_id")
	private String merchantId;
	
	/**商家名字*/
	@Column(name = "merchant_name")
	private String merchantName;
	
	/**商家接入的通道**/
	@Column(name = "channel_name")
	private String channelName;
	
	/**商家接入的通道code*/
	@Column(name = "channel_code")
	private String channelCode;
	
	
	/**目标实际走的通道*/
	@Column(name = "real_channel_code")
	private String realChannelCode;
	
	/**目标实际走的通道**/
	@Column(name = "real_channel_name")
	private String realChannelName;
	
	
	/**订单金额*/
	@Column(name = "amount")
	private BigDecimal amount;
	
	
	/***交易费率*/
	@Column(name = "mer_rate")
	private BigDecimal merRate;
	
	
	/**额外手续费*/
	@Column(name = "mer_extra_fee")
	private BigDecimal merExtraFee;
	
	
	/***平台或代理商成本费率*/
	@Column(name = "own_rate")
	private BigDecimal ownRate;
	
	@Column(name = "own_extra_fee")
	private BigDecimal ownExtraFee;
	
	
	/**平台或代理商的毛利润**/
	@Column(name = "own_income")
	private BigDecimal ownIncome;
	

	@Column(name = "create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date              createTime;



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


	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getMerRate() {
		return merRate;
	}


	public void setMerRate(BigDecimal merRate) {
		this.merRate = merRate;
	}





	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public BigDecimal getMerExtraFee() {
		return merExtraFee;
	}


	public void setMerExtraFee(BigDecimal merExtraFee) {
		this.merExtraFee = merExtraFee;
	}


	


	public BigDecimal getOwnRate() {
		return ownRate;
	}


	public void setOwnRate(BigDecimal ownRate) {
		this.ownRate = ownRate;
	}


	public BigDecimal getOwnExtraFee() {
		return ownExtraFee;
	}


	public void setOwnExtraFee(BigDecimal ownExtraFee) {
		this.ownExtraFee = ownExtraFee;
	}


	public BigDecimal getOwnIncome() {
		return ownIncome;
	}


	public void setOwnIncome(BigDecimal ownIncome) {
		this.ownIncome = ownIncome;
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


	public String getOrderMerchantId() {
		return orderMerchantId;
	}


	public void setOrderMerchantId(String orderMerchantId) {
		this.orderMerchantId = orderMerchantId;
	}
	
	
	
	
	
	
}
