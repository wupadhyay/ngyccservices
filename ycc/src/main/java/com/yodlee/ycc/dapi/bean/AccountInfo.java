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
 * @description: This class represents the financial details of accounts.
 *
 */

public class AccountInfo {
	
	//field names
	private Long accountId;
	private String accountNumber;
	private String accountName;
	private String totalBalance;
	private String lastRefreshAttempt;
	private String lastSuccessfulRefresh;
	private String response;
	private String fiName;
	private String clientName;
	private String refreshStatus;
	private Long siteId;
	private Long sumInfoId;
	private Long memId;
    public Long getMemItemId() {
		return memItemId;
	}
	public void setMemItemId(Long memItemId) {
		this.memItemId = memItemId;
	}
	public Long getCacheItemId() {
		return cacheItemId;
	}
	public void setCacheItemId(Long cacheItemId) {
		this.cacheItemId = cacheItemId;
	}
	private Long memItemId;
    private Long cacheItemId;
	
	
	
	//getter and setter methods
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
	public String getFiName() {
		return fiName;
	}
	public void setFiName(String fiName) {
		this.fiName = fiName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getLastRefreshAttempt() {
		return lastRefreshAttempt;
	}
	public void setLastRefreshAttempt(String lastRefreshAttempt) {
		this.lastRefreshAttempt = lastRefreshAttempt;
	}
	public String getLastSuccessfulRefresh() {
		return lastSuccessfulRefresh;
	}
	public void setLastSuccessfulRefresh(String lastSuccessfulRefresh) {
		this.lastSuccessfulRefresh = lastSuccessfulRefresh;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getRefreshStatus() {
		return refreshStatus;
	}
	public void setRefreshStatus(String refreshStatus) {
		this.refreshStatus = refreshStatus;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getSumInfoId() {
		return sumInfoId;
	}
	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}
	public Long getMemId() {
		return memId;
	}
	public void setMemId(Long memId) {
		this.memId = memId;
	}
	@Override
	public String toString() {
		return "AccountInfo [accountId=" + accountId + ", accountNumber=" + accountNumber + ", accountName="
				+ accountName + ", totalBalance=" + totalBalance + ", lastRefreshAttempt=" + lastRefreshAttempt
				+ ", lastSuccessfulRefresh=" + lastSuccessfulRefresh + ", response=" + response + ", fiName=" + fiName
				+ ", clientName=" + clientName + ", refreshStatus=" + refreshStatus + ", siteId=" + siteId
				+ ", sumInfoId=" + sumInfoId + ", memId=" + memId + "]";
	}

	
}
