/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteErrorCodeContributionResult {
	
	@JsonProperty(value = "type_of_error")
	private  String errorCode;
	@JsonProperty(value = "error_type")
	private  String errorType;
	@JsonProperty(value = "total_errors")
	private  String totalErrors;
	@JsonProperty(value = "cobrand_id")
	private String cobrandId;
	@JsonProperty(value = "site_id")
	private String siteId;
	
	public SiteErrorCodeContributionResult() {
		
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getTotalErrors() {
		return totalErrors;
	}

	public void setTotalErrors(String totalErrors) {
		this.totalErrors = totalErrors;
	}

	@Override
	public String toString() {
		return "SiteErrorCodeContributionResult [errorCode=" + errorCode + ", errorType=" + errorType + ", totalErrors=" + totalErrors + "]";
	}

	public String getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(String cobrandId) {
		this.cobrandId = cobrandId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

}
