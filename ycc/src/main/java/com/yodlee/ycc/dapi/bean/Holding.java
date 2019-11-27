/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class Holding {

	private Long holdingId;
	private String cusip;
	private String symbol;
	private String description;
	private Double quantity;
	private Double value;
	
	public Long getHoldingId() {
		return holdingId;
	}
	public void setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
	}
	public String getCusip() {
		return cusip;
	}
	public void setCusip(String cusip) {
		this.cusip = cusip;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "Holding [holdingId=" + holdingId + ", cusip=" + cusip + ", symbol=" + symbol + ", description="
				+ description + ", quantity=" + quantity + ", value=" + value + "]";
	}
	
	
	
}
