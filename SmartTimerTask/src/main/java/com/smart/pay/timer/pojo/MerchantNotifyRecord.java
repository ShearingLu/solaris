package com.smart.pay.timer.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
@Table(name = "t_merchant_notify_record")
public class MerchantNotifyRecord implements Serializable{

	
		 	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

	    @Column(name = "notify_url")
	    private String            notifyURL;

	    @Column(name = "params")
	    private String            params;
	    
	    @Column(name = "merchant_id")
	    private String merchantId;

	    /** 0表示成功 �? 1表示回调 */
	    @Column(name = "status")
	    private String            status;

	    /** 默认�?24�? */
	    @Column(name = "remain_count")
	    private int               remainCnt;

	    @Column(name = "next_time")
	    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	    private Date              nextCallTime;

	    @Column(name = "create_time")
	    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	    private Date              createTime;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getNotifyURL() {
			return notifyURL;
		}

		public void setNotifyURL(String notifyURL) {
			this.notifyURL = notifyURL;
		}

		public String getParams() {
			return params;
		}

		public void setParams(String params) {
			this.params = params;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getRemainCnt() {
			return remainCnt;
		}

		public void setRemainCnt(int remainCnt) {
			this.remainCnt = remainCnt;
		}

		public Date getNextCallTime() {
			return nextCallTime;
		}

		public void setNextCallTime(Date nextCallTime) {
			this.nextCallTime = nextCallTime;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
	
	
	    
	    
	    
}
