/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.jobstatus.JobStatus;
import com.yodlee.ycc.stats.db.util.RefreshStatDbUtil;

/**
 * 
 * @author knavuluri
 * 
 */
@Service
public class RefreshStatsDbService {
	private static final Logger logger = LoggerFactory.getLogger(RefreshStatsDbService.class);
	private static Set<String> timeStampExcludeList = new HashSet<String>();
	private static Map<String, String> reportCollectionMap = new HashMap<String, String>();
	private static Map<String, String> filterColumnMap = new HashMap<String, String>();
	private static Map<String, String> transformationMap = new HashMap<String, String>();
	private static Set<String> excludeElementsList = new HashSet<String>();
	@Autowired
	@Qualifier("yccRefreshStatTemplate")
	MongoTemplate template;
	
	static {
		logger.info("loading the mongo properties");
		loadProperties();
		logger.info("loaded the mongo properties");
	}

	public String getRefreshStatsData(RefreshStatsDbFilter filter) {
		logger.debug("Invoking the getRefreshStatsData :" + filter);
		String stats = null;
		try {
			JobStatus jobStatus = new RefreshStatDbUtil().getJobDetails(reportCollectionMap.get(filter.getReportType().trim()), template);
			if (jobStatus != null) {
				filter.setJobId(jobStatus.getJob_id());
				filter.setCollectionName(jobStatus.getCollection_name());
				BasicDBObject buildQuery = buildQuery(filter);
				stats = executeQuery(buildQuery, filter);
			}
		}
		catch (Exception e) {
			logger.error("Exception while querying the data for " + filter.getReportType() + ":" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Completed invoking the getRefreshStatsData :" + filter);
		return stats;
	}

	public BasicDBObject buildQuery(RefreshStatsDbFilter filter) throws Exception {
		logger.debug("Invoking the buildQuery :" + filter);
		BasicDBObject queryObj = new BasicDBObject();;
		Set<Entry<String, String>> entrySet = filterColumnMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String name = entry.getKey();
			if ("duration".equalsIgnoreCase(name))
				continue;
			Field field = FieldUtils.getField(filter.getClass(), name, true);
			Object value = field.get(filter);
			if (value != null) {
				if(value instanceof Collection)
					queryObj.append(filterColumnMap.get(name),new BasicDBObject("$in", value));
				else
					queryObj.append(filterColumnMap.get(name), value);
			}
		}
		Field field = FieldUtils.getField(filter.getClass(), "duration", true);
		String duration = (String) field.get(filter);
		Field field1 = FieldUtils.getField(filter.getClass(), "durationOffSet", true);
		String durationOffSet = (String) field1.get(filter);
		if (duration != null && durationOffSet != null) {
			Date durationDate = RefreshStatDbUtil.getDurationDate(duration, durationOffSet);
			Date durationOffSetDate = RefreshStatDbUtil.getDurationDate(durationOffSet, null);
			queryObj.append(filterColumnMap.get("duration"), new BasicDBObject("$gte", durationDate).append("$lte", durationOffSetDate));
		}
		else if (duration != null) {
			Date durationDate = RefreshStatDbUtil.getDurationDate(duration, null);
			queryObj.append(filterColumnMap.get("duration"), new BasicDBObject("$gte", durationDate));
		}
		logger.debug("Completed invoking the buildQuery :" + filter + " query:" + queryObj);
		return queryObj;
	}

	private String executeQuery(BasicDBObject buildQuery, RefreshStatsDbFilter filter) throws Exception {
		logger.debug("Invoking the executeQuery :" + filter);
		String response = null;
		DB db = template.getDb();

		String collectionName = filter.getCollectionName();
		StringBuilder builder = new StringBuilder("{\"results\":[");
		DBCollection collection = db.getCollection(collectionName);
		boolean addSortTimeStamp = addSortTimeStamp(filter);
		DBCursor cursor2 = collection.find(buildQuery);
		if (addSortTimeStamp)
			cursor2.sort(new BasicDBObject("timestamp", -1));
		else if (filter.getNumRecords() != null)
			cursor2.limit(filter.getNumRecords().intValue());
		int count = cursor2.size();
		logger.debug("Filter:"+filter+" :num records:"+count);
		int i = 0;
		while (cursor2.hasNext()) {
			DBObject obj = cursor2.next();
			RefreshStatDbUtil utilObj = new RefreshStatDbUtil();
			Set<Entry<String, String>> entrySet = transformationMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				Object resValue = obj.get(key);
				if (resValue != null) {
					Method method = utilObj.getClass().getDeclaredMethod(value, Date.class, String.class);
					method.setAccessible(true);
					Object invokeMethod = method.invoke(utilObj, resValue, filter.getDuration());
					// Object invokeMethod = MethodUtils.invokeMethod(utilObj,
					// true,value, new Object[] { resValue });
					obj.put(key, invokeMethod);
				}
			}
			for (String element : excludeElementsList) {
				obj.removeField(element);
			}

			builder.append(obj.toString());
			i++;
			if (i >= 1 && i < count)
				builder.append(",");
		}
		builder.append("]}");
		response = builder.toString();
		logger.debug("Completed invoking the executeQuery :" + filter);
		logger.debug("Mongo final response for " + filter + " res:" + response);
		return response;
	}

	private boolean addSortTimeStamp(RefreshStatsDbFilter filter) {
		boolean addTimestamp = true;
		if (timeStampExcludeList.contains(filter.getReportType()))
			addTimestamp = false;
		return addTimestamp;

	}

	private static void loadProperties() {
		Properties prop = RefreshStatDbUtil.loadProperties("mongoreport.properties");
		String exclude = prop.getProperty("com.yodlee.timestamp.exclude.reports");
		List<String> excludeList = Arrays.asList(exclude.split("\\s*(,\\s*)+"));
		timeStampExcludeList.addAll(excludeList);
		String excludeCloumns = prop.getProperty("com.yodlee.column.exclude");
		List<String> excludeColumnList = Arrays.asList(excludeCloumns.split("\\s*(,\\s*)+"));
		excludeElementsList.addAll(excludeColumnList);

		String trnsformation = prop.getProperty("com.yodlee.transformations");
		String[] trnArr = trnsformation.split("\\s*(,\\s*)+");
		for (String trn : trnArr) {
			String[] split = trn.split("=");
			transformationMap.put(split[0], split[1]);
		}
		String columMap = prop.getProperty("com.yodlee.filter.columnmap");
		String[] colArr = columMap.split("\\s*(,\\s*)+");
		for (String col : colArr) {
			String[] split = col.split("=");
			filterColumnMap.put(split[0], split[1]);
		}
		Set<Entry<Object, Object>> entrySet = prop.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = (String) entry.getKey();
			if (key.contains("com.yodlee.report.jobmap")) {
				String[] split = key.split("com.yodlee.report.jobmap.");
				reportCollectionMap.put(split[1], (String) entry.getValue());
			}
		}
	}
	
}
