/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.util.List;
import java.util.Map;

public class Account360 {

	private Long itemAccountId;
	private Long holdingId;
	private Long transactionId;
	private List<ItemAccountInfo> itemAccountInfos;
	private List<Holding> holdings;
	private List<Transaction> transactions;
	private List<ItemAccountHolding> oltpHoldings;
	private List<ItemAccountTransaction> oltpTransactions;
	private List<ItemAccCacheTransaction> oltpCashTransactions;
	private List<Holding360> holding360Aggregated;
	private List<Holding360> holding360Reconciled;
	private String holding360Hadoop;
	private String hadoop;
	private String holding360Extract;
	private String holding360Transaction;
	private String extracts;
	private List<Transaction360> transaction360Aggregated;
	private String transaction360Hadoop;
	private String transaction360Extract;
	private List<Transaction360> transaction360Reconciled;
	private String latestExtractDate;
	private String previousExtractDate;
	
	
	public Long getItemAccountId() {
		return itemAccountId;
	}
	public Account360 setItemAccountId(Long itemAccountId) {
		this.itemAccountId = itemAccountId;
		return this;
	}
	public Long getHoldingId() {
		return holdingId;
	}
	public Account360 setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
		return this;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public Account360 setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
		return this;
	}
	public List<ItemAccountInfo> getItemAccountInfos() {
		return itemAccountInfos;
	}
	public Account360 setItemAccountInfos(List<ItemAccountInfo> itemAccountInfos) {
		this.itemAccountInfos = itemAccountInfos;
		return this;
	}
	public List<Holding> getHoldings() {
		return holdings;
	}
	public Account360 setHoldings(List<Holding> holdings) {
		this.holdings = holdings;
		return this;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public Account360 setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
		return this;
	}
	public String getHadoop() {
		return hadoop;
	}
	public void setHadoop(String hadoop) {
		this.hadoop = hadoop;
	}
	public String getExtracts() {
		return extracts;
	}
	public void setExtracts(String extracts) {
		this.extracts = extracts;
	}
	public List<Holding360> getHolding360Aggregated() {
		return holding360Aggregated;
	}
	public void setHolding360Aggregated(List<Holding360> holding360Aggregated) {
		this.holding360Aggregated = holding360Aggregated;
	}
	public List<Holding360> getHolding360Reconciled() {
		return holding360Reconciled;
	}
	public void setHolding360Reconciled(List<Holding360> holding360Reconciled) {
		this.holding360Reconciled = holding360Reconciled;
	}
	public String getHolding360Hadoop() {
		return holding360Hadoop;
	}
	public void setHolding360Hadoop(String holding360Hadoop) {
		this.holding360Hadoop = holding360Hadoop;
	}
	public String getHolding360Extract() {
		return holding360Extract;
	}
	public void setHolding360Extract(String holding360Extract) {
		this.holding360Extract = holding360Extract;
	}
	public String getHolding360Transaction() {
		return holding360Transaction;
	}
	public void setHolding360Transaction(String holding360Transaction) {
		this.holding360Transaction = holding360Transaction;
	}
	public List<Transaction360> getTransaction360Aggregated() {
		return transaction360Aggregated;
	}
	public void setTransaction360Aggregated(List<Transaction360> transaction360Aggregated) {
		this.transaction360Aggregated = transaction360Aggregated;
	}
	public String getTransaction360Hadoop() {
		return transaction360Hadoop;
	}
	public void setTransaction360Hadoop(String transaction360Hadoop) {
		this.transaction360Hadoop = transaction360Hadoop;
	}
	public String getTransaction360Extract() {
		return transaction360Extract;
	}
	public void setTransaction360Extract(String transaction360Extract) {
		this.transaction360Extract = transaction360Extract;
	}
	public List<Transaction360> getTransaction360Reconciled() {
		return transaction360Reconciled;
	}
	public void setTransaction360Reconciled(List<Transaction360> transaction360Reconciled) {
		this.transaction360Reconciled = transaction360Reconciled;
	}
	public List<ItemAccountHolding> getOltpHoldings() {
		return oltpHoldings;
	}
	public void setOltpHoldings(List<ItemAccountHolding> oltpHoldings) {
		this.oltpHoldings = oltpHoldings;
	}
	public List<ItemAccountTransaction> getOltpTransactions() {
		return oltpTransactions;
	}
	public void setOltpTransactions(List<ItemAccountTransaction> oltpTransactions) {
		this.oltpTransactions = oltpTransactions;
	}
	public List<ItemAccCacheTransaction> getOltpCashTransactions() {
		return oltpCashTransactions;
	}
	public void setOltpCashTransactions(List<ItemAccCacheTransaction> oltpCashTransactions) {
		this.oltpCashTransactions = oltpCashTransactions;
	}
	public String getLatestExtractDate() {
		return latestExtractDate;
	}
	public Account360 setLatestExtractDate(String latestExtractDate) {
		this.latestExtractDate = latestExtractDate;
		return this;
	}
	public String getPreviousExtractDate() {
		return previousExtractDate;
	}
	public Account360 setPreviousExtractDate(String previousExtractDate) {
		this.previousExtractDate = previousExtractDate;
		return this;
	}
	
	@Override
	public String toString() {
		return "Account360 [itemAccountId=" + itemAccountId + ", holdingId=" + holdingId + ", transactionId="
				+ transactionId + ", itemAccountInfos=" + itemAccountInfos + ", holdings=" + holdings
				+ ", transactions=" + transactions + ", oltpHoldings=" + oltpHoldings + ", oltpTransactions="
				+ oltpTransactions + ", oltpCashTransactions=" + oltpCashTransactions + ", holding360Aggregated="
				+ holding360Aggregated + ", holding360Reconciled=" + holding360Reconciled + ", holding360Hadoop="
				+ holding360Hadoop + ", hadoop=" + hadoop + ", holding360Extract=" + holding360Extract
				+ ", holding360Transaction=" + holding360Transaction + ", extracts=" + extracts
				+ ", transaction360Aggregated=" + transaction360Aggregated + ", transaction360Hadoop="
				+ transaction360Hadoop + ", transaction360Extract=" + transaction360Extract
				+ ", transaction360Reconciled=" + transaction360Reconciled + "]";
	}
	
	
}
