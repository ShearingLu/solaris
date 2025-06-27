package com.smart.pay.channel.pojo;

import java.io.Serializable;
import java.math.BigDecimal;


public class PaymentOrder implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private long id;

	private String merchantId;

	private String parentId;

	private String merchantName;

	private String orderCode;

	private String merOrderCode;

	private String realChannelOrderCode;

	/**订单状态
	 * 0待完成 
	 * 
	 * 1表示成功 
	 * 
	 * 
	 * 2表示失败
	 * 
	 * 
	 * 3表示关闭
	 * 
	 * **/
	private String orderStatus;

	private String  bankCard;

	//用户名
	private String  userName;
	//身份证
	private String  idCard;
	/**
	 * 0表示收单
	 * 1表示提现
	 * */
	private String orderType;
	/**订单金额*/
	private BigDecimal amount;
	
	/**订单名称**/

	private String productName;
	
	
	/***交易费率 商家的费率*/

	private BigDecimal merRate;
	
	
	/**单笔额外手续费*/

	private BigDecimal extraFee;
	
	/**实际到账金额*/
	private BigDecimal realAmount;
	
	
	/**商家接入的通道**/
	private String channelName;
	
	/**商家接入的通道code*/

	private String channelCode;
	
	private String realtype;
	
	/**目标实际走的通道*/
	private String realChannelCode;
	
	/***目标实际走的通道的名字*/
	private String realChannelName;

	private String attach;
	
	/***交易费率真实的费率*/
	private BigDecimal realChannelRate;
	
	
	/**交易费率真是的费用*/
	private BigDecimal realChannelExtraFee;
	
	/**回调地址**/
	private String notifyURL;
	
	/**同步回调地址**/
	private String returnURL;

	private String equipmentTag;

	private String createTime;
	
	private String updateTime;
	//通道类型
	private String  channelTag;
	//订单类型   10消费  11还款   1进件  2绑卡
	private String  realOrderType;

	private String  extra;

	private String city;
	//电话
	private String phone;

	//子商户号
	private String subMerchantNo;
	//通道绑卡id
	private String bindId;
	//请求ip地址
	private String  ipAddress;
	//银行卡号
	private String bankCardNumber;
	//银行名称
	private String bankName;
    //银行账户姓名
	private String bankAccountName;
	//cvv码
	private String securityCode;
	//银行卡有效期
	private String expiredTime;

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

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


	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getMerOrderCode() {
		return merOrderCode;
	}


	public void setMerOrderCode(String merOrderCode) {
		this.merOrderCode = merOrderCode;
	}


	public String getRealChannelOrderCode() {
		return realChannelOrderCode;
	}


	public void setRealChannelOrderCode(String realChannelOrderCode) {
		this.realChannelOrderCode = realChannelOrderCode;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getMerRate() {
		return merRate;
	}


	public void setMerRate(BigDecimal merRate) {
		this.merRate = merRate;
	}


	public BigDecimal getRealAmount() {
		return realAmount;
	}


	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}


	public String getChannelName() {
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getChannelCode() {
		return channelCode;
	}


	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}


	


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getNotifyURL() {
		return notifyURL;
	}


	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}


	public String getReturnURL() {
		return returnURL;
	}


	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}


	public BigDecimal getExtraFee() {
		return extraFee;
	}


	public void setExtraFee(BigDecimal extraFee) {
		this.extraFee = extraFee;
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


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public BigDecimal getRealChannelRate() {
		return realChannelRate;
	}


	public void setRealChannelRate(BigDecimal realChannelRate) {
		this.realChannelRate = realChannelRate;
	}


	public BigDecimal getRealChannelExtraFee() {
		return realChannelExtraFee;
	}


	public void setRealChannelExtraFee(BigDecimal realChannelExtraFee) {
		this.realChannelExtraFee = realChannelExtraFee;
	}


	public String getAttach() {
		return attach;
	}


	public void setAttach(String attach) {
		this.attach = attach;
	}


	public String getRealtype() {
		return realtype;
	}


	public void setRealtype(String realtype) {
		this.realtype = realtype;
	}


	public String getEquipmentTag() {
		return equipmentTag;
	}


	public void setEquipmentTag(String equipmentTag) {
		this.equipmentTag = equipmentTag;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getChannelTag() {
		return channelTag;
	}

	public void setChannelTag(String channelTag) {
		this.channelTag = channelTag;
	}

	public String getRealOrderType() {
		return realOrderType;
	}

	public void setRealOrderType(String realOrderType) {
		this.realOrderType = realOrderType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSubMerchantNo() {
		return subMerchantNo;
	}

	public void setSubMerchantNo(String subMerchantNo) {
		this.subMerchantNo = subMerchantNo;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	@Override
	public String toString() {
		return "PaymentOrder{" +
				"id=" + id +
				", merchantId='" + merchantId + '\'' +
				", parentId='" + parentId + '\'' +
				", merchantName='" + merchantName + '\'' +
				", orderCode='" + orderCode + '\'' +
				", merOrderCode='" + merOrderCode + '\'' +
				", realChannelOrderCode='" + realChannelOrderCode + '\'' +
				", orderStatus='" + orderStatus + '\'' +
				", bankCard='" + bankCard + '\'' +
				", userName='" + userName + '\'' +
				", idCard='" + idCard + '\'' +
				", orderType='" + orderType + '\'' +
				", amount=" + amount +
				", productName='" + productName + '\'' +
				", merRate=" + merRate +
				", extraFee=" + extraFee +
				", realAmount=" + realAmount +
				", channelName='" + channelName + '\'' +
				", channelCode='" + channelCode + '\'' +
				", realtype='" + realtype + '\'' +
				", realChannelCode='" + realChannelCode + '\'' +
				", realChannelName='" + realChannelName + '\'' +
				", attach='" + attach + '\'' +
				", realChannelRate=" + realChannelRate +
				", realChannelExtraFee=" + realChannelExtraFee +
				", notifyURL='" + notifyURL + '\'' +
				", returnURL='" + returnURL + '\'' +
				", equipmentTag='" + equipmentTag + '\'' +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", channelTag='" + channelTag + '\'' +
				", realOrderType='" + realOrderType + '\'' +
				", extra='" + extra + '\'' +
				", phone='" + phone + '\'' +
				", ipAddress='" + ipAddress + '\'' +
				'}';
	}
}
