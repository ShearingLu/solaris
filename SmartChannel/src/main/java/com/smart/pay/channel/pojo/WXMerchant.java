package com.smart.pay.channel.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wx_merchant")
public class WXMerchant implements Serializable{

	/**
	 * @author Robin-QQ/WX:354476429
	 * @date 2018年5月30日
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "real_channel_code")
	private String realChannelCode = "";
	@Column(name = "mch_id")
	private String mchId = "";
	@Column(name = "private_key")
	private String privateKey = "";
	@Column(name = "appid")
	private String appid = "";
	@Column(name = "real_type")
	private String realType = "";
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRealChannelCode() {
		return realChannelCode;
	}
	public void setRealChannelCode(String realChannelCode) {
		this.realChannelCode = realChannelCode;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
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
	public String getRealType() {
		return realType;
	}
	public void setRealType(String realType) {
		this.realType = realType;
	}
	
}
