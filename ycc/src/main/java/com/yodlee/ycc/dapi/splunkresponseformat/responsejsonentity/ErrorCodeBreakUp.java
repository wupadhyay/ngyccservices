/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorCodeBreakUp {

	public String code;
	public String volume;
	public String rate;
	public String description;
	
	 @JsonCreator
	public ErrorCodeBreakUp(@JsonProperty("code") String code, @JsonProperty("volume") String volume, @JsonProperty("rate")String rate,@JsonProperty("description") String description) {
		super();
		this.code = code;
		this.volume = volume;
		this.rate = rate;
		this.description = description;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorCodeBreakUp [code=");
		builder.append(code);
		builder.append(", volume=");
		builder.append(volume);
		builder.append(", rate=");
		builder.append(rate);
		builder.append("]");
		return builder.toString();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	 
	
	
	
}
