/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.text.DecimalFormat;
import java.util.List;

import com.yodlee.ycc.dapi.splunkresponseformat.SplunkResponseTransformer;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class FailureExtn {
	

	public String getFailureVolumeExt(JsonDataHandler handler, String type, String jsonpath, boolean includeContainerflag) {
		String volume = "";
		List<String> errors = null;
		if(!includeContainerflag){
			if (type.equalsIgnoreCase("technical")) {
				errors = handler.getValues("$.results[*].TECHNICAL_ERRORS");
			}
			if (type.equalsIgnoreCase("site")) {
				errors = handler.getValues("$.results[*].SITE_DEPENDENT_ERRORS");
			}
			if (type.equalsIgnoreCase("userActionRequired")) {
				errors = handler.getValues("$.results[*].USER_DEPENDENT_ERRORS");
			}
			Long total = 0l;
			for (String error : errors) {
				total = total + Long.valueOf(error);
			}
			volume = total.toString();
			return volume;
		}
		if (type.equalsIgnoreCase("technical")) {
			errors = handler.getValues("$.input[*].results[*].TECHNICAL_ERRORS");
		}
		if (type.equalsIgnoreCase("site")) {
			errors = handler.getValues("$.input[*].results[*].SITE_DEPENDENT_ERRORS");
		}
		if (type.equalsIgnoreCase("userActionRequired")) {
			errors = handler.getValues("$.input[*].results[*].USER_DEPENDENT_ERRORS");
		}
		Long total = 0l;
		for (String error : errors) {
			total = total + Long.valueOf(error);
		}
		volume = total.toString();
		return volume;

	}

	public String getFailureRateExt(JsonDataHandler handler, String type, String jsonpath,boolean flag) {
		String volume = "0.0";
		String failureVolumeExt = getFailureVolumeExt(handler, type, jsonpath,flag);
		List<String> totalrefreshes;
		if(!flag){
			 totalrefreshes = handler.getValues("$.results[*].TOTAL_REFRESHES");
		}else{
			 totalrefreshes = handler.getValues("$.input[*].results[*].TOTAL_REFRESHES");
		}
		Double total1 = 0.0;
		for (String error : totalrefreshes) {
			total1 = total1 + Long.valueOf(error);
		}
		volume = SplunkResponseTransformer.dformat.format((Double.valueOf(failureVolumeExt) / total1) * 100);
		return volume;

	}

}
