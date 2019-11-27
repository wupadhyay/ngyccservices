/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;

/**
 * 
 * @author knavuluri
 * 
 */
public abstract class RefreshStatsService {
	public abstract Map<KeyValue, List<KeyValue>> getReportInfo(RefreshStatsFilter filter);

	public Map<String, String> buildFilter(RefreshStatsFilter filter) {
		Map<String, String> filterMap = new HashMap<String, String>();

		if (filter.isYodlee()) {
			filterMap.put(YSLConstants.USER_TYPE, "tier2");
		}
		else
			filterMap.put(YSLConstants.USER_TYPE, "tier1");

		if (filter.getAllcobrands() != null && filter.getAllcobrands())
			filterMap.put("cobrandId", YSLConstants.ALL_COBRANDS);
		filterMap.put("groupby", filter.getGroupBy());
		if (filter.getTop() != null)
			filterMap.put("statsExtnType", filter.getTop());
		filterMap.put("refreshType", filter.getReportType());
		filterMap.put("duration", filter.getDuration());
		if (!StringUtils.isBlank(filter.getDurationOffset())) {
			filterMap.put(YSLConstants.DURATION_OFFSET, filter.getDurationOffset());
		}

		filterMap.put(YSLConstants.INCLUDE_HISTORIC, "false");
		filterMap.put(YSLConstants.INCLUDE_LATENCY_BREAKUP, "false");
		filterMap.put(YSLConstants.INCLUDE_CONTAINER, "false");
		if (filter.getProviderIds() != null) {
			filterMap.put("providerIds", filter.getProviderIds());
		}
		if (!StringUtils.isEmpty(filter.getInclude())) {
			String[] str = filter.getInclude().split(",");
			for (String inc : str) {
				if (inc.equalsIgnoreCase(YSLConstants.INCLUDE_HISTORIC))
					filterMap.put(YSLConstants.INCLUDE_HISTORIC, "true");
				if (inc.equalsIgnoreCase(YSLConstants.INCLUDE_LATENCY_BREAKUP))
					filterMap.put(YSLConstants.INCLUDE_LATENCY_BREAKUP, "true");
				if (inc.equalsIgnoreCase(YSLConstants.INCLUDE_CONTAINER))
					filterMap.put(YSLConstants.INCLUDE_CONTAINER, "true");
				if (inc.equalsIgnoreCase(YSLConstants.INCLUDE_ERROR_CODE)) {
					filterMap.put(YSLConstants.INCLUDE_ERROR_CODE, "true");
				}
			}
		}

		return filterMap;
	}

	public boolean validateRequestParams(RefreshStatsFilter filter) {
		String cobId = filter.getLogdinCobrandId();
		String cobrandId = filter.getCobrandId();
		
		boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobId));
		filter.setYodlee(yodlee);

		if (StringUtils.isEmpty(cobrandId) || YSLConstants.ALL_COBRANDS.equalsIgnoreCase(cobrandId)) {
			cobrandId = cobId;
			filter.setCobrandId(cobId);
		}
		if (!StringUtil.isNumber(cobrandId)) {
			throw new RefreshStatException("CobrandId is invalid");
		}
		if (StringUtils.isEmpty(filter.getReportType())) {
			throw new RefreshStatException("Report Type is required");
		}
		CobrandUtil.validateCobrand(Long.valueOf(cobId), Long.valueOf(cobrandId));
		if (filter.getInclude() != null && filter.getInclude().contains(YSLConstants.CONSOLIDATE_STATS)) {
			if (yodlee) {
				boolean channel = CobrandUtil.isChannel(Long.valueOf(cobrandId));
				if (!channel)
					throw new RefreshStatException("Please choose only channel");
			}
			else {
				boolean channel = CobrandUtil.isChannel(Long.valueOf(cobId));
				if (!channel)
					throw new RefreshStatException("You are not authorised to get this data");
			}
		}
		String durationConverted = null;
		String durationOffsetConverted = null;
		if (filter.getGroupBy().equalsIgnoreCase(YSLConstants.GROUPBY_COBRAND)) {
			Map<String, String> durationMap = RefreshStatUtil.processDuration(filter.getFilterDuration(), filter.getFilterDurationOffset(), filter.getInclude());
			durationConverted = durationMap.get(YSLConstants.DURATION);
			durationOffsetConverted = durationMap.get(YSLConstants.DURATION_OFFSET);

			if (!StringUtils.isEmpty(filter.getNumRecords()))
				throw new RefreshStatException("NumRecords should be empty");
			if (!StringUtils.isEmpty(filter.getTop()))
				throw new RefreshStatException("Top should be empty");
//			if (filter.getInclude() != null && filter.getInclude().contains(YSLConstants.INCLUDE_ERROR_CODE))
//				throw new RefreshStatException("include should not contain errorcode");
		}
		else if (filter.getGroupBy().equalsIgnoreCase(YSLConstants.GROUPBY_PROVIDER) && filter.getProviderIds() != null && filter.getProviderIds().length() > 0) {

			Map<String, String> durationMap = RefreshStatUtil.processDuration(filter.getFilterDuration(), filter.getFilterDurationOffset(), filter.getInclude());
			durationConverted = durationMap.get(YSLConstants.DURATION);
			durationOffsetConverted = durationMap.get(YSLConstants.DURATION_OFFSET);

		}
		if (filter.getTop() != null) {
			Map<String, String> durationMap = RefreshStatUtil.processDuration(filter.getFilterDuration(), filter.getFilterDurationOffset(), filter.getInclude());
			durationConverted = durationMap.get(YSLConstants.DURATION);
			durationOffsetConverted = durationMap.get(YSLConstants.DURATION_OFFSET);
			if (StringUtils.isEmpty(filter.getNumRecords()))
				filter.setNumRecords("20");
		}
		
		filter.setDuration(durationConverted);
		filter.setDurationOffset(durationOffsetConverted);
		filter.setReportType(filter.getReportType().toUpperCase());
		return true;
	}
}
