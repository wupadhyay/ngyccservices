/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import io.swagger.annotations.ApiImplicitParams;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsService;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;

/**
 * 
 * @author knavuluri
 * 
 */
@Controller
@RequestMapping("/v1/cobrefresh")
public class RefreshStatsController extends MasterController {

	private static final Logger logger = LoggerFactory.getLogger(RefreshStatsController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	RefreshStatUtil rutil;
	
    @ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name="Authorization", value="Authorization token", required=true, dataType="string", paramType="header")})
	@RequestMapping(value = "/stats", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String refreshstats(@RequestParam(value = "cobrandId", required = false) String cobrandId, @RequestParam(value = "groupBy", required = true) String groupBy,
			@RequestParam(value = "reportType", required = true) String reportType, @RequestParam(value = "duration", required = false) String duration,@RequestParam(value = "durationOffset", required = false) String durationOffset,
			@RequestParam(value = "top", required = false) String top, @RequestParam(value = "providerIds", required = false) String providerIds, @RequestParam(value = "numRecords", required = false) String numRecords,
			@RequestParam(value = "include", required = false) String include,@RequestParam(value = "consolidatedBy", required = false) String consolidatedBy) {
		String executeService = null;
		logger.debug("Invoking  refresh stats API");
		Object[] obj = new Object[] { cobrandId, groupBy, reportType, duration, durationOffset,top, numRecords, include, providerIds };
		logger.debug("Input parameters:cobrandId={},groupBy={},reportType={},duration={},durationOffset={},top={},numRecords={},include={},providerIds={}", obj);
		Date sDate = new Date();
		try {
			String cobId = (String) request.getAttribute("cobId");
			RefreshStatsFilter filter = new RefreshStatsFilter();
			filter.setCobrandId(cobrandId);
			if (cobrandId != null && YSLConstants.ALL_COBRANDS.equalsIgnoreCase(cobrandId)) {
				filter.setAllcobrands(true);
				filter.setCobrandId(cobId);
			}
			filter.setGroupBy(groupBy);
			filter.setReportType(reportType);
			filter.setProviderIds(providerIds);
			if (consolidatedBy != null) {
				include = include != null ? include +","+ consolidatedBy : consolidatedBy;
			}
			filter.setInclude(include);
			filter.setTop(top);
			
			filter.setFilterDuration(duration);
			filter.setFilterDurationOffset(durationOffset);
			filter.setNumRecords(numRecords);
			filter.setLogdinCobrandId(cobId);
			
			executeService = rutil.getRefreshStatsData(filter);
		}
		catch (Exception e) {
			logger.info("Input parameters:cobrandId={},groupBy={},reportType={},duration={},durationOffset={},top={},numRecords={},include={},providerIds={}", obj);
			logger.error("Exception while getting the refresh stats:" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("Refresh stats:" + executeService);
		Date eDate = new Date();
		logger.info("Total time taken for refresh stats API:" + (eDate.getTime() - sDate.getTime())  + " milliSec " +"Input parameters:cobrandId="+cobrandId+",groupBy="+groupBy+",reportType="+ reportType +",duration="+ duration +"durationOffset="+
		durationOffset +",top="+ top +",numRecords="+ numRecords +",include="+include +",providerIds="+providerIds );
		return executeService != null ? executeService : "{}";
	}

    
}
