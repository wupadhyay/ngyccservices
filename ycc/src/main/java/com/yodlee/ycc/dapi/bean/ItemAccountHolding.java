/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;



public class ItemAccountHolding {

	private Long itemAccountId;
	private String asOfDate;
	private Long sumInfoId;
	private String itemAccountStatus;
	private Long refreshStatus;
	private String lastRefresh;
	private String accountType;
	private Double availableBalance;
	private Double cash;
	private Long totalHoldings;
	private Long holdingsNormalized;
	private Long holdingsUnnormalized;
	
	
	public Long getItemAccountId() {
		return itemAccountId;
	}
	public void setItemAccountId(Long itemAccountId) {
		this.itemAccountId = itemAccountId;
	}
	public String getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	public Long getSumInfoId() {
		return sumInfoId;
	}
	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}
	public String getItemAccountStatus() {
		return itemAccountStatus;
	}
	public void setItemAccountStatus(String itemAccountStatus) {
		this.itemAccountStatus = itemAccountStatus;
	}
	public Long getRefreshStatus() {
		return refreshStatus;
	}
	public void setRefreshStatus(Long refreshStatus) {
		this.refreshStatus = refreshStatus;
	}
	public String getLastRefresh() {
		return lastRefresh;
	}
	public void setLastRefresh(String lastRefresh) {
		this.lastRefresh = lastRefresh;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public Long getTotalHoldings() {
		return totalHoldings;
	}
	public void setTotalHoldings(Long totalHoldings) {
		this.totalHoldings = totalHoldings;
	}
	public Long getHoldingsNormalized() {
		return holdingsNormalized;
	}
	public void setHoldingsNormalized(Long holdingsNormalized) {
		this.holdingsNormalized = holdingsNormalized;
	}
	public Long getHoldingsUnnormalized() {
		return holdingsUnnormalized;
	}
	public void setHoldingsUnnormalized(Long holdingsUnnormalized) {
		this.holdingsUnnormalized = holdingsUnnormalized;
	}
	
	@Override
	public String toString() {
		return "ItemAccountHolding [itemAccountId=" + itemAccountId + ", asOfDate=" + asOfDate + ", sumInfoId="
				+ sumInfoId + ", itemAccountStatus=" + itemAccountStatus + ", refreshStatus=" + refreshStatus
				+ ", lastRefresh=" + lastRefresh + ", accountType=" + accountType + ", availableBalance="
				+ availableBalance + ", cash=" + cash + ", totalHoldings=" + totalHoldings + ", holdingsNormalized="
				+ holdingsNormalized + ", holdingsUnnormalized=" + holdingsUnnormalized + "]";
	}
	
	
	

}
