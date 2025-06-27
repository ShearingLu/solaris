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



@Entity
@Table(name = "t_withdraw_condition")
public class WithdrawCondition implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	/**单次提现额度限制*/
	@Column(name = "single_limit")
	private BigDecimal singleLimit;
	
	/**单笔最小金额**/
	@Column(name = "single_min_limit")
	private BigDecimal singleMinLimit;
	
	/**单日提现额度限制**/
	@Column(name = "day_limit")
	private BigDecimal dayLimit;
	
	

	
	/**单次提现手续费配置**/
	@Column(name = "outer_fee")
	private BigDecimal outerFee;
	
	
	
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

	public BigDecimal getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(BigDecimal singleLimit) {
		this.singleLimit = singleLimit;
	}

	public BigDecimal getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(BigDecimal dayLimit) {
		this.dayLimit = dayLimit;
	}



	public BigDecimal getOuterFee() {
		return outerFee;
	}

	public void setOuterFee(BigDecimal outerFee) {
		this.outerFee = outerFee;
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

	public BigDecimal getSingleMinLimit() {
		return singleMinLimit;
	}

	public void setSingleMinLimit(BigDecimal singleMinLimit) {
		this.singleMinLimit = singleMinLimit;
	}
	
	
	
	
	
	
	
}
