/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;
import java.util.List;

import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class FailureDetailsExtn {

	 public String	getFailureDetailsExt(JsonDataHandler handler,String jsonpath,String type,int historicDetailsCount,boolean containerstats)
	 {     
		    if(jsonpath.equalsIgnoreCase("volumeExtn")){
		    	return getVolumeExt(handler,type,historicDetailsCount,containerstats);			
		    }
		    if(jsonpath.equalsIgnoreCase("rateExtn")){
		    	return getRateExt(handler,type,historicDetailsCount,containerstats);			
		    }
		    
		    return "0.0";
	}
	 
	 public String	getVolumeExt(JsonDataHandler handler,String type,int historicDetailsCount, boolean containerstats)
		{	
		 	if(containerstats){
		 		if(type.equalsIgnoreCase("technical"))
					return  handler.getValue("$.input["+historicDetailsCount+"].results[0].TECHNICAL_ERRORS");
				  if(type.equalsIgnoreCase("site"))
					  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].SITE_DEPENDENT_ERRORS");
				  if(type.equalsIgnoreCase("userActionRequired"))
					  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].USER_DEPENDENT_ERRORS");
		 	}
		 	else{
		 		  if(type.equalsIgnoreCase("technical"))
						return  handler.getValue("$.results["+historicDetailsCount+"].TECHNICAL_ERRORS");
					  if(type.equalsIgnoreCase("site"))
						return  handler.getValue("$.results["+historicDetailsCount+"].SITE_DEPENDENT_ERRORS");
					  if(type.equalsIgnoreCase("userActionRequired"))
						return  handler.getValue("$.results["+historicDetailsCount+"].USER_DEPENDENT_ERRORS");
				 
		 	}
		      
		 
		 return "0.0";
		}
	 
	 public String	getRateExt(JsonDataHandler handler,String type,int historicDetailsCount,boolean containerstats)
		{	 
		 if(containerstats){
		      if(type.equalsIgnoreCase("technical"))
		    	  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].TECHNICAL_ERR_PER");
			  if(type.equalsIgnoreCase("site"))
				  return  handler.getValue("$.input["+historicDetailsCount+"].results[0].SITE_DEPENDENT_ERR_PER");
			 if(type.equalsIgnoreCase("userActionRequired"))
				 return  handler.getValue("$.input["+historicDetailsCount+"].results[0].USER_DEPENDENT_ERR_PER");
		 }
		 else{
			 if(type.equalsIgnoreCase("technical"))
					return  handler.getValue("$.results["+historicDetailsCount+"].TECHNICAL_ERR_PER");
				  if(type.equalsIgnoreCase("site"))
					return  handler.getValue("$.results["+historicDetailsCount+"].SITE_DEPENDENT_ERR_PER");
				 if(type.equalsIgnoreCase("userActionRequired"))
					return  handler.getValue("$.results["+historicDetailsCount+"].USER_DEPENDENT_ERR_PER");
				 
		 }
			 
			 
		return "0.0";
		 
		}
}
