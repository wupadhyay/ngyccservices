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
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;

/**
 * 
 * @author knavuluri
 * 
 */
public class RefreshReport implements ReportTypeService {

	@Override
	public Map<KeyValue, List<KeyValue>> getCobrandReportInfo(RefreshStatsFilter filter) {

		Map<KeyValue, List<KeyValue>> refreshReportInfo = null;
		if (filter.getInclude() != null && filter.getInclude().contains(YSLConstants.INCLUDE_ERROR_CODE))
			refreshReportInfo = RefreshStatUtil.getRefreshErrorCodeReportInfo(filter.getCobrandId(), filter.getDuration(), filter.getDurationOffset(), filter.getInclude());
		else
			refreshReportInfo = RefreshStatUtil.getRefreshReportInfo(filter.getCobrandId(), filter.getDuration(), filter.getDurationOffset(), filter.getInclude());
		return refreshReportInfo;
	}

	@Override
	public Map<KeyValue, List<KeyValue>> getSiteReportInfo(RefreshStatsFilter filter) {
		Map<KeyValue, List<KeyValue>> reportInfoMap = null;
		String providerIds = filter.getProviderIds();
		if (providerIds != null && providerIds.length() > 0) {
			if (filter.getAllcobrands() !=null && filter.getAllcobrands()) {
				filter.setCobrandId(null);
				reportInfoMap = RefreshStatUtil.getNetworkStatsReportInfo(filter);
			} else
				reportInfoMap = RefreshStatUtil.getSiteReportInfoPerSite(filter.getCobrandId(), filter.getDuration(), filter.getDurationOffset(), filter.getInclude(),
						filter.getTop(), providerIds);
		} else {
			reportInfoMap = RefreshStatUtil.getSiteReportInfo(filter.getCobrandId(), filter.getDuration(), filter.getDurationOffset(), filter.getInclude(), filter.getTop(),
					filter.getNumRecords());
		}
		return reportInfoMap;
	}

}
