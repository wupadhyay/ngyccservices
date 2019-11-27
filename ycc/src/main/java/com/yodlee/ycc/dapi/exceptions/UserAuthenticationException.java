/*
* Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.dapi.exceptions;

public class UserAuthenticationException extends Exception
{
	private String errorCode;
	private Object[] args;
	
	public UserAuthenticationException(String errorCode) {
		this.errorCode=errorCode;
		this.args=null;
	}
	
	public UserAuthenticationException(String errorCode,Object[] args) 
	{
		this.errorCode = errorCode;
		this.args = args;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	

}
