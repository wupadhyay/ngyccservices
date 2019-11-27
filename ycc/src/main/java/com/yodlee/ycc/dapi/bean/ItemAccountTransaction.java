/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;



public class ItemAccountTransaction {

	private Long transactionCount;
	private Long transactionsNormalized;
	private Long transactionsUnnormalized;
	
	public Long getTransactionCount() {
		return transactionCount;
	}
	public void setTransactionCount(Long transactionCount) {
		this.transactionCount = transactionCount;
	}
	public Long getTransactionsUnnormalized() {
		return transactionsUnnormalized;
	}
	public void setTransactionsUnnormalized(Long transactionsUnnormalized) {
		this.transactionsUnnormalized = transactionsUnnormalized;
	}
	public Long getTransactionsNormalized() {
		return transactionsNormalized;
	}
	public void setTransactionsNormalized(Long transactionsNormalized) {
		this.transactionsNormalized = transactionsNormalized;
	}
	
	@Override
	public String toString() {
		return "ItemAccountTransaction [transactionCount=" + transactionCount + ", transactionsNormalized="
				+ transactionsNormalized + ", transactionsUnnormalized=" + transactionsUnnormalized + "]";
	}
	
	
	

}
