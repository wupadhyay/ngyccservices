/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.utils.SplunkUtil;

public class SplunkReportExecutor implements Callable<SplunkOutput>{
	private static final Logger logger = LoggerFactory.getLogger(SplunkReportExecutor.class);
	
	KeyValue reportType;
	String reportName;
	String queryString;
	String joinString;
	String count;
	boolean reload;
	

	public SplunkReportExecutor(KeyValue reportType, String reportName,
			String queryString, String joinString, String count, boolean reload) {
		super();
		this.reportType=reportType;
		this.reportName = reportName;
		this.queryString = queryString;
		this.joinString = joinString;
		this.count = count;
		this.reload = reload;
	}

	@Override
	public SplunkOutput call() throws Exception {
		logger.debug("input for splunk util "+" reportType "+reportType+" report name "+reportName+ " queryString : "+queryString + " join string "+joinString + " count "+count + " reload "+ reload);
		SplunkOutput splunkOutput= new SplunkOutput();
		String response=null;
		try {
			long startTime = System.currentTimeMillis();			
			response = new SplunkUtil().executeSplunkServices(reportName,
					queryString, joinString, count, reload);
			long endTime = System.currentTimeMillis();
			logger.info("input for splunk util "+" reportType "+reportType+" report name "+reportName+ " queryString : "+queryString + " join string "+joinString + " count "+count + " reload "+ reload 
					+ ". Time taken in splunk layer "+(endTime-startTime)/1000L +" seconds");
			logger.debug("response for "+reportName+ " is : "+response);
		} catch (Exception e) {
			splunkOutput.setFailed(true);;
			splunkOutput.setExceptionStacktrace(e.getStackTrace());

		}
		splunkOutput.setReportType(reportType);
		splunkOutput.setSplunkResponse(response);
		return splunkOutput;
	}

}
