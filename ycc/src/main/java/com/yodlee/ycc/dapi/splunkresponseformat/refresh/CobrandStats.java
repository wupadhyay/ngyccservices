/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh;

import java.util.List;
import java.util.Map;

import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.ReportTypeHandler;
/**
 * 
 * @author knavuluri
 *
 */
public class CobrandStats extends RefreshStatsService {

	@Override
	public Map<KeyValue, List<KeyValue>> getReportInfo(RefreshStatsFilter filter) {
		ReportTypeService handler = ReportTypeHandler.getHandler(filter.getReportType());
		return	handler.getCobrandReportInfo(filter);
	}

}
