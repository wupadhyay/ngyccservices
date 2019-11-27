/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.extentions.CommonExtn;
import com.yodlee.ycc.dapi.extentions.LatencyBreakupExtn;
import com.yodlee.ycc.dapi.extentions.SuccessExtn;
import com.yodlee.ycc.dapi.extentions.SummaryExtn;
import com.yodlee.ycc.dapi.extentions.SupportedContainerStatsExtn;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.ContainerStat;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.Details;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.ErrorCodeBreakUp;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.FailureEntity;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.Info;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.Latency;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.LatencyBreakup;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.PartialSuccessEntity;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.RefreshStats;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.RootObject;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.SuccessEntity;
import com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity.Summary;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.Result;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.SiteErrorCodeContribution;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.SiteErrorCodeContributionResult;
import com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity.SplunkEntity;
import com.yodlee.ycc.dapi.utils.MiscUtil;


public class SplunkResponseTransformer {
	
	
	private static final String USERACTIONREQUIRED = "USERACTIONREQUIRED";
	private static final String SITE = "SITE";
	private static final String TECHNICAL = "TECHNICAL";
	private static final String LATENCY_ZERO = "0.0";
	public static DecimalFormat dformat = new DecimalFormat("0.0#");
	 Map<String, List<AttributeProperty>> entityMap1 = new LinkedHashMap<String, List<AttributeProperty>>();
	 Map<String, List<AttributeProperty>> componentsMap1 = new LinkedHashMap<String, List<AttributeProperty>>();
	 Map<String, List<String>> enumMap1 = new LinkedHashMap<String, List<String>>();
	 Map<String, String> excludedEntityFromResponse1 = new HashMap<String, String>();
	 List<String> allExcludedFromResponse = new ArrayList<String>();
	CommonExtn commonExtn;
	SuccessExtn successExtn;
	SummaryExtn summaryExtn;
	
	private static final Logger logger = LoggerFactory.getLogger(SplunkResponseTransformer.class);
	boolean containerStats=false;
	boolean historic=false;
	boolean isContainerStats=false;
	boolean isYodlee=false;
	boolean includeLatencyBreakup=false;
	boolean isErrorCodeBreakUpNeeded = false;
	private String reportType=null;
	ContainerStat buildContainer(Result result,boolean summaryContainers) {
		
		logger.debug(" inside build container. result "+result + " summary containers "+summaryContainers);
		ContainerStat oneContainer = null;
		try {
		List<LatencyBreakup> latencyBreakups=null;
		if(includeLatencyBreakup && isYodlee && !summaryContainers){
			latencyBreakups=new ArrayList<LatencyBreakup>();
			latencyBreakups.add(new LatencyBreakup(YSLConstants._0_TO_20_SECS,result.getLatency_0_20(),"defaultLatencyPercentage"));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._20_TO_40_SECS,result.getLatency_20_40(),"defaultLatencyPercentage"));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._40_TO_60_SECS,result.getLatency_40_60(),"defaultLatencyPercentage"));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._60_TO_80_SECS,result.getLatency_60_80(),"defaultLatencyPercentage"));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._80_TO_100_SECS,result.getLatency_80_100(),"defaultLatencyPercentage"));
			latencyBreakups.add(new LatencyBreakup(YSLConstants.GRT_THAN_100_SECS,result.getLatency_above_100(),"defaultLatencyPercentage"));
			
		}
		
		String min_LATENCY = result.getMin_latency() != null ? result.getMin_latency() : LATENCY_ZERO ;
		String max_LATENCY = result.getMax_latency() != null ? result.getMax_latency() : LATENCY_ZERO  ;
		Latency latency= new Latency(min_LATENCY,max_LATENCY,result.getAvg_latency(),latencyBreakups);
		Long siteId =Long.valueOf(result.getSite_id() );
			Long sumInfoId = null;
			if (!StringUtils.isEmpty(result.getSum_info_id()))
				sumInfoId = Long.valueOf(result.getSum_info_id());
		String siteName=new SupportedContainerStatsExtn().getSiteName(siteId, sumInfoId);
		//String siteName="site"+System.currentTimeMillis();
		SuccessEntity  successEntity=new SuccessEntity(result.getSuccessful_refreshes(),"defaultSuccessPer");
			PartialSuccessEntity partialSuccessEntity = null;
			if (reportType.equalsIgnoreCase("ADD"))
				partialSuccessEntity = new PartialSuccessEntity(result.getPartial_successful_refreshes(), "defaultSuccessPer");
		
		List<FailureEntity> failureArray= new ArrayList<FailureEntity>();
		//error code break up not required
		failureArray.add(new FailureEntity(TECHNICAL,result.getTechnical_errors(),"defaultFailurePercentage",null)); 
		failureArray.add(new FailureEntity(SITE,result.getSite_dependent_errors(),"defaultFailurePercentage",null));
		failureArray.add(new FailureEntity(USERACTIONREQUIRED,result.getUser_dependent_errors(),"defaultFailurePercentage",null));
		String tag = new SupportedContainerStatsExtn().getContainerName(result.getTag_id());
		oneContainer= new ContainerStat(result.getSum_info_id(),siteName,tag,result.getTotal_refreshes(),successEntity,failureArray,isYodlee?latency:null,partialSuccessEntity);
		} catch (Exception e) {
			logger.info("exception while building container for result "+result + " summary container bool val"+summaryContainers);
			logger.error("Exception stack trace:" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
				
		return oneContainer;

	}

	 Summary buildSummary(List<ContainerStat> containers, boolean includeLatencyBreakUpsAtContainer) {

		long totalRefreshes=0;
		long successRefreshes=0;
		long partialSuccessRefreshes=0;
		long techErrors=0;
		long siteErrors=0;
		long uarErrors=0;
		long latency_0_20_vol=0;
		long latency_20_40_vol=0;
		long latency_40_60_vol=0;
		long latency_60_80_vol=0;
		long latency_80_100_vol=0;
		long latency_100_vol=0;
		Double min_latency=0.0;
		Double max_latency=0.0;
		Double avg_latency=0.0;
		Double total_latency=0.0;
		String latency_0_20Percentage="";
		String latency_20_40Percentage="";
		String latency_40_60Percentage="";
		String latency_60_80Percentage="";
		String latency_80_100Percentage="";
		String latency_100_Percentage="";
		for(ContainerStat oneContainer:containers){
			logger.debug(" each container "+ oneContainer);
			 totalRefreshes=totalRefreshes+Long.valueOf(oneContainer.totalVolume);
			 successRefreshes=successRefreshes+Long.valueOf(oneContainer.success.volume);
			 if (reportType.equalsIgnoreCase("ADD"))
				 partialSuccessRefreshes = partialSuccessRefreshes+Long.valueOf(oneContainer.partialSuccess.volume);
			 techErrors=techErrors+Long.valueOf(oneContainer.failure.get(0).volume);
			 siteErrors=siteErrors+Long.valueOf(oneContainer.failure.get(1).volume);
			 uarErrors=uarErrors+Long.valueOf(oneContainer.failure.get(2).volume);
				if(isYodlee && includeLatencyBreakup && !includeLatencyBreakUpsAtContainer) {
					 latency_0_20_vol=latency_0_20_vol+Long.valueOf(oneContainer.latency.breakups.get(0).volume);
					 latency_20_40_vol=latency_20_40_vol+Long.valueOf(oneContainer.latency.breakups.get(1).volume);
					 latency_40_60_vol=latency_40_60_vol+Long.valueOf(oneContainer.latency.breakups.get(2).volume);
					 latency_60_80_vol=latency_60_80_vol+Long.valueOf(oneContainer.latency.breakups.get(3).volume);
					 latency_80_100_vol=latency_80_100_vol+Long.valueOf(oneContainer.latency.breakups.get(4).volume);
					 latency_100_vol=latency_100_vol+Long.valueOf(oneContainer.latency.breakups.get(5).volume);
				}
			 	
			 
			if(isYodlee && !includeLatencyBreakUpsAtContainer){
				 if(min_latency.compareTo(0.0)==0)
					 min_latency=Double.parseDouble(oneContainer.latency.min);
				 if(max_latency.compareTo(0.0)==0)
					 max_latency=Double.parseDouble(oneContainer.latency.max);
				 if(Double.parseDouble(oneContainer.latency.max) > max_latency)
					 max_latency=Double.parseDouble(oneContainer.latency.max);
				 if(Double.parseDouble(oneContainer.latency.min)<min_latency)
					 min_latency=Double.parseDouble(oneContainer.latency.min);
				
			}
			if(isYodlee){
				 total_latency=total_latency+(Double.parseDouble(oneContainer.latency.avg)*Double.parseDouble(oneContainer.totalVolume));
			}
		}
		String successPercentage=SplunkResponseTransformer.dformat.format(Double.valueOf(successRefreshes)/totalRefreshes*100);
		String partialSuccessPercentage = null;
		if (reportType.equalsIgnoreCase("ADD"))
			partialSuccessPercentage = SplunkResponseTransformer.dformat.format(Double.valueOf(partialSuccessRefreshes) / totalRefreshes * 100);
 		String techErrorsPercentage=SplunkResponseTransformer.dformat.format(Double.valueOf(techErrors)/totalRefreshes*100);
		String siteErrorsPercentage=SplunkResponseTransformer.dformat.format(Double.valueOf(siteErrors)/totalRefreshes*100);
		String uarErrorsPercentage=SplunkResponseTransformer.dformat.format(Double.valueOf(uarErrors)/totalRefreshes*100);
		
		List<LatencyBreakup> latencyBreakups=null;
		if(isYodlee && includeLatencyBreakup){
			 latency_0_20Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_0_20_vol)/totalRefreshes*100);
			 latency_20_40Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_20_40_vol)/totalRefreshes*100);
			 latency_40_60Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_40_60_vol)/totalRefreshes*100);
			 latency_60_80Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_60_80_vol)/totalRefreshes*100);
			 latency_80_100Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_80_100_vol)/totalRefreshes*100);
			 latency_100_Percentage=SplunkResponseTransformer.dformat.format(Double.valueOf(latency_100_vol)/totalRefreshes*100);
		
			latencyBreakups=new ArrayList<LatencyBreakup>();
			latencyBreakups.add(new LatencyBreakup(YSLConstants._0_TO_20_SECS,String.valueOf(latency_0_20_vol),latency_0_20Percentage));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._20_TO_40_SECS,String.valueOf(latency_20_40_vol),latency_20_40Percentage));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._40_TO_60_SECS,String.valueOf(latency_40_60_vol),latency_40_60Percentage));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._60_TO_80_SECS,String.valueOf(latency_60_80_vol),latency_60_80Percentage));
			latencyBreakups.add(new LatencyBreakup(YSLConstants._80_TO_100_SECS,String.valueOf(latency_80_100_vol),latency_80_100Percentage));
			latencyBreakups.add(new LatencyBreakup(YSLConstants.GRT_THAN_100_SECS,String.valueOf(latency_100_vol),latency_100_Percentage));
			
		}
		
		Latency latency=null;
		if(isYodlee)
			{
			avg_latency=total_latency/Double.valueOf(totalRefreshes);
			latency= new Latency(SplunkResponseTransformer.dformat.format(min_latency),SplunkResponseTransformer.dformat.format(max_latency),SplunkResponseTransformer.dformat.format(avg_latency),latencyBreakups);
			}
		
		List<FailureEntity> failureArray= new ArrayList<FailureEntity>();
		failureArray.add(new FailureEntity(TECHNICAL,String.valueOf(techErrors),techErrorsPercentage,null));
		failureArray.add(new FailureEntity(SITE,String.valueOf(siteErrors),siteErrorsPercentage,null));
		failureArray.add(new FailureEntity(USERACTIONREQUIRED,String.valueOf(uarErrors),uarErrorsPercentage, null));
		SuccessEntity successEntity=new SuccessEntity(String.valueOf(successRefreshes),successPercentage);
		
		PartialSuccessEntity partialSuccessEntity = null;
		if (reportType.equalsIgnoreCase("ADD"))
			partialSuccessEntity = new PartialSuccessEntity(String.valueOf(partialSuccessRefreshes), partialSuccessPercentage);
		
		if (!this.containerStats)
			containers = null;
		Summary summary= new Summary(String.valueOf(totalRefreshes),successEntity,failureArray,isYodlee?latency:null,containers,partialSuccessEntity);
		return summary;
	
	}
	
	public String formatSplunkResponse(Map<String, String> filterinputs,
			String detailedSiteRefreshLatencyStats,
			String historicRefreshlatencyBreakDownStats,String errorCodeRes, String errorCodeOverallRes) {
		logger.debug("detailedSiteRefreshLatencyStats "+detailedSiteRefreshLatencyStats);
		logger.debug("historicRefreshlatencyBreakDownStats "+historicRefreshlatencyBreakDownStats);
		logger.info("formatting started. filter inputs "+filterinputs);
		String finalResponse = null;
		reportType = filterinputs.get("refreshType");
		if (filterinputs.get("container") != null
				&& filterinputs.get("container").equalsIgnoreCase("true")) {
			containerStats = true;
		}
		if (filterinputs.get("historic") != null
				&& filterinputs.get("historic").equalsIgnoreCase("true")) {
			historic = true;
//			if(historicRefreshlatencyBreakDownStats==null){
//				historicRefreshlatencyBreakDownStats=detailedSiteRefreshLatencyStats;
//			}
		}
		if (filterinputs.get(YSLConstants.USER_TYPE).equalsIgnoreCase("tier2")) {
			isYodlee = true;
		}
		if (filterinputs.get(YSLConstants.INCLUDE_LATENCY_BREAKUP)!= null && filterinputs.get(YSLConstants.INCLUDE_LATENCY_BREAKUP).equalsIgnoreCase("true")) {
			includeLatencyBreakup = true;
		}
		if (filterinputs.get(YSLConstants.INCLUDE_ERROR_CODE) != null && filterinputs.get(YSLConstants.INCLUDE_ERROR_CODE).equalsIgnoreCase("true")) {
			isErrorCodeBreakUpNeeded = true;
		}
		SplunkEntity detailObject=null;
		SplunkEntity summaryObject=null;
		SiteErrorCodeContribution errorCodeBreakUpEntity = null;
		SiteErrorCodeContribution cobrandErrorCodeBreakUpEntity = null;
		if(detailedSiteRefreshLatencyStats !=null){
			try {
				summaryObject = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).readValue(
						detailedSiteRefreshLatencyStats, SplunkEntity.class);
			} catch (Exception e) {
				logger.error("Exception while unparsing summary " + e.getMessage()
						+ " historic " + historic + " error code break up"
						+ isErrorCodeBreakUpNeeded);
				logger.error("stack trace" + ExceptionUtils.getFullStackTrace(e));
				logger.error("summary res"+ detailedSiteRefreshLatencyStats);
				throw new RuntimeException("Jackson Exception");
			}
		}
		if (historic && historicRefreshlatencyBreakDownStats !=null) {
			try {
				detailObject =new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).readValue(
						historicRefreshlatencyBreakDownStats, SplunkEntity.class);
			} catch (Exception e) {
				logger.error("Exception while unparsing detail object "
						+ e.getMessage() + " historic " + historic
						+ " error code break up" + isErrorCodeBreakUpNeeded);
				logger.error("stack trace"
						+ ExceptionUtils.getFullStackTrace(e));
				logger.error("historic  res"+historicRefreshlatencyBreakDownStats);
				throw new RuntimeException("Jackson Exception");
			}
		}
		if (isErrorCodeBreakUpNeeded && errorCodeRes !=null) {
			try {
				errorCodeBreakUpEntity = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).readValue(
						errorCodeRes, SiteErrorCodeContribution.class);

			} catch (Exception e) {
				logger.error("Exception while unparsing error code  "
						+ e.getMessage() + " historic " + historic
						+ " error code break up" + isErrorCodeBreakUpNeeded);
				logger.error("stack trace"
						+ ExceptionUtils.getFullStackTrace(e));
				logger.error("error code res"+errorCodeRes);
				throw new RuntimeException("Jackson Exception");
			}
		}
		if (errorCodeOverallRes != null) {
			try {
				cobrandErrorCodeBreakUpEntity = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).readValue(errorCodeOverallRes, SiteErrorCodeContribution.class);

			} catch (Exception e) {
				logger.error("Exception while unparsing cobrand error code  " + e.getMessage());
				logger.error("stack trace" + ExceptionUtils.getFullStackTrace(e));
				logger.error("error code res" + errorCodeOverallRes);
				throw new RuntimeException("Jackson Exception");
			}
		}
		Result[] detailedResult=null;
		Result[] summaryResult = null;
		if (summaryObject != null)
			summaryResult = summaryObject.getResults();
		SiteErrorCodeContributionResult[] errorCodeResult=null;
		if(historic && detailObject !=null){
			 detailedResult = detailObject.getResults();
		}
		if(isErrorCodeBreakUpNeeded && errorCodeBreakUpEntity !=null) {
			errorCodeResult = errorCodeBreakUpEntity.getResults();
		}
		SiteErrorCodeContributionResult[] cobrandErrorCodeResult=null;
		if(cobrandErrorCodeBreakUpEntity!=null) {
			cobrandErrorCodeResult=cobrandErrorCodeBreakUpEntity.getResults();
		}
		RefreshStats refreshStats=buildRefreshStats(filterinputs,summaryResult,detailedResult,errorCodeResult,cobrandErrorCodeResult);
		RootObject rootObject= new RootObject(refreshStats);
		ObjectMapper mapper = new ObjectMapper();
		 mapper.setSerializationInclusion(Include.NON_NULL);
		
		 try {
			 finalResponse= mapper.writeValueAsString(rootObject);
			 logger.debug("json of result is : "+rootObject);
	        } catch (Exception e) {
	        	logger.error(" refresh stats "+ refreshStats);
	        	logger.error("Testing exception Jackson for summary  "+summaryResult + "detailed result "+detailedResult);
	        	logger.error("Exception while generating refresh stats json " +ExceptionUtils.getFullStackTrace(e));
	        }
		 logger.info("converted ");
		 logger.debug("final response json from splunk response transformer "+finalResponse);
		return finalResponse;

	}

	private RefreshStats buildRefreshStats(Map<String, String> filterinputs,
			Result[] summaryResult, Result[] detailedResult,SiteErrorCodeContributionResult[]  errorCodeResults, SiteErrorCodeContributionResult[] cobrandErrorCodeResult) {
		RefreshStats refreshStats=null;
		List<Info> infoList=new ArrayList<Info>();
		List<Details> DetailsList=new ArrayList<Details>();
		String refreshType=null;
		logger.info("inside build refres stats. filter inputs are "+filterinputs);
		logger.debug("summary result "+summaryResult);
		logger.debug("detailed result "+detailedResult);
		logger.debug("error code result "+errorCodeResults);
		
		if(filterinputs.get("groupby").equalsIgnoreCase(YSLConstants.GROUPBY_PROVIDER)){
			refreshType=filterinputs.get("statsExtnType")==null? "VOLUME":filterinputs.get("statsExtnType");
			commonExtn= new CommonExtn();
			Summary summary=null;
			 List<ContainerStat> containerStatsArray=null;
			 if(this.containerStats){
				 logger.info("inside build refres stats for container stats");
				String siteName = null;
				String timeStampOfInsert = null;
				String siteId = null;
				if (summaryResult != null) {
					siteName = commonExtn.getSiteName(Long.valueOf(summaryResult[0].getSite_id()));
					timeStampOfInsert = summaryResult[0].getTimestamp_of_insert();
					siteId = summaryResult[0].getSite_id();
					Map<String,ContainerStat> containerMap= new HashMap<String, ContainerStat>();
					try {
						performCalcOfSummarySplunkResult(summaryResult, containerMap);
					} catch (Exception e) {
						logger.info("exception occurred while doing pre calcs from summary result. summary result " + summaryResult);
						logger.error("Exception while doing pre calcs from summary result :" + ExceptionUtils.getFullStackTrace(e));
						throw e;
					}
					containerStatsArray=new ArrayList<ContainerStat>(containerMap.values());
					
					summary=buildSummary(containerStatsArray,true);
					
					for(ContainerStat container:containerStatsArray){
						//ashish
						container.success.rate=calculatePercentge(container.success.volume,container.totalVolume);
						List<FailureEntity> failureArray = container.failure;
						for(FailureEntity f:failureArray){
							f.rate=calculatePercentge(f.volume,container.totalVolume);
						}
					}
				}
				 List<Details> detailsList= null;
				if (detailedResult != null) {
					 try {
						if (summaryResult == null) {
							siteName = commonExtn.getSiteName(Long.valueOf(detailedResult[0].getSite_id()));
							timeStampOfInsert = detailedResult[0].getTimestamp_of_insert();
							siteId = detailedResult[0].getSite_id();
						}
						 
						 detailsList = buildRefStatsContainerNHistoric(detailedResult,
								 detailsList);
					 } catch (Exception e) {
						 logger.info("exception occurred while buidling ref statsFor container and historic, detailed result " + detailedResult);
						 logger.error("Exception stack trace:" + ExceptionUtils.getFullStackTrace(e));
						 throw e;
						 
					 }
					 
				 }
				 //build for error code break up
				 if(isErrorCodeBreakUpNeeded) {
					 	 
						 addErrorCodeBreakUp(summary,siteName,errorCodeResults);
				 }
				
				Info  info= new Info(siteId, siteName, commonExtn.convertEpocToDate(timeStampOfInsert), summary, detailsList);
				 logger.debug(" info obj built for refresh stats for site "+info);
				 infoList.add(info);				 
				 refreshStats= new RefreshStats(filterinputs.get("groupby"), "REFRESH",refreshType ,infoList);
				 return refreshStats;
			 } else {
				 String siteName = null;
				 if((filterinputs.get("statsExtnType") != null && filterinputs.get("statsExtnType").startsWith("TOP")) || YSLConstants.ALL_COBRANDS.equalsIgnoreCase(filterinputs.get("cobrandId"))) {
				 for(Result result: summaryResult){
						summary=buildSummaryNode(result);
						//build for error code break up
						Info  info= new Info(result.getSite_id(), commonExtn.getSiteName(Long.valueOf(result.getSite_id())), commonExtn.convertEpocToDate(result.getTimestamp_of_insert()), summary, null);
						//Info  info= new Info(result.SITE_ID, "SiteName1", commonExtn.convertEpocToDate(result.TIMESTAMP_OF_INSERT), summary, null);
						if( filterinputs.get("statsExtnType")!=null && filterinputs.get("statsExtnType").equalsIgnoreCase(YSLConstants.SPLUNK_TOP_FAILURE)){
							String failurePer = dformat.format((Double.valueOf(result.getSite_dependent_errors()) + Double.valueOf(result.getTechnical_errors())) / Double.valueOf(result.getTotal_refreshes()) * 100);
							info.setPerWithOutUar(Float.parseFloat(failurePer));
						}
						infoList.add(info);
					}
				 if(filterinputs.get("statsExtnType") !=null && filterinputs.get("statsExtnType").equalsIgnoreCase(YSLConstants.SPLUNK_TOP_FAILURE)){
					 Collections.sort(infoList,new Comparator<Info>() {
						 @Override
						 public int compare(Info info1, Info info2) {
							 return info2.getPerWithOutUar().compareTo(info1.getPerWithOutUar()) ;
						 }
					 });
				 }
				 } else {
					 //Fix for Bug 876604
					String timestampOfInsert = null;
					String siteId = null;
					 if(summaryResult !=null){
						 summary = buildOneSummaryNodeForAllData(summaryResult);
						 siteName = commonExtn.getSiteName(Long.valueOf(summaryResult[0].getSite_id()));
						timestampOfInsert = summaryResult[0].getTimestamp_of_insert();
						siteId = summaryResult[0].getSite_id();
					 }
					 List<Details> detailsList= null;
					if (detailedResult != null) {
						 try {
							timestampOfInsert = detailedResult[0].getTimestamp_of_insert();
							siteId = detailedResult[0].getSite_id();
							 detailsList = buildRefStatsContainerNHistoric(detailedResult,
									 detailsList);
						 } catch (Exception e) {
							 logger.info("exception occurred while buidling ref statsFor container and historic, detailed result " + detailedResult);
							 logger.error("Exception stack trace:" + ExceptionUtils.getFullStackTrace(e));
							 throw e;
							 
						 }
					 }
						Info  info= new Info(siteId, siteName, commonExtn.convertEpocToDate(timestampOfInsert), summary, detailsList);
						infoList.add(info);
				
				 }
				if (isErrorCodeBreakUpNeeded) {
					addErrorCodeBreakUp(summary, siteName, errorCodeResults);
				}
			 }
			
	
		
		}
		else if (filterinputs.get("groupby").equalsIgnoreCase(YSLConstants.GROUPBY_COBRAND)) {
			Info info = null;
			Summary summary = null;
			String cobrandName = null;
			String strCobrandId = null;
			String timeStampOfInsert = null;
			commonExtn = new CommonExtn();
			if (cobrandErrorCodeResult != null) {
				strCobrandId = cobrandErrorCodeResult[0].getCobrandId();
				cobrandName = commonExtn.getCobrandName(strCobrandId);
				summary = buildCobrandErrorCode(cobrandErrorCodeResult);
				info = new Info(strCobrandId, cobrandName, null, summary, null);
				infoList.add(info);
			} else {
				if (summaryResult != null) {
					summary = buildOneSummaryNodeForAllData(summaryResult);
					strCobrandId = summaryResult[0].getCobrand_id();
					timeStampOfInsert = summaryResult[0].getTimestamp_of_insert();
				}
				if (!historic) {
					cobrandName = commonExtn.getCobrandName(strCobrandId);
					info = new Info(strCobrandId, cobrandName, commonExtn.convertEpocToDate(timeStampOfInsert), summary, null);
					infoList.add(info);
				} else {
					if (detailedResult != null) {
						for (Result resultPerDate : detailedResult) {
							Details details = buildDetailNode(resultPerDate);
							DetailsList.add(details);
						}
						if (summaryResult == null) {
							strCobrandId = detailedResult[0].getCobrand_id();
							timeStampOfInsert = detailedResult[0].getTimestamp_of_insert();
							cobrandName = commonExtn.getCobrandName(strCobrandId);
						}
					}
					if (detailedResult != null || summaryResult != null) {
						cobrandName = commonExtn.getCobrandName(strCobrandId);
						info = new Info(strCobrandId, cobrandName, commonExtn.convertEpocToDate(timeStampOfInsert), summary, DetailsList);
						infoList.add(info);
					}

				}
			}
			
		}
		refreshStats= new RefreshStats(filterinputs.get("groupby"), filterinputs.get("refreshType"),refreshType ,infoList);
		return refreshStats;
	}
	
	
	private Summary buildCobrandErrorCode(SiteErrorCodeContributionResult[] cobrandErrorCodeResult) {
		Long totalVolume = 0l;
		Map<String, ErrorCodeBreakUp> agentErrorMap = new HashMap<String, ErrorCodeBreakUp>();
		Map<String, ErrorCodeBreakUp> siteErrorMap = new HashMap<String, ErrorCodeBreakUp>();
		Map<String, ErrorCodeBreakUp> uarErrorMap = new HashMap<String, ErrorCodeBreakUp>();
		Long totalAgentErrors = 0l;
		Long totalSiteErrors = 0l;
		Long totalUarErrors = 0l;
		for (SiteErrorCodeContributionResult errorCodeObj : cobrandErrorCodeResult) {
			String errors = errorCodeObj.getTotalErrors();
			if (errors != null)
				totalVolume = totalVolume + Long.valueOf(errors);

			switch (errorCodeObj.getErrorType()) {
			case "AGENT_ERROR": {
				totalAgentErrors = totalAgentErrors + Long.parseLong(errorCodeObj.getTotalErrors());
				String errorCode = errorCodeObj.getErrorCode();
				ErrorCodeBreakUp errorCodeBreakUp = agentErrorMap.get(errorCode);
				errorCodeBreakUp = buildErrorCodeBreakUpObj(errorCodeBreakUp, errorCodeObj);
				agentErrorMap.put(errorCode, errorCodeBreakUp);
				break;
			}
			case "SITE_ERROR": {
				totalSiteErrors = totalSiteErrors + Long.parseLong(errorCodeObj.getTotalErrors());
				String errorCode = errorCodeObj.getErrorCode();
				ErrorCodeBreakUp errorCodeBreakUp = siteErrorMap.get(errorCode);
				errorCodeBreakUp = buildErrorCodeBreakUpObj(errorCodeBreakUp, errorCodeObj);
				siteErrorMap.put(errorCode, errorCodeBreakUp);
				break;
			}
			case "UAR_ERROR": {
				totalUarErrors = totalUarErrors + Long.parseLong(errorCodeObj.getTotalErrors());
				String errorCode = errorCodeObj.getErrorCode();
				ErrorCodeBreakUp errorCodeBreakUp = uarErrorMap.get(errorCode);
				errorCodeBreakUp = buildErrorCodeBreakUpObj(errorCodeBreakUp, errorCodeObj);
				uarErrorMap.put(errorCode, errorCodeBreakUp);
				break;
			}
			default: {
				logger.error("unknown error type specified " + errorCodeObj.getErrorType());
				break;
			}
			}

		}

		List<ErrorCodeBreakUp> agentErrorBreakups = new ArrayList<ErrorCodeBreakUp>(agentErrorMap.values());
		if (!agentErrorBreakups.isEmpty()) {
			for (ErrorCodeBreakUp errorCodeBreakUp : agentErrorBreakups) {
				errorCodeBreakUp.rate = dformat.format((Double.valueOf(errorCodeBreakUp.volume) / Double.valueOf(totalAgentErrors)) * 100);
			}
		}
		List<ErrorCodeBreakUp> siteErrorBreakups = new ArrayList<ErrorCodeBreakUp>(siteErrorMap.values());
		if (!siteErrorBreakups.isEmpty()) {
			for (ErrorCodeBreakUp errorCodeBreakUp : siteErrorBreakups) {
				errorCodeBreakUp.rate = dformat.format((Double.valueOf(errorCodeBreakUp.volume) / Double.valueOf(totalSiteErrors)) * 100);
			}
		}
		List<ErrorCodeBreakUp> uarErrorBreakups = new ArrayList<ErrorCodeBreakUp>(uarErrorMap.values());
		if (!uarErrorBreakups.isEmpty()) {
			for (ErrorCodeBreakUp errorCodeBreakUp : uarErrorBreakups) {
				errorCodeBreakUp.rate = dformat.format((Double.valueOf(errorCodeBreakUp.volume) / Double.valueOf(totalUarErrors)) * 100);
			}
		}
		List<FailureEntity> failureArray = new ArrayList<FailureEntity>();
		String techErrorPer = calculatePercentge(totalAgentErrors, totalVolume);
		String siteErrorPer = calculatePercentge(totalSiteErrors, totalVolume);
		String uarErrorPer = calculatePercentge(totalUarErrors, totalVolume);
		FailureEntity technicalEntity = new FailureEntity(TECHNICAL, String.valueOf(totalAgentErrors), techErrorPer);
		FailureEntity siteEntity = new FailureEntity(SITE, String.valueOf(totalSiteErrors), siteErrorPer);
		FailureEntity uarEntity = new FailureEntity(USERACTIONREQUIRED, String.valueOf(totalUarErrors), uarErrorPer);
		technicalEntity.setBreakups(agentErrorBreakups);
		siteEntity.setBreakups(siteErrorBreakups);
		uarEntity.setBreakups(uarErrorBreakups);
		failureArray.add(technicalEntity);
		failureArray.add(siteEntity);
		failureArray.add(uarEntity);
		Summary summary = new Summary(String.valueOf(totalVolume), null, failureArray, null, null, null);
		return summary;
	}

	private String calculatePercentge(Long errorVolume, Long totalVolume) {
			return dformat.format((Double.valueOf(errorVolume)/Double.valueOf(totalVolume))*100);
	}

	private ErrorCodeBreakUp buildErrorCodeBreakUpObj(ErrorCodeBreakUp errorCodeBreakUp, SiteErrorCodeContributionResult errorCodeObj) {
		if (errorCodeBreakUp == null) {
			String errDescValue = MiscUtil.getPropertyValue(errorCodeObj.getErrorCode(), false);
			errDescValue = errDescValue == null ? "Un-defined error code" : errDescValue;
			errDescValue = MessageFormat.format(errDescValue, "Site");
			errorCodeBreakUp = new ErrorCodeBreakUp(errorCodeObj.getErrorCode(), errorCodeObj.getTotalErrors(), null, errDescValue);
		} else {
			String volume = errorCodeBreakUp.getVolume();
			Long totalvol = Long.valueOf(volume) + Long.valueOf(errorCodeObj.getTotalErrors());
			errorCodeBreakUp.setVolume(String.valueOf(totalvol));
		}
		
		return errorCodeBreakUp;
	}


	private void addErrorCodeBreakUp(Summary summary, String siteName,
			SiteErrorCodeContributionResult[] errorCodeResults) {
		logger.debug("Inside for Error code breakup");
		Map<String, ErrorCodeBreakUp> agentErrorMap = new HashMap<String, ErrorCodeBreakUp>();
		Map<String, ErrorCodeBreakUp> siteErrorMap = new HashMap<String, ErrorCodeBreakUp>();
		Map<String, ErrorCodeBreakUp> uarErrorMap = new HashMap<String, ErrorCodeBreakUp>();

		long agentFailureVolume = 0L;
		long siteFailureVolume = 0L;
		long uarFailureVolume = 0L;

		if (isErrorCodeBreakUpNeeded && errorCodeResults != null) {

			String errorDesc = null;
			for (SiteErrorCodeContributionResult errorCodeResult : errorCodeResults) {
				String errDescValue = MiscUtil.getPropertyValue(errorCodeResult.getErrorCode(), false);
				errDescValue = errDescValue == null ? "Un-defined error code" : errDescValue;
				errorDesc = MessageFormat.format(errDescValue, siteName);
				switch (errorCodeResult.getErrorType()) {
				case "AGENT_ERROR": {
					agentFailureVolume = agentFailureVolume + Long.parseLong(errorCodeResult.getTotalErrors());
					String errorCode = errorCodeResult.getErrorCode();
					ErrorCodeBreakUp errorCodeBreakUp = agentErrorMap.get(errorCode);
					errorCodeBreakUp = buildErrorCodeBreakUpForSite(errorCodeBreakUp, errorCodeResult, errorDesc);
					agentErrorMap.put(errorCode, errorCodeBreakUp);
					break;
				}
				case "SITE_ERROR": {
					siteFailureVolume = siteFailureVolume + Long.parseLong(errorCodeResult.getTotalErrors());
					String errorCode = errorCodeResult.getErrorCode();
					ErrorCodeBreakUp errorCodeBreakUp = siteErrorMap.get(errorCode);
					errorCodeBreakUp = buildErrorCodeBreakUpForSite(errorCodeBreakUp, errorCodeResult, errorDesc);
					siteErrorMap.put(errorCode, errorCodeBreakUp);
					break;
				}
				case "UAR_ERROR": {
					uarFailureVolume = uarFailureVolume + Long.parseLong(errorCodeResult.getTotalErrors());
					String errorCode = errorCodeResult.getErrorCode();
					ErrorCodeBreakUp errorCodeBreakUp = uarErrorMap.get(errorCode);
					errorCodeBreakUp = buildErrorCodeBreakUpForSite(errorCodeBreakUp, errorCodeResult, errorDesc);
					uarErrorMap.put(errorCode, errorCodeBreakUp);
					break;
				}
				default: {
					logger.error("unknown error type specified " + errorCodeResult.getErrorType());
					break;
				}
				}

			}
			List<ErrorCodeBreakUp> techErrorCodeBreakUps = new ArrayList<ErrorCodeBreakUp>(agentErrorMap.values());
			List<ErrorCodeBreakUp> siteErrorCodeBreakUps = new ArrayList<ErrorCodeBreakUp>(siteErrorMap.values());
			List<ErrorCodeBreakUp> uarErrorCodeBreakUps = new ArrayList<ErrorCodeBreakUp>(uarErrorMap.values());

			for (FailureEntity failureEntity : summary.failure) {
				switch(failureEntity.type) {
				case TECHNICAL:
					for (ErrorCodeBreakUp errorCodeBreakUp : techErrorCodeBreakUps) {
						errorCodeBreakUp.rate = SplunkResponseTransformer.dformat.format(
								(Double.valueOf(errorCodeBreakUp.volume)/Double.valueOf(agentFailureVolume)) * 100);
					}

					failureEntity.setBreakups(techErrorCodeBreakUps);
					break;
				case SITE:
					for (ErrorCodeBreakUp errorCodeBreakUp : siteErrorCodeBreakUps) {
						errorCodeBreakUp.rate = SplunkResponseTransformer.dformat.format(
								(Double.valueOf(errorCodeBreakUp.volume)/Double.valueOf(siteFailureVolume)) * 100);
					}

					failureEntity.setBreakups(siteErrorCodeBreakUps);
					break;
				case USERACTIONREQUIRED:
					for (ErrorCodeBreakUp errorCodeBreakUp : uarErrorCodeBreakUps) {
						errorCodeBreakUp.rate = SplunkResponseTransformer.dformat.format(
								(Double.valueOf(errorCodeBreakUp.volume)/Double.valueOf(uarFailureVolume)) * 100);
					}
					failureEntity.setBreakups(uarErrorCodeBreakUps);
					break;
				default: {
					logger.error("failure entity type " + failureEntity.type);
					break;
				}
				}
			}
		}

	}

	private void performCalcOfSummarySplunkResult(Result[] summaryResult,
			Map<String, ContainerStat> containerMap) {
		ContainerStat containerStat;
		for(Result result: summaryResult){
			 containerStat= buildContainer(result,true);
			 logger.debug("container stat after building from result "+containerStat);
			 if(containerMap.containsKey(containerStat.container)){
				 ContainerStat oldEntry=containerMap.get(containerStat.container);
				 String tempVolume=containerStat.totalVolume;
				 containerStat.totalVolume=String.valueOf(Integer.parseInt( containerStat.totalVolume)+Integer.parseInt(oldEntry.totalVolume));
				 containerStat.success.volume=String.valueOf(Integer.parseInt( containerStat.success.volume)+Integer.parseInt(oldEntry.success.volume));
				 if (reportType.equalsIgnoreCase("ADD"))
					 containerStat.partialSuccess.volume=String.valueOf(Integer.parseInt( containerStat.partialSuccess.volume)+Integer.parseInt(oldEntry.partialSuccess.volume));
				 //calculate new percentage
				 List<FailureEntity> failiures=containerStat.failure;
				 List<FailureEntity> failiuresOldEntry=oldEntry.failure;
				 for(FailureEntity newFailure:failiures){
					 for(FailureEntity oldFailure:failiuresOldEntry){
						 if(newFailure.type.equalsIgnoreCase(oldFailure.type)){
							 newFailure.volume=String.valueOf(Integer.parseInt(newFailure.volume)+Integer.parseInt(oldFailure.volume));
							 newFailure.rate=calculatePercentge(newFailure.volume, containerStat.totalVolume);
						 }
					 }
				 }
				 if(isYodlee){
					 double finalLatency = ((Double
							 .parseDouble(containerStat.latency.avg) * Double
							 .parseDouble(tempVolume)) + (Double
									 .parseDouble(oldEntry.latency.avg) * Double
									 .parseDouble(oldEntry.totalVolume)))
									 / (Double.valueOf(oldEntry.totalVolume) + Double
											 .valueOf(tempVolume));
					 containerStat.latency.avg=dformat.format(finalLatency);
				 }
				 
				
				 containerMap.put(containerStat.container, containerStat);
			 }else{
				 containerMap.put(containerStat.container, containerStat);
			 }
		 }
		 logger.debug("container map "+containerMap);
	}


	
	private ErrorCodeBreakUp buildErrorCodeBreakUpForSite(ErrorCodeBreakUp errorCodeBreakUp, SiteErrorCodeContributionResult errorCodeObj, String errDescValue) {
		if (errorCodeBreakUp == null) {
			errorCodeBreakUp = new ErrorCodeBreakUp(errorCodeObj.getErrorCode(), errorCodeObj.getTotalErrors(), null, errDescValue);
		} else {
			String volume = errorCodeBreakUp.getVolume();
			Long totalvol = Long.valueOf(volume) + Long.valueOf(errorCodeObj.getTotalErrors());
			errorCodeBreakUp.setVolume(String.valueOf(totalvol));
		}
		return errorCodeBreakUp;
	}
	
	private List<Details> buildRefStatsContainerNHistoric(
			Result[] detailedResult, List<Details> detailsList) {
		if(historic){
			 detailsList= new ArrayList<Details>();
			 //code here
			 Map<String,List<Result>> mymap= new LinkedHashMap<String,List<Result>>();
				
				for(Result rs:detailedResult){
					if(mymap.containsKey(rs.getTimestamp())){
						mymap.get(rs.getTimestamp()).add(rs);
					}
					else{
						List<Result> ar= new ArrayList<Result>();
						ar.add(rs);
						mymap.put(rs.getTimestamp(), ar);
					}
					
				}
				for(String date :mymap.keySet()){

					List<Result> containerList= mymap.get(date);
					List<ContainerStat> containers= new ArrayList<ContainerStat>();
						for(Result oneResult: containerList){
							ContainerStat oneContainer =buildContainer(oneResult,false);
							containers.add(oneContainer);
						}
						Details detail= new Details(date,buildSummary(containers,false));								
						detailsList.add(detail);								 
						 for(ContainerStat container:containers){
							 //ashish
							 container.success.rate=calculatePercentge(container.success.volume,container.totalVolume);
							 if(includeLatencyBreakup && isYodlee){
								 List<LatencyBreakup> latenctBreakup = container.latency.breakups;
								 for(LatencyBreakup l: latenctBreakup){
										l.rate=calculatePercentge(l.volume,detail.summary.totalVolume);
									}
							 }
							 List<FailureEntity> failureArray = container.failure;
							 for(FailureEntity f:failureArray){
									f.rate=calculatePercentge(f.volume,container.totalVolume);
								}
						 }
				
				}
		 }
		return detailsList;
	}

	private Details buildDetailNode(Result resultPerDate) {
		Summary summary=buildSummaryNode(resultPerDate);
		String date=resultPerDate.getTimestamp();
		Details d= new Details(date, summary);
		return d;
	}

	private Summary buildOneSummaryNodeForAllData(Result[] summaryResult) {
		summaryExtn= new SummaryExtn();
		LatencyBreakupExtn latencyBreakupExtn = new LatencyBreakupExtn();
		String totalVolume=summaryExtn.getTotalVolume(summaryResult);
		SuccessEntity successEntity= buildSuccessNode(summaryResult,totalVolume);
		List<FailureEntity> failureEntities= buildFailureArray(summaryResult,totalVolume);
		Latency latency = null;
		if(isYodlee){
			latency = buildLatency(summaryResult,totalVolume);
			if(includeLatencyBreakup) {
				List<LatencyBreakup> latencyBreakups = latencyBreakupExtn.aggregateLatencyBreakup(summaryResult,totalVolume);
				latency.breakups = latencyBreakups;
			}
		}
		PartialSuccessEntity partialSuccessEntity = null;
		if (reportType.equalsIgnoreCase("ADD"))
			partialSuccessEntity = buildPartialSuccessNode(summaryResult, totalVolume);
		
		Summary summary= new Summary(totalVolume, successEntity, failureEntities, latency,null,partialSuccessEntity);
		
		logger.debug("summary obj aggregated from multiple results "+summary);
		return summary;
	}

	private Latency buildLatency(Result[] summaryResult, String totalVolume) {
		
		Double min_latency=0.0;
		Double max_latency=0.0;
		Double avg_latency=0.0;
		Double total_latency=0.0;
		for(Result result:summaryResult){
			 if(min_latency.compareTo(0.0)==0 && result.getMin_latency()!=null)
				 min_latency=Double.parseDouble(result.getMin_latency());
			 if(max_latency.compareTo(0.0)==0 && result.getMin_latency()!=null)
				 max_latency=Double.parseDouble(result.getMax_latency());
			 if(result.getMax_latency()!=null && Double.parseDouble(result.getMax_latency()) > max_latency)
				 max_latency=Double.parseDouble(result.getMax_latency());
			 if(result.getMin_latency()!=null&&  Double.parseDouble(result.getMin_latency()) < min_latency )
				 min_latency=Double.parseDouble(result.getMin_latency());
			 total_latency=total_latency+(Double.parseDouble(result.getAvg_latency())*Double.parseDouble(result.getTotal_refreshes()));
		}
		avg_latency=total_latency/Double.valueOf(totalVolume);
		List<LatencyBreakup> latenctBreakup=null;
		//TODO - handle latencybrak up i believe. not sure though
	
		
		Latency latency= new Latency(dformat.format(min_latency),dformat.format(max_latency),dformat.format(avg_latency),latenctBreakup);
		return latency;
	}

	private List<FailureEntity> buildFailureArray(Result[] summaryResult,
			String totalVolume) {
		List<FailureEntity> failureArray= new ArrayList<FailureEntity>();
		String techErrorVolume=calculateFailureVolume(TECHNICAL, summaryResult);
		String siteErrorVolume=calculateFailureVolume(SITE, summaryResult);
		String uarErrorVolume=calculateFailureVolume(USERACTIONREQUIRED, summaryResult);
		String techErrorPer=calculatePercentge(techErrorVolume,totalVolume);
		String siteErrorPer=calculatePercentge(siteErrorVolume,totalVolume);
		String uarErrorPer=calculatePercentge(uarErrorVolume,totalVolume);
		failureArray.add(new FailureEntity(TECHNICAL,techErrorVolume,techErrorPer));
		failureArray.add(new FailureEntity(SITE,siteErrorVolume,siteErrorPer));
		failureArray.add(new FailureEntity(USERACTIONREQUIRED,uarErrorVolume,uarErrorPer));
		return failureArray;
	}

	private SuccessEntity buildSuccessNode(Result[] summaryResult,
			String totalVolume) {
		successExtn= new SuccessExtn();
		String successVolume=successExtn.getSuccessVolume(summaryResult);
		String successPer=dformat.format((Double.valueOf(successVolume)/Double.valueOf(totalVolume))*100);
		SuccessEntity successEntity= new SuccessEntity(successVolume, successPer);
		return successEntity;
	}

	private PartialSuccessEntity buildPartialSuccessNode(Result[] summaryResult, String totalVolume) {
		successExtn = new SuccessExtn();
		String successVolume = successExtn.getPartialSuccessVolume(summaryResult);
		String successPer = dformat.format((Double.valueOf(successVolume) / Double.valueOf(totalVolume)) * 100);
		PartialSuccessEntity successEntity = new PartialSuccessEntity(successVolume, successPer);
		return successEntity;
	}
	private Summary buildSummaryNode(Result result) {
		summaryExtn= new SummaryExtn();
		String totalVolume=result.getTotal_refreshes();
		SuccessEntity successEntity= buildSuccessNode(result,totalVolume);
		List<FailureEntity> failureEntities= buildFailureArray(result,totalVolume);
		Latency latency = buildLatency(result,totalVolume);
		Summary summary=null;
		PartialSuccessEntity partialSuccessEntity = null;
		if (reportType.equalsIgnoreCase("ADD"))
			partialSuccessEntity = buildPartialSuccessNode(result, totalVolume);
	//	if(!isContainerStats)
			summary= new Summary(totalVolume, successEntity, failureEntities, latency, null,partialSuccessEntity);
		/*else{
			ContainerStat = buildContainer(result);
		}*/
		return summary;
	}



	private Latency buildLatency(Result result, String totalVolume) {
		
		String min_latency=null;;
		String max_latency=null;;
		String avg_latency=null;
		min_latency=result.getMin_latency() != null ? result.getMin_latency() : LATENCY_ZERO;
	    max_latency=result.getMax_latency() != null ? result.getMax_latency() : LATENCY_ZERO;
		avg_latency=result.getAvg_latency() != null ? result.getAvg_latency() : LATENCY_ZERO;
		List<LatencyBreakup> latencyBreakup=null;
		if(isYodlee && includeLatencyBreakup){
			latencyBreakup=new ArrayList<LatencyBreakup>();
			
			String latency_0_20_vol = result.getLatency_0_20();
			String latency_20_40_vol = result.getLatency_20_40();
			String latency_40_60_vol = result.getLatency_40_60();
			String latency_60_80_vol = result.getLatency_60_80();
			String latency_80_100_vol = result.getLatency_80_100();
			String latency_ABOVE_100_vol = result.getLatency_above_100();
			
			latencyBreakup.add(new LatencyBreakup(YSLConstants._0_TO_20_SECS,latency_0_20_vol,dformat.format(Double.valueOf(latency_0_20_vol)/Double.valueOf(totalVolume)*100)));
			latencyBreakup.add(new LatencyBreakup(YSLConstants._20_TO_40_SECS,latency_20_40_vol,dformat.format(Double.valueOf(latency_20_40_vol)/Double.valueOf(totalVolume)*100)));
			latencyBreakup.add(new LatencyBreakup(YSLConstants._40_TO_60_SECS,latency_40_60_vol,dformat.format(Double.valueOf(latency_40_60_vol)/Double.valueOf(totalVolume)*100)));
			latencyBreakup.add(new LatencyBreakup(YSLConstants._60_TO_80_SECS,latency_60_80_vol,dformat.format(Double.valueOf(latency_60_80_vol)/Double.valueOf(totalVolume)*100)));
			latencyBreakup.add(new LatencyBreakup(YSLConstants._80_TO_100_SECS,latency_80_100_vol,dformat.format(Double.valueOf(latency_80_100_vol)/Double.valueOf(totalVolume)*100)));
			latencyBreakup.add(new LatencyBreakup(YSLConstants.GRT_THAN_100_SECS,latency_ABOVE_100_vol,dformat.format(Double.valueOf(latency_ABOVE_100_vol)/Double.valueOf(totalVolume)*100)));
		}
		Latency latency= new Latency(min_latency,max_latency,avg_latency,latencyBreakup);
		return latency;
		
	}

	private List<FailureEntity> buildFailureArray(Result result,
			String totalVolume) {
		List<FailureEntity> failureArray= new ArrayList<FailureEntity>();
		String techErrorVolume=result.getTechnical_errors();
		String siteErrorVolume=result.getSite_dependent_errors();
		String uarErrorVolume=result.getUser_dependent_errors();
		String techErrorPer=calculatePercentge(techErrorVolume,totalVolume);
		String siteErrorPer=calculatePercentge(siteErrorVolume,totalVolume);
		String uarErrorPer=calculatePercentge(uarErrorVolume,totalVolume);
		failureArray.add(new FailureEntity(TECHNICAL,techErrorVolume,techErrorPer));
		failureArray.add(new FailureEntity(SITE,siteErrorVolume,siteErrorPer));
		failureArray.add(new FailureEntity(USERACTIONREQUIRED,uarErrorVolume,uarErrorPer));
		return failureArray;
	}

	private String calculatePercentge(String errorVolume, String totalVolume) {
		return SplunkResponseTransformer.dformat.format((Double.valueOf(errorVolume)/Double.valueOf(totalVolume))*100);
	}

	private String calculateFailureVolume(String failureType, Result[] summaryResult) {
		long sumOfFailure=0;  
		for(Result result:summaryResult){
			if(failureType.equalsIgnoreCase(TECHNICAL)){
				sumOfFailure=sumOfFailure+Long.parseLong(result.getTechnical_errors());
			}else if(failureType.equalsIgnoreCase(SITE)){
				sumOfFailure=sumOfFailure+Long.parseLong(result.getSite_dependent_errors());
			}else if(failureType.equalsIgnoreCase(USERACTIONREQUIRED)){
				sumOfFailure=sumOfFailure+Long.parseLong(result.getUser_dependent_errors());
			}
			
          }
		  return String.valueOf(sumOfFailure);
	}

	private SuccessEntity buildSuccessNode(Result result, String totalVolume) {
		successExtn= new SuccessExtn();
		String successVolume=result.getSuccessful_refreshes();
		String successPer=dformat.format((Double.valueOf(successVolume)/Double.valueOf(totalVolume))*100);
		SuccessEntity successEntity= new SuccessEntity(successVolume, successPer);
		return successEntity;
		
	}
	private PartialSuccessEntity buildPartialSuccessNode(Result result, String totalVolume) {
		String successVolume=result.getPartial_successful_refreshes();
		if (successVolume == null)
			successVolume = "0";
		String successPer=dformat.format((Double.valueOf(successVolume)/Double.valueOf(totalVolume))*100);
		PartialSuccessEntity successEntity= new PartialSuccessEntity(successVolume, successPer);
		return successEntity;
		
	}

	
	
public static void main(String[] args) throws IOException {
	//String summaryRes=new String(Files.readAllBytes(Paths.get("D:/softwares/issue/SummaryResponse.txt")));
	//String summaryRes=new String(Files.readAllBytes(Paths.get("D:/softwares/issue/SummaryResponse_provider.txt")));

	 String result = MessageFormat.format(
	     "At {0} value is null",null);
	 System.out.println("result"+result);
	 
	 double[] arr = { 23.59604,
			    35.7,
			    3.0,
			    9,0
			};

			for ( double dub : arr ) {
			  //System.out.println( String.format( "%.2f", dub ) );
			}
			System.out.println((double)(2)/3);
			System.out.println("divide by zero"+SplunkResponseTransformer.dformat.format((Double.valueOf(0)/Double.valueOf(0))*100));
	 
			String summaryRes=new String(Files.readAllBytes(Paths.get("C:\\Users\\knavuluri\\Desktop\\errorcode.txt")));
			System.out.println(summaryRes);
	testIncludeErrorOnly();
	//testIncludeContainerNErrorOnly();
}

private static void testIncludeErrorOnly() throws IOException {
	String summaryRes=new String(Files.readAllBytes(Paths.get("C:\\Users\\knavuluri\\Desktop\\errorcode.txt")));
	//String historicRes=new String(Files.readAllBytes(Paths.get("D:/chetan/errorResponse.txt")));
	//String errorRes=new String(Files.readAllBytes(Paths.get("D:/chetan/errorResponse.txt")));
	Map<String, String> filterMap= new HashMap<String, String>();
	filterMap.put("duration","24h");
	//filterMap.put("providerids","12");
	filterMap.put("latencyBreakup","false");
	filterMap.put("groupby","COBRAND");
	filterMap.put("refreshType","REFRESH");
	filterMap.put("userType","tier2");
	filterMap.put("historic","false");
	filterMap.put(YSLConstants.INCLUDE_ERROR_CODE, "true");
	//filterMap.put("container","false");
	//filterMap.put("refreshType", "REFRESH");
	
	String finalResonse=new SplunkResponseTransformer().formatSplunkResponse(filterMap, null, null,null,summaryRes);
	System.out.println(finalResonse);
}

private static void testIncludeContainerNErrorOnly() throws IOException {
	String summaryRes=new String(Files.readAllBytes(Paths.get("D:/chetan/summaryResponse_inc_container.txt")));
	//String historicRes=new String(Files.readAllBytes(Paths.get("D:/chetan/errorResponse.txt")));
	String errorRes=new String(Files.readAllBytes(Paths.get("D:/chetan/errorResponse.txt")));
	Map<String, String> filterMap= new HashMap<String, String>();
	filterMap.put("duration","24h");
	filterMap.put("providerids","12");
	filterMap.put("latencyBreakup","false");
	filterMap.put("groupby","PROVIDER");
	filterMap.put("refreshType","REFRESH");
	filterMap.put("userType","tier2");
	filterMap.put("historic","false");
	filterMap.put(YSLConstants.INCLUDE_ERROR_CODE, "true");
	filterMap.put("container","true");
	filterMap.put("refreshType", "REFRESH");
	
	String finalResonse=new SplunkResponseTransformer().formatSplunkResponse(filterMap, summaryRes, null,errorRes,null);
	System.out.println(finalResonse);
}
}
