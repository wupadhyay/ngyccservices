/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author knavuluri
 *
 */
public class GlobalMessage {
	private long id;
	private String title;
	private String description;
	private String impact;
	private String classification = "SITE_NOTIFICATION";
	@JsonIgnore
	private String issueTypeIds;
	private String lastUpdated;
	private String issueCreatedDate;
	@JsonIgnore
	private Long categoryId;
	private String expectedResolutionTime;
	@JsonIgnore
	private Long statusId;
	private String issueStartDate;
	private List<Provider> impactedProvider;
	private List<String> issueType;
	private String category;
	private String status;
	private List<Notification> updates;
	private List<Cobrand> impactedCobrand;
	@JsonIgnore
	private Provider provider;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getIssueTypeIds() {
		return issueTypeIds;
	}

	public void setIssueTypeIds(String issueTypeIds) {
		this.issueTypeIds = issueTypeIds;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getExpectedResolutionTime() {
		return expectedResolutionTime;
	}

	public void setExpectedResolutionTime(String expectedResolutionTime) {
		this.expectedResolutionTime = expectedResolutionTime;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getIssueStartDate() {
		return issueStartDate;
	}

	public void setIssueStartDate(String issueStartDate) {
		this.issueStartDate = issueStartDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Cobrand> getImpactedCobrand() {
		return impactedCobrand;
	}

	public void setImpactedCobrand(List<Cobrand> impactedCobrand) {
		this.impactedCobrand = impactedCobrand;
	}

	public List<Notification> getUpdates() {
		return updates;
	}

	public void setUpdates(List<Notification> updates) {
		this.updates = updates;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getIssueType() {
		return issueType;
	}

	public void setIssueType(List<String> issueType) {
		this.issueType = issueType;
	}

	public List<Provider> getImpactedProvider() {
		return impactedProvider;
	}

	public void setImpactedProvider(List<Provider> impactedProvider) {
		this.impactedProvider = impactedProvider;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getIssueCreatedDate() {
		return issueCreatedDate;
	}

	public void setIssueCreatedDate(String issueCreatedDate) {
		this.issueCreatedDate = issueCreatedDate;
	}

	

	
}
