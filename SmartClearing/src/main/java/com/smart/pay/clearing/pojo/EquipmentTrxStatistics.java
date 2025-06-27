package com.smart.pay.clearing.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_equipment_trx_statistics")
public class EquipmentTrxStatistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "equipment_tag")
	private String equipmentTag;
	
	
	@Column(name = "trade_date")
	private String tradeDate;
	
	
	@Column(name = "total_trade_amt")
	private BigDecimal totalTradeAmt;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEquipmentTag() {
		return equipmentTag;
	}

	public void setEquipmentTag(String equipmentTag) {
		this.equipmentTag = equipmentTag;
	}

	

	public BigDecimal getTotalTradeAmt() {
		return totalTradeAmt;
	}

	public void setTotalTradeAmt(BigDecimal totalTradeAmt) {
		this.totalTradeAmt = totalTradeAmt;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	
	
	
	
}
