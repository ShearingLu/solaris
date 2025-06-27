package com.smart.pay.merchant.pojo;

import java.io.Serializable;

public class MerchantInfo implements Serializable{


	private static final long serialVersionUID = 1L;


	private String merchantId;
	

	private String merchantName;
	

	private String contactEmail;
	

	private String contactPhone;
	
	
	private String loginId;
	

	private String loginPass;
	

	private String payPass;
	
	private String token;
	
	
	/***商户认证状态
	 * 
	 * 0未认证  1表示已经认证
	 * */
	private String merAuthStatus;
		
	/**商家的资质证书**/
	private String merBusinessLicense;
	
	/**0 表示总平台，  1表示代理商，  2表示商户，  商户是最终*/
	private String identity;
	
	/**只有属于总平台或者代理商*/
	private String parent;
	
	
	/**MD5  RSA*/
	private String keyType;
	
	
	/**
	 * 平台私钥
	 * 如果是RSA, 用key加密给商家回调
	 * */
	private String key;
	
	
	/**
	 * 商户的公钥
	 * 如果是RSA签名， 需要用pub_key 解密 
	 * */
	private String pubKey;


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


	public String getPayPass() {
		return payPass;
	}


	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
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


	public String getKeyType() {
		return keyType;
	}


	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getPubKey() {
		return pubKey;
	}


	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	
	
	
	
	
	
}
