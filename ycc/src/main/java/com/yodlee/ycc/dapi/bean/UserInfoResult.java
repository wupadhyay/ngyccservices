/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class UserInfoResult {

	private Long cobrandId;
	private String email;
	public Long getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
