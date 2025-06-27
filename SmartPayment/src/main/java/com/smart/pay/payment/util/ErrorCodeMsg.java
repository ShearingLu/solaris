package com.smart.pay.payment.util;

public enum ErrorCodeMsg {

	
	NOTWHITELIST("1001", "未设置白名单或IP未在白名单内"),
	SIGNERROR("1002", "签名错误"), 
	ERRORPARAM("1003", "参数的格式有误"), 
	REQUESTHASEXIST("1004", "订单已经被提交"),
	MERCHANTNOEXIST("1005", "商户未注册或信息不完整"),
	MERCHANTNORATE("1006", "商家未设置费率"),
	CHANNELERROR("1007", "通道异常,请再次重试"),
	VERSIONERROR("1008", "版本号有误"),
	CHARSETERROR("1009", "字符集有误"),
	SIGNTYPEERROR("1010", "签名类型有误"),
	NOTENOUGHERROR("1011", "缺少必传参数"),
    ORDERNOTEXISTERROR("1012", "平台订单号不存在"),
	SERVICENOSUPPORTERROR("1013", "请求服务不支持或没有开通"),
	AMOUTERROR("1014", "金额格式不正确,支持小数点两位"),
	MERORDERNOTEXISTERROR("1015", "商家订单号不存在"),
	CHANNELNOTOPENERROR("1016", "商家支付未开通"),
	NOPAYAUTHERROR("1017", "商家交易授权失败"),
	MERCHANTAMOUTLIMITERROR("1018", "商家该通道交易已经满额,请选择其他通道"),
	PARAMSMUST("1019", "缺少必传参数"),
	MAXLIMIT("1020", "交易额度不在范围内"),
	WEIXINMAXLIMIT("1021", "微信支付最大限额为499元"),
	NOPRIVILEDGE("1022", "没有提现权限"),
	OUTOFAMOUNTRANGE("1023", "超出单笔提现金额范围"),
	OUTOFDAYAMOUNTRANGE("1024", "超出日提现金额范围"),
	OUTOFUSERBALANCE("1025", "超出账户余额"),
	NONAMESYSERROR("1026", "未知错误");
	private String errorCode ;
    private String errormsg ;
     
    private ErrorCodeMsg(String errorCode , String errormsg){
        this.errorCode = errorCode ;
        this.errormsg = errormsg ;
    }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
     
    
    
    
    
}
