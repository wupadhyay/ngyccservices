/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class Transaction360 {

	private Long investmentTransactionId;
	private String srcElementId;
	private String transDate;
	private String description;
	private Double amount;
	private Double quantity;
	private String scrapedCusipNumber;
	private String scrapedSymbol;
	private String scrapedIsin;
	private String scrapedSedol;
	private String normalizedCusipNumber;
	private String normalizedSymbol;
	private String normalizedIsin;
	private String normalizedSedol;
	private Long securitiesId;
	private String normalizedExchange;
	private String created;
	private String lastUpdated;
	private Long itemAccountId;
	private String transactionBaseType;
	private String transactionType;
	private String transactionStatus;
	private String currencyOfAmount;
	
	
	public Long getInvestmentTransactionId() {
		return investmentTransactionId;
	}
	
	public void setInvestmentTransactionId(Long investmentTransactionId) {
		this.investmentTransactionId = investmentTransactionId;
	}
	public String getSrcElementId() {
		return srcElementId;
	}
	public void setSrcElementId(String srcElementId) {
		this.srcElementId = srcElementId;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getScrapedCusipNumber() {
		return scrapedCusipNumber;
	}
	public void setScrapedCusipNumber(String scrapedCusipNumber) {
		this.scrapedCusipNumber = scrapedCusipNumber;
	}
	public String getScrapedSymbol() {
		return scrapedSymbol;
	}
	public void setScrapedSymbol(String scrapedSymbol) {
		this.scrapedSymbol = scrapedSymbol;
	}
	public String getScrapedIsin() {
		return scrapedIsin;
	}
	public void setScrapedIsin(String scrapedIsin) {
		this.scrapedIsin = scrapedIsin;
	}
	public String getScrapedSedol() {
		return scrapedSedol;
	}
	public void setScrapedSedol(String scrapedSedol) {
		this.scrapedSedol = scrapedSedol;
	}
	public String getNormalizedCusipNumber() {
		return normalizedCusipNumber;
	}
	public void setNormalizedCusipNumber(String normalizedCusipNumber) {
		this.normalizedCusipNumber = normalizedCusipNumber;
	}
	public String getNormalizedSymbol() {
		return normalizedSymbol;
	}
	public void setNormalizedSymbol(String normalizedSymbol) {
		this.normalizedSymbol = normalizedSymbol;
	}
	public String getNormalizedIsin() {
		return normalizedIsin;
	}
	public void setNormalizedIsin(String normalizedIsin) {
		this.normalizedIsin = normalizedIsin;
	}
	public String getNormalizedSedol() {
		return normalizedSedol;
	}
	public void setNormalizedSedol(String normalizedSedol) {
		this.normalizedSedol = normalizedSedol;
	}
	public Long getSecuritiesId() {
		return securitiesId;
	}
	public void setSecuritiesId(Long securitiesId) {
		this.securitiesId = securitiesId;
	}
	public String getNormalizedExchange() {
		return normalizedExchange;
	}
	public void setNormalizedExchange(String normalizedExchange) {
		this.normalizedExchange = normalizedExchange;
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
	public Long getItemAccountId() {
		return itemAccountId;
	}
	public void setItemAccountId(Long itemAccountId) {
		this.itemAccountId = itemAccountId;
	}
	public String getTransactionBaseType() {
		return transactionBaseType;
	}
	public void setTransactionBaseType(String transactionBaseType) {
		this.transactionBaseType = transactionBaseType;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getCurrencyOfAmount() {
		return currencyOfAmount;
	}
	public void setCurrencyOfAmount(String currencyOfAmount) {
		this.currencyOfAmount = currencyOfAmount;
	}
	
	@Override
	public String toString() {
		return "Transaction360[investmentTransactionId: "+investmentTransactionId+" srcElementId: "+srcElementId+" transDate: "+transDate+
				" description: "+description+" amount: "+amount+" quantity: "+quantity+
				" scrapedCusipNumber: "+scrapedCusipNumber+" scrapedSymbol: "+scrapedSymbol+" scrapedIsin: "+
				scrapedIsin+" scrapedSedol: "+scrapedSedol+" normalizedCusipNumber: "+normalizedCusipNumber+" normalizedSymbol: "+
				normalizedSymbol+" normalizedIsin:"+normalizedIsin+" normalizedSedol: "+normalizedSedol+
				" securitiesId: "+securitiesId+" normalizedExchange: "+normalizedExchange+
				" created: "+created+" lastUpdated: "+lastUpdated+" itemAccountId: "+itemAccountId+
				" transactionBaseType: "+transactionBaseType+" transactionType: "+transactionType+" transactionStatus: "+transactionStatus+
				" currencyOfAmount: "+currencyOfAmount+"]";
	}
	
}
