/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.splunkresponseformat.SplunkResponseTransformer;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.LatencyBreakup;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.Result;

public class LatencyBreakupExtn {
	
	public List<LatencyBreakup> aggregateLatencyBreakup(Result[] summaryResult, String totalVolume) {
		Map<String,Long> volPerbreakupCategory = new LinkedHashMap<>();
		for(Result result: summaryResult){
			
			if(volPerbreakupCategory.containsKey(YSLConstants._0_TO_20_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants._0_TO_20_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_0_20());
				volPerbreakupCategory.put(YSLConstants._0_TO_20_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_0_20());
				volPerbreakupCategory.put(YSLConstants._0_TO_20_SECS,breakupVolume);
			}
			
			if(volPerbreakupCategory.containsKey(YSLConstants._20_TO_40_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants._20_TO_40_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_20_40());
				volPerbreakupCategory.put(YSLConstants._20_TO_40_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_20_40());
				volPerbreakupCategory.put(YSLConstants._20_TO_40_SECS,breakupVolume);
			}
			
			if(volPerbreakupCategory.containsKey(YSLConstants._40_TO_60_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants._40_TO_60_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_40_60());
				volPerbreakupCategory.put(YSLConstants._40_TO_60_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_40_60());
				volPerbreakupCategory.put(YSLConstants._40_TO_60_SECS,breakupVolume);
			}
			
			if(volPerbreakupCategory.containsKey(YSLConstants._60_TO_80_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants._60_TO_80_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_60_80());
				volPerbreakupCategory.put(YSLConstants._60_TO_80_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_60_80());
				volPerbreakupCategory.put(YSLConstants._60_TO_80_SECS,breakupVolume);
			}
			
			if(volPerbreakupCategory.containsKey(YSLConstants._80_TO_100_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants._80_TO_100_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_80_100());
				volPerbreakupCategory.put(YSLConstants._80_TO_100_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_80_100());
				volPerbreakupCategory.put(YSLConstants._80_TO_100_SECS,breakupVolume);
			}
			
			if(volPerbreakupCategory.containsKey(YSLConstants.GRT_THAN_100_SECS)) {
				long breakupVolume = volPerbreakupCategory.get(YSLConstants.GRT_THAN_100_SECS);
				breakupVolume = breakupVolume + Long.parseLong(result.getLatency_above_100());
				volPerbreakupCategory.put(YSLConstants.GRT_THAN_100_SECS,breakupVolume);
			} else {
				long breakupVolume =  Long.parseLong(result.getLatency_above_100());
				volPerbreakupCategory.put(YSLConstants.GRT_THAN_100_SECS,breakupVolume);
			}
		}
		List<LatencyBreakup> latencyBreakups = new ArrayList<>();
		for (Entry<String, Long> volPerbreakupCategoryEntry : volPerbreakupCategory.entrySet()) {
			Long volume = volPerbreakupCategoryEntry.getValue();
			String rate = SplunkResponseTransformer.dformat.format((Double.valueOf(volume)/Double.valueOf(totalVolume))*100);
			LatencyBreakup latencyBreakup = new LatencyBreakup(volPerbreakupCategoryEntry.getKey(), String.valueOf(volume), rate);
			latencyBreakups.add(latencyBreakup);
		}
		
		return latencyBreakups;
	}

}
