package com.smart.pay.merchant.pojo;

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
@Table(name = "t_merchant_account_freeze_history")
public class MerchantAccountFreezeHistory implements Serializable{

	
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
	
	
	
	@Column(name = "order_code")
	private String orderCode;
	
	
	
	/**收款， 提现，  
	 * 0  = 提现
	 * 1  = 其他
	 * */
	@Column(name = "from_type")
	private String fromType;
	
	
	
	/**变更金额*/
	@Column(name = "freeze_amount")
	private BigDecimal freezeAmount;
	
	/**
	 * 变更方向
	 * 0 表示增加
	 * 1 表示减少
	 * */
	@Column(name = "add_or_sub")
	private String addOrSub;
	
	
	/**当前余额*/
	@Column(name = "cur_freeze_amount")
	private BigDecimal curFreezeAmount;

	
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	

	public String getAddOrSub() {
		return addOrSub;
	}

	public void setAddOrSub(String addOrSub) {
		this.addOrSub = addOrSub;
	}

	public BigDecimal getCurFreezeAmount() {
		return curFreezeAmount;
	}

	public void setCurFreezeAmount(BigDecimal curFreezeAmount) {
		this.curFreezeAmount = curFreezeAmount;
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

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	
	
	
	
	
}
