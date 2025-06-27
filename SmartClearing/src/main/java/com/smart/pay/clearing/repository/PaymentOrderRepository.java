package com.smart.pay.clearing.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.pay.clearing.pojo.PaymentOrder;


@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, String>, JpaSpecificationExecutor<PaymentOrder> {

		
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.orderCode=:orderCode")
	  PaymentOrder findPaymentOrderBycode(@Param("orderCode") String orderCode);
		
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merOrderCode=:merOrderCode")
	  PaymentOrder findPaymentOrderBymerOrderCode(@Param("merOrderCode") String merOrderCode);
	
	  
	  @Modifying
	  @Query(value = "insert into smarttimer.t_merchant_notify_record(merchant_id, status, notify_url, params, remain_count, next_time, create_time) values(:merchantid, :status, :notifyurl, :params, :remaincount, :nexttime, :createtime)", nativeQuery = true)
	  void insertTimers(@Param("merchantid") String merchantId, @Param("status") String status, @Param("notifyurl") String notifyurl, @Param("params") String params, @Param("remaincount") int remaincount, @Param("nexttime") Date nexttime, @Param("createtime") Date createtime);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.realChannelOrderCode=:realChannelOrderCode")
	  PaymentOrder findPaymentOrderByrealChannelOrderCode(@Param("realChannelOrderCode") String realChannelOrderCode);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByMerchantid(@Param("merchantId") String merchantId, @Param("orderType") String orderType, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByOrderType(@Param("orderType") String orderType, Pageable pageAble);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidAndStatus(@Param("merchantId") String merchantId, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidAndStatus(@Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("orderType") String orderType, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartDate(@Param("startTime") Date startTime, @Param("orderType") String orderType, Pageable pageAble);
	  
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartDateAndStatus(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("orderType") String orderType,  @Param("orderStatus") String orderStatus,  Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartDateAndStatus(@Param("startTime") Date startTime, @Param("orderType") String orderType,  @Param("orderStatus") String orderStatus,  Pageable pageAble);
	  
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartEndDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, Pageable pageAble);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  List<PaymentOrder>  findPaymentOrdersByMerchantidStartEndDateAndStatus(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>:startTime and paymentorder.orderType='0' and paymentorder.orderStatus='0' order by paymentorder.createTime")
	  List<PaymentOrder>  findNoFinishPaymentOrders(@Param("startTime") Date startTime);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartEndDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, Pageable pageAble);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartEndDateAndStatus(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByMerchantidStartEndDateAndStatus(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and (paymentorder.orderCode=:orderCode or paymentorder.merOrderCode=:orderCode or paymentorder.realChannelOrderCode=:orderCode) and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findMerchantPaymentOrderBycode(@Param("merchantId") String merchantId, @Param("orderCode") String orderCode, @Param("orderType") String orderType, Pageable pageAble);

	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where (paymentorder.orderCode=:orderCode or paymentorder.merOrderCode=:orderCode or paymentorder.realChannelOrderCode=:orderCode) and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findMerchantPaymentOrderBycode(@Param("orderCode") String orderCode, @Param("orderType") String orderType, Pageable pageAble);
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType")
	  BigDecimal  findSumAmountByMerchantidStartDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("orderType") String orderType);
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType  and paymentorder.channelCode=:channelCode")
	  BigDecimal  findSumAmountByMerchantidStartDateAndChannelCode(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("orderType") String orderType, @Param("channelCode") String channelCode);
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType")
	  BigDecimal  findSumAmountByMerchantidStartEndDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.equipmentTag=:equipmentTag and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType")
	  BigDecimal  findSumAmountByEquipmentTagStartEndDate(@Param("equipmentTag") String equipmentTag, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType and paymentorder.channelCode=:channelCode")
	  BigDecimal  findSumAmountByMerchantidStartEndDateAndChannelcode(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("channelCode") String channelCode);
	  
	  @Query("select count(paymentorder.id) from  PaymentOrder paymentorder where paymentorder.merchantId=:merchantId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType")
	  int  findTotalCntByMerchantidStartEndDate(@Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
	 
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByparentId(@Param("parentId") String parentId, @Param("orderType") String orderType, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByparentIdStatus(@Param("parentId") String parentId, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPlatformPaymentOrderByordertype(@Param("orderType") String orderType, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.orderType=:orderType and  paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPlatformPaymentOrderByordertypeAndStatus(@Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByparentIdStartDate(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("orderType") String orderType, Pageable pageAble);
	 
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByparentIdStartDateAndStatus(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByStartDate(@Param("startTime") Date startTime, @Param("orderType") String orderType, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByStartDateAndStatus(@Param("startTime") Date startTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByparentIdStartEndDate(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, Pageable pageAble);

	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByparentIdStartEndDateStatus(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);

	  
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where  paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType")
	  Page<PaymentOrder>  findPaymentOrderByStartEndDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, Pageable pageAble);

	  @Query("select paymentorder from  PaymentOrder paymentorder where  paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderType=:orderType and paymentorder.orderStatus=:orderStatus")
	  Page<PaymentOrder>  findPaymentOrderByStartEndDateAndStatus(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType, @Param("orderStatus") String orderStatus, Pageable pageAble);
	  
	  @Query("select paymentorder from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and (paymentorder.orderCode=:orderCode or paymentorder.merOrderCode=:orderCode or paymentorder.realChannelOrderCode=:orderCode)")
	  Page<PaymentOrder>  findAgentPaymentOrderBycode(@Param("parentId") String parentId, @Param("orderCode") String orderCode, Pageable pageAble);

	  @Query("select paymentorder from  PaymentOrder paymentorder where (paymentorder.orderCode=:orderCode or paymentorder.merOrderCode=:orderCode or paymentorder.realChannelOrderCode=:orderCode)")
	  Page<PaymentOrder>  findPlatformPaymentOrderBycode(@Param("orderCode") String orderCode, Pageable pageAble);
	  
	
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType ")
	  BigDecimal  findSumAmountByAgentidStartDate(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("orderType") String orderType);
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.parentId=:parentId and paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType ")
	  BigDecimal  findSumAmountByAgentidStartEndDate(@Param("parentId") String parentId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
	  
	  @Query("select sum(paymentorder.amount) from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType ")
	  BigDecimal  findPlatformSumAmountByStartEndDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
	  
	  @Query("select count(paymentorder.id) from  PaymentOrder paymentorder where paymentorder.createTime>=:startTime and paymentorder.createTime<:endTime and paymentorder.orderStatus='1' and paymentorder.orderType=:orderType ")
	  int  findPlatformTotalCntByStartEndDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orderType") String orderType);
	  
}


