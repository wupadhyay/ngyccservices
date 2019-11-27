/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.controllers.RefreshStatsController;
import com.yodlee.ycc.dapi.extentions.CommonExtn;
import com.yodlee.ycc.dapi.extentions.FailureDetailsExtn;
import com.yodlee.ycc.dapi.extentions.FailureExtn;
import com.yodlee.ycc.dapi.extentions.LatencyExtn;
import com.yodlee.ycc.dapi.extentions.SuccessExtn;
import com.yodlee.ycc.dapi.extentions.SummaryExtn;
import com.yodlee.ycc.dapi.extentions.SupportedContainerStatsExtn;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.ContainerStat;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.Details;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.Result;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.SplunkEntity;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;
import com.yodlee.ycc.dapi.utils.MiscUtil;

@Deprecated
public class SplunkResponseFormat {
	private static final Logger logger = LoggerFactory.getLogger(RefreshStatsController.class);
	static Map<String, List<AttributeProperty>> entityMap = new LinkedHashMap<String, List<AttributeProperty>>();
	static Map<String, List<AttributeProperty>> componentsMap = new LinkedHashMap<String, List<AttributeProperty>>();
	static Map<String, List<String>> enumMap = new LinkedHashMap<String, List<String>>();
	static Map<String, String> excludedEntityFromResponse = new HashMap<String, String>();
	 Map<String,List<String>> myMapSuminfo= new LinkedHashMap<String,List<String>>();
	 Map<String,List<String>> myMapDateinfo= new LinkedHashMap<String,List<String>>();
	 String historicRes="";
	 boolean containerStats=false;
	 boolean historic=false;
	 boolean isYodlee=false;
	 boolean includeLatencyBreakup=false;
	 Map<String, List<AttributeProperty>> entityMap1 = new LinkedHashMap<String, List<AttributeProperty>>();
	 Map<String, List<AttributeProperty>> componentsMap1 = new LinkedHashMap<String, List<AttributeProperty>>();
	 Map<String, List<String>> enumMap1 = new LinkedHashMap<String, List<String>>();
	 Map<String, String> excludedEntityFromResponse1 = new HashMap<String, String>();
	
	List<String> allExcludedFromResponse = new ArrayList<String>();
	private JsonObject refreshStats = new JsonObject();
	private JsonObject rootEntity = new JsonObject();
	private JsonArray rootEntityAttributes = new JsonArray();
	JsonDataHandler detailedSiteRefreshLatencyStatsHandler = new JsonDataHandler();
	JsonDataHandler historicRefreshlatencyBreakDownStatsHandler = new JsonDataHandler();
	JsonDataHandler myHandeller = new JsonDataHandler();

	FailureExtn failureExtn = new FailureExtn();
	SummaryExtn summaryExtn = new SummaryExtn();
	SuccessExtn successExtn = new SuccessExtn();
	LatencyExtn latencyExtn = new LatencyExtn();
	FailureDetailsExtn fdExtn = new FailureDetailsExtn();
	SupportedContainerStatsExtn supportedContainerStatsExtn= new SupportedContainerStatsExtn();
	CommonExtn commonExtn = new CommonExtn();
	static Properties props;
	boolean isProcessLatencyDetails = false;
	int historicDetailsCount = 0;
	private Map<String, String> responseFilters;
	public static DecimalFormat dformat = new DecimalFormat(".##");
	static {
		String sdm = MiscUtil.getPropertyValue("SDM_PATH_NAME", false);
		sdm = sdm.replace("sdm", "yccsdm");
		SDMProcessingHelper sdmph = new SDMProcessingHelper();
		File sdmPath = new File(sdm);
		if (sdmPath.exists() && sdmPath.isDirectory() && !sdmPath.isFile() && (sdmPath.list().length > 0)) {
			for (String f : sdmPath.list()) {
				File file = new File(sdmPath + File.separator + f);
				if(!file.isDirectory())
					sdmph.processFile(file);
			}
		} else {
			throw new RuntimeException("Not valid SDM path Configured.Check Config.properties");
		}
		entityMap = sdmph.getEntityMap();
		componentsMap = sdmph.getComponentsMap();
		enumMap = sdmph.getEnumMap();	
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("FinalJsonResponseFilter.properties");
			Properties p = new Properties();
			p.load(inputStream);
			Set<Entry<Object,Object>> entrySet = p.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				excludedEntityFromResponse.put(entry.getKey().toString(), entry.getValue().toString());
			}
			inputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Oops some issue at our end");
		}

	}

	public String formatSplunkResponse(Map<String, String> filterinputs, String detailedSiteRefreshLatencyStats, String historicRefreshlatencyBreakDownStats) {
		if(filterinputs.get("container") !=null && filterinputs.get("container").equalsIgnoreCase("true")){
			containerStats=true;
		}
		if(filterinputs.get("historic")!=null && filterinputs.get("historic").equalsIgnoreCase("true")){
			historic=true;
		}
		if(filterinputs.get(YSLConstants.USER_TYPE).equalsIgnoreCase("tier2")){
			isYodlee=true;
		}
		if (filterinputs.get(YSLConstants.INCLUDE_LATENCY_BREAKUP).equalsIgnoreCase("true")) {
			includeLatencyBreakup=true;
		}
		detailedSiteRefreshLatencyStatsHandler.parseJson(detailedSiteRefreshLatencyStats);
		if (((filterinputs.get("duration") != null) && !(filterinputs
				.get("duration").contains("h")))
				|| ((filterinputs.get("duration") == null))){
			historicRefreshlatencyBreakDownStatsHandler
			.parseJson(historicRefreshlatencyBreakDownStats);
		}
		else if((filterinputs.get("duration") != null) && filterinputs
				.get("duration").contains("h") && historicRefreshlatencyBreakDownStats==null){
			historicRefreshlatencyBreakDownStats=detailedSiteRefreshLatencyStats;
			historicRefreshlatencyBreakDownStatsHandler
			.parseJson(detailedSiteRefreshLatencyStats);
		}
		
		else
			historicRefreshlatencyBreakDownStatsHandler
					.parseJson(detailedSiteRefreshLatencyStats);
	   responseFilters=new HashMap<String,String>(filterinputs);
		finalJSONResponseStructure(filterinputs);
		if (entityMap1.size() == 0) {
			logger.debug("Response Should contain Entity");
		} else {
			Map.Entry rootentityMap1 = entityMap1.entrySet().iterator().next();
			List<AttributeProperty> attrList = new ArrayList<AttributeProperty>();
			String rootEntityName = rootentityMap1.getKey().toString();
			if (rootEntityName != null && rootEntityName != "") {
				attrList = (ArrayList<AttributeProperty>) rootentityMap1.getValue();
				for (int i = 0; i < attrList.size(); i++) {
					AttributeProperty ap = attrList.get(i);
					processAttribute(rootEntity, filterinputs, ap,
							detailedSiteRefreshLatencyStats,historicRefreshlatencyBreakDownStats);
				}
				refreshStats.add(rootEntityName, rootEntity);
			}
		}
		containerStats=false;
		historic=false;
		String refreshStatsResponse=refreshStats.toString();
		refreshStatsResponse=refreshStatsResponse.replaceAll("supportedContainerStats", "containerStats");
		return refreshStatsResponse;
	}

	private void processAttribute(JsonObject jobj,Map<String, String> filterinputs, AttributeProperty ap, String detailedSiteRefreshLatencyStats, String historicRefreshlatencyBreakDownStats) {
		Map<String,List<String>> myMap= new LinkedHashMap<String,List<String>>();
		Map<String,List<String>> myMapx= new LinkedHashMap<String,List<String>>();
		historicRes=historicRefreshlatencyBreakDownStats;
       if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
			if (ap.getAttribute().equalsIgnoreCase("groupBy"))
				rootEntity.addProperty(ap.getAttribute(), responseFilters.get("groupby"));
			else if (ap.getAttribute().equalsIgnoreCase("orderBy") && responseFilters.get("groupby").toString().equalsIgnoreCase(YSLConstants.GROUPBY_PROVIDER))
				rootEntity.addProperty(ap.getAttribute(), responseFilters.get("statsExtnType")==null? "VOLUME":responseFilters.get("statsExtnType"));
			else if (ap.getAttribute().equalsIgnoreCase("refreshType"))
				rootEntity.addProperty(ap.getAttribute(), responseFilters.get("refreshType"));

		} else if (ap.isEntity()) {
			if (ap.isEntity()) {
				String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
				if (entityType.length() == 0) {
					rootEntity.add(ap.getAttribute(), getRootEntityAttributes(ap.getAttribute(),detailedSiteRefreshLatencyStatsHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats));
				} else {
					if ("info".equalsIgnoreCase(ap.getAttribute()) && responseFilters.get("groupby").equalsIgnoreCase("PROVIDER")) {
						if(containerStats && historic){
							JsonElement jelementx = new JsonParser().parse(historicRefreshlatencyBreakDownStats);
							JsonObject jobjectx = jelementx.getAsJsonObject();
							JsonArray jarrayx = jobjectx.getAsJsonArray("results");
							for (int i = 0; i < jarrayx.size(); i++) {
								JsonElement jsonElement = jarrayx.get(i);
								String jsonelStr = jsonElement.toString();
								jsonelStr = "{\"results\":[" + jsonelStr + "]}";
								myHandeller.parseJson(jsonelStr);
								String Site_id=myHandeller.getValue("$.results[0].SITE_ID");
								if(myMapx.get(Site_id)==null){
									myMapx.put(Site_id, new ArrayList<String>());
								}
								myMapx.get(Site_id).add(jsonelStr);
							}
						}
						
						
						JsonArray rootEntityAttributes2 = null;
						JsonElement jelement = new JsonParser().parse(detailedSiteRefreshLatencyStats);
						JsonObject jobject = jelement.getAsJsonObject();
						JsonArray jarray = jobject.getAsJsonArray("results");
						if(containerStats){
						for (int i = 0; i < jarray.size(); i++) {
							JsonElement jsonElement = jarray.get(i);
							String jsonelStr = jsonElement.toString();
							jsonelStr = "{\"results\":[" + jsonelStr + "]}";
							myHandeller.parseJson(jsonelStr);
							String Site_id=myHandeller.getValue("$.results[0].SITE_ID");
							if(myMap.get(Site_id)==null){
								myMap.put(Site_id, new ArrayList<String>());
							}
							myMap.get(Site_id).add(jsonelStr);
						}
						}
						if(containerStats){
						
						 for(String str:myMap.keySet()){
					    		detailedSiteRefreshLatencyStatsHandler = new JsonDataHandler();
					    		String fs="";
					    		List<String> myList= myMap.get(str);
					    		for(String s: myList){
					    			fs+=s+",";
					    		}
					    		String jsonelStr = "{\"input\":[" + fs.substring(0, fs.length()-1) ;
								detailedSiteRefreshLatencyStatsHandler.parseJson(jsonelStr+ "]}");
								//detailed
								if(containerStats && historic){
									String fsx="";
						    		List<String> myListx= myMapx.get(str);
						    		for(String s: myListx){
						    			fsx+=s+",";
						    		}
						    		String jsonelStrx = "{\"input\":[" + fsx.substring(0, fsx.length()-1) ;
									historicRefreshlatencyBreakDownStatsHandler.parseJson(jsonelStrx+ "]}");
									//detailed
								}
								
								JsonArray rootEntityAttributes3 = getRootEntityAttributes(ap.getAttribute(),detailedSiteRefreshLatencyStatsHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats);
								rootEntityAttributes2 = rootEntityAttributes3;
					       }
							
						}
						else{
							for (int i = 0; i < jarray.size(); i++) {
								JsonElement jsonElement = jarray.get(i);
								String jsonelStr = jsonElement.toString();
								jsonelStr = "{\"results\":[" + jsonelStr + "]}";
								detailedSiteRefreshLatencyStatsHandler = new JsonDataHandler();
								detailedSiteRefreshLatencyStatsHandler.parseJson(jsonelStr);
								JsonArray rootEntityAttributes3 = getRootEntityAttributes(ap.getAttribute(),detailedSiteRefreshLatencyStatsHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats);
								rootEntityAttributes2 = rootEntityAttributes3;
							}
						}
						detailedSiteRefreshLatencyStatsHandler.parseJson(detailedSiteRefreshLatencyStats);
						rootEntity.add(ap.getAttribute(), rootEntityAttributes2);
					} else
						rootEntity.add(ap.getAttribute(), getRootEntityAttributes(entityType,detailedSiteRefreshLatencyStatsHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats));
				}
			}
		} else if (ap.isComponent()) {
			rootEntity.add(ap.getAttribute(), getComponentAttributes(ap.getAttribute(),detailedSiteRefreshLatencyStatsHandler,false));
		} else if (ap.isEnum()) {

		}
      
	}

	private JsonArray getRootEntityAttributes(String rootEntityAttrName,JsonDataHandler jsonDataHandler,JsonDataHandler historicRefreshlatencyHandeller,boolean includeContainerflag) {
		JsonObject infoEntity = new JsonObject();
		JsonArray infoEntityArray = new JsonArray();
		JsonObject summaryEntity = new JsonObject();
		JsonArray summaryEntityArray = new JsonArray();
		JsonObject summaryDetailsEntity = new JsonObject();
		JsonArray summaryDetailsEntityArray = new JsonArray();
		JsonObject summaryDateEntity = new JsonObject();
		JsonArray summaryDateEntityArray = new JsonArray();
		JsonObject successEntity = new JsonObject();
		JsonObject failureEntity = new JsonObject();
		JsonArray detailsEntityArray = new JsonArray();
		JsonObject latencyEntity = new JsonObject();
		JsonObject failuredetailsEntity = new JsonObject();
		JsonArray supportedContainerEntityArray = new JsonArray();
		List<String> failureType = new ArrayList<String>();
		List<String> failureDetailsType = new ArrayList<String>();
		new ArrayList<String>();
		List<AttributeProperty> attrList = new ArrayList<AttributeProperty>();
		new ArrayList<JsonObject>();
		boolean addEnumtoFailueEntity = false;
		boolean addEnumtoFailueDetailsEntity = false;
		attrList = (ArrayList) entityMap1.get(rootEntityAttrName);
		logger.debug("root entity is "+rootEntityAttrName);
		for (int i = 0; i < attrList.size(); i++) {
			AttributeProperty ap = (AttributeProperty) attrList.get(i);
			if (rootEntityAttrName.equalsIgnoreCase("Info")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					 if(responseFilters.get("groupby").toString().equalsIgnoreCase("PROVIDER")){
							 infoEntity.addProperty(ap.getAttribute(), 
						 (!(ap.getJsonPath().contains("Extn"))) ? jsonDataHandler.getValue(ap.getJsonPath()) : commonExtn.getCommonExt(jsonDataHandler, ap.getJsonPath(),responseFilters.get("groupby"),containerStats));
					 }else if(responseFilters.get("groupby").toString().equalsIgnoreCase("COBRAND")){
						 infoEntity.addProperty(ap.getAttribute(), 
								 (!(ap.getJsonPath().contains("Extn"))) ? jsonDataHandler.getValue(ap.getJsonPath()) : commonExtn.getCommonExt(jsonDataHandler, ap.getJsonPath(),responseFilters.get("groupby"),containerStats));
			    	 }else
					infoEntity.addProperty(ap.getAttribute(), jsonDataHandler.getValue(ap.getJsonPath()));
				}else if(ap.isEntity() && containerStats && ap.getAttribute().equalsIgnoreCase("details")){
					logger.debug("debug is "+ap.getAttribute());
					SplunkEntity detailObject=null;
					try{
					 detailObject = new ObjectMapper().readValue(historicRes, SplunkEntity.class);
					}catch(Exception e){
						logger.debug("Exception while unparsing detailObject");
						throw new RuntimeException("Jackson Exception");
					}
					Result[] result=detailObject.getResults();
					Map<String,List<Result>> mymap= new LinkedHashMap<String,List<Result>>();
					
					for(Result rs:result){
						if(mymap.containsKey(rs.getTimestamp())){
							mymap.get(rs.getTimestamp()).add(rs);
						}
						else{
							List<Result> ar= new ArrayList<Result>();
							ar.add(rs);
							mymap.put(rs.getTimestamp(), ar);
						}
						
					}
			   		JsonArray summaryDateEntityArrayforContainers = new JsonArray();
			   		SplunkResponseTransformer splunkResponseFormatUtil= new SplunkResponseTransformer();
			   		splunkResponseFormatUtil.isYodlee=isYodlee;
			   		
			   		splunkResponseFormatUtil.includeLatencyBreakup=includeLatencyBreakup;
			   		for(String date :mymap.keySet()){
						List<Result> containerList= mymap.get(date);
						List<ContainerStat> containers= new ArrayList<ContainerStat>();
							for(Result oneResult: containerList){
								ContainerStat oneContainer =splunkResponseFormatUtil.buildContainer(oneResult,false);
								containers.add(oneContainer);
							}
							Details detail= new Details(date,splunkResponseFormatUtil.buildSummary(containers,false));
							
							 ObjectMapper mapper = new ObjectMapper();
							 mapper.setSerializationInclusion(Include.NON_NULL);
							 String s= new String();
							 JsonObject summaryDateEntityForContainers = new JsonObject();
							 try {
						           s= mapper.writeValueAsString(detail);
						           logger.debug("json of result is : "+s);
						           JsonElement jelementx = new JsonParser().parse(s);
						           summaryDateEntityForContainers=jelementx.getAsJsonObject();
						        } catch (Exception e) {
						        	logger.debug("Testing exception Jackson");
						        }
							 summaryDateEntityArrayforContainers.add(summaryDateEntityForContainers);
							 
					}
			   		
			   		infoEntity.add(ap.getAttribute(), summaryDateEntityArrayforContainers);
			   		}
				else if (ap.isEntity()) { // Begin of Info Entitity
					String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
					JsonArray ja = new JsonArray();
					
					if (entityType.length() == 0) {// Here processing failure and latnecy
						ja = getRootEntityAttributes(ap.getAttribute(),jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					} else {
						ja = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					}

					JsonObject jo = new JsonObject();
					if (entityType.equalsIgnoreCase("summary") && !jsonDataHandler.isNullHandler()) // To loop summay Array
					{
						for (int k = 0; k < ja.size(); k++) {
							jo = ja.get(k).getAsJsonObject();
						}
						infoEntity.add(ap.getAttribute(), jo);
					} else

					if (ap.getAttribute().equalsIgnoreCase("details") && !historicRefreshlatencyBreakDownStatsHandler.isNullHandler() ) { // To process detailed	 report data as each JsonObject
						 List<String> cobrandIds=new ArrayList<String>();
						 if(containerStats)
							 cobrandIds = historicRefreshlatencyBreakDownStatsHandler.getValues("$.input[*].results[*].COBRAND_ID");
						 else
							 cobrandIds = historicRefreshlatencyBreakDownStatsHandler.getValues("$.results[*].COBRAND_ID");
					     for (int x = 0; x < (cobrandIds.size() - 1); x++) { // Loop to run through number historic details entries
							historicDetailsCount++;
							JsonArray historyDetail = new JsonArray();
							historyDetail = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
							for (int k = 0; k < historyDetail.size(); k++) // Loop to convert array to object
							{
								jo = historyDetail.get(k).getAsJsonObject();
							}
							ja.add(jo);
						 }
						 infoEntity.add(ap.getAttribute(), ja);
						 isProcessLatencyDetails = false;
						 historicDetailsCount = 0;
					} else if (ja.get(0).getAsJsonObject() != null && ja.get(0).getAsJsonObject().entrySet() != null && ja.get(0).getAsJsonObject().entrySet().size() > 0)
						 infoEntity.add(ap.getAttribute(), ja);
				}
			}
			if("supportedContainerStats".equalsIgnoreCase(rootEntityAttrName) && containerStats){
				List myarray=jsonDataHandler.getValues("$.input[*]");
					for (int y = 0; y < myarray.size(); y++) {
						Map map=(LinkedHashMap)myarray.get(y);
						String jsonelStr =  String.valueOf(map.get("results"));
						// jsonelStr = "{\"input\":[{\"results\":" +jsonelStr+ "}]}";
						jsonelStr = "{\"results\":" +jsonelStr+ "}";
					JsonDataHandler mydataDataHandler= new JsonDataHandler();
					mydataDataHandler.parseJson(jsonelStr);
					String suminfo=mydataDataHandler.getValue("$.results[0].SUM_INFO_ID");
					if(myMapSuminfo.get(suminfo)==null){
						myMapSuminfo.put(suminfo, new ArrayList<String>());
					}
					myMapSuminfo.get(suminfo).add(jsonelStr);
					logger.debug("suminfo is "+suminfo);
				}
					for(String str:myMapSuminfo.keySet()){
						 JsonDataHandler mydataDataHandlerContainer = new JsonDataHandler();
				    		String fs="";
				    		List<String> myList= myMapSuminfo.get(str);
				    		for(String s: myList){
				    			fs+=s+",";
				    		}
				    		String jsonelStr1 = "{\"input\":[" + fs.substring(0, fs.length()-1) ;
				    		mydataDataHandlerContainer.parseJson(jsonelStr1+ "]}");
				    		supportedContainerEntityArray.add(buildSupportedContainerStats(mydataDataHandlerContainer));
				       }
					myMapSuminfo.clear();
			
				return supportedContainerEntityArray;
			}
			
			if("ContainerStats".equalsIgnoreCase(rootEntityAttrName) && containerStats){
					List myarray=historicRefreshlatencyBreakDownStatsHandler.getValues("$.input[*]");
					for (int y = 0; y < myarray.size(); y++) {
						Map map=(LinkedHashMap)myarray.get(y);
						String jsonelStr =  String.valueOf(map.get("results"));
						jsonelStr = "{\"results\":" +jsonelStr+ "}";
					JsonDataHandler mydataDataHandler= new JsonDataHandler();
					mydataDataHandler.parseJson(jsonelStr);
					String suminfo=mydataDataHandler.getValue("$.results[0].SUM_INFO_ID");
					if(myMapSuminfo.get(suminfo)==null){
						myMapSuminfo.put(suminfo, new ArrayList<String>());
					}
					myMapSuminfo.get(suminfo).add(jsonelStr);
					logger.debug("suminfo is "+suminfo);
					
				}
					for(String str:myMapSuminfo.keySet()){
						 JsonDataHandler mydataDataHandlerContainer = new JsonDataHandler();
				    		String fs="";
				    		List<String> myList= myMapSuminfo.get(str);
				    		for(String s: myList){
				    			fs+=s+",";
				    		}
				    		String jsonelStr1 = "{\"input\":[" + fs.substring(0, fs.length()-1) ;
				    		mydataDataHandlerContainer.parseJson(jsonelStr1+ "]}");
				    		supportedContainerEntityArray.add(buildSupportedContainerStats(mydataDataHandlerContainer));
				       }
					myMapSuminfo.clear();
				return supportedContainerEntityArray;
			}

			if (rootEntityAttrName.equalsIgnoreCase("summary")) {
				
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					summaryEntity.addProperty(
							ap.getAttribute(),
							(!(ap.getJsonPath().contains("Extn"))) ? jsonDataHandler.getValue(ap.getJsonPath()) : summaryExtn.getSummaryExt(
									jsonDataHandler, ap.getJsonPath() ,includeContainerflag));
				} else if (ap.isEntity()) {
					String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
					JsonArray ja = new JsonArray();
					if (entityType.length() == 0)// Here processing failure and latnecy
					{
						ja = getRootEntityAttributes(ap.getAttribute(),jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					} else {
						ja = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					}

					JsonObject jo = new JsonObject();
					if (entityType.equalsIgnoreCase("latency")) {
						for (int k = 0; k < ja.size(); k++) {
							jo = ja.get(k).getAsJsonObject();
						}
						summaryEntity.add(ap.getAttribute(), jo);
					} 
					else if(entityType.contains("failure")){
						summaryEntity.add(ap.getAttribute(), ja);
					}
					else if(entityType.contains("ContainerStats") && containerStats){
						summaryEntity.add(ap.getAttribute(), ja);
					}

				} else if (ap.isComponent()) {
					summaryEntity.add(ap.getAttribute(), getComponentAttributes(ap.getAttribute(),jsonDataHandler,includeContainerflag));
				}
			}

			if (rootEntityAttrName.equalsIgnoreCase("summarydate")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum() && !historicRefreshlatencyBreakDownStatsHandler.isNullHandler()) {
					summaryDateEntity.addProperty(ap.getAttribute(), commonExtn.getCommonExt(historicRefreshlatencyBreakDownStatsHandler, ap.getJsonPath(), null, historicDetailsCount,containerStats));
				} else if (ap.isEntity() && !historicRefreshlatencyBreakDownStatsHandler.isNullHandler()) {
					String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
					JsonArray ja = new JsonArray();
					if (entityType.length() == 0)// Here processing Details Summary array
					{
						ja = getRootEntityAttributes(ap.getAttribute(),jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					} else {
						ja = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					}
					JsonObject jo = new JsonObject();
					if (entityType.equalsIgnoreCase("summarydetails")) {
						for (int k = 0; k < ja.size(); k++) {
							jo = ja.get(k).getAsJsonObject();
						}
						summaryDateEntity.add(ap.getAttribute(), jo);
					} else {
						summaryDateEntity.add(ap.getAttribute(), ja);
					}
					

				}
				
			}

			if (rootEntityAttrName.equalsIgnoreCase("summarydetails")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					 summaryDetailsEntity.addProperty(ap.getAttribute(),commonExtn.getCommonExt(
							 historicRefreshlatencyBreakDownStatsHandler,ap.getJsonPath(),null,historicDetailsCount,containerStats));
				} else if (ap.isEntity()) {
					String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
					if (entityType.equalsIgnoreCase("latency")) {
						isProcessLatencyDetails = true;
					}
					JsonArray ja = new JsonArray();
					if (entityType.length() == 0)// Here processing failure and latnecy
					{
						ja = getRootEntityAttributes(ap.getAttribute(),jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					} else {
						ja = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,includeContainerflag);
					}
					JsonObject jo = new JsonObject();
					if (entityType.equalsIgnoreCase("latency")) {
						for (int k = 0; k < ja.size(); k++) {
							jo = ja.get(k).getAsJsonObject();
						}
						summaryDetailsEntity.add(ap.getAttribute(), jo);
					} else if(entityType.contains("failure")) {
						summaryDetailsEntity.add(ap.getAttribute(), ja);
					}
					else if(entityType.equalsIgnoreCase("ContainerStats") && containerStats) {
						summaryDetailsEntity.add(ap.getAttribute(), ja);

					}

				} else if (ap.isEnum()) {
					getEnumAttributes(ap.getType().replaceAll("<ENUM>", ""));
				} else if (ap.isComponent()) {
					summaryDetailsEntity.add(ap.getAttribute(), getComponentAttributes(ap.getType().replaceAll("<COMPONENT>", ""),jsonDataHandler,includeContainerflag));
				}
			}
			if (rootEntityAttrName.equalsIgnoreCase("success")) {
				successEntity.addProperty(ap.getAttribute(), ap.getJsonPath());
			}
			if (rootEntityAttrName.equalsIgnoreCase("failure")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {

					failureEntity.addProperty(ap.getAttribute(), 12345);
				} else if (ap.isEntity()) {
					failureEntity.add(ap.getAttribute(), new JsonArray());
				} else if (ap.isEnum()) {
					failureType = getEnumAttributes(ap.getType().replaceAll("<ENUM>", ""));
					addEnumtoFailueEntity = true;
				}
			}
			if (rootEntityAttrName.equalsIgnoreCase("failuredetails")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					failuredetailsEntity.addProperty(ap.getAttribute(), ap.getJsonPath());
				} else if (ap.isEntity()) {
					failuredetailsEntity.add(ap.getAttribute(), new JsonArray());
				} else if (ap.isEnum()) {
					failureDetailsType = getEnumAttributes(ap.getType().replaceAll("<ENUM>", ""));
					addEnumtoFailueDetailsEntity = true;
				}
			}

			if (rootEntityAttrName.equalsIgnoreCase("latency")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					latencyEntity.addProperty(ap.getAttribute(),
							(!(isProcessLatencyDetails)?	
									latencyExtn.getLatencyExtn(jsonDataHandler, ap.getJsonPath(), ap.getType(),includeContainerflag) //To process MIN,MAX,AVG Latency for Summary
								   :latencyExtn.getLatencyExtn(historicRefreshlatencyBreakDownStatsHandler, ap.getJsonPath(),
												((responseFilters.get("duration")!=null)&&(responseFilters.get("duration").contains("h"))?"h":ap.getType()), historicDetailsCount,containerStats))); //To process MIN,MAX,AVG Latency for Details
				} else if (ap.isComponent()&&responseFilters.get("duration")!=null&&(!responseFilters.get("duration").contains("h")) && ap.getAttribute().equalsIgnoreCase("breakups") && !includeContainerflag) {
					latencyEntity.add(ap.getAttribute(), getComponentAttributesArray(ap.getAttribute(), historicDetailsCount));
				}
			}

		}

		if (rootEntityAttrName.equalsIgnoreCase("Info")){
			rootEntityAttributes.add(infoEntity);
		}
		

		if (rootEntityAttrName.equalsIgnoreCase("summary")) {
			infoEntityArray.add(summaryEntity);
			return infoEntityArray;
		}

		if (rootEntityAttrName.equalsIgnoreCase("latency")) {
			detailsEntityArray.add(latencyEntity);
			return detailsEntityArray;
		}
		

		if (rootEntityAttrName.equalsIgnoreCase("failuredetails")) {
			if (addEnumtoFailueDetailsEntity) { // Add enum property to Failure Entity
				for (String failureDetailsTypeValue : failureDetailsType) {
					JsonObject failuredetailsEntityEnumAdded = new JsonObject();
					failuredetailsEntityEnumAdded.addProperty("type", failureDetailsTypeValue);

					Iterator it = failuredetailsEntity.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry me = (Map.Entry) it.next();
						String value = me.getValue().toString().replaceAll("\"", "");
						failuredetailsEntityEnumAdded.addProperty(
								me.getKey().toString(),
								(!(value.contains("Extn"))) ? historicRefreshlatencyBreakDownStatsHandler.getValue(value) : fdExtn.getFailureDetailsExt(historicRefreshlatencyBreakDownStatsHandler,
										value, failureDetailsTypeValue, historicDetailsCount,containerStats));
					}
					detailsEntityArray.add(failuredetailsEntityEnumAdded);
					failuredetailsEntityEnumAdded = null;
				}
			} else {
				detailsEntityArray.add(failuredetailsEntity);
			}
			return detailsEntityArray;
		}

		if (rootEntityAttrName.equalsIgnoreCase("summarydetails")) {
			summaryDetailsEntityArray.add(summaryDetailsEntity);
			return summaryDetailsEntityArray;
		}
		if (rootEntityAttrName.equalsIgnoreCase("success")) { // Success is not	added to summery entity as it is a component
			summaryEntityArray.add(successEntity);
			return summaryEntityArray;
		}
		if (rootEntityAttrName.equalsIgnoreCase("summarydate")) {
			summaryDateEntityArray.add(summaryDateEntity);
			return summaryDateEntityArray;
		}

		if (rootEntityAttrName.equalsIgnoreCase("failure")) {

			if (addEnumtoFailueEntity) { // Add enum property to Failure Entity
				for (String failureTypeValue : failureType) {
					JsonObject failureEntityEnumAdded = new JsonObject();
					failureEntityEnumAdded.addProperty("type", failureTypeValue);
					failureEntityEnumAdded.addProperty("volume", failureExtn.getFailureVolumeExt(jsonDataHandler, failureTypeValue, failureTypeValue,includeContainerflag));
					failureEntityEnumAdded.addProperty("rate", failureExtn.getFailureRateExt(jsonDataHandler, failureTypeValue, failureTypeValue,includeContainerflag));
					// for(Object entry : failureEntity.entrySet()) {
					// failureEntityEnumAdded.addProperty(((Map.Entry)entry).getKey().toString(),((Map.Entry)entry).getValue().toString());
					// }
					if(!containerStats)
						summaryEntityArray.add(failureEntityEnumAdded);
					else{
						supportedContainerEntityArray.add(failureEntityEnumAdded);
					}
					failureEntityEnumAdded = null;
				}
				if(containerStats){
					return supportedContainerEntityArray;
				}
			} else {
				summaryEntityArray.add(failureEntity);
			}
			return summaryEntityArray;
		}
		return rootEntityAttributes;
	
	}

	private JsonArray getComponentAttributesArray(String ComponentName, int historicDetailsCount) {
		boolean addEnumtobreakupsComponent = false;
		JsonArray breakupComponentArray = new JsonArray();
		JsonArray componentArray = new JsonArray();
		JsonObject breakupsComponent = new JsonObject();
		List attrList = new ArrayList();
		List<String> latencyRange = new ArrayList<String>();

		attrList = (ArrayList) componentsMap1.get(ComponentName);
		for (int i = 0; i < attrList.size(); i++) {
			AttributeProperty ap = (AttributeProperty) attrList.get(i);
			ap.getAttribute();
			if (ComponentName.equalsIgnoreCase("breakups")) {
				if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
					breakupsComponent.addProperty(ap.getAttribute(), ap.getJsonPath());
				} else if (ap.isEnum()) {
					latencyRange = getEnumAttributes(ap.getType().replaceAll("<ENUM>", ""));
					addEnumtobreakupsComponent = true;
				}
			}

		}

		if (ComponentName.equalsIgnoreCase("breakups")) {
			if (addEnumtobreakupsComponent) { // Add enum property to Failure Entity
				for (String latencyRangeValue : latencyRange) {
					JsonObject latencyRangeEnumAdded = new JsonObject();
					latencyRangeEnumAdded.addProperty("range", latencyRangeValue);
					for (Object entry : breakupsComponent.entrySet()) {
						String s1 = ((Map.Entry) entry).getKey().toString();
						String s2 = ((Map.Entry) entry).getValue().toString().replace("\"", "");
						latencyRangeEnumAdded.addProperty(s1,
								(!(isProcessLatencyDetails)) ? "0.0" : (!(s2.contains("Extn")) ? "0.0" : latencyExtn.getLatencyExtn(historicRefreshlatencyBreakDownStatsHandler, s2, latencyRangeValue,
										historicDetailsCount,containerStats)));
					}
					breakupComponentArray.add(latencyRangeEnumAdded);
					latencyRangeEnumAdded = null;
				}
			} else {
				breakupComponentArray.add(breakupsComponent);
			}
			return breakupComponentArray;
		}
		return componentArray;
	}

	private JsonObject getComponentAttributes(String rootComponentName,JsonDataHandler dataHandler,boolean flag) {
		JsonObject component = new JsonObject();
		JsonObject successDetailsComponent = new JsonObject();
		JsonObject successComponent = new JsonObject();
		List attrList = new ArrayList();

		attrList = (ArrayList) componentsMap1.get(rootComponentName);
		for (int i = 0; i < attrList.size(); i++) {
			AttributeProperty ap = (AttributeProperty) attrList.get(i);
			ap.getAttribute();
			if (rootComponentName.equalsIgnoreCase("success")) {
				successComponent.addProperty(
						ap.getAttribute(),
						(!(ap.getJsonPath().contains("Extn"))) ? dataHandler.getValue(ap.getJsonPath()) : successExtn.getSuccessExt(dataHandler,
								ap.getJsonPath(),flag));
			}
			if (rootComponentName.equalsIgnoreCase("successdetails")) {
				successDetailsComponent.addProperty(ap.getAttribute(), commonExtn.getCommonExt(historicRefreshlatencyBreakDownStatsHandler, ap.getJsonPath(), null, historicDetailsCount,containerStats));

			}
		}

		if (rootComponentName.equalsIgnoreCase("success")) {
			return successComponent;
		}

		if (rootComponentName.equalsIgnoreCase("successdetails")) {
			return successDetailsComponent;
		}

		return component;

	}

	public  List<String> getEnumAttributes(String rootEnumName) {
		return enumMap1.get(rootEnumName);
	}

	private  void finalJSONResponseStructure(Map<String, String> filterinputs) { 
		cloneMaps(entityMap1,componentsMap1,enumMap1,excludedEntityFromResponse1);
		Iterator it = filterinputs.entrySet().iterator();
		boolean isOnlyLatencyBreakup=false;
		if(Boolean.valueOf(filterinputs.get(YSLConstants.INCLUDE_LATENCY_BREAKUP))
				&&!Boolean.valueOf(filterinputs.get(YSLConstants.INCLUDE_HISTORIC))
				&&(filterinputs.get(YSLConstants.USER_TYPE).toString().equalsIgnoreCase("tier2")))
		{
			isOnlyLatencyBreakup=true;
		}
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String str[];
			String s = "";
			if (YSLConstants.USER_TYPE.equalsIgnoreCase(entry.getKey().toString())) {
				s = excludedEntityFromResponse1.get(entry.getValue());
			}else if (entry.getKey().equals(YSLConstants.INCLUDE_HISTORIC) && !Boolean.valueOf(entry.getValue().toString())&&!Boolean.valueOf(filterinputs.get(YSLConstants.INCLUDE_LATENCY_BREAKUP))) {
				s = excludedEntityFromResponse1.get(entry.getKey());
			}else if (entry.getKey().equals(YSLConstants.INCLUDE_LATENCY_BREAKUP) &&(filterinputs.get(YSLConstants.USER_TYPE).toString().equalsIgnoreCase("tier2")) &&!Boolean.valueOf(entry.getValue().toString())){
			 	s = excludedEntityFromResponse1.get(entry.getKey());
			}else if (entry.getKey().equals(YSLConstants.INCLUDE_LATENCY_BREAKUP) &&(filterinputs.get(YSLConstants.USER_TYPE).toString().equalsIgnoreCase("tier1"))){
				s = excludedEntityFromResponse1.get(entry.getKey());
			}
			if(s.equalsIgnoreCase("") && isOnlyLatencyBreakup){
				 s=	excludedEntityFromResponse1.get("onlyDetailsLatencyBreakup");
				 isOnlyLatencyBreakup=false;
			}
			if (s != null && s.length() > 0) {
				str = s.split(",");
				for (String s1 : str) {
					allExcludedFromResponse.add(s1);
					entityMap1.remove(s1);
					componentsMap1.remove(s1);
					enumMap1.remove(s1);

					Iterator it1 = entityMap1.entrySet().iterator();
					while (it1.hasNext()) {
						Map.Entry me = (Map.Entry) it1.next();
						List<AttributeProperty> l = new ArrayList<AttributeProperty>((ArrayList<AttributeProperty>) me.getValue());
						//TODO change to iterators
						for (int i = 0; i < l.size(); i++) {
                            String entityType = l.get(i).getType().replaceAll("<ENTITY>", "").replaceAll("<COMPONENT>", "").replaceAll("<ENUM>","").replace("[]", "");
                            if(entityType.equalsIgnoreCase(s1)){
                                   l.remove(i);
                            }
                     }

						entityMap1.put(me.getKey().toString(), l);
					}
					Iterator it2 = componentsMap1.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry me = (Map.Entry) it2.next();
						List<AttributeProperty> l = new ArrayList<AttributeProperty>((ArrayList<AttributeProperty>) me.getValue());
						//TODO change to iterators
						for (int i = 0; i < l.size(); i++) {
                            String entityType = l.get(i).getType().replaceAll("<ENTITY>", "").replaceAll("<COMPONENT>", "").replaceAll("<ENUM>","").replace("[]", "");
                            if(entityType.equalsIgnoreCase(s1)){
                                   l.remove(i);
                            }
                     }

						componentsMap1.put(me.getKey().toString(), l);
					}

					Iterator it3 = enumMap1.entrySet().iterator();
					while (it3.hasNext()) {
						Map.Entry me = (Map.Entry) it3.next();
						List<String> l = new ArrayList<String>((ArrayList<String>) me.getValue());
						//TODO change to iterators
						for (int i = 0; i < l.size(); i++) {
							if (l.get(i).equalsIgnoreCase(s1)) {
								l.remove(i);
							}
						}
						enumMap1.put(me.getKey().toString(), l);
					}
				}
			}			
		}		
		
	}

	private void cloneMaps(Map<String, List<AttributeProperty>> entityMap1, Map<String, List<AttributeProperty>> componentsMap1, Map<String, List<String>> enumMap1,
			Map<String, String> excludedEntityFromResponse1) {
		try {
			Set<Entry<String, List<AttributeProperty>>> entrySet = entityMap.entrySet();
			for (Entry<String, List<AttributeProperty>> entry : entrySet) {
				String key = entry.getKey();
				List<AttributeProperty> value = entry.getValue();
				List<AttributeProperty> list = new ArrayList<AttributeProperty>();
				for (AttributeProperty attributeProperty : value) {
					list.add(attributeProperty.copy());
				}
				entityMap1.put(key, list);
			}
			Set<Entry<String, List<AttributeProperty>>> entrySet2 = componentsMap.entrySet();
			for (Entry<String, List<AttributeProperty>> entry : entrySet2) {
				String key = entry.getKey();
				List<AttributeProperty> value = entry.getValue();
				List<AttributeProperty> list = new ArrayList<AttributeProperty>();
				for (AttributeProperty attributeProperty : value) {
					list.add(attributeProperty.copy());
				}
				componentsMap1.put(key, list);
			}
			Set<Entry<String, List<String>>> entrySet3 = enumMap.entrySet();
			for (Entry<String, List<String>> entry : entrySet3) {
				String key = entry.getKey();
				List<String> value = entry.getValue();
				List<String> list = new ArrayList<String>();
				for (String string : value) {
					list.add(string);
				}
				enumMap1.put(key, list);
			}
			excludedEntityFromResponse1.putAll(excludedEntityFromResponse);
		} catch (Exception e) {
			logger.error("Exception while cloning :" + ExceptionUtils.getFullStackTrace(e));
		}

	}
	private JsonObject buildSupportedContainerStats(JsonDataHandler jsonDataHandler) {
		List attrList = new ArrayList();
		JsonObject supportedContainerEntity = new JsonObject();
		attrList = (ArrayList) entityMap1.get("supportedContainerStats");

		for (int i = 0; i < attrList.size(); i++) {
			AttributeProperty ap = (AttributeProperty) attrList.get(i);
			if (!ap.isEntity() && !ap.isComponent() && !ap.isEnum()) {
			supportedContainerEntity.addProperty(
				ap.getAttribute(),
				(!(ap.getJsonPath().contains("Extn"))) ? jsonDataHandler.getValue(ap.getJsonPath()) : supportedContainerStatsExtn.getSupportedContainerExt(
						jsonDataHandler,responseFilters.get("groupby"), ap.getJsonPath()));
			} else if (ap.isEntity()) {
				
				String entityType = ap.getType().replaceAll("<ENTITY>", "").replace("[]", "");
				JsonArray ja = new JsonArray();
				if (entityType.length() == 0)// Here processing failure and latnecy
				{
					ja = getRootEntityAttributes(ap.getAttribute(),jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats);
				} else {
					ja = getRootEntityAttributes(entityType,jsonDataHandler,historicRefreshlatencyBreakDownStatsHandler,containerStats);
				}

				JsonObject jo = new JsonObject();
				if (entityType.equalsIgnoreCase("latency")) {
					for (int k = 0; k < ja.size(); k++) {
						jo = ja.get(k).getAsJsonObject();
					}
					supportedContainerEntity.add(ap.getAttribute(), jo);
				} else {
					supportedContainerEntity.add(ap.getAttribute(), ja);
				}

			} else if (ap.isComponent()) {
				supportedContainerEntity.add(ap.getAttribute(), getComponentAttributes(ap.getAttribute(),jsonDataHandler,containerStats));
				
			}
			
		}
		return supportedContainerEntity;
	}
}
