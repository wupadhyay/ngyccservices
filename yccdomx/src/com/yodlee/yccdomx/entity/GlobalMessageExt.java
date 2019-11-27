/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.yccdomx.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yodlee.dom.entity.DbMessageCatalog;
import com.yodlee.dom.entity.DbMessageCatalog_;
import com.yodlee.dom.entity.Site;
import com.yodlee.dom.entity.SumInfoSptdLocale;
import com.yodlee.domx.base.ServiceBaseExt;
import com.yodlee.domx.storage.SearchSiteLocalStorage;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.annotation.Derived;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.shared.meta.DerivedAttribute_;
import com.yodlee.framework.runtime.shared.meta.DerivedAttribute_.Category;
import com.yodlee.framework.service.dao.FilterOptionHandler;
import com.yodlee.framework.service.dao.OptionFilter;
import com.yodlee.framework.service.shared.domx.IFilterOption;
import com.yodlee.yccdom.entity.CobGlobalMessage_;
import com.yodlee.yccdom.entity.GlobalMessage;
import com.yodlee.yccdom.entity.GlobalMessageStatus;
import com.yodlee.yccdom.entity.NotificationCategory;
import com.yodlee.yccdom.entity.NotificationIssue;

/**
 * 
 * @author knavuluri
 * 
 */
public class GlobalMessageExt extends ServiceBaseExt<GlobalMessage> {

	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private static final String PREFIX = "com.yodlee";

	public static class GlobalMessageExt_ {

		public static DerivedAttribute_<String> cobrandIds = new DerivedAttribute_<String>(Category.DOMX, "GlobalMessage.cobrandIds", String.class);
	}

	static {
		setCobrandFilterOption();
	}

	public static List<Long> getCobrandIds() {
		List<Long> cobIds = new ArrayList<Long>();
		String cobrandId = SearchSiteLocalStorage.getSearchSiteCriteria().get("cobrandIds");
		if (cobrandId != null) {
			cobIds.add(Long.valueOf(cobrandId));
			return cobIds;
		}
		return null;
	}

	public GlobalMessageExt() {
		super(GlobalMessage.class);
	}

	@Derived(arguments = { "impact" })
	public String getImpactExt() {
		return entity.getImpact();
	}

	@Derived(arguments = { "notificationIssueIds" })
	public ArrayList<String> getNotificationIssueIdsExt() {
		ArrayList<String> list = new ArrayList<String>();
		String notificationIssueIds = entity.getNotificationIssueIds();
		if (notificationIssueIds != null) {
			List<Long> ids = StringUtil.getListFromCommaSeparatedValues(notificationIssueIds);
			for (Long id : ids) {
				NotificationIssue notificationIssue = NotificationIssue.DAO.get(id);
				String notificationIssueName = notificationIssue.getNotificationIssueName().replaceAll("\\s+","");
				notificationIssueName = getCamelCapitalize(notificationIssueName);
				list.add(notificationIssueName);
			}
		}
		return list;
	}

	@Derived(arguments = { "notificationCatId" })
	public String getCategoryExt() {
		String category = null;
		Long notificationCatId = entity.getNotificationCatId();
		if (notificationCatId != null) {
			NotificationCategory notificationCategory = NotificationCategory.DAO.get(notificationCatId);
			if (notificationCategory != null)
				category = notificationCategory.getNotificationCatName();
		}

		return category;
	}

	@Derived(arguments = { "rowLastUpdated" })
	public String getLastUpdatedExt() {
		if (entity.getRowLastUpdated() != null)
			return df.format(entity.getRowLastUpdated());
		else
			return null;
	}

	public String getClassificationExt() {
		return "SITE_NOTIFICATION";
	}

	@Derived(arguments = { "globalMessageStatusId" })
	public String getStatusExt() {
		String status = null;
		long globalMessageStatusId = entity.getGlobalMessageStatusId();
		if (globalMessageStatusId != 0l) {
			GlobalMessageStatus globalMessageStatus = GlobalMessageStatus.DAO.get(globalMessageStatusId);
			status = globalMessageStatus.getName();
		}
		return status;
	}

	@Derived(arguments = { "title" })
	public String getTitleExt() {
		return entity.getTitle();
	}

	@Derived(arguments = { "description" })
	public String getDescriptionExt() {
		return entity.getDescription();
	}

	@Derived(arguments = { "startTime" })
	public String getIssueStartDateExt() {
		if (entity.getStartTime() != null)
			return df.format(entity.getStartTime());
		else
			return null;
	}

	@Derived(arguments = { "endTime" })
	public String getExpectedResolutionTimeExt() {
		if (entity.getEndTime() != null)
			return df.format(entity.getEndTime());
		else
			return null;
	}

	@Derived(arguments = { "siteId", "containers" })
	public String getImpactedProviderExt() {
		if (entity.getSiteId() != null) {
//			ProviderDetails provider=new ProviderDetails();
//			provider.setId(entity.getSiteId());
//			provider.setName("sample");
//			String containers = entity.getContainers();
//			if(containers!=null){
//				ArrayList<String> list = new ArrayList<String>();
//				String[] split = containers.split(",");
//				for (String container : split) {
//					list.add(container);
//				}
//				provider.setContainers(list);
//			}
//			return provider;
			JSONObject obj = new JSONObject();
			JSONArray arr = new JSONArray();
			obj.put("id", entity.getSiteId());
			String containers = entity.getContainers();
			if (containers != null) {
				ArrayList<String> list = new ArrayList<String>();
				String[] split = containers.split(",");
				for (String container : split) {
					list.add(container);
				}
				obj.put("containers", list);
			}
			arr.add(obj);
			return arr.toString();

		}
		return null;
	}

	@Override
	public SumInfoSptdLocale getDftSptdLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SumInfoSptdLocale> getSptdLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	public enum CobGlobal implements IFilterOption {
		COBGLOBAL;

		@Override
		public String getName() {
			return name();
		}
	}

	public static void setCobrandFilterOption() {
		FilterOptionHandler.initFilterOptionTypeMap(GlobalMessage.class, CobGlobal.class, new OptionFilter(CobGlobal.COBGLOBAL, getCobCriteria()));
	}

	public static Criteria getCobCriteria() {
		Criteria cri = new Criteria().andIn(CobGlobalMessage_.cobrandId, GlobalMessageExt_.cobrandIds).join(CobGlobalMessage_.globalMessage);
		return cri;
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
	
	public static String getCamelCapitalize(String value) {
		return value.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
	}

}
