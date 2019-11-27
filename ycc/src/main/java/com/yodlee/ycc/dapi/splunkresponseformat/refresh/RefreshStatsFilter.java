/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh;

/**
 * 
 * @author knavuluri
 * 
 */
public class RefreshStatsFilter {
	private String cobrandId;
	private String duration;
	private String durationOffset;
	private String include;
	private String top;
	private String providerIds;
	private String reportType;
	private String numRecords;
	private String groupBy;
	private boolean isYodlee;
	private String filterDuration;
	private String filterDurationOffset;
	private String logdinCobrandId;
	private Boolean allcobrands;
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(String cobrandId) {
		this.cobrandId = cobrandId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDurationOffset() {
		return durationOffset;
	}

	public void setDurationOffset(String durationOffset) {
		this.durationOffset = durationOffset;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getProviderIds() {
		return providerIds;
	}

	public void setProviderIds(String providerIds) {
		this.providerIds = providerIds;
	}

	public String getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(String numRecords) {
		this.numRecords = numRecords;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public boolean isYodlee() {
		return isYodlee;
	}

	public void setYodlee(boolean isYodlee) {
		this.isYodlee = isYodlee;
	}

	public String getFilterDuration() {
		return filterDuration;
	}

	public void setFilterDuration(String filterDuration) {
		this.filterDuration = filterDuration;
	}

	public String getFilterDurationOffset() {
		return filterDurationOffset;
	}

	public void setFilterDurationOffset(String filterDurationOffset) {
		this.filterDurationOffset = filterDurationOffset;
	}

	public String getLogdinCobrandId() {
		return logdinCobrandId;
	}

	public void setLogdinCobrandId(String logdinCobrandId) {
		this.logdinCobrandId = logdinCobrandId;
	}

	public Boolean getAllcobrands() {
		return allcobrands;
	}

	public void setAllcobrands(Boolean allcobrands) {
		this.allcobrands = allcobrands;
	}

}
