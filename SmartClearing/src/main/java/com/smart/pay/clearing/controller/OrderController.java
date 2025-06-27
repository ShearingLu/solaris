package com.smart.pay.clearing.controller;

import com.smart.pay.clearing.business.OrderBusiness;
import com.smart.pay.clearing.business.PaymentOrderDealBusiness;
import com.smart.pay.clearing.business.WithdrawOrderDealBusiness;
import com.smart.pay.clearing.business.impl.AsyncTaskService;
import com.smart.pay.clearing.pojo.OnlinePaymentOrder;
import com.smart.pay.clearing.pojo.OrderMarkupLog;
import com.smart.pay.clearing.pojo.PaymentOrder;
import com.smart.pay.common.tools.Constss;
import com.smart.pay.common.tools.DateUtil;
import com.smart.pay.common.tools.RandomUtils;
import com.smart.pay.common.tools.ResultWrap;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderBusiness orderBusiness;
	
	
	@Autowired
	private PaymentOrderDealBusiness paymentOrderDealBusiness;
	
	@Autowired
	private WithdrawOrderDealBusiness withdrawOrderDealBusiness;
	
	
	@Autowired
	private AsyncTaskService asynTaskService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/notify/test")
	public @ResponseBody Object testnotify(HttpServletRequest request) {
		paymentOrderDealBusiness.createNotifyRecord("12313", "http://localhost", "{'12313':'12313'}");
			return null;
	}
	
	
	/***进行账单自动补单机制*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/markup/online")
	public @ResponseBody Object onlineMarkup(HttpServletRequest request) {
		logger.info("在线账单补单开始。。。。。。。。。。。。。");
		List<OnlinePaymentOrder> onlinePaymentOrders = asynTaskService.getAllMarkupOrders();
		for(OnlinePaymentOrder onlinePaymentOrder : onlinePaymentOrders) {
			asynTaskService.markupOrder2(onlinePaymentOrder);
		}
		
		logger.info("在线账单补单结束。。。。。。。。。。。。。");
		
		return null;
	}
	
	/***进行补单操作**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/markup/auto")
	public @ResponseBody Object markupAuto(HttpServletRequest request) {
		
		/**拿到最后一条统计的未完成的订单*/
		OrderMarkupLog  lastestOrderMarkupLog  = orderBusiness.queryLastestOrderMarkupLog();
		Date priStatisticsOrderDate = null;
		if(lastestOrderMarkupLog == null) {
			priStatisticsOrderDate = new Date();
		}else {
			priStatisticsOrderDate = lastestOrderMarkupLog.getEndOrderTime();
		}
		
		logger.info("补单就开始。。。。。。。。。。。。。");
		
		//将所有从上一次开始到目前为止未统计的订单获取到		
		List<PaymentOrder>  paymentOrders = orderBusiness.queryAllNoFinishOrders(priStatisticsOrderDate);
		
		int markupCnt = 0;
		Date startOrderTime = null;
		Date endOrderTime   = null;
		for(int i=0;  i<paymentOrders.size();  i++) {
			
			if(i==0) {
				startOrderTime = paymentOrders.get(i).getCreateTime();
			}
			
			
			if(i== paymentOrders.size()-1) {
				endOrderTime = paymentOrders.get(i).getCreateTime();
			}
			
			asynTaskService.makeupOrder(paymentOrders.get(i));
			
		}
		
		if(endOrderTime != null) {
			OrderMarkupLog orderMarkupLog = new OrderMarkupLog();
			
			orderMarkupLog.setEndOrderTime(endOrderTime);
			orderMarkupLog.setMarkupCnts(0);
			orderMarkupLog.setStatisticsDate(new Date());
			orderMarkupLog.setStartOrderTime(startOrderTime);
			orderMarkupLog.setTotalCnts(paymentOrders ==null ? 0: paymentOrders.size());
			orderBusiness.saveOrderMarkupLog(orderMarkupLog);
		}
		logger.info("补单就结束。。。。。。。。。。。。。");
		return null;
	
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/update/equipmenttag")
	public @ResponseBody Object updateEquipmentTag(HttpServletRequest request, 
		@RequestParam("order_code") String ordercode,
		@RequestParam(value="equipment_tag") String equipmentTag
		) {
		
		PaymentOrder order = orderBusiness.queryPaymentOrderBycode(ordercode);
		
		order.setEquipmentTag(equipmentTag);
		
		order = orderBusiness.createPaymentOrder(order);
		return ResultWrap.init(Constss.SUCCESS, "成功", order);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/withdraw/new")
	public @ResponseBody Object createNewWithdrawOrder(HttpServletRequest request, 
		@RequestParam("merchant_id") String merchantid,
		@RequestParam(value="merchant_name") String merchantName,
		@RequestParam("parent_id") String parentid,
		@RequestParam(value="order_type", required=false, defaultValue="1") String orderType,
		@RequestParam(value="settle_peroid", required=false, defaultValue="0") String settleperoid,
		@RequestParam(value="amount") BigDecimal amount,
		@RequestParam(value="order_code", required=false) String orderCode,
		@RequestParam(value="product_name", required=false,  defaultValue="提现") String productName,
		@RequestParam(value="attach", required=false) String attach,
		@RequestParam(value="extra_fee", required=false, defaultValue="0") String extraFee,
		@RequestParam(value="merchant_order_code", required=false) String merchantOrderCode
		
		) {
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setCreateTime(new Date());
		paymentOrder.setExtraFee(BigDecimal.ZERO);
		paymentOrder.setMerchantId(merchantid);
		paymentOrder.setMerchantName(merchantName);
		if(orderCode != null && !orderCode.equalsIgnoreCase("")) {
			paymentOrder.setOrderCode(orderCode);
		}else {
			paymentOrder.setOrderCode(RandomUtils.generateNewLowerString(20));			
		}
		paymentOrder.setOrderStatus("0");//刚开始待完成
		paymentOrder.setOrderType(orderType);
		paymentOrder.setParentId(parentid);
		paymentOrder.setProductName(productName);
		paymentOrder.setRealAmount(amount.subtract(new BigDecimal(extraFee)));
		paymentOrder.setAttach(attach);
		paymentOrder.setUpdateTime(new Date());
		paymentOrder.setExtraFee(new BigDecimal(extraFee));
		paymentOrder.setSettlePeroid(Integer.parseInt(settleperoid));
		if(merchantOrderCode != null && !merchantOrderCode.equalsIgnoreCase("")) {
			paymentOrder.setMerOrderCode(merchantOrderCode);
		}
		
		
		try {
			orderBusiness.createPaymentOrder(paymentOrder);
			return ResultWrap.init(Constss.SUCCESS, "成功", paymentOrder);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResultWrap.init(Constss.FALIED, "创建失败", "");
		}
		
	}
	
	
	
	/**创建待支付一笔订单*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/new")
	public @ResponseBody Object createNewOrder(HttpServletRequest request, 
		@RequestParam("merchant_id") String merchantid,
		@RequestParam(value="merchant_name") String merchantName,
		@RequestParam("parent_id") String parentid,
		@RequestParam("mer_rate") BigDecimal merRate,
		@RequestParam(value="extra_fee", defaultValue="0", required=false) BigDecimal extraFee,
		@RequestParam(value="mer_order_code", required=false) String merOrdercode,
		@RequestParam(value="order_type", required=false, defaultValue="0") String orderType,
		@RequestParam(value="amount") BigDecimal amount,
		@RequestParam(value="product_name") String productName,
		@RequestParam(value="channel_code") String channelCode,
		@RequestParam(value="channel_name", required=false) String channelName,
		@RequestParam(value="real_channel_code") String realChannelCode,
		@RequestParam(value="real_channel_name", required=false) String realChannelName,
		@RequestParam(value="notify_url", required=false) String notifyURL,
		@RequestParam(value="return_url", required=false) String returnURL,
		@RequestParam("real_channel_rate") BigDecimal realChannelRate,
		@RequestParam(value="real_type", required=false) String realType,
		@RequestParam(value="attach", required=false, defaultValue="") String attach,
		@RequestParam(value="real_channel_extra_fee", defaultValue="0", required=false) BigDecimal realChannelExtraFee,
		@RequestParam(value="phone") String phone,
		@RequestParam(value="idCard") String idCard,
		@RequestParam(value="bankCardNumber") String bankCardNumber,
		@RequestParam(value="bankAccountName") String bankAccountName
		) {
		//将订单金额（分）恢复实际金额
		amount=processAmountDivide(amount);
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setChannelCode(channelCode);
		paymentOrder.setChannelName(channelName);
		paymentOrder.setAttach(attach);
		paymentOrder.setCreateTime(new Date());
		paymentOrder.setExtraFee(extraFee);
		paymentOrder.setMerchantId(merchantid);
		paymentOrder.setMerchantName(merchantName);
		paymentOrder.setMerOrderCode(merOrdercode);
		paymentOrder.setMerRate(merRate);
		paymentOrder.setNotifyURL(notifyURL);
		paymentOrder.setOrderCode(RandomUtils.generateNewLowerString(20));
		paymentOrder.setOrderStatus("0");//刚开始待完成
		paymentOrder.setOrderType(orderType);
		paymentOrder.setParentId(parentid);
		paymentOrder.setProductName(productName);
		BigDecimal fee = amount.multiply(merRate).add(extraFee);
		paymentOrder.setRealAmount(amount.subtract(fee));
		paymentOrder.setRealChannelCode(realChannelCode);
		paymentOrder.setRealChannelName(realChannelName);
		paymentOrder.setReturnURL(returnURL);
		paymentOrder.setUpdateTime(new Date());
		paymentOrder.setRealChannelRate(realChannelRate);
		paymentOrder.setRealChannelExtraFee(realChannelExtraFee);
		paymentOrder.setRealtype(realType);
		paymentOrder.setIdCard(idCard);
		paymentOrder.setPhone(phone);
		paymentOrder.setBankCardNumber(bankCardNumber);
		paymentOrder.setBankAccountName(bankAccountName);
		
		try {
			orderBusiness.createPaymentOrder(paymentOrder);
			return ResultWrap.init(Constss.SUCCESS, "成功", paymentOrder);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResultWrap.init(Constss.FALIED, "创建失败", "");
		}
			
		
	}

	public BigDecimal processAmountDivide(BigDecimal amount){
		amount=amount.divide(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN);
		return amount;
	}
	
	
	 private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet)  
	    {  
	        HSSFRow row = sheet.createRow(0);  
	        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度  
	        sheet.setColumnWidth(2, 12*256);  
	        sheet.setColumnWidth(3, 17*256);  
	          
	        //设置为居中加粗  
	        HSSFCellStyle style = workbook.createCellStyle();  
	        HSSFFont font = workbook.createFont();    
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        style.setFont(font);  
	          
	        HSSFCell cell;  
	        cell = row.createCell(0);  
	        cell.setCellValue("序号");  
	        cell.setCellStyle(style);  
	          
	        cell = row.createCell(1);  
	        cell.setCellValue("商家编号");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell(2);  
	        cell.setCellValue("商家订单号");  
	        cell.setCellStyle(style); 
	        
	        
	        cell = row.createCell(3);  
	        cell.setCellValue("平台号");  
	        cell.setCellStyle(style);  
	          
	        cell = row.createCell(4);  
	        cell.setCellValue("第三方通道号");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell(5);  
	        cell.setCellValue("交易金额");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell(6);  
	        cell.setCellValue("实收金额");  
	        cell.setCellStyle(style);  
	        
	        
	        cell = row.createCell(7);  
	        cell.setCellValue("产品名称");  
	        cell.setCellStyle(style); 
	        
	        
	        cell = row.createCell(8);  
	        cell.setCellValue("额外费用");  
	        cell.setCellStyle(style); 
	        
	        cell = row.createCell(9);  
	        cell.setCellValue("渠道名称");  
	        cell.setCellStyle(style); 
	        
	        cell = row.createCell(10);  
	        cell.setCellValue("成功时间");  
	        cell.setCellStyle(style);
	        
	        
	        cell = row.createCell(11);  
	        cell.setCellValue("状态");  
	        cell.setCellStyle(style);
	    }
	
	
	/**导出excel文件**/
	@RequestMapping(method = RequestMethod.GET, value = "/smartclearing/order/excel/download")
	public String downloadExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="merchant_id") String merchantid,
			@RequestParam(value="start_time") String starttime,
			@RequestParam(value="end_time") String endtime,
			@RequestParam(value="order_status", required=false, defaultValue="1") String orderstatus
		) throws IOException, ParseException{
		
		//创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet=wb.createSheet("订单流水表");
        createTitle(wb, sheet);  
        
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = sdf.parse(starttime);
        
        Date endDate = null;
        if(endtime != null && !endtime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endtime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
        
        
        
        
        List<PaymentOrder>  paymentOrders = orderBusiness.findPaymentOrdersByMerchantidStartEndDateAndStatus(merchantid, "0", startTime, endDate, orderstatus);
      //设置日期格式  
        HSSFCellStyle style=wb.createCellStyle();  
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));  
          
        //新增数据行，并且设置单元格数据  
        int rowNum = 1;  
        for (PaymentOrder paymentorder:paymentOrders) {  
              
            HSSFRow row = sheet.createRow(rowNum);  
            row.createCell(0).setCellValue(rowNum);  
            row.createCell(1).setCellValue(paymentorder.getMerchantId());  
            row.createCell(2).setCellValue(paymentorder.getMerOrderCode());  
            row.createCell(3).setCellValue(paymentorder.getOrderCode());
            row.createCell(4).setCellValue(paymentorder.getRealChannelOrderCode());
            row.createCell(5).setCellValue(paymentorder.getAmount()+"");
            row.createCell(6).setCellValue(paymentorder.getRealAmount()+"");
            row.createCell(7).setCellValue(paymentorder.getProductName());
            row.createCell(8).setCellValue(paymentorder.getExtraFee()+"");
            row.createCell(9).setCellValue(paymentorder.getChannelName());
            HSSFCell cell = row.createCell(10);  
            cell.setCellValue(paymentorder.getUpdateTime());  
            cell.setCellStyle(style); 
            row.createCell(11).setCellValue(paymentorder.getOrderStatus().equalsIgnoreCase("1") ? "成功":"失败");
            rowNum++;  
        }  
        
        
        String filename = merchantid+"_"+starttime+"_to_"+endtime;

        //输出Excel文件
        OutputStream output=response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename="+filename+".xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
        return null;
	}
	
	
	/**回调接口*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/hand/callback")
	public @ResponseBody Object handCallback(HttpServletRequest request,
			@RequestParam(value="order_code") String orderCode
		) {
		
		
		
		String[] orders = orderCode.split(",");
		
		logger.info("进入手动回调");
		for(int i=0; i<orders.length; i++) {
			
			String ordercodeTmp =orders[i];	
			PaymentOrder order = orderBusiness.queryPaymentOrderBycode(ordercodeTmp);
			
			
			paymentOrderDealBusiness.handCallBack(order);
		}
		logger.info("处理成功了");
		return null;
	}
	
	/**
	 * 更新订单
	 * 
	 * 充值订单
	 * 如果已经是成功的那就不需要更新了。 
	 * 
	 * 如果是未成功
	 * 状态是成功的， 那就更新订单，
	 * 
	 *   发放分润订单， 
	 *   更新相应的所有分润账户。（循环）
	 * 
	 * 
	 * 提现订单
	 * 
	 * 如果成功， 更新订单， 更新账户， （解冻加扣款）
	 * 如果失败， 更新订单，  解冻账户
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/order/update")
	public @ResponseBody Object updateOrder(HttpServletRequest request,
			@RequestParam(value="order_code") String orderCode,
			@RequestParam(value="order_status") String orderStatus,
			@RequestParam(value="real_channel_order_code", required=false) String realChannelOrderCode
		) {
		
		System.out.println("交易开始处理了="+orderCode);
		PaymentOrder order = orderBusiness.queryPaymentOrderBycode(orderCode);
		if(order.getOrderStatus().equalsIgnoreCase(orderStatus)) {
			return ResultWrap.init(Constss.FALIED, "已经处理", "");
		}
		
		 Map<String, Object> resultMap = new HashMap<>();
	        
		 switch (order.getOrderType()) {
	            case Constss.ORDER_TYPE_TOPUP:
	                resultMap = paymentOrderDealBusiness.dealOrder(orderCode, orderStatus, realChannelOrderCode);
	                break;
	            case "1":
	                resultMap = withdrawOrderDealBusiness.dealOrder(orderCode, orderStatus, realChannelOrderCode);
	                break;
	            default:
	                break;
	     }

	     return resultMap;
		
	}
	
	
	
	
	/**
	 * 商家批量查询交易订单
	 * 订单号
	 * 时间 
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/paymentorder/history")
    public @ResponseBody Object queryMerchantPaymentOrderHistory(HttpServletRequest request, //
            @RequestParam(value = "merchant_id") String merchantId, //
            @RequestParam(value = "order_code", required = false) String orderCode, //
            @RequestParam(value = "order_status", required = false) String orderstatus,
            @RequestParam(value = "start_time", required = false) String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime, //
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
		Page<PaymentOrder>  pageOrders   = orderBusiness.queryPaymentOrdersByMerchantId(merchantId, orderCode, "0", startDate, endDate, orderstatus, pageable);
		return ResultWrap.init(Constss.SUCCESS, "成功", pageOrders);
		
	}
	
	
	
	/**
	 * 商家批量查询提现订单
	 * 订单号
	 * 时间 
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/withdraworder/history")
    public @ResponseBody Object queryMerchantWithdrawOrderHistory(HttpServletRequest request, //
            @RequestParam(value = "merchant_id", required=false) String merchantId, //
            @RequestParam(value = "order_code", required = false) String orderCode, //
            @RequestParam(value = "order_status", required = false) String orderstatus,
            @RequestParam(value = "start_time", required = false) String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime, //
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
		Page<PaymentOrder>  pageOrders   = orderBusiness.queryPaymentOrdersByMerchantId(merchantId, orderCode, "1", startDate, endDate, orderstatus, pageable);
		return ResultWrap.init(Constss.SUCCESS, "成功", pageOrders);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/agent/paymentorder/history")
    public @ResponseBody Object queryPlatformPaymentOrderHistory(HttpServletRequest request, //
            @RequestParam(value = "order_code", required = false) String orderCode, //订单号
            @RequestParam(value = "start_time", required = false) String startTime, //结束时间
            @RequestParam(value = "end_time", required = false) String endTime, //开始时间
            @RequestParam(value = "order_status", required = false) String orderstatus, //订单状态
            @RequestParam(value = "agent_id") String agentid, //代理商的商户id
            @RequestParam(value = "merchant_id", required = false) String merchantId, //代理商下级商户号
            @RequestParam(value = "channel_code", required = false) String channelCode, //外放通道
            @RequestParam(value = "real_channel_code", required = false) String realChannelCode, //上游通道
            @RequestParam(value = "order_type", defaultValue = "0",required = false) String orderType, //订单类型
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
//		 Page<PaymentOrder>  pageOrders   = orderBusiness.queryPlatformPaymentOrders(orderCode, orderType, startDate, endDate, orderstatus, pageable);
		 
		Map<String, Object>  pageOrders   = orderBusiness.queryAgentsPaymentOrders(orderCode, orderType, startDate, endDate, orderstatus,agentid,merchantId,channelCode,realChannelCode,pageable);
		 
		 
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageOrders);
		
		
		
		
	}
		
	
	
	
	
	
	/**
	 * 平台分页查询时间订单
	 * 订单号
	 * 时间
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/platform/paymentorder/history")
    public @ResponseBody Object queryPlatformPaymentOrderHistory(HttpServletRequest request, //
            @RequestParam(value = "order_code", required = false) String orderCode, //订单号
            @RequestParam(value = "start_time", required = false) String startTime, //结束时间
            @RequestParam(value = "end_time", required = false) String endTime, //开始时间
            @RequestParam(value = "order_status", required = false) String orderstatus, //订单状态
            @RequestParam(value = "merchant_id", required = false) String merchantId, //商户号
            @RequestParam(value = "channel_code", required = false) String channelCode, //外放通道
            @RequestParam(value = "real_channel_code", required = false) String realChannelCode, //上游通道
            @RequestParam(value = "order_type", defaultValue = "0",required = false) String orderType, //订单类型
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
//		 Page<PaymentOrder>  pageOrders   = orderBusiness.queryPlatformPaymentOrders(orderCode, orderType, startDate, endDate, orderstatus, pageable);
		 
		Map<String, Object>  pageOrders   = orderBusiness.queryPlatformPaymentOrders(orderCode, orderType, startDate, endDate, orderstatus,merchantId,channelCode,realChannelCode,pageable);
		 
		 
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageOrders);
	}
	
	
	
	/**
	 * 代理商/总平台分页查询时间提现订单
	 * 订单号
	 * 时间
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/platform/withdraworder/history")
    public @ResponseBody Object queryPlatformWithdrawOrderHistory(HttpServletRequest request, //
    		@RequestParam(value = "agent_id", required=false) String agentid, //
            @RequestParam(value = "order_code", required = false) String orderCode, //
            @RequestParam(value = "order_status", required = false) String orderstatus, //
            @RequestParam(value = "start_time", required = false) String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime, //
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, //
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction, //
            @RequestParam(value = "sort", defaultValue = "createTime", required = false) String sortProperty) throws Exception{
		
		
		
		Pageable pageable = new PageRequest(page, size, new Sort(direction, sortProperty));
		 
		Date startDate  = null;
		 
		Date endDate   = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
			 startDate = sdf.parse(startTime);
		}
		 
		 
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			Date  tempDate = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		 
		 
		 Page<PaymentOrder>  pageOrders   = orderBusiness.queryPaymentOrdersByAgentid(agentid, orderCode, "1", startDate, endDate, orderstatus, pageable);
		 return ResultWrap.init(Constss.SUCCESS, "成功", pageOrders);
	}
	
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/totalcnts/query")
    public @ResponseBody Object queryMerchantTotalCnts(HttpServletRequest request, //
            @RequestParam(value = "merchant_id") String merchantId, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime,//
            @RequestParam(value = "order_type", required = false, defaultValue="0") String orderType//
            ) throws Exception{
	
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		int totalCnt = orderBusiness.queryTotalCntByMerchantid(merchantId, orderType, startDate, endDate);
		

		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalCnt);
	
	}
	
	
	
	
	
	
	
	/**
	 * 商户按照时间统计交易总量
	 * 
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/merchant/sumamount/query")
    public @ResponseBody Object queryMerchantSumAmount(HttpServletRequest request, //
            @RequestParam(value = "merchant_id") String merchantId, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime,//
            @RequestParam(value = "order_type", required = false, defaultValue="0") String orderType,
            @RequestParam(value = "channel_code", required = false) String channelcode//
            ) throws Exception{
		
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		
		
		
		BigDecimal totalSumAmount = orderBusiness.querySumAmountByMerchantid(merchantId, orderType, channelcode, startDate, endDate);
		
		if(totalSumAmount == null) {
			totalSumAmount = BigDecimal.ZERO;
		}
		
		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalSumAmount);
	}
	
	
	
	/**
	 * 总平台按照时间统计交易总订单数
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/platform/totalcnts/query")
    public @ResponseBody Object queryPlatformTotalCnts(HttpServletRequest request, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime,//
            @RequestParam(value = "order_type", required = false, defaultValue="0") String orderType//
            ) throws Exception{
		
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		int totalCnt = orderBusiness.queryPlatformTotalCnt(orderType, startDate, endDate);
		
		
		
		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalCnt);
		
	}
	
	
	/**
	 * 总平台按照时间统计交易总交易量
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/platform/totalamount/query")
    public @ResponseBody Object queryPlatformTotalAmount(HttpServletRequest request, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime,//
            @RequestParam(value = "order_type", required = false, defaultValue="0") String orderType//
            ) throws Exception{
		
		
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		BigDecimal totalSumAmount = orderBusiness.queryPlatformSumAmount(orderType, startDate, endDate);
		
		if(totalSumAmount == null) {
			totalSumAmount = BigDecimal.ZERO;
		}
		
		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalSumAmount);
		
	}
	
	
	
	
	/**
	 * 代理商时间统计交易总量
	 * 
	 * **/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/agent/sumamount/query")
    public @ResponseBody Object queryAgentSumAmount(HttpServletRequest request, //
            @RequestParam(value = "agent_id") String platformid, //
            @RequestParam(value = "start_time") String startTime, //
            @RequestParam(value = "end_time", required = false) String endTime,//
            @RequestParam(value = "order_type", required = false, defaultValue="0") String orderType//
            ) throws Exception{
		
		
		Date startDate = null;
		Date endDate   = null;
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		if(startTime != null && !startTime.equalsIgnoreCase("")) {
				
			startDate  = sdf.parse(startTime);
			
		}
		
		
		if(endTime != null && !endTime.equalsIgnoreCase("")) {
			
			Date tempDate  = sdf.parse(endTime);
			endDate = DateUtil.getTomorrow(tempDate);
		}
		
		
		
		
		
		BigDecimal totalSumAmount = orderBusiness.querySumAmountByAgentId(platformid, orderType, startDate, endDate);
		
		if(totalSumAmount == null) {
			totalSumAmount = BigDecimal.ZERO;
		}
		
		
		return ResultWrap.init(Constss.SUCCESS, "成功", totalSumAmount);
	}
	
	
	
	
	/**根据订单号*/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/transaction/order/query")
    public @ResponseBody Object queryTransactionOrder(HttpServletRequest request, //
            @RequestParam(value = "order_code") String transactionId //      
            ) throws Exception{
		
		PaymentOrder order = orderBusiness.queryPaymentOrderBycode(transactionId);
	
		
		if(order != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", order);
		}else {
			
			return ResultWrap.init(Constss.FALIED, "订单不存在", "");
			
		}
	}
	
	
	
	/**根据商家订单号**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/transaction/merorder/query")
    public @ResponseBody Object queryTransactionMerchantOrder(HttpServletRequest request, //
            @RequestParam(value = "merchant_code") String merchantOrdercode //      
            ) throws Exception{
		
		
		PaymentOrder order = orderBusiness.queryPaymentOrderByMercode(merchantOrdercode);
	
		
		if(order != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", order);
		}else {
			
			return ResultWrap.init(Constss.FALIED, "订单不存在", "");
			
		}
	}
	
	/**根据通道的订单号**/
	@RequestMapping(method = RequestMethod.POST, value = "/smartclearing/transaction/outorder/query")
    public @ResponseBody Object queryTransactionOutOrder(HttpServletRequest request, //
            @RequestParam(value = "out_order_code") String outOrdercode //      
            ) throws Exception{
		
		PaymentOrder order = orderBusiness.queryPaymentOrderByChannelcode(outOrdercode);
	
		
		if(order != null) {
			return ResultWrap.init(Constss.SUCCESS, "成功", order);
		}else {
			
			return ResultWrap.init(Constss.FALIED, "订单不存在", "");
			
		}
	}
	
}
