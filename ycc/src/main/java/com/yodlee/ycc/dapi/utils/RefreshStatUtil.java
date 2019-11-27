/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.ycc.dapi.bean.KeyValue;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.splunkresponseformat.SplunkResponseTransformer;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsService;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;

/**
 * 
 * @author knavuluri
 * 
 */
@Service
public class RefreshStatUtil {
	
	@Autowired
	RefreshStatsDbService dbService;
	
	@Autowired 
	private RefreshStatsHandler handler;

	private static final Logger logger = LoggerFactory.getLogger(RefreshStatUtil.class);

	public static String defaltHDuration = "24h";
	
	public static String defaultDDuration="7d";
	
	public String getRefreshStatsData(RefreshStatsFilter filter) {
		RefreshStatsService refreshStatsService = handler.getHandler(filter.getGroupBy());
		refreshStatsService.validateRequestParams(filter);
		Map<KeyValue, List<KeyValue>> reportInfoMap = refreshStatsService.getReportInfo(filter);
		Map<String, String> filterMap = refreshStatsService.buildFilter(filter);
		String response = executeService(reportInfoMap, filterMap, filter);

		return response;
	}
	
	public static Map<KeyValue, List<KeyValue>> getSiteReportInfoPerSite(String cobrandId, String duration, String durationOffset, String include, String top, String providerIds) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		logger.info("inside getSiteReportInfoPerSite " + providerIds + " include " + include + " cobrandId " + cobrandId + " duration " + duration + " duration offset" + durationOffset + " top "
				+ top);
		if (providerIds != null && (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC) || include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP)))) {
			if (duration.contains("d")) {
				String report = YSLConstants.INCLUDE_CONTAINER_HISTORIC_REPORT;
				if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					report = YSLConstants.YCC_CHANNEL_SITE_HISTORIC_REPORT;
				KeyValue siteValueObj = buildMapForContainerHistoric(cobrandId, duration, durationOffset, providerIds, map,report);
				String report2 = YSLConstants.INCLUDE_CONTAINER_SUMMARY_REPORT;
				if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					report2 = YSLConstants.YCC_CHANNEL_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerSummaryWithHistoric(cobrandId, defaltHDuration, durationOffset, map, siteValueObj,report2);

				return map;
			}
			else {
				String report = YSLConstants.INCLUDE_CONTAINER_SUMMARY_REPORT;
				if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					report = YSLConstants.YCC_CHANNEL_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerForSummaryOnly(cobrandId, duration, durationOffset, providerIds, map,report);

				String report2 = YSLConstants.SplunkRepMapping.SITE_ERROR_BREAKUP_REPORT;
				if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					report2 = YSLConstants.YCC_CHANNEL_SITE_ERRORCODE_BREAKUP_REPORT;
				buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,report2,duration);
				return map;
			}
		}
		else if (providerIds != null) {
			logger.info("not historic and no include provided but provider id is not null");
			String reportName1 = MiscUtil.getPropertyValue(YSLConstants.INCLUDE_CONTAINER_SUMMARY_REPORT, false);
			if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
				reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_SITE_OVERALL_SUMMARY_REPORT, false);
			List<KeyValue> list1 = new ArrayList<KeyValue>();
			KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
			KeyValue valueObj3 = new KeyValue("duration", duration);
			list1.add(valueObj2);
			list1.add(valueObj3);

			if (!StringUtils.isBlank(durationOffset)) {
				KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
				list1.add(valueObj4);
			}
			KeyValue siteValueObj = new KeyValue("SITE_ID", providerIds);
			list1.add(siteValueObj);
			/*
			 * if (numRecords != null) { KeyValue valueObjj = new
			 * KeyValue("numRecords", numRecords); list1.add(valueObjj); }
			 */

			map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);

			// ADD error code break up
			String report2 = YSLConstants.SplunkRepMapping.SITE_ERROR_BREAKUP_REPORT;
			if(include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS)) 
				report2 = YSLConstants.YCC_CHANNEL_SITE_ERRORCODE_BREAKUP_REPORT;

			buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,report2,duration);
			return map;
		}
		return map;
	}
	private static void buildMapForErrorCodeBreakUp(String cobrandId,
			String include, String providerIds,
			Map<KeyValue, List<KeyValue>> map, String reportName2, String duration) {
		logger.debug("Build Map for Error code: cobrandId"+cobrandId+" duration="+duration+" include="+include);
		if (include != null && include.contains(YSLConstants.INCLUDE_ERROR_CODE)) {
			String reportName;
			reportName = MiscUtil.getPropertyValue(reportName2, false);
			logger.debug("Report name for refresh stats"+reportName);
			KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
			KeyValue valueObj2;

			KeyValue siteValueObj1 = new KeyValue("SITE_ID", providerIds);
			List<KeyValue> list = new ArrayList<KeyValue>();
			if (!StringUtils.isEmpty(duration)) {
				logger.debug("Object for duration"+duration);
				valueObj2 = new KeyValue("duration", duration);
				list.add(valueObj2);
			}
			list.add(valueObj);
			list.add(siteValueObj1);

			map.put(new KeyValue(reportName, reportName2), list);
		}
	}
	private static void buildMapForContainerForSummaryOnly(String cobrandId,
			String duration, String durationOffset,String providerIds,
			Map<KeyValue, List<KeyValue>> map, String reportName2) {
		String reportName1 = MiscUtil.getPropertyValue(reportName2, false);
		List<KeyValue> list1 = new ArrayList<KeyValue>();
		KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj3 = new KeyValue("duration", duration);
		list1.add(valueObj2);
		list1.add(valueObj3);
		if(!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list1.add(valueObj4);
		}
		KeyValue siteValueObj = new KeyValue("SITE_ID", providerIds);
		/*if (numRecords != null) {
			KeyValue valueObjj = new KeyValue("numRecords", numRecords);
			list1.add(valueObjj);
		}*/
		
		list1.add(siteValueObj);
		map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);
	}
	private static void buildMapForContainerSummaryWithHistoric(
			String cobrandId, String duration,String durationOffset,
			Map<KeyValue, List<KeyValue>> map, KeyValue siteValueObj, String reportName2) {
		String reportName1 = MiscUtil.getPropertyValue(reportName2, false);
		List<KeyValue> list1 = new ArrayList<KeyValue>();
		KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj3 = new KeyValue("duration", duration);
		list1.add(valueObj2);
		list1.add(valueObj3);
		if(!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list1.add(valueObj4);
		}
		list1.add(siteValueObj);
		map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);
	}
	private static KeyValue buildMapForContainerHistoric(String cobrandId,
			String duration, String durationOffset,String providerIds,
			Map<KeyValue, List<KeyValue>> map, String reportname2) {
		String reportName;
		reportName = MiscUtil.getPropertyValue(reportname2, false);
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj1 = new KeyValue("duration", duration);
		KeyValue siteValueObj = new KeyValue("SITE_ID", providerIds);
		List<KeyValue> list = new ArrayList<KeyValue>();
		list.add(valueObj);
		list.add(valueObj1);
		if(!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list.add(valueObj4);
		}
		list.add(siteValueObj);				
		map.put(new KeyValue(reportName, YSLConstants.SPLUNK_HISTORIC), list);
		return siteValueObj;
	}

	public static Map<KeyValue, List<KeyValue>> getSiteReportInfo(String cobrandId, String duration, String durationOffset, String include, String top, String numRecords) {
		logger.debug("Duration Value"+duration+"Include"+include);
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = null;
		if (top != null && YSLConstants.SPLUNK_TOP_VOLUME.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_VOLUME_SITE_STATS_4H, false);
				logger.debug("Report Name is"+reportName);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_VOLUME_SITE_STATS_12H, false);
				logger.debug("Report Name is"+reportName);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_VOLUME_SITE_STATS, false);
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_VOLUME_SITE_STATS_4H, false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_VOLUME_SITE_STATS_12H, false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_VOLUME_SITE_STATS, false);
			}
		}
		else if (top != null && YSLConstants.SPLUNK_TOP_FAILURE.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_FAILURE_SITE_STATS_4H, false);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_FAILURE_SITE_STATS_12H, false);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_TOP_FAILURE_SITE_STATS, false);
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_FAILURE_SITE_STATS_4H, false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_FAILURE_SITE_STATS_12H, false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_TOP_FAILURE_SITE_STATS, false);
			}
		}
		List<KeyValue> list = new ArrayList<KeyValue>();
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		if (duration != null) {
			KeyValue valueObj1 = new KeyValue("duration", duration);
			list.add(valueObj1);
		}
		if (numRecords != null) {
			KeyValue valueObj2 = new KeyValue("numRecords", numRecords);
			list.add(valueObj2);
		}
		list.add(valueObj);

		if (reportName != null)
			map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);

		return map;
	}

	public String executeService(Map<KeyValue, List<KeyValue>> reportMap, Map<String, String> filterMap, RefreshStatsFilter filter) {
		LinkedHashMap<String, String> mappingKey = new LinkedHashMap<String, String>();
		mappingKey.put("cobrandId", "COBRAND_ID");
		mappingKey.put("duration", "TIMESTAMP");
		mappingKey.put("SITE_ID", "SITE_ID");
		logger.info(" maps content report map " + reportMap + " filter map " + filterMap);
		Set<Entry<KeyValue, List<KeyValue>>> reportMapEntrySet = reportMap.entrySet();
		String summaryRes = null;
		String historicRes = null;
		String errorCodeRes = null;
		String errorCodeOverallRes = null;
		for (Entry<KeyValue, List<KeyValue>> entry : reportMapEntrySet) {
			KeyValue keyval = entry.getKey();
			String reportName = keyval.getKey();
			logger.debug("Executing Service for report ==="+reportName);
			String value = keyval.getValue();
			RefreshStatsDbFilter dbFilter = new RefreshStatsDbFilter();
			if (filter.getCobrandId() != null)
				dbFilter.setCobrandId(Long.valueOf(filter.getCobrandId()));
			dbFilter.setReportType(reportName);
			if (filter.getProviderIds() != null) {
				String[] siteids = filter.getProviderIds().split("\\s*,\\s*");
				List<Long> siteIdList = new ArrayList<Long>();
				for (String string : siteids) {
					siteIdList.add(Long.valueOf(string));
				}
				dbFilter.setSiteId(siteIdList);
			}
			if (filter.getNumRecords() != null)
				dbFilter.setNumRecords(Integer.valueOf(filter.getNumRecords()));
			List<KeyValue> keyValueList = entry.getValue();
			if (keyValueList != null && keyValueList.size() > 0) {
				for (KeyValue keyValue : keyValueList) {
					String key = keyValue.getKey();
					String value2 = keyValue.getValue();
					if (key.equals("duration") || key.equals(YSLConstants.DURATION_OFFSET)) {
						if (key.equals("duration") && filter.getTop()==null) {
							logger.debug("Duration for executing service is"+value2);
							dbFilter.setDuration(value2);
						}
						if (key.equals(YSLConstants.DURATION_OFFSET)) {
							dbFilter.setDurationOffSet(value2);
						}

					}
				}
			}
			Date sDate = new Date();
			String refreshStatsData = dbService.getRefreshStatsData(dbFilter);
			Date eDate = new Date();
			logger.info("Time taken for " + reportName + " Db query:" + (eDate.getTime() - sDate.getTime()) + " millis: " + filter);
			if (value.equals(YSLConstants.SPLUNK_HISTORIC))
				historicRes = refreshStatsData;
			if (value.equals(YSLConstants.SPLUNK_SUMMARY))
				summaryRes = refreshStatsData;
			if (value.equals(YSLConstants.SPLUNK_SUMMARY_HIST)) {
				summaryRes = refreshStatsData;
				historicRes = refreshStatsData;
			}
			if (value.equals(YSLConstants.ERRORCODE_OVERALL))
				errorCodeOverallRes = refreshStatsData;
			
			if (value.equals(YSLConstants.SplunkRepMapping.SITE_ERROR_BREAKUP_REPORT) || value.equals(YSLConstants.YCC_IAV_SITE_ERRORCODE_BREAKUP_REPORT)
					|| value.equals(YSLConstants.YCC_ADD_SITE_ERRORCODE_BREAKUP_REPORT) || value.equals(YSLConstants.YCC_CHANNEL_SITE_ERRORCODE_BREAKUP_REPORT)
					|| value.equals(YSLConstants.YCC_CHANNEL_IAV_SITE_ERRORCODE_BREAKUP_REPORT)) {
				errorCodeRes = refreshStatsData;
			}
			logger.debug("Db results for report type:" + filter + " : " + refreshStatsData);
		}
		int count = 0;
		if (summaryRes != null) {
			logger.info("summary response is not null");
			logger.debug("summary response is " + summaryRes);
			JsonElement jelement = new JsonParser().parse(summaryRes);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("results");
			if (jarray.size() <= 0) {
				logger.info("summary response is " + summaryRes);
				count = count + 1;
				summaryRes = null;
			}
		} else
			count = count + 1;
		if (historicRes != null) {
			logger.info("history response is not null");
			logger.debug("history response is " + historicRes);
			JsonElement jelement = new JsonParser().parse(historicRes);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("results");
			if (jarray.size() <= 0) {
				logger.info("history response is " + historicRes);
				count = count + 1;
				historicRes = null;
			}
		} else
			count = count + 1;
		if (errorCodeRes != null) {
			logger.info("error code response is not null");
			logger.debug("errorCode response is " + errorCodeRes);
			JsonElement jelement = new JsonParser().parse(errorCodeRes);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("results");
			if (jarray.size() <= 0) {
				logger.info("errorCode response is " + errorCodeRes);
				errorCodeRes = null;
			}
		}
		if (errorCodeOverallRes != null) {
			logger.info("Cobrand error code response is not null");
			logger.debug("Cobrand errorCode response is " + errorCodeOverallRes);
			JsonElement jelement = new JsonParser().parse(errorCodeOverallRes);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("results");
			if (jarray.size() <= 0) {
				logger.info("Cobrand errorCode response is " + errorCodeOverallRes);
				errorCodeOverallRes = null;
			}
		}
		if (count >= 2 && errorCodeOverallRes == null)
			return "{}";

		logger.debug("Started the formattting the response");
		String response = new SplunkResponseTransformer().formatSplunkResponse(filterMap, summaryRes, historicRes, errorCodeRes,errorCodeOverallRes);
		logger.debug("Formatted output:" + response);
		return response;
	}

	public static Map<KeyValue, List<KeyValue>> getRefreshReportInfo(String cobrandId, String duration,String durationOffset,String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
			String reportName = "";
			if (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC)||include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP))) {
				if (duration.contains("d")) {
					List<KeyValue> list = new ArrayList<KeyValue>();
					reportName =MiscUtil.getPropertyValue(YSLConstants.YCC_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false); 
					if (include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					list.add(valueObj);
					list.add(valueObj1);
					
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_HISTORIC), list);

					String reportName1 =MiscUtil.getPropertyValue(YSLConstants.YCC_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj3 = new KeyValue("duration", defaltHDuration);
					List<KeyValue> list1 = new ArrayList<KeyValue>();
					list1.add(valueObj2);
					list1.add(valueObj3);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj4);
					}
					map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);
				} else {
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					List<KeyValue> list = new ArrayList<KeyValue>();
					list.add(valueObj);
					list.add(valueObj1);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj2);
					}
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY_HIST), list);
				}
			} else {
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
				if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
				KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
				KeyValue valueObj1 = new KeyValue("duration", duration);
				List<KeyValue> list = new ArrayList<KeyValue>();
				list.add(valueObj);
				list.add(valueObj1);
				if(!StringUtils.isBlank(durationOffset)) {
					KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
					list.add(valueObj2);
				}
				map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
			}

		return map;

	}

	@SuppressWarnings("serial")
	public static String replaceAll(String inputStr) {
		String regexp = "d|h";
		Map<String, String> replacements = new HashMap<String, String>() {
			{
				put("d", "");
				put("h", "");
			}
		};
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(inputStr);

		while (m.find())
			m.appendReplacement(sb, replacements.get(m.group()));
		m.appendTail(sb);
		return sb.toString();
	}
	public static Map<String, String> processDuration(final String duration,final String durationOffset,
			String include) {
		Map<String,String> durationMap = new HashMap<>();
		String durationConverted = null;
		String durationOffsetConverted = null;
		if (StringUtils.isEmpty(duration)) {
			if (StringUtils.isNotEmpty(include) && include.contains(YSLConstants.INCLUDE_HISTORIC))
				durationConverted = RefreshStatUtil.defaultDDuration;
			else
				durationConverted = RefreshStatUtil.defaltHDuration;
		}
		else {
			durationConverted = duration.toLowerCase();
			if (!(StringUtils.containsIgnoreCase(duration, "h") || StringUtils.containsIgnoreCase(duration, "d")))
				throw new RefreshStatException("Duration should contain h or d");
			else {
				
				durationConverted = parseISOFormatForPeriod(duration);
			}
			if(!StringUtils.isEmpty(durationOffset))
				if(StringUtils.containsIgnoreCase(durationOffset, "h") || StringUtils.containsIgnoreCase(durationOffset, "d")) {
					durationOffsetConverted = parseISOFormatForPeriod(durationOffset);
				} else 
				throw new RefreshStatException("Duration Offset should contain h or d");
		}
		durationMap.put(YSLConstants.DURATION_OFFSET, durationOffsetConverted);
		durationMap.put(YSLConstants.DURATION, durationConverted);
		return durationMap;
	}

	private static String parseISOFormatForPeriod(String periodValue) {
		Period period = Period.parse(periodValue);
		if(StringUtils.containsIgnoreCase(periodValue, "h")) {
			periodValue = period.getHours() + "h";
		} else {
			periodValue = period.getDays() + "d";
		}
		return periodValue;
	}
	public static Map<KeyValue, List<KeyValue>> getIavRefreshReportInfo(String cobrandId, String duration,String durationOffset,String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
			String reportName = "";
			if (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC)||include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP))) {
				if (duration.contains("d")) {
					List<KeyValue> list = new ArrayList<KeyValue>();
					reportName =MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false); 
					if (include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					list.add(valueObj);
					list.add(valueObj1);
					
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_HISTORIC), list);

					String reportName1 =MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj3 = new KeyValue("duration", defaltHDuration);
					List<KeyValue> list1 = new ArrayList<KeyValue>();
					list1.add(valueObj2);
					list1.add(valueObj3);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj4);
					}
					map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);
				} else {
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					List<KeyValue> list = new ArrayList<KeyValue>();
					list.add(valueObj);
					list.add(valueObj1);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj2);
					}
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY_HIST), list);
				}
			} else {
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
				if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
				KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
				KeyValue valueObj1 = new KeyValue("duration", duration);
				List<KeyValue> list = new ArrayList<KeyValue>();
				list.add(valueObj);
				list.add(valueObj1);
				if(!StringUtils.isBlank(durationOffset)) {
					KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
					list.add(valueObj2);
				}
				map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
			}

		return map;

	}
	
	public static Map<KeyValue, List<KeyValue>> getIavSiteReportInfoPerSite(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String cobrandId = filter.getCobrandId();
		String include = filter.getInclude();
		String duration = filter.getDuration();
		String durationOffset = filter.getDurationOffset();
		String providerIds = filter.getProviderIds();
		String top = filter.getTop();
		logger.info("inside getSiteReportInfoPerSite " + providerIds + " include " + include + " cobrandId " + cobrandId + " duration " + duration + " duration offset" + durationOffset + " top "
				+ top);
		if (providerIds != null && (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC) || include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP)))) {
			if (duration.contains("d")) {
				String reportname = YSLConstants.YCC_IAV_SITE_HISTORIC_REPORT;
				if (include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportname = YSLConstants.YCC_CHANNEL_IAV_SITE_HISTORIC_REPORT;
				KeyValue siteValueObj = buildMapForContainerHistoric(cobrandId, duration, durationOffset, providerIds, map,reportname);
				String report = YSLConstants.YCC_IAV_SITE_OVERALL_SUMMARY_REPORT;
				if (include.contains(YSLConstants.CONSOLIDATE_STATS))
					report = YSLConstants.YCC_CHANNEL_IAV_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerSummaryWithHistoric(cobrandId, defaltHDuration, durationOffset, map, siteValueObj,report);

				return map;
			}
			else {
				String report = YSLConstants.YCC_IAV_SITE_OVERALL_SUMMARY_REPORT;
				if (include.contains(YSLConstants.CONSOLIDATE_STATS))
					report = YSLConstants.YCC_CHANNEL_IAV_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerForSummaryOnly(cobrandId, duration, durationOffset, providerIds, map,report);

				String reportName  = YSLConstants.YCC_IAV_SITE_ERRORCODE_BREAKUP_REPORT;
				if (include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportName = YSLConstants.YCC_CHANNEL_IAV_SITE_ERRORCODE_BREAKUP_REPORT;
				buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,reportName,duration);
				return map;
			}
		}
		else if (providerIds != null) {
			logger.info("not historic and no include provided but provider id is not null");
			String reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_SITE_OVERALL_SUMMARY_REPORT, false);
			if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
				reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_SITE_OVERALL_SUMMARY_REPORT, false);
			List<KeyValue> list1 = new ArrayList<KeyValue>();
			KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
			KeyValue valueObj3 = new KeyValue("duration", duration);
			list1.add(valueObj2);
			list1.add(valueObj3);

			if (!StringUtils.isBlank(durationOffset)) {
				KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
				list1.add(valueObj4);
			}
			KeyValue siteValueObj = new KeyValue("SITE_ID", providerIds);
			list1.add(siteValueObj);
			/*
			 * if (numRecords != null) { KeyValue valueObjj = new
			 * KeyValue("numRecords", numRecords); list1.add(valueObjj); }
			 */

			map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);

			// ADD error code break up
			String reportName  = YSLConstants.YCC_IAV_SITE_ERRORCODE_BREAKUP_REPORT;
			if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
				reportName = YSLConstants.YCC_CHANNEL_IAV_SITE_ERRORCODE_BREAKUP_REPORT;
			buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,reportName,duration);
			return map;
		}
		return map;
	}
	public static Map<KeyValue, List<KeyValue>> getIavSiteReportInfo(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = null;
		String top = filter.getTop();
		String duration = filter.getDuration();
		String cobrandId = filter.getCobrandId();
		String numRecords = filter.getNumRecords();
		String include = filter.getInclude();
		if (top != null && YSLConstants.SPLUNK_TOP_VOLUME.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume IAV site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_VOLUME_SITE_STATS_4H, false);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume IAV site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_VOLUME_SITE_STATS_12H, false);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_VOLUME_SITE_STATS, false);
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				logger.debug("Top Volume IAV Channel site stats");
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume IAV Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_VOLUME_SITE_STATS_4H,
							false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume IAV Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_VOLUME_SITE_STATS_12H,
							false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_VOLUME_SITE_STATS, false);
			}
		}
		else if (top != null && YSLConstants.SPLUNK_TOP_FAILURE.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure IAV site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_FAILURE_SITE_STATS_4H, false);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure IAV site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_FAILURE_SITE_STATS_12H, false);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_IAV_TOP_FAILURE_SITE_STATS, false);
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				logger.debug("Top Failure IAV Channel site stats");
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure IAV Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_FAILURE_SITE_STATS_4H,
							false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure IAV Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_FAILURE_SITE_STATS_12H,
							false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_TOP_FAILURE_SITE_STATS, false);
			}
		}
		List<KeyValue> list = new ArrayList<KeyValue>();
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		if (duration != null) {
			KeyValue valueObj1 = new KeyValue("duration", duration);
			list.add(valueObj1);
		}
		if (numRecords != null) {
			KeyValue valueObj2 = new KeyValue("numRecords", numRecords);
			list.add(valueObj2);
		}
		list.add(valueObj);

		if (reportName != null)
			map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);

		return map;
	}
	public static Map<KeyValue, List<KeyValue>> getAddRefreshReportInfo(String cobrandId, String duration,String durationOffset,String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
			String reportName = "";
			if (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC)||include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP))) {
				if (duration.contains("d")) {
					List<KeyValue> list = new ArrayList<KeyValue>();
					reportName =MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false);
					if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_HISTORIC_REFRESH_LATENCY_BREAKDOWN_STATS, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					list.add(valueObj);
					list.add(valueObj1);
					
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_HISTORIC), list);

					String reportName1 =MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_OVERALL_REFRESH_LATENCY_STATS, false);
					KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj3 = new KeyValue("duration", defaltHDuration);
					List<KeyValue> list1 = new ArrayList<KeyValue>();
					list1.add(valueObj2);
					list1.add(valueObj3);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj4);
					}
					map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);
				} else {
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
					if (include!=null && include.contains(YSLConstants.CONSOLIDATE_STATS))
						reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_OVERALL_REFRESH_LATENCY_STATS, false);
					KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
					KeyValue valueObj1 = new KeyValue("duration", duration);
					List<KeyValue> list = new ArrayList<KeyValue>();
					list.add(valueObj);
					list.add(valueObj1);
					if(!StringUtils.isBlank(durationOffset)) {
						KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
						list.add(valueObj2);
					}
					map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY_HIST), list);
				}
			} else {
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_OVERALL_REFRESH_LATENCY_STATS_NEW, false);
				if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_OVERALL_REFRESH_LATENCY_STATS, false);
				KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
				KeyValue valueObj1 = new KeyValue("duration", duration);
				List<KeyValue> list = new ArrayList<KeyValue>();
				list.add(valueObj);
				list.add(valueObj1);
				if(!StringUtils.isBlank(durationOffset)) {
					KeyValue valueObj2 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
					list.add(valueObj2);
				}
				map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
			}

		return map;

	}
	
	public static Map<KeyValue, List<KeyValue>> getAddSiteReportInfoPerSite(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String cobrandId = filter.getCobrandId();
		String include = filter.getInclude();
		String duration = filter.getDuration();
		String durationOffset = filter.getDurationOffset();
		String providerIds = filter.getProviderIds();
		String top = filter.getTop();
		logger.info("inside getSiteReportInfoPerSite " + providerIds + " include " + include + " cobrandId " + cobrandId + " duration " + duration + " duration offset" + durationOffset + " top "
				+ top);
		if (providerIds != null && (include != null && (include.contains(YSLConstants.INCLUDE_HISTORIC) || include.contains(YSLConstants.INCLUDE_LATENCY_BREAKUP)))) {
			if (duration.contains("d")) {
				String reportname = YSLConstants.YCC_ADD_SITE_HISTORIC_REPORT;
				if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportname = YSLConstants.YCC_CHANNEL_ADD_SITE_HISTORIC_REPORT;
				KeyValue siteValueObj = buildMapForContainerHistoric(cobrandId, duration, durationOffset, providerIds, map,reportname);
				String report = YSLConstants.YCC_ADD_SITE_OVERALL_SUMMARY_REPORT;
				if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportname = YSLConstants.YCC_CHANNEL_ADD_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerSummaryWithHistoric(cobrandId, defaltHDuration, durationOffset, map, siteValueObj,report);

				return map;
			}
			else {
				String report = YSLConstants.YCC_ADD_SITE_OVERALL_SUMMARY_REPORT;
				if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					report = YSLConstants.YCC_CHANNEL_ADD_SITE_OVERALL_SUMMARY_REPORT;
				buildMapForContainerForSummaryOnly(cobrandId, duration, durationOffset, providerIds, map,report);

				String reportName  = YSLConstants.YCC_ADD_SITE_ERRORCODE_BREAKUP_REPORT;
				if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
					reportName = YSLConstants.YCC_CHANNEL_ADD_SITE_ERRORCODE_BREAKUP_REPORT;
				buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,reportName,duration);
				return map;
			}
		}
		else if (providerIds != null) {
			logger.info("not historic and no include provided but provider id is not null");
			String reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_SITE_OVERALL_SUMMARY_REPORT, false);
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
				reportName1 = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_SITE_OVERALL_SUMMARY_REPORT, false);
			List<KeyValue> list1 = new ArrayList<KeyValue>();
			KeyValue valueObj2 = new KeyValue("cobrandId", cobrandId);
			KeyValue valueObj3 = new KeyValue("duration", duration);
			list1.add(valueObj2);
			list1.add(valueObj3);

			if (!StringUtils.isBlank(durationOffset)) {
				KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
				list1.add(valueObj4);
			}
			KeyValue siteValueObj = new KeyValue("SITE_ID", providerIds);
			list1.add(siteValueObj);
			/*
			 * if (numRecords != null) { KeyValue valueObjj = new
			 * KeyValue("numRecords", numRecords); list1.add(valueObjj); }
			 */

			map.put(new KeyValue(reportName1, YSLConstants.SPLUNK_SUMMARY), list1);

			// ADD error code break up
			String reportName  = YSLConstants.YCC_ADD_SITE_ERRORCODE_BREAKUP_REPORT;
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS))
				reportName = YSLConstants.YCC_CHANNEL_ADD_SITE_ERRORCODE_BREAKUP_REPORT;
			buildMapForErrorCodeBreakUp(cobrandId, include, providerIds, map,reportName,duration);
			return map;
		}
		return map;
	}
	public static Map<KeyValue, List<KeyValue>> getAddSiteReportInfo(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = null;
		String top = filter.getTop();
		String duration = filter.getDuration();
		String cobrandId = filter.getCobrandId();
		String numRecords = filter.getNumRecords();
		String include=filter.getInclude();
		if (top != null && YSLConstants.SPLUNK_TOP_VOLUME.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume Add site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_VOLUME_SITE_STATS_4H, false);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Volume Add site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_VOLUME_SITE_STATS_12H, false);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_VOLUME_SITE_STATS, false);
			
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume Add Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_VOLUME_SITE_STATS_4H,
							false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Volume Add Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_VOLUME_SITE_STATS_12H,
							false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_VOLUME_SITE_STATS, false);
			}
		}
		else if (top != null && YSLConstants.SPLUNK_TOP_FAILURE.equalsIgnoreCase(top)) {
			if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure Add site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_FAILURE_SITE_STATS_4H, false);
			} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
				logger.debug("Top Failure Add site stats for duration-"+duration);
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_FAILURE_SITE_STATS_12H, false);
			} else
				reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_TOP_FAILURE_SITE_STATS, false);
			
			if (include != null && include.contains(YSLConstants.CONSOLIDATE_STATS)) {
				if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure Add Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_FAILURE_SITE_STATS_4H,
							false);
				} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
					logger.debug("Top Failure Add Channel site stats for duration-"+duration);
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_FAILURE_SITE_STATS_12H,
							false);
				} else
					reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ADD_TOP_FAILURE_SITE_STATS, false);
			}

		}
		List<KeyValue> list = new ArrayList<KeyValue>();
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		if (duration != null) {
			KeyValue valueObj1 = new KeyValue("duration", duration);
			list.add(valueObj1);
		}
		if (numRecords != null) {
			KeyValue valueObj2 = new KeyValue("numRecords", numRecords);
			list.add(valueObj2);
		}
		list.add(valueObj);

		if (reportName != null)
			map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);

		return map;
	}


	public static Map<KeyValue, List<KeyValue>> getRefreshErrorCodeReportInfo(String cobrandId, String duration, String durationOffset, String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		List<KeyValue> list = new ArrayList<KeyValue>();
		reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_COBRAND_ERRORCODE_BREAKUP_REPORT, false);
		if (include.contains(YSLConstants.CONSOLIDATE_STATS))
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_ERRORCODE_BREAKUP_REPORT, false);
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj1 = new KeyValue("duration", duration);
		list.add(valueObj);
		list.add(valueObj1);
		if (!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list.add(valueObj4);
		}
		map.put(new KeyValue(reportName, YSLConstants.ERRORCODE_OVERALL), list);
		return map;
	}

	public static Map<KeyValue, List<KeyValue>> getIavRefreshErrorCodeReportInfo(String cobrandId, String duration, String durationOffset, String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		List<KeyValue> list = new ArrayList<KeyValue>();
		reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_COBRAND_IAV_ERRORCODE_BREAKUP_REPORT, false);
		if (include.contains(YSLConstants.CONSOLIDATE_STATS))
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_CHANNEL_IAV_ERRORCODE_BREAKUP_REPORT, false);
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj1 = new KeyValue("duration", duration);
		list.add(valueObj);
		list.add(valueObj1);
		if (!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list.add(valueObj4);
		}
		map.put(new KeyValue(reportName, YSLConstants.ERRORCODE_OVERALL), list);
		return map;
	}

	public static LinkedHashMap<KeyValue, List<KeyValue>> getNetworkStatsReportInfo(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		List<KeyValue> list = new ArrayList<KeyValue>();
		String duration=filter.getDuration();
		if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_SITE_STATS_4H, false);
		} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_SITE_STATS_12H, false);
		} else
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_SITE_STATS, false);
		KeyValue valueObj = new KeyValue("cobrandId", YSLConstants.ALL_COBRANDS);
		list.add(valueObj);
		map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
		return map;
		
	}
	public static Map<KeyValue, List<KeyValue>> getAddErrorCodeReportInfo(String cobrandId, String duration, String durationOffset, String include) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		List<KeyValue> list = new ArrayList<KeyValue>();
		reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_COBRAND_ADD_ERRORCODE_BREAKUP_REPORT, false);
		if (include.contains(YSLConstants.CONSOLIDATE_STATS))
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_ADD_CHANNEL_ERRORCODE_BREAKUP_REPORT, false);
		KeyValue valueObj = new KeyValue("cobrandId", cobrandId);
		KeyValue valueObj1 = new KeyValue("duration", duration);
		list.add(valueObj);
		list.add(valueObj1);
		if (!StringUtils.isBlank(durationOffset)) {
			KeyValue valueObj4 = new KeyValue(YSLConstants.DURATION_OFFSET, durationOffset);
			list.add(valueObj4);
		}
		map.put(new KeyValue(reportName, YSLConstants.ERRORCODE_OVERALL), list);
		return map;
	}

	public static Map<KeyValue, List<KeyValue>> getAddNetworkStatsReportInfo(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		String duration=filter.getDuration();
		List<KeyValue> list = new ArrayList<KeyValue>();
		if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_ADD_SITE_STATS_4H, false);
		} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_ADD_SITE_STATS_12H, false);
		} else
		reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_ADD_SITE_STATS, false);
		KeyValue valueObj = new KeyValue("cobrandId", YSLConstants.ALL_COBRANDS);
		list.add(valueObj);
		map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
		return map;
	}
	public static Map<KeyValue, List<KeyValue>> getIavNetworkStatsReportInfo(RefreshStatsFilter filter) {
		LinkedHashMap<KeyValue, List<KeyValue>> map = new LinkedHashMap<KeyValue, List<KeyValue>>();
		String reportName = "";
		String duration=filter.getDuration();
		List<KeyValue> list = new ArrayList<KeyValue>();
		if (duration != null && YSLConstants.TOP_DURATION_4H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_IAV_SITE_STATS_4H, false);
		} else if (duration != null && YSLConstants.TOP_DURATION_12H.equalsIgnoreCase(duration)) {
			reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_IAV_SITE_STATS_12H, false);
		} else
		reportName = MiscUtil.getPropertyValue(YSLConstants.YCC_NETWORK_IAV_SITE_STATS, false);
		KeyValue valueObj = new KeyValue("cobrandId", YSLConstants.ALL_COBRANDS);
		list.add(valueObj);
		map.put(new KeyValue(reportName, YSLConstants.SPLUNK_SUMMARY), list);
		return map;
	}
}
