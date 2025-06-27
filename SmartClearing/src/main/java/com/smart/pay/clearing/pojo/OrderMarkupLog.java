package com.smart.pay.clearing.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_markup_log")
public class OrderMarkupLog implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "statistics_date")
    private Date  statisticsDate;
    
	@Column(name = "start_order_time")
    private Date startOrderTime;

	@Column(name = "end_order_time")
    private Date endOrderTime;
    
	@Column(name = "total_cnt")
    private int totalCnts;
    
	
	@Column(name = "markup_cnt")
    private int markupCnts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStatisticsDate() {
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public Date getStartOrderTime() {
		return startOrderTime;
	}

	public void setStartOrderTime(Date startOrderTime) {
		this.startOrderTime = startOrderTime;
	}

	public Date getEndOrderTime() {
		return endOrderTime;
	}

	public void setEndOrderTime(Date endOrderTime) {
		this.endOrderTime = endOrderTime;
	}

	public int getTotalCnts() {
		return totalCnts;
	}

	public void setTotalCnts(int totalCnts) {
		this.totalCnts = totalCnts;
	}

	public int getMarkupCnts() {
		return markupCnts;
	}

	public void setMarkupCnts(int markupCnts) {
		this.markupCnts = markupCnts;
	}
    
    
    
	
}
