/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;



public class ItemAccCacheTransaction {

	private Long totalCashTransactions;

	public Long getTotalCashTransactions() {
		return totalCashTransactions;
	}

	public void setTotalCashTransactions(Long totalCashTransactions) {
		this.totalCashTransactions = totalCashTransactions;
	}

	@Override
	public String toString() {
		return "ItemAccCacheTransaction [totalCashTransactions=" + totalCashTransactions + "]";
	}
	
	

}
