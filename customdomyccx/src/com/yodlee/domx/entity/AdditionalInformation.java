/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.domx.entity;

public class AdditionalInformation {
	
	private String informationType;
	private String notes;
	private String disabledReason;
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getInformationType() {
		return informationType;
	}
	
	public void setInformationType(String informationType) {
		
		this.informationType = informationType;
	}
	public String getDisabledReason() {
		return disabledReason;
	}
	
	public void setDisabledReason(String disabledReason) {
		this.disabledReason = disabledReason;
	}
	
	
}
