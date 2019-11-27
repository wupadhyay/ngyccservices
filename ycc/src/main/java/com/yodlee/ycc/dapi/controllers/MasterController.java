/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.yodlee.dom.entity.CobParam;
import com.yodlee.dom.entity.CobParam_;
import com.yodlee.dom.entity.Cobrand;
import com.yodlee.dom.entity.Cobrand_;
import com.yodlee.dom.entity.MemPref;
import com.yodlee.dom.entity.MemPref_;
import com.yodlee.dom.entity.ParamKey;
import com.yodlee.dom.entity.ParamKey_;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.dao.DAO;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.ICobrandContext;
import com.yodlee.framework.runtime.shared.context.SharedConstants;
import com.yodlee.framework.service.metamodel.SdmParam;
import com.yodlee.framework.service.metamodel.ServiceMetamodelHelper;
import com.yodlee.framework.service.rest.JSONResource;
import com.yodlee.nextgen.engine.ApiSqlTrace;
import com.yodlee.nextgen.logging.MessageController;
import com.yodlee.nextgen.session.exceptions.InvalidSessionException;
import com.yodlee.nextgen.validation.session.SessionValidationServices;
import com.yodlee.ycc.dapi.exceptions.ApiException;

public abstract class MasterController extends JSONResource{
	
	public static String EMPTY_JSON = "{}";
	public static String toreplace1 = "There is no refresh request for mem site account id in MFA_MESSAGE_STATUS";
	public static String toreplace2 = "Invalid argument value: Site is not having any content service";
	public static String replaced1 = "There is no MFA request for provider account";
	public static String replaced2 = "Provider is not having any containers that is enabled and is not custom";

	private static final String FQCN = MasterController.class.getName();

	
	
	@Autowired(required=true)
    @Qualifier(value="ApiSqlTrace")
    protected ApiSqlTrace sqlTrace;
	 
	protected static Pattern p = Pattern.compile("[^A-Za-z0-9 /\\\\]");
	protected static Pattern dateFormatYYYY_MM_DD = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	
	private static ObjectMapper mapper=null;
	public static  volatile Map<String, ArrayList<String>> questionMapMFA=null;
	
	static {
		
		mapper = new ObjectMapper();
		questionMapMFA=new HashMap<String, ArrayList<String>>();

	}

	
	public ICobrandContext getCobrandContext(HttpServletRequest request) throws ApiException {
		String cobId = (String) request.getAttribute("cobId");
		String appId = (String) request.getAttribute("appId");
		ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, appId);
		ICobrandContext cc =  ContextAccessorUtil.getContext();
		return cc;
	}
	
	
	
	public ICobrandContext processContext() throws MalformedURLException, InvalidSessionException, ApiException {
		 return processContext(false);
   }

		
	protected ICobrandContext processContext(boolean isSubbrand) throws MalformedURLException, InvalidSessionException, ApiException {
			
		    // As the cobrandId, AppId are stored in threadLocal, so getting it from threadLocale
			ICobrandContext context = ContextAccessorUtil.getContext();
			HttpServletRequest request  = (HttpServletRequest)context.getObject(SharedConstants.REQUEST);			
			ICobrandContext cc = getCobrandContext(request);
			long cobrandId = cc.getCobrandId();
			
			// if subbrand then createContext by setting channelId
			if(isSubbrand) {
				
				// if subbrand is logged in then cobrandId is nothing but subbrandId, 
				//so when below line is executed cobrandId will be storing the subbrandId
								
				// populate context object with channelId , if isSubrand flag is true
				if(context.getChannelId() > 0 ) {
				  ContextAccessorUtil.setContext(context.getChannelId(), context.getMemId(), context.getApplicationId());
				  cobrandId = context.getChannelId();
			    }
		     }
			// setContextAccessor is used by framework
   		ContextAccessorUtil.setContextAccessor(cobrandId, context.getMemId(), context.getApplicationId());
   		// setContext is used in controllers
   		ContextAccessorUtil.setContext(cobrandId, cc.getMemId(), cc.getApplicationId());
		
	   return context;
	}
	
	
	
	
	public boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
			Matcher matcher = dateFormatYYYY_MM_DD.matcher(inDate.trim());
			if(!matcher.matches())
				return false;
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
	public String convertToDate(String dateInMills){
		Date date = new Date(Long.parseLong(dateInMills) * 1000L);
		DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return(format.format(date));
	}
	
	public Long convertToTime(String date, Boolean addTime){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		dateFormat.setLenient(false);
		long addSec = 0;
		if(addTime){
			addSec = 86399; // 23 hours 59 min 59 sec
		}
		try {
			return dateFormat.parse(date.trim()).getTime()/1000 + addSec;
		} catch (ParseException pe) {
			return null;
		}
	}
	
	
	public Long convertToTime(String date, Boolean addTime,String interval){
		Long dateInSeconds=null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		dateFormat.setLenient(false);
		long addSec = 0;
		if(addTime){
			addSec = 86399; // 23 hours 59 min 59 sec
		}
		
		try{
			if(interval!=null && !interval.isEmpty()){
			   Calendar calendar = Calendar.getInstance();
	           calendar.setTime(dateFormat.parse(date.trim()));
	        	if(interval.equalsIgnoreCase("M")){
					 calendar.set(Calendar.DAY_OF_MONTH, 1);
			         System.out.println("First date of the month"+dateFormat.parse(dateFormat.format(calendar.getTime())).getTime()/1000+addSec);
			         dateInSeconds=dateFormat.parse(dateFormat.format(calendar.getTime())).getTime()/1000+addSec;
				}
				else if((interval.equalsIgnoreCase("W")) && (!addTime)){
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					
					if (week != Calendar.SUNDAY)
					{
					    int days = (Calendar.SUNDAY - week) % 7;
					    calendar.add(Calendar.DAY_OF_YEAR, days);
					}
					 dateInSeconds=dateFormat.parse(dateFormat.format(calendar.getTime())).getTime()/1000+addSec;

				} else{
					dateInSeconds= dateFormat.parse(date.trim()).getTime()/1000+addSec;
				}
			} 
		}
	
		catch (ParseException pe) {
			MessageController.log(FQCN, 249, "convertToTime:" +pe.getMessage() , MessageController.FATAL);
			pe.printStackTrace();
		}
		return dateInSeconds;
	}
	
	public static boolean checkDateRange(String fromDate,String toDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		Date from = dateFormat.parse(fromDate.trim());
		Date to = dateFormat.parse(toDate.trim());
		
		if(from.after(to)) {
			return false;
		} 
		
		return true;
	}
	
	public static boolean isFutureDate(Long date) {
		Date currentDate = new Date(); 
		return(date>(currentDate.getTime()/1000+86399)?true:false);
	}
	
	public static String getResponseCount(Integer skip,Integer top,HttpServletRequest request,StringBuffer keys,
			                                Integer totalCount){
		
		Integer calculatedCount=0;
		String retqueryparams ="";
		StringBuilder previousSkipAndTop=new StringBuilder();
		previousSkipAndTop.append(keys.toString());
		if ((skip-top)<=0){
			previousSkipAndTop.append("top="+skip);
		}else{
			previousSkipAndTop.append("skip="+(skip-top));
			previousSkipAndTop.append("&top="+top);
		}
		StringBuilder linkBuilder=new StringBuilder();
		if(skip==0){
			calculatedCount=Math.abs(totalCount-(top));
		}
		if(skip+top>totalCount){
				calculatedCount=0;
		} else{
			calculatedCount=Math.abs(totalCount-(skip+top));
		}
		
		
		if(calculatedCount>500){
			calculatedCount=500;
		}
		String urlWithOutPagination=keys.toString();
		if(keys.length()==1){
			urlWithOutPagination=urlWithOutPagination.replace("?", "");
		}
				
         int removeExtraChar=urlWithOutPagination.lastIndexOf("&");
		 if(removeExtraChar!=-1){
			 urlWithOutPagination=urlWithOutPagination.substring(0, removeExtraChar);
		 }
	
		keys.append("skip="+(skip+top));
		if(calculatedCount!=0 ){
			if(top==500 || calculatedCount<top){
				keys.append("&top="+calculatedCount);
			}else{
				keys.append("&top="+top);
			}
			
		}
			retqueryparams = keys.toString();
		
			if(skip==0 || skip==null){
				if(!((skip+top)>=totalCount)){
					linkBuilder.append(request.getRequestURI().toString()+retqueryparams.substring(0, retqueryparams.length())+ ";rel=next, ");
				}
				linkBuilder.append(request.getRequestURI().toString()+"/count"+urlWithOutPagination+ ";rel=count");
			
				
			}else{
				linkBuilder.append(request.getRequestURI().toString()+previousSkipAndTop+ ";rel=previous, ");
				if(!((skip+top)>=totalCount)){
					linkBuilder.append(request.getRequestURI().toString()+retqueryparams.substring(0, retqueryparams.length())+ ";rel=next, ");
				}
				linkBuilder.append(request.getRequestURI().toString()+"/count"+urlWithOutPagination+ ";rel=count");

			}
			
	 
			
	return 	linkBuilder.toString();
  }
	

	public static HashMap<String, String> getRestRequest(String jsonRequest) {
		HashMap<String, String> output = new HashMap<>();
		try {
			JsonNode node = mapper.readTree(jsonRequest);
			
			JsonNode nodeObject =  mapper.readTree(jsonRequest);
			
			StringBuffer path = new StringBuffer();
			HashMap<String, String> jsonMap = new HashMap<String, String>();
			populateNodeMap(nodeObject, path, jsonMap);
			System.out.println(node.toString());

			return jsonMap;
		} catch (JsonProcessingException e) {
			System.out.println("JsonProcessingException");
		} catch (IOException e) {
            System.out.println("IOException");		
            }
		
		return output;
	}
	
	
	private static  void populateNodeMap(JsonNode node,StringBuffer path,HashMap<String,String> jsonMap) {
		JsonNodeType nodeType = node.getNodeType();

		if (nodeType == JsonNodeType.ARRAY) {
			Iterator<JsonNode> datasetElements = node.iterator();
			String temppath = path.toString();
			int count=0;
			while (datasetElements.hasNext()) {
				JsonNode datasetElement = datasetElements.next();
				path.append(count+".");
				populateNodeMap(datasetElement,path,jsonMap);
				path = new StringBuffer(temppath);
				count++;
			}
		} else if (nodeType == JsonNodeType.OBJECT) {
			Iterator<Entry<String, JsonNode>> fields = node.fields();
			while (fields.hasNext()) {
				Entry<String, JsonNode> nextNode = fields.next();
				if(nextNode.getValue().isContainerNode()) {
					path.append(nextNode.getKey()+".");
					populateNodeMap(nextNode.getValue(),path,jsonMap);
					path = new StringBuffer(path.substring(0, path.lastIndexOf(nextNode.getKey()+".")));
				} else {
					String value = nextNode.getValue().asText();
					if(value!="") {
						jsonMap.put(trunc(path+nextNode.getKey()), value);
					}
				}
			}
		} else {
			String value = node.asText();
			if(value!="") {
				jsonMap.put(trunc(path.toString()), value);
			}
		}
	}
	
	private static String trunc(String str) {
		if(str.charAt(str.length()-1)=='.') {
			str=str.substring(0, str.length()-1);
		}
		return str;
	}
	
	
	  public static String format(String format, Map<String, String> dataPointMap) {
	        for (Map.Entry<String, String> entry : dataPointMap.entrySet()) {
	            String key = entry.getKey();
	            Object value = entry.getValue();
	            if(value!=null)
	            format = format.replace("{"+key+"}", value.toString());
	        }
	
	        return format;
	    }
		 public static String replaceErrorStatusForAddAcc(String json){
			 
			  if (json==null||json.isEmpty()||json.equals("{}"))
					  return json;
			  int start = -1;
			  int end = -1;
			  
			  try {
				if(json.contains(toreplace1)){
					 start = json.indexOf(toreplace1);
					  
					 end = json.indexOf("\"",start+1);
					 String toPlace = json.substring(start,end);

					  json = json.replaceFirst(toPlace, replaced1);
							 
				  }
				  
				  if(json.contains(toreplace2)){
					  start = json.indexOf(toreplace2);
					  
					  end = json.indexOf("\"",start+1);
					  String toPlace = json.substring(start,end);
					  json = json.replace(toPlace, replaced2);
				  }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return json;
			}
			  return json;
		  }
		 
		
		 
		 public static void selectCobrandLocale(String cobSessionToken){
			 
			 String appId = ContextAccessorUtil.getContext().getApplicationId();
			long cobrandId = ContextAccessorUtil.getContext().getCobrandId();
			SdmParam  sdmParam=ServiceMetamodelHelper.getSdmParam("COBRAND_DEFAULT_LOCALE");
			ParamKey paramKey =  ParamKey.DAO.getSingle(ParamKey_.paramKeyName, sdmParam.key);
			long paramKeyId = paramKey.getParamKeyId(); 
			 CobParam cobParam = CobParam.DAO.getSingle(CobParam_.paramKeyId, paramKeyId,CobParam_.cobrandId,cobrandId,CobParam_.appId,appId); 
				if(cobParam==null) {
					cobParam = CobParam.DAO.getSingle(CobParam_.paramKeyId, paramKeyId,CobParam_.cobrandId,cobrandId, CobParam_.appId,null);
				}
				
//				LocaleBuilder.getLocaleStorageMap(cobSessionToken)
//	             .cobParamLocale(cobParam!=null?cobParam.getParamValue():null)
//	             .cobrandDefaulLocale(paramKey.getDefaultValue());
				
			 Cobrand cobrand = Cobrand.DAO.get(cobrandId);
			 if(cobrand.getChannelId()!=null && cobrand.getChannelId()>0) {
				cobrandId = cobrand.getChannelId();
				cobParam = CobParam.DAO.getSingle(CobParam_.paramKeyId, paramKeyId,CobParam_.cobrandId,cobrandId,CobParam_.appId,appId); 
				if(cobParam==null) {
					 cobParam = CobParam.DAO.getSingle(CobParam_.paramKeyId, paramKeyId,CobParam_.cobrandId,cobrandId, CobParam_.appId,null);
				}
//				LocaleBuilder.getLocaleStorageMap(cobSessionToken)
//				             .channelCobParamLocale(cobParam!=null?cobParam.getParamValue():null)
//				             .channelDefaulLocale(paramKey.getDefaultValue());
			}
						
				
				             
			                                                  
		 }
		 
		 public static String selectMemPrefLoginLocale(Long cobrandId,Long memId) {
			 List<MemPref> memPrefLst=null;
			  if(cobrandId!=null){
				  
				  Criteria criteria = new Criteria();
					criteria.add(MemPref_.prefKeyName,"com.yodlee.userProfile.COBRAND.LOCALE");
					criteria.add(MemPref_.memId,cobrandId);
				    memPrefLst = MemPref.DAO.select(criteria);
				  
			  } else if(memId!=null){
				    Criteria criteria = new Criteria();
					criteria.add(MemPref_.prefKeyName,"com.yodlee.userProfile.USER.LOCALE");
					criteria.add(MemPref_.memId,memId);
				    memPrefLst = MemPref.DAO.select(criteria);
					
			  }
			  return (memPrefLst!=null && memPrefLst.size()>0)?memPrefLst.get(0).getPrefKeyValue():null;
			}
		 
		 public static String selectMemPrefLocale(Long memId) {
				Criteria criteria = new Criteria();
				criteria.innerJoin(MemPref_.mem);
				criteria.add(MemPref_.prefKeyName,"com.yodlee.userprofile.LOCALE");
				criteria.add(MemPref_.memId,memId);
			    List<MemPref> memPrefLst = MemPref.DAO.select(criteria);
			    
			    if(memPrefLst==null || memPrefLst.size()==0){
			    	Criteria criteria1 = criteria.clear();
					criteria1.innerJoin(MemPref_.mem);
					criteria1.add(MemPref_.prefKeyName,"COM.YODLEE.USER.LOCALE");
					criteria1.add(MemPref_.memId,memId);
				    memPrefLst = MemPref.DAO.select(criteria1);
				    
			    }
			    
				return (memPrefLst!=null && memPrefLst.size()>0)?memPrefLst.get(0).getPrefKeyValue():null;
			}
		 
				 
		 public  boolean valiateLocale(String locale,long cobrandId) throws InvalidSessionException, MalformedURLException, ApiException{
		    	String suportedLocalesForLoggedInContext;
		    		
		        Criteria cr = new Criteria();
		        cr.and(Cobrand.class, Cobrand_.cobrandId, cobrandId);
		        List<Cobrand> cobrands = DAO.select(Cobrand.class, cr);
		        Cobrand cobrand = cobrands.get(0);
		        Long channelId = cobrand.getChannelId();
		      //  SdmParam  sdmParam=ServiceMetamodelHelper.getSdmParam("SUPPORTED_LOCALE");
		        suportedLocalesForLoggedInContext = ServiceMetamodelHelper.getParamValue("SUPPORTED_LOCALE");
		        boolean localeAlreadyChecked=false;
		    	List<String> supportedLocales=Arrays.asList(suportedLocalesForLoggedInContext.trim().split(","));

		        // cobrand locale fetch
		        if(channelId==null){
		        	 if(supportedLocales.contains(locale)){
		        		 localeAlreadyChecked=true;
		                 return true;
		        	 }

		        }
		        else{
		        	// already locale is checked so no need to again query for locale in case of subrand
		        	if(localeAlreadyChecked){
		        		return true;
		        	} 
		        	 else{
				        	// already locale is checked so no need to again query for locale in case of subrand
				        	if(localeAlreadyChecked){
				        		return true;
				        	} 
				        	else{
				        		//as in the context subbrand is available, so overriding it to channel and set back to subbrand again
				        		processContext(true);
				        		suportedLocalesForLoggedInContext = ServiceMetamodelHelper.getParamValue("SUPPORTED_LOCALE");
				        		processContext();
					  			 supportedLocales=Arrays.asList(suportedLocalesForLoggedInContext.trim().split(","));
				        		if(supportedLocales.contains(locale)){
				                    return true;
				           	    }
				        	}
				        	
				        }
		        	
		        }
		     
		      return false;
		 }
	
}

