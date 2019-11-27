/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.exceptions;

/**
 * 
 * @author knavuluri
 * 
 */
public class RefreshStatException extends RuntimeException {
	private String errorCode;
	private String message;

	public RefreshStatException(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;

	}

	public RefreshStatException(String message) {
		this.message = message;

	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
