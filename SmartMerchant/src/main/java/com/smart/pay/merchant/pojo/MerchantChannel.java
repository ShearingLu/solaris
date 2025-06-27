package com.smart.pay.merchant.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***商家目前所有可用的通道*/
@Entity
@Table(name = "t_merchant_channel")
public class MerchantChannel implements Serializable{

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

	/**合作商户名字**/
	@Column(name = "merchant_name")
	private String merchantName;

	/**商家接入的通道code*/
	@Column(name = "channel_code")
	private String channelCode;

	/**商家接入的通道**/
	@Column(name = "channel_name")
	private String channelName;


	@Column(name = "status")
	private String status;


	/***
	 *  如果商家有固定路由， 在所有的固定路由都不可用的情况下， 是否允许自动匹配灵活的路由选择方案
	 *
	 * 0 = 表示允许
	 *
	 *
	 * 1 = 表示不允许
	 */
	@Column(name = "is_allow_free_sel")
	private String isAllowFreeSel = "0";

	@Column(name="sort")
	private Integer sort;

	@Column(name = "create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getIsAllowFreeSel() {
		return isAllowFreeSel;
	}

	public void setIsAllowFreeSel(String isAllowFreeSel) {
		this.isAllowFreeSel = isAllowFreeSel;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
