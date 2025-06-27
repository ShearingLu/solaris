package com.smart.pay.clearing.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "t_payment_order")
public class PaymentOrder implements Serializable{
	

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**商家id*/
	@Column(name = "merchant_id")
	private String merchantId;
	
	
	/**代理或者总平台的id*/
	@Column(name = "parent_id")
	private String parentId;
	
	
	/**商家名字*/
	@Column(name = "merchant_name")
	private String merchantName;
	
	/**平台订单号*/
	@Column(name = "order_code")
	private String orderCode;
	
	
	/**商家订单号*/
	@Column(name = "mer_order_code")
	private String merOrderCode;
	
	
	/**通道订单号,成功状态的有*/
	@Column(name = "real_channel_order_code")
	private String realChannelOrderCode;
	
	
	/**订单状态
	 * 0待完成 
	 * 
	 * 1表示成功 
	 * 
	 * 
	 * 2表示失败
	 * 
	 * 
	 * 3表示关闭
	 * 
	 * **/
	@Column(name = "order_status")
	private String orderStatus;
	
	
	/**
	 * 0表示收单
	 * 1表示提现
	 * */
	@Column(name = "order_type")
	private String orderType;
	
	/**当提现的时候告诉商户改笔订单的结算周期*/
	@Column(name = "settle_peroid")
	private int settlePeroid;

	
	/**订单金额*/
	@Column(name = "amount")
	private BigDecimal amount;
	
	/**订单名称**/
	@Column(name = "product_name")
	private String productName;
	
	
	/***交易费率 商家的费率*/
	@Column(name = "mer_rate")
	private BigDecimal merRate;
	
	
	/**单笔额外手续费*/
	@Column(name = "extra_fee")
	private BigDecimal extraFee;

	//订单分润
	@Column(name="rebate")
	private BigDecimal rebate=new BigDecimal("0.0");

	/**实际到账金额*/
	@Column(name = "real_amount")
	private BigDecimal realAmount;
	
	
	/**商家接入的通道**/
	@Column(name = "channel_name")
	private String channelName;
	
	/**商家接入的通道code*/
	@Column(name = "channel_code")
	private String channelCode;
	
	/**目标实际走的通道*/
	@Column(name = "real_channel_code")
	private String realChannelCode;
	
	/***目标实际走的通道的名字*/
	@Column(name = "real_channel_name")
	private String realChannelName;
	
	
	@Column(name = "real_type")
	private String realtype;
	
	
	
	/***交易费率真实的费率*/
	@Column(name = "real_channel_rate")
	private BigDecimal realChannelRate;
	
	
	/**交易费率真是的费用*/
	@Column(name = "real_channel_extra_fee")
	private BigDecimal realChannelExtraFee;
	
	/**回调地址**/
	@Column(name = "notify_url")
	private String notifyURL;
	
	@Column(name = "attach")
	private String attach;
	
	/**同步回调地址**/
	@Column(name = "return_url")
	private String returnURL;
	
	/****/
	@Column(name = "equipment_tag")
	private String equipmentTag;

	//订单手机
	@Column(name="phone")
	private String phone;

	//身份证号码
	@Column(name="id_card")
	private String idCard;

	@Column(name="card_number")
	private String bankCardNumber;

	@Column(name="bank_account_name")
	private String bankAccountName;

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


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getMerOrderCode() {
		return merOrderCode;
	}


	public void setMerOrderCode(String merOrderCode) {
		this.merOrderCode = merOrderCode;
	}


	public String getRealChannelOrderCode() {
		return realChannelOrderCode;
	}


	public void setRealChannelOrderCode(String realChannelOrderCode) {
		this.realChannelOrderCode = realChannelOrderCode;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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


	public BigDecimal getRealAmount() {
		return realAmount;
	}


	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
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


	


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getNotifyURL() {
		return notifyURL;
	}


	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}


	public String getReturnURL() {
		return returnURL;
	}


	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}


	public BigDecimal getExtraFee() {
		return extraFee;
	}


	public void setExtraFee(BigDecimal extraFee) {
		this.extraFee = extraFee;
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


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public BigDecimal getRealChannelRate() {
		return realChannelRate;
	}


	public void setRealChannelRate(BigDecimal realChannelRate) {
		this.realChannelRate = realChannelRate;
	}


	public BigDecimal getRealChannelExtraFee() {
		return realChannelExtraFee;
	}


	public void setRealChannelExtraFee(BigDecimal realChannelExtraFee) {
		this.realChannelExtraFee = realChannelExtraFee;
	}


	public String getAttach() {
		return attach;
	}


	public void setAttach(String attach) {
		this.attach = attach;
	}


	public int getSettlePeroid() {
		return settlePeroid;
	}


	public void setSettlePeroid(int settlePeroid) {
		this.settlePeroid = settlePeroid;
	}


	public String getRealtype() {
		return realtype;
	}


	public void setRealtype(String realtype) {
		this.realtype = realtype;
	}


	public String getEquipmentTag() {
		return equipmentTag;
	}


	public void setEquipmentTag(String equipmentTag) {
		this.equipmentTag = equipmentTag;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBankCardNumber() {
		return bankCardNumber;
	}

	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public BigDecimal getRebate() {
		return rebate;
	}

	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}
}
