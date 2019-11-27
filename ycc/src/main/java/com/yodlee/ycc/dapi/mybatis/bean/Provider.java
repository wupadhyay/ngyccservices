/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.bean;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author knavuluri
 *
 */
public class Provider {
	private Long id;
	@JsonIgnore
	private String containerStr;
	private String name;
	private List<String> containers;

	public String getContainerStr() {
		return containerStr;
	}

	public void setContainerStr(String containerStr) {
		this.containerStr = containerStr;
		setContainers(containerStr);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getContainers() {
		return containers;
	}

	public void setContainers(String containerStr) {
		if (containerStr != null) {
			List<String> result = Arrays.asList(containerStr.split("\\s*,\\s*"));
			this.containers = result;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
