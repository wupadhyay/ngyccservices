/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

import com.yodlee.restbridge.RestBridgeExecutor;
import com.yodlee.restbridge.RestBridgeRequestVO;
import com.yodlee.restbridge.exceptions.RestBridgeException;


 public  class RestBridgeTemplate implements RestBridgeService {
	 
   
    private RestBridgeExecutor executor;
    private RestBridgeRequestVO restBridgeRequestVO=null;
	private HashMap<String,String> restBridgeRequestParams=null;
	private String cobrandName=null;
	private HashMap<String,String> restBridgeResponseParams=null;
	private Map<String,String[]> requestBodyParamMap=null;
	private HashMap<String,String> requestInputParams=null;
	HttpServletRequest request=null;
	
	public RestBridgeTemplate( RestBridgeExecutor executor,HttpServletRequest request,HashMap<String,String> restBridgeRequestParams,
	                                                         String cobrandName,
															 HashMap<String,String> restBridgeResponseParams,Map<String,String[]> requestBodyParamMap){
		                this.executor=executor;
		                this.request=request;
						this.restBridgeRequestParams=restBridgeRequestParams;	
						this.requestBodyParamMap=requestBodyParamMap;
						this.requestInputParams=populateRequestParams(requestBodyParamMap);
						this.cobrandName=cobrandName;
                        this.restBridgeResponseParams=restBridgeResponseParams;	
                        
															 
															 }
	
	
	
		   // 1. Standardize the skeleton of an algorithm in a "template" method
		  public final String processRestBridgeRequest() throws HttpException, IOException, RestBridgeException {
		      String restBridgeResponse=null;
		      createAndPopulateRequestVO();
			  restBridgeResponse=callRestBridge();
		      handleRestBridgeResponse(restBridgeResponse);
		      return restBridgeResponse;
		   }
		   
		  private void  createAndPopulateRequestVO(){
		   restBridgeRequestVO=new RestBridgeRequestVO();
		   List<NameValuePair> listNameValuePairs=new ArrayList<NameValuePair>();
		   if(restBridgeRequestParams!=null){
			   
			   for (Map.Entry<String, String> entry : restBridgeRequestParams.entrySet())
			   {
			       
			       if(entry.getKey()!=null && entry.getKey().equalsIgnoreCase("cobSessionToken")){
			    	   NameValuePair cobsessionToken = new NameValuePair(entry.getKey(),requestInputParams.get(truncateRequest(entry.getValue())));
			    	   restBridgeRequestVO.setCobrandSessionToken(cobsessionToken);
			    	   
			       }else if(entry.getKey()!=null && entry.getKey().equalsIgnoreCase("userSessionToken")){
			    	   NameValuePair userToken = new NameValuePair(entry.getKey(),requestInputParams.get(truncateRequest(entry.getValue())));
			    	   restBridgeRequestVO.setUserSessionToken(userToken);
			       }else if(entry.getKey()!=null && entry.getKey().equalsIgnoreCase("restURI")){
			    	   restBridgeRequestVO.setRestURI(cobrandName+entry.getValue());
			       }
			      			       
			       else if(entry.getKey()!=null && entry.getValue()!=null){
			    	    NameValuePair restBridgeParams = new NameValuePair(entry.getKey(),requestInputParams.get(truncateRequest(entry.getValue()))==null?entry.getValue():requestInputParams.get(truncateRequest(entry.getValue())));
			    	    listNameValuePairs.add(restBridgeParams);

				}

			}

			restBridgeRequestVO.setNameValuePairs(listNameValuePairs.toArray(new NameValuePair[]{}));
		}

	}
		  
		  private String truncateRequest(String key){
			  if(key.indexOf("request_")!=-1){
				  key=key.substring(key.indexOf("_")+1);
			  }
		  return key;
		  }
		  
		  
		  public HashMap getRequestParamMap(){
			 return requestInputParams;
		  }
		  
		  @SuppressWarnings("unchecked")
		 private HashMap populateRequestParams(Map<String,String[]> requestBodyParamMap){
			    HashMap restRequestMap=new HashMap();
			    String cobSessionToken = request.getAttribute("cobSession").toString();
			    String userSessionToken = request.getAttribute("userSession").toString();
			    if(cobSessionToken!=null && !cobSessionToken.isEmpty()){
			    	restRequestMap.put("cobSession", cobSessionToken);
			    }
			    if(userSessionToken!=null && !userSessionToken.isEmpty()){
			    	restRequestMap.put("userSession", userSessionToken);
			    }
		    	 Map<String,String[]> map = request.getParameterMap();
		    	 if(map==null || map.isEmpty()){
		    		 if(requestBodyParamMap!=null)
		    		      map=requestBodyParamMap;
		    	 }
				 for(String key : map.keySet()){
					 if( map.get(key)[0]!=null){
					 restRequestMap.put(key, (String) map.get(key)[0]);
					 }
				 }
				return restRequestMap;
			  
		  }
			  
		 private String  callRestBridge() throws HttpException, IOException, RestBridgeException{
		  	String restBridgeResponse= executor.execute(restBridgeRequestVO);
			return restBridgeResponse;
		  }
		  
		 private void handleRestBridgeResponse(String restBridgeResponse){
		
			}
			 
		  
	
				  
}
