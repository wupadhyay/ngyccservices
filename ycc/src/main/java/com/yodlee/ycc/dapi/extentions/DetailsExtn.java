package com.yodlee.ycc.dapi.extentions;

import java.util.List;

import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class DetailsExtn {
	
	
	
 public String	getDetailsExtn( JsonDataHandler handler,String jsonpath,boolean isOverall)
	{
			  
	    if(isOverall&&jsonpath.equalsIgnoreCase("totalVolumeExtn")){
	    	  List<String> 	totalrefreshes=handler.getValues("$.results[*].TOTAL_REFRESHES");
	    	  return "0.0";
	    } else {
	    		  
	    	  }
	    	
		
	
	     return "0.0";
	
	}

}
