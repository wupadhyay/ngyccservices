/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

/**
 * 
 * @author bkumar1
 * @description: This class represents the positions information of accounts.
 *
 */

public class PositionInfo {
	
	
			//field names
			private Long accountId;
			private String accountNumber;
			private String accountName;
			private String description;
			private String ticker;
			private String cusip;
			private Double quantity;
			private String price;
			private String value;
			private String asOfDate;
			private String holdingType;
			private String settlementCurrency;
			private String baseCurrency;
			private String localAmount;
			private Long stockExchangeId;
			private Long holdingId;
			private String securityType;
			
			
			//getter setter methods
	
			public Long getAccountId() {
				return accountId;
			}
			public void setAccountId(Long accountId) {
				this.accountId = accountId;
			}
			public String getAccountNumber() {
				return accountNumber;
			}
			public void setAccountNumber(String accountNumber) {
				this.accountNumber = accountNumber;
			}
			public String getAccountName() {
				return accountName;
			}
			public void setAccountName(String accountName) {
				this.accountName = accountName;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public String getTicker() {
				return ticker;
			}
			public void setTicker(String ticker) {
				this.ticker = ticker;
			}
			public String getCusip() {
				return cusip;
			}
			public void setCusip(String cusip) {
				this.cusip = cusip;
			}
			
			
			public Double getQuantity() {
				return quantity;
			}
			public void setQuantity(Double quantity) {
				this.quantity = quantity;
			}
			public String getPrice() {
				return price;
			}
			public void setPrice(String price) {
				this.price = price;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getAsOfDate() {
				return asOfDate;
			}
			public void setAsOfDate(String asOfDate) {
				this.asOfDate = asOfDate;
			}
			public String getHoldingType() {
				return holdingType;
			}
			public void setHoldingType(String holdingType) {
				this.holdingType = holdingType;
			}
			public String getSettlementCurrency() {
				return settlementCurrency;
			}
			public void setSettlementCurrency(String settlementCurrency) {
				this.settlementCurrency = settlementCurrency;
			}
			public String getBaseCurrency() {
				return baseCurrency;
			}
			public void setBaseCurrency(String baseCurrency) {
				this.baseCurrency = baseCurrency;
			}
			public Long getStockExchangeId() {
				return stockExchangeId;
			}
			public void setStockExchangeId(Long stockExchangeId) {
				this.stockExchangeId = stockExchangeId;
			}
			public Long getHoldingId() {
				return holdingId;
			}
			public void setHoldingId(Long holdingId) {
				this.holdingId = holdingId;
			}
			public String getSecurityType() {
				return securityType;
			}
			public void setSecurityType(String securityType) {
				this.securityType = securityType;
			}
			public String getLocalAmount() {
				return localAmount;
			}
			public void setLocalAmount(String localAmount) {
				this.localAmount = localAmount;
			}
			
			
}
