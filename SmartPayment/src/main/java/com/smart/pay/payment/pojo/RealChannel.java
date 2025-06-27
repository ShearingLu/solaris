package com.smart.pay.payment.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**实际接入的通道，  如果状态不可用， 就不会选择，
 * 如果有多个备选， 优选选择费率最低的通道
 * 如果多个通道都是可用的情况下，也考虑量的均衡性  
 * */
@Entity
@Table(name = "t_real_channel")
public class RealChannel implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name="channel_id")
	private String channelId;

	/**目标通道的id*/
	@Column(name = "real_channel_code")
	private String realChannelCode;
	
	/**目标通道的名字*/
	@Column(name = "real_channel_name")
	private String realChannelName;
	
	/**费率*/
	@Column(name = "rate")
	private BigDecimal rate;
	
	/**单笔收费费*/
	@Column(name = "extra_fee")
	private BigDecimal extraFee;
	
	/**可用状态
	 * 0 表示可用 1表示不可用
	 * */
	@Column(name = "channel_status")
	private String status;
	
	
	/***如果是0表示可以自由使用
	 * 如果是1只能特别被使用*/
	@Column(name = "lock_status")	
	private String lockStatus="0";
	
	@Column(name = "create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date              createTime;
	
	@Column(name = "update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	
	/**所属的结算主体对象**/
	@Column(name = "settlement_id")
	private String settlementid;
	
	
	
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
	
	
	/**
	 * 默认通道交易量分配比率
	 * 同一种real_pay_type 
	 * ratio 是 大于 1的整数， 越大证明出现的概率越高。
	 * 默认是1
	 * */
	@Column(name = "ratio")
	private int ratio = 1;
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getExtraFee() {
		return extraFee;
	}

	public void setExtraFee(BigDecimal extraFee) {
		this.extraFee = extraFee;
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

	public String getSettlementid() {
		return settlementid;
	}

	public void setSettlementid(String settlementid) {
		this.settlementid = settlementid;
	}

	public String getRealPayType() {
		return realPayType;
	}

	public void setRealPayType(String realPayType) {
		this.realPayType = realPayType;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
