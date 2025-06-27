package com.smart.pay.merchant.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="t_merchant_user_register")
public class MerchantUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="merchant_id")
    private String merchantId;

    @Column(name="channel_tag")
    private String channelTag;

    @Column(name="channel_id")
    private Integer channelId;

    @Column(name="phone")
    private String phone;

    @Column(name="id_card")
    private String idCard;

    @Column(name="sub_merchant_id")
    private String subMerchantId;

    @Column(name="sub_rate")
    private BigDecimal subRate;

    @Column(name="sub_extra_fee")
    private BigDecimal subExtraFee;

    @Column(name="status")
    private Integer status;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_time;

    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

    public void setSubMerchantId(String subMerchantId) {
        this.subMerchantId = subMerchantId;
    }

    public BigDecimal getSubRate() {
        return subRate;
    }

    public void setSubRate(BigDecimal subRate) {
        this.subRate = subRate;
    }

    public BigDecimal getSubExtraFee() {
        return subExtraFee;
    }

    public void setSubExtraFee(BigDecimal subExtraFee) {
        this.subExtraFee = subExtraFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

}
