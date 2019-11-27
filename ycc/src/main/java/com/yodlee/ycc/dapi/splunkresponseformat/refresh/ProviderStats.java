/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.ReportTypeHandler;

/**
 * 
 * @author knavuluri
 * 
 */
public class ProviderStats extends RefreshStatsService {

	@Override
	public Map<KeyValue, List<KeyValue>> getReportInfo(RefreshStatsFilter filter) {
		if (StringUtils.isEmpty(filter.getTop()) && StringUtils.isEmpty(filter.getProviderIds())) {
			throw new RefreshStatException("Top or ProviderId(s) is required");
		}
		if (!StringUtils.isEmpty(filter.getTop())) {
			if (!(YSLConstants.SPLUNK_TOP_FAILURE.equalsIgnoreCase(filter.getTop()) || YSLConstants.SPLUNK_TOP_VOLUME.equalsIgnoreCase(filter.getTop())))
				throw new RefreshStatException("Invalid value for Top");
		}
		ReportTypeService handler = ReportTypeHandler.getHandler(filter.getReportType());
		return handler.getSiteReportInfo(filter);
	}

}
