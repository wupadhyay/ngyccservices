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
 * @description: This class represents the transaction information of accounts.
 *
 */

public class TransactionInfo {

	   //field names
		private Long accountId;
		private String accountNumber;
		private String description;
		private String cusip;
		private String symbol;
		private String transactionDate;
		private String quantity;
		private String amount;
		private String price;
		private String value;
		private String settlemenDate;
		private String transactionBaseType;
		private String transactionType;
		private String cancelFlag;
		private String settlementCurrency;
		private String localAmount;
		private Long transactionId;
		private Long stockExchangeId;
		private String transactionStatus;
		
		
		//getter setters
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getTransactionDate() {
			return transactionDate;
		}
		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}
		public String getSettlemenDate() {
			return settlemenDate;
		}
		public void setSettlemenDate(String settlemenDate) {
			this.settlemenDate = settlemenDate;
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
		public String getCancelFlag() {
			return cancelFlag;
		}
		public void setCancelFlag(String cancelFlag) {
			this.cancelFlag = cancelFlag;
		}
		public String getSettlementCurrency() {
			return settlementCurrency;
		}
		public void setSettlementCurrency(String settlementCurrency) {
			this.settlementCurrency = settlementCurrency;
		}
		public Long getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(Long transactionId) {
			this.transactionId = transactionId;
		}
		public Long getStockExchangeId() {
			return stockExchangeId;
		}
		public void setStockExchangeId(Long stockExchangeId) {
			this.stockExchangeId = stockExchangeId;
		}
		public String getTransactionStatus() {
			return transactionStatus;
		}
		public void setTransactionStatus(String transactionStatus) {
			this.transactionStatus = transactionStatus;
		}
		public String getCusip() {
			return cusip;
		}
		public void setCusip(String cusip) {
			this.cusip = cusip;
		}
		public String getQuantity() {
			return quantity;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getLocalAmount() {
			return localAmount;
		}
		public void setLocalAmount(String localAmount) {
			this.localAmount = localAmount;
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
	    

}
