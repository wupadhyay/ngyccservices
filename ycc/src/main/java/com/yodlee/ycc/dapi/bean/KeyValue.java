/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.util.List;

/**
 * 
 * @author knavuluri
 *
 */
public class KeyValue {
	private String key;
	private String value;
	private List<KeyValue> keyValueList;

	public KeyValue() {

	}

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeyValue [key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

	public List<KeyValue> getKeyValueList() {
		return keyValueList;
	}

	public void setKeyValueList(List<KeyValue> keyValueList) {
		this.keyValueList = keyValueList;
	}
	
	
}
