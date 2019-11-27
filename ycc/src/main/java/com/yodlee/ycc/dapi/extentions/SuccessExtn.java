/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.util.List;

import com.yodlee.ycc.dapi.splunkresponseformat.SplunkResponseTransformer;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.Result;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class SuccessExtn {

	public String getSuccessExt(JsonDataHandler handler, String jsonpath, boolean includeContainerflag) {
		if(!includeContainerflag){
			if (jsonpath.equalsIgnoreCase("successVolumeExtn")) {
				List<String> successfullRefreshes = handler.getValues("$.results[*].SUCCESSFUL_REFRESHES");
				return getSuccessVolumeExt(successfullRefreshes);

			}
			if (jsonpath.equalsIgnoreCase("successRateExtn")) {
				List<String> totalrefreshes = handler.getValues("$.results[*].TOTAL_REFRESHES");
				return getSuccessRateExt(handler, totalrefreshes,includeContainerflag);

			}
			return "0.0";
		}
		if (jsonpath.equalsIgnoreCase("successVolumeExtn")) {
			List<String> successfullRefreshes = handler.getValues("$.input[*].results[*].SUCCESSFUL_REFRESHES");
			return getSuccessVolumeExt(successfullRefreshes);

		}
		if (jsonpath.equalsIgnoreCase("successRateExtn")) {
			List<String> totalrefreshes = handler.getValues("$.input[*].results[*].TOTAL_REFRESHES");
			return getSuccessRateExt(handler, totalrefreshes,includeContainerflag);

		}
		return "0.0";

	}

	public String getSuccessVolumeExt(List<String> successfullRefreshes) {
		long sumOfSuccessFullRefreshes = 0;
		for (String str : successfullRefreshes) {
			sumOfSuccessFullRefreshes = sumOfSuccessFullRefreshes + Long.parseLong(str);
		}
		return String.valueOf(sumOfSuccessFullRefreshes);

	}

	public String getSuccessRateExt(JsonDataHandler handler, List<String> totalrefreshes, boolean includeContainerflag) {
		String successfullRefreshes ;
		if(!includeContainerflag){
			successfullRefreshes=getSuccessVolumeExt(handler.getValues("$.results[*].SUCCESSFUL_REFRESHES"));
		}else{
			successfullRefreshes=getSuccessVolumeExt(handler.getValues("$.input[*].results[*].SUCCESSFUL_REFRESHES"));

		}
		long sumOfTotalRefreshes = 0;
		String successRate = "0.0";
		for (String str : totalrefreshes) {
			sumOfTotalRefreshes = sumOfTotalRefreshes + Long.parseLong(str);
		}
		successRate = SplunkResponseTransformer.dformat.format((Double.valueOf(successfullRefreshes) / sumOfTotalRefreshes) * 100);
		return successRate;
	}
	
	public String getSuccessVolume(Result[] summaryResult) {
		int successVolume=0;
		for(Result result: summaryResult){
			successVolume+=Long.parseLong(result.getSuccessful_refreshes());
		}
		return SplunkResponseTransformer.dformat.format(successVolume);
	}
	
	public String getPartialSuccessVolume(Result[] summaryResult) {
		int successVolume = 0;
		for (Result result : summaryResult) {
			if (result.getPartial_successful_refreshes() != null)
				successVolume += Long.parseLong(result.getPartial_successful_refreshes());
		}
		return SplunkResponseTransformer.dformat.format(successVolume);
	}
}
