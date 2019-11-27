/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.yodlee.dom.entity.DbMessageCatalog;
import com.yodlee.dom.entity.DbMessageCatalog_;
import com.yodlee.dom.entity.Site;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.framework.runtime.util.SimpleTypeUtil;
import com.yodlee.ycc.dapi.context.ApplicationContextProvider;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.ycc.dapi.utils.DateUtil;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;
import com.yodlee.yccdom.entity.FiRepository;

public class CommonExtn {
	private static final Logger logger = LoggerFactory.getLogger(CommonExtn.class);
	private static final String PREFIX = "com.yodlee";
	public String getCommonExt(JsonDataHandler handler, String jsonpath, String type, int historicDetailsCount,boolean containerStats) {
		String jpathdata="";
		if(containerStats){
		String str = jsonpath.substring("$.results[0].".length());
		 jpathdata = "$.input["+ historicDetailsCount + "].results[0]." + str;
		}else{
			String str = jsonpath.substring("$.results[0].".length());
			jpathdata = "$.results[" + historicDetailsCount + "]." + str;
		}
		return handler.getValue(jpathdata);
	}

	public String getCommonExt(JsonDataHandler handler, String jsonpath, String type,boolean includeContainerflag) {
		if(!includeContainerflag){
			if (type.equalsIgnoreCase("PROVIDER"))
				type = "SITE";

			if (jsonpath.startsWith("id")) {
				return handler.getValue("$.results[0]." + type + "_ID");
			}
			if (jsonpath.startsWith("lastModified")){
				return convertEpocToDate(handler.getValue("$.results[0].TIMESTAMP_OF_INSERT"));
			}
			
			if (jsonpath.startsWith("name")) {
				if (type.equalsIgnoreCase("SITE")) {
					Long siteId = Long.valueOf(handler.getValue("$.results[0]." + type + "_ID"));
					return getSiteName(siteId);
				}
				//String cobId=handler.getValue("$.results[0]." + type + "_ID");
				 return handler.getValue("$.results[0]." + type + "_NAME");
			}
			 return "1.0";
		}
		
		if (type.equalsIgnoreCase("PROVIDER"))
			type = "SITE";
		
		logger.debug("json path is "+jsonpath);
		if (jsonpath.startsWith("id")) {
			return handler.getValue("$.input[0]results[0]." + type + "_ID");
			
		}
		if (jsonpath.startsWith("lastModified")){
			return convertEpocToDate(handler.getValue("$.input[0]results[0].TIMESTAMP_OF_INSERT"));
		}
		if (jsonpath.startsWith("name")) {
			if (type.equalsIgnoreCase("SITE")) {
				//Site site = Site.DAO.get(Long.valueOf(handler.getValue("$.input[0].results[0]." + type + "_ID")));
				Long siteId =Long.valueOf(handler.getValue("$.input[0].results[0]." + type + "_ID"));
				return getSiteName(siteId);
			}
			return handler.getValue("$.input[0].results[0]." + type + "_NAME");
			
		}

		return "1.0";
	}

	

	public String convertEpocToDate(String value) {
		long epoch = Long.parseLong(value + "000");
		Date date = new Date(epoch);
		String dateString = DateUtil.convertDateToString(date);
		logger.debug("epoch to date is " + dateString);
		return dateString;
	}

	public String getSiteName(Long siteId) {
		if (getDummyData())
			return "";
		String name = null;
		Site site = Site.DAO.get(siteId);
		if (site != null && site.getPrimaryLocaleId() != null) {
			Long localeId = site.getPrimaryLocaleId();
			String key = new StringBuilder(PREFIX).append(".").append(DBMCConstants.SITE_NEW_BUNDLE_NAME).append(".").append(site.getSiteId()).toString();
			name = getDbCatalogName(key, localeId);
			if (name == null) {
				key = new StringBuilder(PREFIX).append(".").append(DBMCConstants.SITE_BUNDLE_NAME).append(".").append(site.getSiteId()).toString();
				name = getDbCatalogName(key, localeId);
			}
			if (name == null)
				name = site.getName();
		}
		return name == null ? "" : name;
	}

	private String getDbCatalogName(String key, Long localeId) {
		List<DbMessageCatalog> dbmc = DbMessageCatalog.DAO.get(DbMessageCatalog_.mcKey, key, DbMessageCatalog_.localeId, localeId);
		String value = null;
		if (dbmc != null && dbmc.size() > 0) {
			value = dbmc.get(0).getValue();
		}
		return value;
	}
	
	public String getCobrandName(String cobrandId) {
		if (getDummyData())
			return "";
		return CobrandUtil.getCobrandName(Long.valueOf(cobrandId));
	}
	public static boolean getDummyData() {
		Environment env = ApplicationContextProvider.getContext().getEnvironment();
		String[] activeProfiles = env.getActiveProfiles();
		if(activeProfiles != null && Arrays.asList(activeProfiles).contains("testng")) {
			return true;
		} 
		return false;
	}
}
