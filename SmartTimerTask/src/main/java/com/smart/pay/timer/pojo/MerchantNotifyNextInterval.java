package com.smart.pay.timer.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "t_merchant_notify_next_interval")
public class MerchantNotifyNextInterval implements Serializable{

		private static final long serialVersionUID = 1L;


		@Id
		@Column(name = "id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

	 	
	    @Column(name = "cur_index")
	    private int  curIndex;

	    /***以秒为单�?*/
	    @Column(name = "interval_time")
	    private int  intervalTime;

	    @Column(name = "create_time")
	    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	    private Date              createTime;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public int getCurIndex() {
			return curIndex;
		}

		public void setCurIndex(int curIndex) {
			this.curIndex = curIndex;
		}

		public int getIntervalTime() {
			return intervalTime;
		}

		public void setIntervalTime(int intervalTime) {
			this.intervalTime = intervalTime;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		
	    
}
