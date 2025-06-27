package com.smart.pay.common.tools;




public interface Constss {

    public static final String ERROR_VERI_CODE                  = "000002";

    public static final String ERROR_PARAM                      = "000001";

    public static final String ERROR_PASS_ERROR                 = "000003";

    public static final String ERROR_PAY_PASS                   = "000004";

    public static final String ERROR_TOKEN                      = "000005";

    public static final String ERROR_CARD_FAILED                = "000006";
    
    
    public static final String ERROR_MERCHANT_NOT_EXISTED       =  "000777";
    
    public static final String ERROR_MERCHANT_RATE_NOT_EXISTED  =  "000771";

    public static final String REDIRECT_CARD                    = "77";

    public static final String ERROR_USER_NOT_EXISTED           = "000007";

    public static final String ERROR_USER_HAS_REGISTER          = "000008";

    public static final String ERROR_CARD_ERROR                 = "000009";

    /** 支付请求错误 */
    public static final String ERROR_AMOUNT_ERROR               = "000010";

    public static final String ERRRO_ORDER_HAS_CHECKED          = "000011";

    public static final String ERROR_USER_BLACK                 = "000012";

    public static final String ERROR_USER_NO_REGISTER           = "000013";

    /** 提现下单错误 */
    public static final String ERROR_WITHDRAW_ORDER_FAIL        = "000014";

    public static final String ERROR_USER_NO_DEFAULT_CARD       = "000015";

    public static final String ERROR_WITHDRAW_REQ_FAILD         = "000016";

    public static final String ERROR_WITHDRAW_BALANCE_NO_ENOUGH = "000017";

    public static final String ERROR_PAYMENT_NOT_EXIST          = "000019";

    public static final String OUT_TRADE_TIME                   = "000025";

    /** 该笔订单已经提交 */
    public static final String ERROR_WITHDRAW_ORDER_HASREQ      = "000020";

    /** 签名无效 */
    public static final String ERROR_SIGN_NOVALID               = "000018";

    public static final String SUCCESS                          = "000000";      // 已成功，弹窗提示

    public static final String FALIED                           = "999999";      // 已失败，弹窗提示

    public static final String UNKNOW                           = "222222";      // 未知异常

    public static final String WAIT_CHECK                       = "666666";

    public static final String PROCESSING                       = "111111";      // 处理中，弹窗提示

    public static final String SECRETKEY                        = "tq-20170822";

    public static final String RESULT                           = "result";

    public static final String STATUS_VALID                     = "0";

    public static final String STATUS_INVALID                   = "1";

    public static final String RESP_CODE                        = "resp_code";
    
    
    public static final String ERROR_CODE                        = "err_code";
    
    public static final String ERROR_MSG                        =  "err_msg";

    public static final String RESP_MESSAGE                     = "resp_message";

    public static final String REDIRECT_URL                     = "redirect_url";

    /** 品牌类型 */
    public static final String BRAND_MAIN                       = "0";

    public static final String BRAND_OTHER                      = "1";

    /** 银行卡的默认状态 */
    public static final String CARD_DEFAULT                     = "1";

    public static final String CARD_NOT_DEFAULT                 = "0";

    /** 充值/支付/代付的订单状态 */
    public static final String ORDER_READY                      = "0";

    public static final String ORDER_SUCCESS                    = "1";

    public static final String ORDER_CANCEL                     = "2";

    public static final String ORDER_FALIED                     = "5";

    public static final String ORDER_INIT                       = "9";           // 初始化

    // 提现订单等待查询
    public static final String ORDER_WAITING_QUERY              = "3";

    // 等待清算
    public static final String ORDER_WAITING_CLEARING           = "4";

    /** 充值/支付/提现/退款的类型标识 */
    public static final String ORDER_TYPE_TOPUP                 = "0";

    public static final String ORDER_TYPE_PAY                   = "1";

    public static final String ORDER_TYPE_WITHDRAW              = "2";

    public static final String ORDER_TYPE_REFUND                = "3";

    public static final String ORDER_TYPE_BILL                  = "4";

    public static final String ORDER_TYPE_WZDJ                  = "5";

    public static final String ORDER_TYPE_WZDJ_PAY              = "6";

    /** 积分类型 */
    public static final String COIN_TYPE_ADD                    = "0";

    public static final String COIN_TYPE_SUB                    = "1";

    /** 订单的结算类型 */
    public static final String CLEARING_T_0                     = "0";

    public static final String CLEARING_T_1                     = "1";

    public static final String CLEARING_D_0                     = "2";

    public static final String CLEARING_D_1                     = "3";

    /** 活动错误信息 */
    // 用户已经获取
    public static final String MARKETING_HAS_ACQUIRE            = "000021";

    // 交易已经关闭
    public static final String MARKETING_ACTIVITY_CLOSE         = "000022";

    // 当前角色不能享受返现
    public static final String MARKETING_NO_AUTH                = "000023";

    /** 返现金额超出设置的最大值 */
    public static final String MARKETING_OUT_MAX_MONEY          = "000024";

    /** 普通消费者 **/
    public static final String NORMAL_USER                      = "0";

    /***/
    public static final String AGENT_USER                       = "1";

    /** 提交的数据错误 */
    public static final String POST_DATA_ERROR                  = "888888";

}
