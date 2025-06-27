package com.smart.pay.channel.pojo;

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

/**手机设备信息*/
@Entity
@Table(name = "t_mobile_equipment")
public class MobileEquipment implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "equipment_no")
	private String equipmentNo;   //设备编号
	
	@Column(name = "equipment_tag")
	private String equipmentTag;  //设备标识
	
	@Column(name = "mobile_url")
	private String mobileURL;     //手机对外暴露的url
	
	@Column(name = "brand")
	private String brand;         //产品的品牌
	
	@Column(name = "sys_version")
	private String sysVersion;    //系统的版本
	
	@Column(name = "enable")
	private String enable   = "1";       //是否可用
	
	
	@Column(name = "takeup_status")   
	private String takeupStatus;   //当前是否可用      0表示没有被占用。  1表示已经被占用
	
	
	@Column(name = "takeup_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date takeupTime;      //如果当前被占用，  那么开始被占用的时间， 到秒
	
	@Column(name = "release_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date releaseTime;    //被释放的时间,  如果需要释放， 那么释放的时间。
	
	
	@Column(name = "day_limit")
	private BigDecimal daylimit;        //预留参数
	
	
	/**
	 * 默认通道交易量分配比率
	 * ratio 是 大于 1的整数， 越大证明出现的概率越高。
	 * 默认是1
	 * */
	@Column(name = "ratio")
	private int ratio;
	
	
	@Column(name = "extra1")
	private String extra1;        //预留参数
	
	
	@Column(name = "extra2")
	private String extra2;        //预留参数
	
	@Column(name = "extra3")
	private String extra3;        //预留参数
	
	@Column(name = "extra4")
	private String extra4;        //预留参数
	
	@Column(name = "extra5")
	private String extra5;        //预留参数
	
	
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

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getEquipmentTag() {
		return equipmentTag;
	}

	public void setEquipmentTag(String equipmentTag) {
		this.equipmentTag = equipmentTag;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSysVersion() {
		return sysVersion;
	}

	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getExtra1() {
		return extra1;
	}

	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}

	public String getExtra2() {
		return extra2;
	}

	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}

	public String getExtra3() {
		return extra3;
	}

	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}

	public String getExtra4() {
		return extra4;
	}

	public void setExtra4(String extra4) {
		this.extra4 = extra4;
	}

	public String getExtra5() {
		return extra5;
	}

	public void setExtra5(String extra5) {
		this.extra5 = extra5;
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

	public String getTakeupStatus() {
		return takeupStatus;
	}

	public void setTakeupStatus(String takeupStatus) {
		this.takeupStatus = takeupStatus;
	}

	public Date getTakeupTime() {
		return takeupTime;
	}

	public void setTakeupTime(Date takeupTime) {
		this.takeupTime = takeupTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getMobileURL() {
		return mobileURL;
	}

	public void setMobileURL(String mobileURL) {
		this.mobileURL = mobileURL;
	}

	public BigDecimal getDaylimit() {
		return daylimit;
	}

	public void setDaylimit(BigDecimal daylimit) {
		this.daylimit = daylimit;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	
	
}
