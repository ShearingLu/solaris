package com.smart.pay.merchant.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
@Table(name = "t_merchant")
public class Merchant implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**总平台就是999999*/
	@Column(name = "merchant_id")
	private String merchantId;
	
	/**合作商户名字**/
	@Column(name = "merchant_name")
	private String merchantName;
	
	/**联系email*/
	@Column(name = "contact_email")
	private String contactEmail;
	
	/**联系手机号码**/
	@Column(name = "contact_phone")
	private String contactPhone;
	
	
	/**管理端登录帐号，默认是手机号码**/
	@Column(name = "login_id")
	private String loginId;
	
	/**管理端登录密码**/
	@Column(name = "login_pass")
	private String loginPass;
	
	/**交易密码*/
	@Column(name = "pay_pass")
	private String payPass;
	
	
	/***商户认证状态
	 * 
	 * 0未认证  1表示已经认证
	 * */
	@Column(name = "mer_auth_status")
	private String merAuthStatus;
	
	
	/**商家的资质证书**/
	@Column(name = "mer_business_license")
	private String merBusinessLicense;
	
	/**0 表示总平台，  1表示代理商，  2表示商户，  商户是最终*/
	@Column(name = "identity")
	private String identity;
	
	/**只有属于总平台或者代理商*/
	@Column(name = "parent_id")
	private String parent;
	
	
	/**结算周器*/
	@Column(name = "settlement_peroid")
	private int settlePeroid;
	
	
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


	public String getContactEmail() {
		return contactEmail;
	}


	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}


	public String getContactPhone() {
		return contactPhone;
	}


	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}


	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


	public String getLoginPass() {
		return loginPass;
	}


	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}


	public String getMerAuthStatus() {
		return merAuthStatus;
	}


	public void setMerAuthStatus(String merAuthStatus) {
		this.merAuthStatus = merAuthStatus;
	}


	public String getMerBusinessLicense() {
		return merBusinessLicense;
	}


	public void setMerBusinessLicense(String merBusinessLicense) {
		this.merBusinessLicense = merBusinessLicense;
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


	public String getIdentity() {
		return identity;
	}


	public void setIdentity(String identity) {
		this.identity = identity;
	}


	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}


	public String getPayPass() {
		return payPass;
	}


	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}


	public int getSettlePeroid() {
		return settlePeroid;
	}


	public void setSettlePeroid(int settlePeroid) {
		this.settlePeroid = settlePeroid;
	}
	
	
	
	
	
}
