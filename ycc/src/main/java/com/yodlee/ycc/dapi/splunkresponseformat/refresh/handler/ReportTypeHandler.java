/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler;

import com.yodlee.ycc.dapi.splunkresponseformat.refresh.AddReport;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.IavReport;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshReport;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.ReportTypeService;

public class ReportTypeHandler {

	public static ReportTypeService getHandler(String reportType) {

		switch (reportType) {
		case "REFRESH":
			return new RefreshReport();

		case "IAV":
			return new IavReport();
			
		case "ADD":
			return new AddReport();	
			
		default:
			return new RefreshReport();
		}
	}
}
