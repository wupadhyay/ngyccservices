/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.bean;

import java.io.Serializable;
/**
 * 
 * @author knavuluri
 *
 */
public class Cobrand implements Serializable{
	private long cobrandId;
	private String name;

	public long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
