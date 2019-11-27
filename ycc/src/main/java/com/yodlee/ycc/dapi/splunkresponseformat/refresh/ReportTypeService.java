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

/**
 * 
 * @author knavuluri
 *
 */
public interface ReportTypeService {
	public  Map<KeyValue, List<KeyValue>> getCobrandReportInfo(RefreshStatsFilter filter);
	public  Map<KeyValue, List<KeyValue>> getSiteReportInfo(RefreshStatsFilter filter);
}
