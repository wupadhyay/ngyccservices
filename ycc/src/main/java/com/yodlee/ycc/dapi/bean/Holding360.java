/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class Holding360 {

	private String holdingType;
	private String securityType;
	private String securitiesExchangeCode;
	private String currencyOfMarketValue;
	private Long holdingId;
	private String description;
	private Double quantity;
	private Double vestedQuantity;
	private Double price;
	private Double marketValue;
	private String scrapedCusipNumber;
	private String scrapedSymbol;
	private String scrapedIsin;
	private String scrapedSedol;
	private String normalizedCusipNumber;
	private String normalizedSymbol;
	private String normalizedIsin;
	private String normalizedSedol;
	private Long securitiesId;
	private String created;
	private String lastUpdated;
	
	public String getHoldingType() {
		return holdingType;
	}
	public void setHoldingType(String holdingType) {
		this.holdingType = holdingType;
	}
	public String getSecurityType() {
		return securityType;
	}
	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}
	public String getSecuritiesExchangeCode() {
		return securitiesExchangeCode;
	}
	public void setSecuritiesExchangeCode(String securitiesExchangeCode) {
		this.securitiesExchangeCode = securitiesExchangeCode;
	}
	public String getCurrencyOfMarketValue() {
		return currencyOfMarketValue;
	}
	public void setCurrencyOfMarketValue(String currencyOfMarketValue) {
		this.currencyOfMarketValue = currencyOfMarketValue;
	}
	public Long getHoldingId() {
		return holdingId;
	}
	public void setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
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
	public Double getVestedQuantity() {
		return vestedQuantity;
	}
	public void setVestedQuantity(Double vestedQuantity) {
		this.vestedQuantity = vestedQuantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
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
	
	@Override
	public String toString() {
		return "Holding360[holdingType: "+holdingType+" securityType: "+securityType+" securitiesExchangeCode: "+securitiesExchangeCode+
				" currencyOfMarketValue: "+currencyOfMarketValue+" holdingId: "+holdingId+" description: "+description+
				" quantity: "+quantity+" vestedQuantity: "+vestedQuantity+" price: "+
				price+" marketValue: "+marketValue+" scrapedCusipNumber: "+scrapedCusipNumber+" scrapedSymbol: "+
				scrapedSymbol+" scrapedIsin:"+scrapedIsin+" scrapedSedol: "+scrapedSedol+
				" normalizedCusipNumber: "+normalizedCusipNumber+" normalizedSymbol: "+normalizedSymbol+
				" normalizedIsin: "+normalizedIsin+" normalizedSedol: "+normalizedSedol+
				" securitiesId: "+securitiesId+" created: "+created+" lastUpdated: "+lastUpdated+"]";
	}
}
