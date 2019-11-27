/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class ItemAccountInfo {

	private Long itemAcctId;
	private String reconciledItemAcctId;
	private Long investmentAcctId;
	private String status;
	private Long memId;
	private Long cacheItemId;
	private Long cobrandId;
	private String derivedAcctType;
	private String firmId;
	private Long siteId;
	private String displayName;
	private String latestHadoopDate;
	
	
	public Long getItemAcctId() {
		return itemAcctId;
	}
	public void setItemAcctId(Long itemAcctId) {
		this.itemAcctId = itemAcctId;
	}
	public String getReconciledItemAcctId() {
		return reconciledItemAcctId;
	}
	public void setReconciledItemAcctId(String reconciledItemAcctId) {
		this.reconciledItemAcctId = reconciledItemAcctId;
	}
	public Long getInvestmentAcctId() {
		return investmentAcctId;
	}
	public void setInvestmentAcctId(Long investmentAcctId) {
		this.investmentAcctId = investmentAcctId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getMemId() {
		return memId;
	}
	public void setMemId(Long memId) {
		this.memId = memId;
	}
	public Long getCacheItemId() {
		return cacheItemId;
	}
	public void setCacheItemId(Long cacheItemId) {
		this.cacheItemId = cacheItemId;
	}
	public Long getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}
	public String getDerivedAcctType() {
		return derivedAcctType;
	}
	public void setDerivedAcctType(String derivedAcctType) {
		this.derivedAcctType = derivedAcctType;
	}
	public String getFirmId() {
		return firmId;
	}
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getLatestHadoopDate() {
		return latestHadoopDate;
	}
	public void setLatestHadoopDate(String latestHadoopDate) {
		this.latestHadoopDate = latestHadoopDate;
	}
	@Override
	public String toString() {
		return "ItemAccountInfo[itemAcctId: "+itemAcctId+" reconciledItemAcctId: "+reconciledItemAcctId+" investmentAcctId: "+investmentAcctId+
				" status: "+status+" memId: "+memId+" cacheItemId: "+cacheItemId+
				" cobrandId: "+cobrandId+" derivedAcctType: "+derivedAcctType+" firmId: "+
				firmId+" siteId: "+siteId+" displayName: "+displayName+"]";
	}
	
}
