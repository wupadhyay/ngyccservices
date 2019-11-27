/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.extentions;

import java.util.List;

import com.yodlee.dom.entity.DbMessageCatalog;
import com.yodlee.dom.entity.DbMessageCatalog_;
import com.yodlee.dom.entity.Site;
import com.yodlee.dom.entity.Tag;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.ycc.dapi.utils.JsonDataHandler;

public class SupportedContainerStatsExtn {
	private static final String SUM_INFO_BUNDLE = "db.display_name.sum_info";
    private static final String PREFIX = "com.yodlee";
	
	public String getSupportedContainerExt(JsonDataHandler handler,String type,String jsonpath)
	{
		if (type.equalsIgnoreCase("PROVIDER"))
			type = "SUM_INFO";
		
		if (jsonpath.startsWith("container")) {
			return (String) handler.getValues("$.*.*.results[*].CATEGORY").get(0);
		}
		if (jsonpath.equalsIgnoreCase("totalVolumeExtn")) {
			 List<String> 	totalrefreshes=handler.getValues("$.*.*.results[*].TOTAL_REFRESHES");
		    	return getTotalVolumeExt(totalrefreshes);
		}
		if (jsonpath.startsWith("id")) {
			return (String) handler.getValues("$.*.*.results[*].SUM_INFO_ID").get(0);
		}
		if (jsonpath.startsWith("name")) {
			if (type.equalsIgnoreCase("SUM_INFO")) {
				Long siteId =Long.valueOf((String) handler.getValues("$.*.*.results[0].SITE_ID").get(0));
				Long sumInfoId =Long.valueOf((String) handler.getValues("$.*.*.results[0].SUM_INFO_ID").get(0));

				return getSiteName(siteId,sumInfoId);
			}
			return handler.getValue("$.results[0]." + type + "_NAME");
			
		}
		return "0.0";
	
	}
	
	public String getSiteName(Long siteId,Long sumInfoId) {
		if(CommonExtn.getDummyData())
			return "";
		Site site = Site.DAO.get(siteId);
		String name = null;
		if (site != null && site.getPrimaryLocaleId() != null && sumInfoId !=null) {
			Long localeId = site.getPrimaryLocaleId();
			String key = new StringBuilder(PREFIX).append(".").append(DBMCConstants.SUM_INFO_BUNDLE_NAME).append(".").append(sumInfoId).toString();
			name = getDbCatalogName(key, localeId);
			if (name == null) {
				key = new StringBuilder(PREFIX).append(".").append(DBMCConstants.SUM_INFO_BUNDLE_NAME).append(".").append(sumInfoId).toString();
				name = getDbCatalogName(key, localeId);
			}
		}
		if (name == null)
			name = site.getName();
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
 
 public String	getTotalVolumeExt(List<String> totalrefreshes)
	{
	  long sumOfTotalRefreshes=0;
	  for(String str:totalrefreshes){
 	              sumOfTotalRefreshes=sumOfTotalRefreshes+Long.parseLong(str);
              }
	return String.valueOf(sumOfTotalRefreshes);
   
	}
 
 	public String getContainerName(String tagId){
		if (CommonExtn.getDummyData() || tagId == null)
			return "";
 		try {
 		Tag tag = Tag.DAO.get(Long.valueOf(tagId));
 		return tag.getTag();
 		} catch (Exception e) {
 			return "";
 		}
 		
 	}

 
}


