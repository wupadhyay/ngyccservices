/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yodlee.ycc.stats.db.jobstatus.JobStatus;

/**
 * 
 * @author knavuluri
 * 
 */
public class RefreshStatDbUtil {
	private static final Logger logger = LoggerFactory.getLogger(RefreshStatDbUtil.class);

	public Long getJobId(String reportName, MongoTemplate template) {
		Long jobId = null;
		DB db = template.getDb();
		DBCollection refColl = db.getCollection("job_status");
		DBObject query = new BasicDBObject("job_status", "COMPLETED").append("job_name", reportName);
		DBCursor cursor = refColl.find(query).sort(new BasicDBObject("job_id", -1)).limit(1);
		while (cursor.hasNext()) {
			DBObject next = cursor.next();
			Object jobObj = next.get("job_id");
			jobId = Long.valueOf(jobObj.toString());
			logger.debug("Job Id:" + Long.valueOf(jobId.toString()));
		}
		return jobId;
	}
	public JobStatus getJobDetails(String reportName, MongoTemplate template) {
		JobStatus status = null;
		DB db = template.getDb();
		DBCollection refColl = db.getCollection("job_status");
		DBObject query = new BasicDBObject("job_status", "COMPLETED").append("job_name", reportName);
		DBCursor cursor = refColl.find(query).sort(new BasicDBObject("job_id", -1)).limit(1);
		while (cursor.hasNext()) {
			DBObject next = cursor.next();
			Object jobObj = next.get("job_id");
			Object collectionName = next.get("collection_name");
			Long jobId = Long.valueOf(jobObj.toString());
			status=new JobStatus();
			status.setJob_id(Long.valueOf(jobObj.toString()));
			status.setCollection_name(collectionName.toString());
			logger.debug("Job Id:" + jobId +" collectionName:"+collectionName.toString());
		}
		return status;
	}

	public static Date getDurationDate(String duration, String durationOffSet) {
		if (duration == null && durationOffSet == null) {
			return null;
		}
		int time = 0;
		if (durationOffSet != null) {
			String[] splitDuration = durationOffSet.split("[d|h]");
			String durationPart = splitDuration[0];
			time = Integer.parseInt(durationPart);
		}
		if (duration != null) {
			String[] splitDuration = duration.split("[d|h]");
			String durationPart = splitDuration[0];
			time = time + Integer.parseInt(durationPart);
		}
		Date today = new Date();
		Calendar cal = GregorianCalendar.getInstance();
		cal.clear();
		cal.setTime(today);

		if (duration.contains("h")) {
			cal.add(Calendar.HOUR, -(Integer.valueOf(time) + 1));
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

		}
		if (duration.contains("d")) {
			cal.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(time));
			cal.add(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		}
		Date dt = getDate(cal.getTime(), duration);
		logger.debug("Filter Date Time:" + dt);
		return dt;

	}

	public static Date getDate(Date date, String identifier) {
		DateTime dtus = new DateTime(date.getTime());
		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		if (identifier.equalsIgnoreCase("d"))
			formater = DateTimeFormat.forPattern("yyyy-MM-dd");
		else if (identifier.equalsIgnoreCase("h"))
			formater = DateTimeFormat.forPattern("yyyy-MM-dd HH");
		formater.withZoneUTC();
		DateTime time = formater.parseDateTime(formater.print(dtus));
		return time.toDate();

	}

	public static String processResponse(String response) {
		JsonElement jsonElement = new JsonParser().parse(response);
		JsonElement providerElement = jsonElement.getAsJsonObject().get("results");
		JsonArray providerArray = providerElement.getAsJsonArray();
		JsonArray nArray = new JsonArray();
		JsonObject nElement = new JsonObject();
		int size = providerArray.size();
		for (int i = 0; i < size; i++) {
			JsonElement curElement = providerArray.get(i);
			JsonObject currObj = curElement.getAsJsonObject();
			JsonElement nameEl = currObj.get("timestamp");
			if (nameEl != null) {
				currObj.remove("timestamp");
			}
			nArray.add(curElement);

		}
		nElement.add("results", nArray);
		String json = nElement.toString();
		json = json.replaceAll("timeStampNew", "timestamp");
		return json;
	}

	public String formatResponseDate(Date date,String type) {
		String dateStr = null;
		if (date != null && type.contains("h")) {
			dateStr = new SimpleDateFormat("yyyy/MM/dd HH").format(date);
		}else if (date != null && type.contains("d")){
			dateStr = new SimpleDateFormat("yyyy/MM/dd").format(date);
		}
		return dateStr;
	}
	public static Properties loadProperties(String propPath) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(propPath);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		}
		catch (Exception ex) {
			logger.error("File:" + propPath + "Loading proerties:" + ExceptionUtils.getFullStackTrace(ex));
		}
		finally {
			try {
				inputStream.close();
			}
			catch (IOException ex) {
				logger.error("Loading proerties:" + ExceptionUtils.getFullStackTrace(ex));
			}
		}
		return p;
	}

}
