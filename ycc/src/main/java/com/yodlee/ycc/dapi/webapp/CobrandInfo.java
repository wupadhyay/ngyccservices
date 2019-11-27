/*
* Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc. 
* Use is subject to license terms.
*/
package com.yodlee.ycc.dapi.webapp;

import java.io.Serializable;

public class CobrandInfo implements Serializable {
	private String name;
	private Long id;
	private String appId;
	private String extension;

	public CobrandInfo(String name, Long id, String appId) {
		super();
		this.name = name;
		this.id = id;
		this.appId = appId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 * 				the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	
}
