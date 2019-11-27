/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

public class ReconDataInfo {
	
	private Account360 account360 ;
	
	
	public Account360 getAccount360() {
		return account360;
	}

	public void setAccount360(Account360 account360) {
		this.account360 = account360;
	}

	@Override
	public String toString() {
		return "ReconDataInfo[account360: "+account360+"]";
	}	

}
