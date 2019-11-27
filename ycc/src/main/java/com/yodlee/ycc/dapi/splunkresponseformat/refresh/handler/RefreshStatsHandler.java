/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler;

import org.springframework.stereotype.Component;

import com.yodlee.ycc.dapi.splunkresponseformat.refresh.CobrandStats;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.ProviderStats;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsService;

/**
 * 
 * @author knavuluri
 * 
 */
@Component
public class RefreshStatsHandler {

	public RefreshStatsService getHandler(String groupBy) {

		switch (groupBy) {
		case "COBRAND":
			return new CobrandStats();
		case "PROVIDER":
			return new ProviderStats();
		default:
			return new CobrandStats();
		}

	}
}
