/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.util.List;

import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.Result;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class SummaryExtn {

	
 public String	getSummaryExt(JsonDataHandler handler,String jsonpath, boolean includeContainerflag)
	{
	  if(!includeContainerflag){
		  if(jsonpath.equalsIgnoreCase("totalVolumeExtn")){
	    	  List<String> 	totalrefreshes=handler.getValues("$.results[*].TOTAL_REFRESHES");
	    	return getTotalVolumeExt(totalrefreshes);
		
	    }
	     return "0.0";
	  }
	    if(jsonpath.equalsIgnoreCase("totalVolumeExtn")){
	    	  List<String> 	totalrefreshes=handler.getValues("$.input[*].results[*].TOTAL_REFRESHES");
	    	return getTotalVolumeExt(totalrefreshes);
		
	    }
	     return "0.0";
	
	}
 
 public String	getTotalVolumeExt(List<String> totalrefreshes)
	{
	  long sumOfTotalRefreshes=0;
	  for(String str:totalrefreshes){
 	              sumOfTotalRefreshes=sumOfTotalRefreshes+Long.parseLong(str);
              }
	return String.valueOf(sumOfTotalRefreshes);
   
	}
 
 public String	getTotalVolume(Result[] results)
	{
	  long sumOfTotalRefreshes=0;
	  for(Result result:results){
	              sumOfTotalRefreshes=sumOfTotalRefreshes+Long.parseLong(result.getTotal_refreshes());
           }
	return String.valueOf(sumOfTotalRefreshes);

	}

}
