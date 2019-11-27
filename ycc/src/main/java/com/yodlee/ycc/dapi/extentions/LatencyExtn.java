/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.util.List;
import com.yodlee.ycc.dapi.splunkresponseformat.SplunkResponseTransformer;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class LatencyExtn {
	
	 public String	getLatencyExtn(JsonDataHandler handler,String jsonpath,String type,int historicDetailsCount,boolean container)
	 {      if(jsonpath.equalsIgnoreCase("volumeExtn")){
		    	return getVolumeExt(handler,type,historicDetailsCount,container);			
		    }
		    if(jsonpath.equalsIgnoreCase("rateExtn")){
		    	return getRateExt(handler,type,historicDetailsCount,container);			
		    }
		    if(jsonpath.startsWith("$.results[0]") && type!=null && !type.equalsIgnoreCase("h")){
		    	return getLatency(handler,jsonpath,historicDetailsCount,container);
		    }
		   if(type!=null&&type.equalsIgnoreCase("h") && jsonpath.contains("AVG_LATENCY")){			   
			   return   handler.getValue("$.results["+historicDetailsCount+"].AVG_LATENCY");		   
		   }
		    
		    return "0.0";
	}
	 
	 public String	getLatencyExtn(JsonDataHandler handler,String jsonpath,String duration,boolean includeContainerflag)
	 { 
		if(!includeContainerflag){
			if (jsonpath.contains("AVG_LATENCY")) {
				String volume = "0.0";
				List<String> totalrefreshes = handler.getValues("$.results[*].AVG_LATENCY");
				Double total1 = 0.0;
				for (String error : totalrefreshes) {

					total1 = total1 + Double.valueOf(error);

				}
				volume = SplunkResponseTransformer.dformat.format((Double
						.valueOf(total1) / totalrefreshes.size()));

				return volume;
			}
		}
		if (jsonpath.contains("AVG_LATENCY")) {
			String volume = "0.0";
			List<String> totalrefreshes = handler.getValues("$.input[*].results[*].AVG_LATENCY");
			List<String> totalVolume = handler.getValues("$.input[*].results[*].TOTAL_REFRESHES");
			Double total1 = 0.0;
			Double total2 = 0.0;
			for(int i=0;i<totalVolume.size();i++){
				total1=total1 +Double.valueOf(totalVolume.get(i))*Double.valueOf(totalrefreshes.get(i));
				total2=total2+Double.valueOf(totalVolume.get(i));
			}
			volume = SplunkResponseTransformer.dformat.format((Double
					.valueOf(total1) / total2));
			System.out.println("volume is "+volume);
			return volume;
			
		}
		

		return "0.0";	
	 }
	 public String	getLatency(JsonDataHandler handler,String jsonpath,int historicDetailsCount,boolean container)
		{   	
		 String jpathlatency="";
		    if(container)  {
		    	String str=jsonpath.substring("$.results[0].".length());
			 	 jpathlatency="$.input["+historicDetailsCount+"].results[0]"+str;
		    }
		    else{
		    	  String str=jsonpath.substring("$.results[0].".length());
				   jpathlatency="$.results["+historicDetailsCount+"]."+str;
		    }
			   
			return handler.getValue(jpathlatency);		 
				
		}
	
	 public String	getVolumeExt(JsonDataHandler handler,String type,int historicDetailsCount, boolean container)
		{ 
		  		 
		if(container)
		{
			 if(type.equalsIgnoreCase("0_TO_20_SECS"))
			        return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_0_20");
			  if(type.equalsIgnoreCase("20_TO_40_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_20_40");	 
			  if(type.equalsIgnoreCase("40_TO_60_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_40_60");
			  if(type.equalsIgnoreCase("60_TO_80_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_60_80");
			  if(type.equalsIgnoreCase("80_TO_100_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_80_100");	
			  if(type.equalsIgnoreCase("GRT_THAN_100_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_ABOVE_100");	
		}
		else{
			 if(type.equalsIgnoreCase("0_TO_20_SECS"))
			        return  handler.getValue("$.results["+historicDetailsCount+"].LATENCY_0_20");
			  if(type.equalsIgnoreCase("20_TO_40_SECS"))
			  	    return handler.getValue("$.results["+historicDetailsCount+"].LATENCY_20_40");	 
			  if(type.equalsIgnoreCase("40_TO_60_SECS"))
					return  handler.getValue("$.results["+historicDetailsCount+"].LATENCY_40_60");
			  if(type.equalsIgnoreCase("60_TO_80_SECS"))
					return handler.getValue("$.results["+historicDetailsCount+"].LATENCY_60_80");
			  if(type.equalsIgnoreCase("80_TO_100_SECS"))
					return  handler.getValue("$.results["+historicDetailsCount+"].LATENCY_80_100");	
			  if(type.equalsIgnoreCase("GRT_THAN_100_SECS"))
					return  handler.getValue("$.results["+historicDetailsCount+"].LATENCY_ABOVE_100");	
			  
		}
		  return "0.0";
		}
	 
	 public String	getRateExt(JsonDataHandler handler,String type,int historicDetailsCount,boolean container)
		{		 
		  if(container){
			  if(type.equalsIgnoreCase("0_TO_20_SECS"))			
			      return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_0_20_PER");
			  if(type.equalsIgnoreCase("20_TO_40_SECS"))
			  	  return   handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_20_40_PER");	 
			  if(type.equalsIgnoreCase("40_TO_60_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_40_60_PER");
			  if(type.equalsIgnoreCase("60_TO_80_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_60_80_PER");
			  if(type.equalsIgnoreCase("80_TO_100_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_80_100_PER");	
			  if(type.equalsIgnoreCase("GRT_THAN_100_SECS"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].LATENCY_ABOVE_100_PER");;	
		  }
		  else{
			  if(type.equalsIgnoreCase("0_TO_20_SECS"))			
			      return   handler.getValue("$.results["+historicDetailsCount+"].LATENCY_0_20_PER");
			  if(type.equalsIgnoreCase("20_TO_40_SECS"))
			  	  return  handler.getValue("$.results["+historicDetailsCount+"].LATENCY_20_40_PER");	 
			  if(type.equalsIgnoreCase("40_TO_60_SECS"))
					return   handler.getValue("$.results["+historicDetailsCount+"].LATENCY_40_60_PER");
			  if(type.equalsIgnoreCase("60_TO_80_SECS"))
					return   handler.getValue("$.results["+historicDetailsCount+"].LATENCY_60_80_PER");
			  if(type.equalsIgnoreCase("80_TO_100_SECS"))
					return   handler.getValue("$.results["+historicDetailsCount+"].LATENCY_80_100_PER");	
			  if(type.equalsIgnoreCase("GRT_THAN_100_SECS"))
					return   handler.getValue("$.results["+historicDetailsCount+"].LATENCY_ABOVE_100_PER");;	
		  }
		  return "0.0";
		}

}
