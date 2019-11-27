/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author knavuluri
 * 
 */
public class RefreshStatsDbFilter implements Serializable {
	private String reportType;
	private Long cobrandId;
	private String duration;
	private List<Long> siteId;
	private String durationOffSet;
	private Long jobId;
	private String collectionName;
	private Integer numRecords;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public List<Long> getSiteId() {
		return siteId;
	}

	public void setSiteId(List<Long> siteId) {
		this.siteId = siteId;
	}

	public String getDurationOffSet() {
		return durationOffSet;
	}

	public void setDurationOffSet(String durationOffSet) {
		this.durationOffSet = durationOffSet;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "RefreshStatsDbFilter [reportType=" + reportType + ", cobrandId=" + cobrandId + ", duration=" + duration + ", siteId=" + siteId + ", durationOffSet=" + durationOffSet + ", jobId="
				+ jobId + "numRecords=" + numRecords + "]";
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Integer getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(Integer numRecords) {
		this.numRecords = numRecords;
	}

}
