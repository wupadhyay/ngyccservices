/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.bean;

import java.util.List;

/**
 * 
 * @author knavuluri
 *
 */
public class NotificationDetail {
	private List<GlobalMessage> notification;

	public List<GlobalMessage> getNotification() {
		return notification;
	}

	public void setNotification(List<GlobalMessage> notification) {
		this.notification = notification;
	}
}
