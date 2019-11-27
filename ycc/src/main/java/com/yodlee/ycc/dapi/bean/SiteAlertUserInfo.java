/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.util.List;

public class SiteAlertUserInfo {
	
	private Long cobrandId;
	private List<String> email;
	public Long getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}
	public List<String> getEmail() {
		return email;
	}
	public void setEmail(List<String> email) {
		this.email = email;
	}
}
