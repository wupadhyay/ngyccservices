/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.elasticsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.ycc.dapi.utils.MiscUtil;


/**
 * The Class ElasticQueryExecutor.
 *
 * @author apunekar
 */
public class ElasticQueryExecutor {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ElasticQueryExecutor.class);
	/** The es base URL key. */
	private static String esBaseURLKey = "com.yodlee.elasticsearch.baseurl";
	
	/** The es end point key. */
	private static String esEndPointKey = "com.yodlee.elasticsearch.endpoint";
	
	private static String esConnectionTimeOutKey="com.yodlee.elasticsearch.connectiontimeout";
	
	
	/**
	 * Post.
	 *
	 * @param query the query
	 * @return the sets the
	 * @throws IOException 
	 * @throws Exception the exception
	 */
	public static Set<Long> executeQuery(String query) throws IOException,MalformedURLException
	{
		Long startTime = System.currentTimeMillis();
		LOGGER.info("--ElasticQueryExceutor StartTime-- "+startTime);
		LOGGER.debug("--Inside executeQuery ElasticQueryExecutor---");

		StringBuffer strLine = new StringBuffer();
		String esBaseURL = MiscUtil.getPropertyValue(esBaseURLKey, false);
		String esEndPoint = MiscUtil.getPropertyValue(esEndPointKey, false);
		int connectionTimeOut = Integer.parseInt(MiscUtil.getPropertyValue(esConnectionTimeOutKey, false));
		URL url = new URL(esBaseURL+esEndPoint);
		try
		{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(connectionTimeOut);
		connection.setRequestProperty("Content-Type",MediaType.APPLICATION_JSON.toString());
		if (query != null && !"".equalsIgnoreCase(query.trim())) {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true);
			pw.print(query);
			pw.flush();
			pw.close();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String output;
		while ((output = br.readLine()) != null) {
			strLine.append(output);
		}
		
		connection.disconnect();
		Set<Long> siteIds = parseResponse(strLine.toString());
		LOGGER.info("--End Time in ElasticQueryExecutor-- "+String.valueOf(System.currentTimeMillis()-startTime));
		LOGGER.debug("--Leaving executeQuery ElasticQueryExecutor---");

		return siteIds;
		}
		catch(Exception e)
		{
			LOGGER.info("--Failed in ElasticQueryExecutor--");
			LOGGER.error("--Exception in ElasticQueryExecutor failed with : " +ExceptionUtils.getFullStackTrace(e));
			return null;
		}
	}
	
	/**
	 * Parses the response.
	 *
	 * @param response the response
	 * @return the sets the
	 */
	private static Set<Long> parseResponse(String response)
	{
		LOGGER.debug("--Inside Parse response ElasticQueryExecutor---");
		Set<Long> siteIds = new LinkedHashSet<>();
		JsonParser jsonParser = new JsonParser();
		JsonObject responseData = (JsonObject) jsonParser.parse(response);
		JsonObject obj = responseData.getAsJsonObject("hits");
		JsonArray hitsArray = obj.getAsJsonArray("hits");
		for(JsonElement jsonEle : hitsArray)
		{
			JsonObject obj1 = jsonEle.getAsJsonObject();
			obj1 = obj1.getAsJsonObject("_source");
			String siteId = obj1.get("site_id").getAsString();
			siteIds.add(Long.parseLong(siteId));
			
		}
		LOGGER.debug("--Leaving Parse response ElasticQueryExecutor---");
		return siteIds;
	}
	
}
