/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.sql.Date;

public class Transaction {
	
	private Long investmentTransactionId;
	private Date transactionDate;
	private String created;
	private String lastUpdated;
	private String symbol;
	private String description;
	private Double quantity;
	private Double amount;
	private Long isDeleted;
	
	public Long getInvestmentTransactionId() {
		return investmentTransactionId;
	}
	public void setInvestmentTransactionId(Long investmentTransactionId) {
		this.investmentTransactionId = investmentTransactionId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Long getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Override
	public String toString() {
		return "Transaction[investmentTransactionId: "+investmentTransactionId+" transactionDate: "+transactionDate+" created: "+created+
				" lastUpdated: "+lastUpdated+" symbol: "+symbol+" description: "+description+
				" quantity: "+quantity+" amount: "+amount+" isDeleted: "+isDeleted+"]";
	}
	
	
}
