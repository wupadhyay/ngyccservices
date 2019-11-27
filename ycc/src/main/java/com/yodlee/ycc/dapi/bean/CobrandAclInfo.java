/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.util.List;

public class CobrandAclInfo {
	
	private List<CobrandAcl> cobrands;

	public List<CobrandAcl> getCobrands() {
		return cobrands;
	}

	public void setCobrands(List<CobrandAcl> cobrandACL) {
		this.cobrands = cobrandACL;
	}

}
