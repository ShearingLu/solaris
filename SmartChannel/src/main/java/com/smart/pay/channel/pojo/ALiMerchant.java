package com.smart.pay.channel.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 支付宝商户表
 * @author Robin-QQ/WX:354476429
 * @date 2018年5月7日
 */
@Entity
@Table(name = "t_ali_merchant")
public class ALiMerchant implements Serializable {

	/**
	 * @author Robin-QQ/WX:354476429
	 * @date 2018年5月7日
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "real_channel_code")
	private String realChannelCode = "";
	@Column(name = "ali_public_key")
	private String aLiPublicKey ="";
	@Column(name = "public_key")
	private String publicKey = "";
	@Column(name = "private_key")
	private String privateKey = "";
	@Column(name = "appid")
	private String appid = "";
	@Column(name = "partner")
	private String partner = "";
	
	@Column(name = "real_type")
	private String realtype = "";
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getaLiPublicKey() {
		return aLiPublicKey;
	}

	public void setaLiPublicKey(String aLiPublicKey) {
		this.aLiPublicKey = aLiPublicKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRealtype() {
		return realtype;
	}

	public void setRealtype(String realtype) {
		this.realtype = realtype;
	}

	
	
	
}
