/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.dom.entity.DbMessageCatalog;
import com.yodlee.dom.entity.DbMessageCatalog_;
import com.yodlee.dom.entity.Site;
import com.yodlee.domx.storage.SearchSiteLocalStorage;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.mybatis.bean.GlobalMessageFilter;
import com.yodlee.ycc.dapi.mybatis.service.SiteNotificationService;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.ycc.dapi.utils.MiscUtil;

/**
 * 
 * @author knavuluri
 * 
 */
@Controller
@RequestMapping("/v1/notification/provider")
public class SiteNotificationController extends MasterController {

	private static final Logger logger = LoggerFactory.getLogger(SiteNotificationController.class);
	private static final String PREFIX = "com.yodlee";

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String search(@RequestParam(value = "cobrandId", required = false) String cobrandId,
			@RequestParam(value = "providerIds", required = false) String providerIds, @RequestParam(value = "statuses", required = false) String statuses)
			throws ApiException {
		logger.debug("Invoking Notification search API");
		Object[] obj = new Object[] { cobrandId, providerIds, statuses };
		logger.debug("Input parameters:cobrandId={},providerIds={},statuses={}", obj);
		Date sDate = new Date();
		String cobId = (String) request.getAttribute("cobId");
		logger.info("Cobrand Id from request in SiteNotificationController-"+cobId);
		if (StringUtils.isEmpty(cobrandId)) {
			cobrandId = cobId;
		} else {
			if (!StringUtil.isNumber(cobrandId)) {
				throw new RefreshStatException("CobrandId is invalid");
			} else {
				CobrandUtil.validateCobrand(Long.valueOf(cobId), Long.valueOf(cobrandId));
			}
		}
		boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobId));
		Long dataCobrandId = Long.valueOf(cobrandId);
		CobrandInfo cobrandInfoVO = CobrandUtil.getCobrandInfoVO(dataCobrandId);
		if (cobrandInfoVO != null && cobrandInfoVO.getChannelId() != null && cobrandInfoVO.getChannelId().longValue() > 0l)
			dataCobrandId = cobrandInfoVO.getChannelId();
		String notificationResponse = null;

		try {
			String mybatis = MiscUtil.getPropertyValue("com.yodlee.ycc.mybatis", false);
			logger.info("Going to my batis flow:" + mybatis);
			if (mybatis != null && mybatis.equalsIgnoreCase("true")) {
				GlobalMessageFilter filter = new GlobalMessageFilter();
				filter.setCobrandId(dataCobrandId);
				filter.setYodlee(yodlee);
				if (providerIds != null) {
					List<Long> siteIds = StringUtil.getListFromCommaSeparatedValues(providerIds);
					filter.setSiteIds(siteIds);
				}
				if (statuses != null) {
					List<Long> satusList = StringUtil.getListFromCommaSeparatedValues(statuses);
					filter.setStatusIds(satusList);
				}
				ContextAccessorUtil.setContext(Long.valueOf(cobrandId), 0l, null);
				Connection connection = PersistenceContext.getInstance().getConnection("yccdom");
				notificationResponse = SiteNotificationService.getNotifications(filter, connection);
			} else {
				StringBuilder whereQueryBuilder = new StringBuilder();

				StringBuilder impactCobrandBuilder = new StringBuilder();

				StringBuilder updatesBuilder = new StringBuilder();

				impactCobrandBuilder.append(
						"notification INCLUDE notification id classification issueType impact category status title description issueStartDate expectedResolutionTime lastUpdated impactedProvider cobGlobalMessage as impactedCobrand (cobrandable all cobrandId name)");
				boolean includeAnd = false;
				if (providerIds != null || statuses != null)
					whereQueryBuilder.append(" where id gt 0 and ");
				if (providerIds != null) {
					List<Long> siteIds = StringUtil.getListFromCommaSeparatedValues(providerIds);
					int i = 0;
					whereQueryBuilder.append(" (");
					for (Long siteId : siteIds) {
						whereQueryBuilder.append("siteId eq " + siteId);
						i++;
						if (i < siteIds.size())
							whereQueryBuilder.append(" or ");
					}
					whereQueryBuilder.append(")");
					includeAnd = true;
				}
				if (statuses != null) {
					if (includeAnd)
						whereQueryBuilder.append(" and ");
					List<Long> satusList = StringUtil.getListFromCommaSeparatedValues(statuses);
					int i = 0;
					whereQueryBuilder.append(" (");
					for (Long statusId : satusList) {
						whereQueryBuilder.append("globalMessageStatusId eq " + statusId);
						i++;
						if (i < satusList.size())
							whereQueryBuilder.append(" or ");
					}
					whereQueryBuilder.append(")");
				}
				Map<String, String> map = new HashMap<String, String>();
				if (cobrandId != null) {
					map.put("cobrandIds", dataCobrandId.toString());
				}
				if (yodlee) {
					map.put(YSLConstants.USER_TYPE, "tier2");
				} else
					map.put(YSLConstants.USER_TYPE, "tier1");

				impactCobrandBuilder.append(whereQueryBuilder.toString());
				updatesBuilder.append(" INCLUDE notification notificationDetails as updates (lastUpdated message updatedBy)");
				updatesBuilder.append(whereQueryBuilder.toString());

				impactCobrandBuilder.append(updatesBuilder.toString());
				SearchSiteLocalStorage.SetSearchSiteCriteria(map);
				ContextAccessorUtil.setContext(dataCobrandId, 0l, null);
				logger.debug("Query for notification:" + impactCobrandBuilder.toString());
				notificationResponse = getResource(impactCobrandBuilder.toString());
				if (notificationResponse != null && !notificationResponse.equalsIgnoreCase("{}"))
					notificationResponse = buildJsonString(notificationResponse);
			}

		} catch (Exception e) {
			logger.debug("Input parameters:cobrandId={},providerIds={},statuses={}", obj);
			logger.error("Error while searching the notifications:" + ExceptionUtils.getFullStackTrace(e));
			throw new RefreshStatException(e.getMessage());
		} finally {
			ContextAccessorUtil.unsetContext();
			SearchSiteLocalStorage.unSearchSiteCriteria();
			PersistenceContext.getInstance().closeEntityManager("yccdom");
		}
		Date eDate = new Date();
		logger.info("Total time taken for notification API:" + (eDate.getTime() - sDate.getTime()) + " milliSec "
				+ "Input parameters:cobrandId={},providerIds={},statuses={}", obj);
		logger.debug("Final response for notification:" + notificationResponse);

		return notificationResponse == null ? "{}" : notificationResponse;

	}

	private String buildJsonString(String notificationResponse) {
		JsonElement jsonElement = new JsonParser().parse(notificationResponse);
		JsonElement notificationElement = jsonElement.getAsJsonObject().get("notification");
		JsonArray notificationArray = notificationElement.getAsJsonArray();
		JsonArray nArray = new JsonArray();
		JsonObject nElement = new JsonObject();
		int size = notificationArray.size();
		List<Long> idList = new ArrayList<Long>();
		for (int i = 0; i < size; i++) {
			JsonElement curElement = notificationArray.get(i);
			JsonObject currObj = curElement.getAsJsonObject();
			JsonElement update = currObj.get("updates");
			if (update != null) {
				currObj.remove("updates");
				JsonArray providerArray = update.getAsJsonArray();
				JsonArray nArray1 = new JsonArray();
				int size1 = providerArray.size();
				for (int j = 0; j < size1; j++) {
					JsonElement curElement1 = providerArray.get(j);
					JsonObject currObj1 = curElement1.getAsJsonObject();
					JsonElement updatedBy = currObj1.get("updatedBy");
					if (updatedBy != null) {
						JsonElement updatedBy1 = new JsonParser()
								.parse(updatedBy.getAsString().replaceAll("\\\\", "").replaceAll("\"\\[", "\\[").replaceAll("\\]\"", "\\]"));
						currObj1.remove("updatedBy");
						currObj1.add("updatedBy", updatedBy1);
					}
					nArray1.add(currObj1);
				}
				currObj.add("updates", nArray1);
			}
			JsonElement impactedProvider = currObj.get("impactedProvider");
			if (impactedProvider != null) {
				currObj.remove("impactedProvider");
				impactedProvider = new JsonParser()
						.parse(impactedProvider.getAsString().replaceAll("\\\\", "").replaceAll("\"\\[", "\\[").replaceAll("\\]\"", "\\]"));
				JsonArray providerArray = impactedProvider.getAsJsonArray();
				JsonArray nArray1 = new JsonArray();
				int size1 = providerArray.size();
				for (int j = 0; j < size1; j++) {
					JsonElement curElement1 = providerArray.get(j);
					JsonObject currObj1 = curElement1.getAsJsonObject();
					JsonElement nameEl = currObj1.get("id");
					if (nameEl != null) {
						String siteName = getSiteName(nameEl.getAsLong());
						if (siteName != null)
							currObj1.addProperty("name", siteName);
					}
					nArray1.add(currObj1);
				}
				currObj.add("impactedProvider", nArray1);
			}
			JsonElement idEl = currObj.get("id");
			String idStr = idEl.getAsString();
			if (idList.contains(Long.valueOf(idStr))) {
				for (int j = 0; j < nArray.size(); j++) {
					JsonElement jsonElement2 = nArray.get(j);
					JsonObject jsonElementObj = jsonElement2.getAsJsonObject();
					String idLong = jsonElementObj.get("id").getAsString();
					if (idStr.equalsIgnoreCase(idLong)) {
						JsonElement update1 = jsonElementObj.get("updates");
						JsonElement impactedCobrand = jsonElementObj.get("impactedCobrand");
						if (impactedCobrand != null) {
							break;
						} else {
							nArray = removeEl(nArray, j);
							nArray.add(curElement);
							break;
						}
					}
				}
				continue;
			} else {
				idList.add(Long.valueOf(idStr));
				nArray.add(curElement);
			}

		}
		nElement.add("notification", nArray);
		return nElement.toString();
	}

	private String getSiteName(Long siteId) {
		Site site = Site.DAO.get(siteId);
		String name = null;
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

	public static JsonArray removeEl(JsonArray jarray, int pos) {

		JsonArray array = new JsonArray();
		try {
			for (int i = 0; i < jarray.size(); i++) {
				if (i != pos)
					array.add(jarray.get(i));
			}
		} catch (Exception e) {
			logger.error("error while removing the element" + ExceptionUtils.getFullStackTrace(e));
		}
		return array;

	}
}
