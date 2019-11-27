/*
 * Copyright (c) 2012 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.util.HashMap;

public class JsonExceptionInfo {

	private String errorCode;
	private String errorMessage;
	private String referenceCode;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getReferenceCode() {
		return referenceCode;
	}
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	@Override
	public String toString() {
		return "JsonExceptionInfo [errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", referenceCode=" + referenceCode + "]";
	}
	
	
}
