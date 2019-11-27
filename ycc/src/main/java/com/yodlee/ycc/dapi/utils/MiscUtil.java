/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.io.BufferedReader;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.yodlee.nextgen.jdbc.DBPoolInitInfo;

/**
 * 
 * @author knavuluri
 * 
 */
public class MiscUtil {

	private static final Logger logger = LoggerFactory.getLogger(MiscUtil.class);

	private static Properties configProp = null;
	
	private static Hashtable<String, String> queries = null;


	static {
		configProp = loadProperties("com/yodlee/config/Config.properties");
		Properties props = loadProperties("CobrandableParams.properties");
		configProp.putAll(props);
		Properties props1 = loadProperties("SplunkReports.properties");
		configProp.putAll(props1);
		Properties props2 = loadProperties("ErrorCodeDescMapping.properties");
		configProp.putAll(props2);
		Properties props3 = loadProperties("notificationconfig.properties");
		configProp.putAll(props3);
		loadQueries("queries.json");
		ReconDataAccessor.loadCobrandInfo(); // Loading Cobrand Info for Account360
		
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

	public static String getPropertyValue(String key, boolean isdecrypt) {
		String value = null;
		value = configProp.getProperty(key);
		if (isdecrypt) {
			if (value != null && value.trim().length() > 0) {
				if (value.length() > 60) {
					try {
						value = DBPoolInitInfo.decrypt(value);
					}
					catch (Exception e) {
						logger.error("Decyrpt properties:" + ExceptionUtils.getFullStackTrace(e));
					}
				}
			}
		}

		return value;
	}
	
	
	private static void loadQueries(final String path){

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final InputStream inputStream = classLoader.getResourceAsStream(path);
		final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;

		final StringBuilder responseData = new StringBuilder();
		try {
			while((line = in.readLine()) != null) {
				responseData.append(line);
			}
		} catch (final IOException e) {
			logger.error("loading queries from json failed...");
			e.printStackTrace();
			return;
		}

		queries = new Hashtable<String, String>();
		JsonElement jsonElement = new JsonParser().parse(responseData.toString());
		JsonElement notificationElement = jsonElement.getAsJsonObject().get("queries");
		JsonArray notificationArray = notificationElement.getAsJsonArray();
		int size = notificationArray.size();
		for (int i = 0; i < size; i++) {
			JsonObject jsonObject = notificationArray.get(i).getAsJsonObject();
			String name = jsonObject.get("name").getAsString();
			String query = jsonObject.get("query").getAsString();
			queries.put(name, query);
		}
	}

	public static String getQuery(String name) {
		if(name == null)
			return null;
		return queries.get(name);
	}

}
