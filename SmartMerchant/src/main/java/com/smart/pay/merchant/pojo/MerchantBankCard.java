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

/**商家提现绑定的结算卡*/
@Entity
@Table(name = "t_merchant_bank_card")
public class MerchantBankCard implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "merchant_id")
	private String merchantId;
	

    @Column(name = "merchant_name")
    private String            merchantName;

    /** 银行卡名字 */
    @Column(name = "bank_name")
    private String            bankName;

    @Column(name = "bank_code")
    private String            bankCode;              // 总行联行号

    @Column(name = "branch_code")
    private String            branchCode;            // 开户行联行号

    /** 银行卡品牌 */
    @Column(name = "bank_brand")
    private String            bankBrand;

    /** 卡的号码 */
    @Column(name = "card_no")
    private String            cardNo;

    /** 联行号 */
    @Column(name = "line_no")
    private String            lineNo;

    /** 预留手机号码 */
    @Column(name = "phone")
    private String            phone;

    /** 身份证号 */
    @Column(name = "id_card")
    private String            idcard;

    /** 卡的类型， 1借记卡，2信用卡 */
    @Column(name = "card_type")
    private String            cardType;

    /** 0对私还1对公帐户 */
    @Column(name = "pri_or_pub")
    private String            priOrPub         = "0";

    /**
     * 区别
     **/
    @Column(name = "nature")
    private String            nature;

    /** 使用状态0正常1禁用 */
    @Column(name = "state")
    private String            state="0";

    /** 是否默认结算卡 */
    @Column(name = "is_def")
    private String            idDef;

    /** logo */
    @Column(name = "logo")
    private String            logo;


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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBankBrand() {
		return bankBrand;
	}

	public void setBankBrand(String bankBrand) {
		this.bankBrand = bankBrand;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPriOrPub() {
		return priOrPub;
	}

	public void setPriOrPub(String priOrPub) {
		this.priOrPub = priOrPub;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIdDef() {
		return idDef;
	}

	public void setIdDef(String idDef) {
		this.idDef = idDef;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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
	

	
	
	
	
}
