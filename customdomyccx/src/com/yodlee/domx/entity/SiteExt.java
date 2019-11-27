/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.domx.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yodlee.dom.entity.BaseAggrType;
import com.yodlee.dom.entity.Country;
import com.yodlee.dom.entity.DefCat_;
import com.yodlee.dom.entity.DefTab_;
import com.yodlee.dom.entity.DocTypeSuminfoConfig;
import com.yodlee.dom.entity.DocTypeSuminfoConfig_;
import com.yodlee.dom.entity.FiLogo;
import com.yodlee.dom.entity.FiLogo_;
import com.yodlee.dom.entity.Language;
import com.yodlee.dom.entity.Locale;
import com.yodlee.dom.entity.LoginForm;
import com.yodlee.dom.entity.MfaType;
import com.yodlee.dom.entity.Notes;
import com.yodlee.dom.entity.Notes_;
import com.yodlee.dom.entity.Site;
import com.yodlee.dom.entity.SiteExtn;
import com.yodlee.dom.entity.SiteExtn_;
import com.yodlee.dom.entity.SiteSptdLocale;
import com.yodlee.dom.entity.SiteSptdLocale_;
import com.yodlee.dom.entity.Site_;
import com.yodlee.dom.entity.SumInfo;
import com.yodlee.dom.entity.SumInfoCobExtn;
import com.yodlee.dom.entity.SumInfoCobExtn_;
import com.yodlee.dom.entity.SumInfoDefCat;
import com.yodlee.dom.entity.SumInfoDefCat_;
import com.yodlee.dom.entity.SumInfoExtn;
import com.yodlee.dom.entity.SumInfoExtn_;
import com.yodlee.dom.entity.SumInfoParamKey;
import com.yodlee.dom.entity.SumInfoParamKey_;
import com.yodlee.dom.entity.SumInfoParamValue;
import com.yodlee.dom.entity.SumInfoParamValue_;
import com.yodlee.dom.entity.SumInfoSiteIconMap;
import com.yodlee.dom.entity.SumInfoSiteIconMap_;
import com.yodlee.dom.entity.SumInfoSptdLocale;
import com.yodlee.dom.entity.SumInfo_;
import com.yodlee.dom.entity.Tag;
import com.yodlee.domx.base.ServiceBaseExt;
import com.yodlee.domx.storage.SearchSiteLocalStorage;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.meta.DerivedAttribute_;
import com.yodlee.framework.runtime.shared.meta.DerivedAttribute_.Category;
import com.yodlee.framework.service.binding.Context;
import com.yodlee.framework.service.dao.FilterOptionHandler;
import com.yodlee.framework.service.dao.OptionFilter;
import com.yodlee.framework.service.shared.domx.IFilterOption;
import com.yodlee.nextgen.logging.MessageController;

/**
 * 
 * @author knavuluri
 * 
 */
final public class SiteExt extends ServiceBaseExt<Site> {
	private static final Logger logger = LoggerFactory.getLogger(SiteExt.class);
	private static final String FQCN = SiteExt.class.getName();
	public static final String DATE_TIME_FORMAT_PATTERN_EXT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String IMAGE_TYPE_FAVICON = "&imageType=FAVICON";
	private static final String IMAGE_TYPE_LOGO = "&imageType=LOGO";
	public static final Locale NO_PRIMARY_LOCALE = new Locale();
	public static final LoginForm NO_LOGIN_FORM = new LoginForm();
	private static final String PREFIX = "com.yodlee";
	private static Set<Long> suminfoDisReasons = new HashSet<Long>();
	private List<SumInfoExt> sumInfoExts;
	private Locale primaryLocale;
	private LoginForm loginForm;
	private static final String cisidDisReasons = "9,10,11,12,13";
	private static final String siteDisReasons = "2,3,4";
	private static List<Long> siteDisList = null;
	ObjectMapper mapper = new ObjectMapper();
	private static List<KeyValue> ivaSuminfoKeys = new ArrayList<KeyValue>();
	private static final String FULL_ACC_NUMBER_SUPPORTED = "COM.YODLEE.CORE.ACCOUNTVERIFICATION.FULL_ACC_NUMBER_SUPPORTED";
	private static final String END_SITE_RTN_SUPPORTED = "COM.YODLEE.CORE.INSTANTACCOUNTVERIFICATION.END_SITE.RTN_SUPPORTED";
	private static final String ACC_HOLDER_NAME_SUPPORTED = "COM.YODLEE.CORE.ACCOUNTVERIFICATION.ACC_HOLDER_NAME_SUPPORTED";
	private static final String HOLDER_DETAILS_SUPPORTED = "COM.YODLEE.CORE.ACCT_PROFILE.HOLDER_DETAILS_SUPPORTED";
	private static Map<Long, String> suminfoCategoryMap = new HashMap<Long, String>();
	private Long categorySuminfoId = null;
	private static String tagOrder = "5,4,12,21,22,84,13,65,17,87,10,91,88,2,92,94,86,89,90";
	private static List<Long> tagIdOrderList=null;
	public static final String SVG = "SVG";
 	public static final String NONSVG = "NONSVG";
	public static String replaceChars = "['|\"]";
	private static Map<Long, String> siteDisablementReasonMap = new HashMap<Long, String>();
	static {
		siteDisList = StringUtil.getListFromCommaSeparatedValues(siteDisReasons);
		tagIdOrderList = StringUtil.getListFromCommaSeparatedValues(tagOrder);
		setSiteFilterOption();
		setAgentFilterOption();
		loadSuminfoDisablementReasons();
		populateIavKeys();
		populateCategories();
		populateSiteDisablementReasons();
	}

	public SiteExt() {
		super(Site.class);
	}

	enum Status {
		Beta, Supported, Unsupported, Unavailable, UnsupportedBeta, Invisible
	}

	public static class SiteExt_ {

		public static DerivedAttribute_<String> siteIds = new DerivedAttribute_<String>(Category.DOMX, "Site.siteIds", String.class);
		public static DerivedAttribute_<String> agentname = new DerivedAttribute_<String>(Category.DOMX, "Site.agentname", String.class);
	}

	@Override
	public boolean isCacheable() {
		return true;
	}

	private static void populateSiteDisablementReasons() {
		siteDisablementReasonMap.put(1l, "SITE_IS_MERGED_OR_ACQURIED");
		siteDisablementReasonMap.put(5l, "SITE_NO_LONGER_SUPPORTED_BY_YODLEE");
		siteDisablementReasonMap.put(6l, "SITE_IS_TEMPORARY_DISABLED");
		siteDisablementReasonMap.put(7l, "SITE_DOES_NOT_WANT_TO_BE_SCRAPPED");
		siteDisablementReasonMap.put(8l, "SITE_CAN_NOT_BE_SCRAPPED");
	}
	private static void populateCategories() {
		suminfoCategoryMap.put(1l, "General");
		suminfoCategoryMap.put(2l, "Dag Site");
		suminfoCategoryMap.put(3l, "Testing");
		suminfoCategoryMap.put(4l, "Backend Site");
		suminfoCategoryMap.put(5l, "Client Specific");
		suminfoCategoryMap.put(6l, "Product Specific");

	}

	private static void populateIavKeys() {

		SumInfoParamKey fullKey = SumInfoParamKey.DAO.getSingle(SumInfoParamKey_.sumInfoParamKeyName, FULL_ACC_NUMBER_SUPPORTED);
		SumInfoParamKey rtnKey = SumInfoParamKey.DAO.getSingle(SumInfoParamKey_.sumInfoParamKeyName, END_SITE_RTN_SUPPORTED);
		SumInfoParamKey accKey = SumInfoParamKey.DAO.getSingle(SumInfoParamKey_.sumInfoParamKeyName, ACC_HOLDER_NAME_SUPPORTED);
		SumInfoParamKey holderKey = SumInfoParamKey.DAO.getSingle(SumInfoParamKey_.sumInfoParamKeyName, HOLDER_DETAILS_SUPPORTED);

		KeyValue fullAcc = new KeyValue(FULL_ACC_NUMBER_SUPPORTED, "TRUE", "FULL_ACCT_NUMBER", fullKey.getSumInfoParamKeyId());
		KeyValue rtn = new KeyValue(END_SITE_RTN_SUPPORTED, "ON", "BANK_TRANSFER_CODE", rtnKey.getSumInfoParamKeyId());
		KeyValue accHolder = new KeyValue(ACC_HOLDER_NAME_SUPPORTED, "TRUE", "HOLDER_NAME", accKey.getSumInfoParamKeyId());
		KeyValue holderDetails = new KeyValue(HOLDER_DETAILS_SUPPORTED, "TRUE", "HOLDER_DETAILS", holderKey.getSumInfoParamKeyId());
		ivaSuminfoKeys.add(fullAcc);
		ivaSuminfoKeys.add(rtn);
		ivaSuminfoKeys.add(accHolder);
		ivaSuminfoKeys.add(holderDetails);

	}

	public String getLoginUrlExt() {
		return getLoginUrlExt(getPrimaryLocale(), true);
	}

	private String getLoginUrlExt(Locale locale, boolean isLocalReq) {
		String loginUrl = null;
		for (SumInfoExt sumInfoExt : getSumInfoExts()) {
			if (sumInfoExt.getSumInfo().getIsReady()) {
				loginUrl = sumInfoExt.getLoginUrlExt(locale, isLocalReq);
				if (loginUrl != null)
					return loginUrl;
			}
			else {
				loginUrl = sumInfoExt.getLoginUrlExt(locale, isLocalReq);
			}
		}
		return loginUrl;
	}

	public Site getSite() {
		return entity;
	}

	public String getNameExt() {
		boolean showProviderCategory = showProviderCategory();
		if(!showProviderCategory)
			return "-1";
		Locale locale = getPrimaryLocale();
		String name = getNameLocaleExt(locale);
		return name;
	}

	public String getNameLocaleExt(Locale locale) {
		String name = getCobrandedDisplayName(locale);
		if (name == null)
			name = getMessageForKey(PREFIX, DBMCConstants.SITE_NEW_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale, locale);
		if (name == null)
			name = getMessageForKey(PREFIX, DBMCConstants.SITE_NEW_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale);
		if (name == null)
			name = getMessageForKey(PREFIX, DBMCConstants.SITE_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale, locale);

		return name;
	}

	public String getDefaultName() {
		String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
		if (userType != null && "tier2".equalsIgnoreCase(userType)) {
			Locale locale = getPrimaryLocale();
			String cobrandedDisplayName = getCobrandedDisplayName(locale);
			return cobrandedDisplayName;
		}
		return null;
	}

	public String getCobrandedDisplayName(Locale locale) {
		long cobrandId = getSubrandId();
		String siteKey = new StringBuilder(String.valueOf(cobrandId)).append("." + DBMCConstants.SITE_NEW_BUNDLE_NAME).append(".").append(entity.getSiteId()).toString();
		String name = getMessageForKey(PREFIX, "cobrands", siteKey, locale, locale);
		return name;
	}

	public String getPrimaryLanguageExt() {
		return getLanguageExt(getPrimaryLocale());
	}

	private String getLanguageExt(Locale locale) {
		if (locale != null && locale.getLanguage() != null) {
			Language language = getLanguageForLanguageId(locale.getLanguageId());
			return (language == null) ? null : language.getName();
		}
		return null;
	}

	public String[] getContainerNames() {
		List<String> result = new ArrayList<String>();
		List<SumInfoExt> suminfoExtlist = sortSumInfosBasedOnTagIdOrder(getSumInfoExts());
		for (SumInfoExt sumInfoExt : suminfoExtlist) {
			boolean addContiner = true;
			if (!sumInfoExt.getSumInfo().getIsReady()) {
				addContiner = addDisabledContainer(sumInfoExt.getSumInfo().getSumInfoId());
			}
			if (addContiner) {
				String containerName = sumInfoExt.getContainerName();
				if (containerName != null){
					if(result.contains(containerName))
						MessageController.log(FQCN, 249, "It seems duplicate contsainers are exists for site:" + entity.getSiteId(), MessageController.SERIOUS);
					result.add(containerName);
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public boolean addDisabledContainer(long sumInfoId) {
		boolean addContiner = true;
			List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfoId);
			if (list != null && !list.isEmpty()) {
				SumInfoExtn sumInfoExtn = list.get(0);
				Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
				if (disbalementReasonId != null && suminfoDisReasons.contains(disbalementReasonId)) {
					addContiner = false;
				}
			}
		return addContiner;
	}

	public String getCountryExt() {
		return getCountryExt(getPrimaryLocale());
	}

	private String getCountryExt(Locale locale) {
		if (locale != null && locale.getCountry() != null) {
			Country country = getCountryForCountryId(locale.getCountryId());
			return (country == null) ? null : country.getCountryIsoCode();
		}
		return null;
	}

	public String getCountryNameExt() {
		return getCountryNameExt(getPrimaryLocale());
	}

	private String getCountryNameExt(Locale locale) {
		if (locale != null && locale.getCountry() != null) {
			Country country = getCountryForCountryId(locale.getCountryId());
			return (country == null) ? null : country.getCountryName();
		}
		return null;
	}

	public String getStatus() {
		boolean isready = entity.getIsReady() == null ? false : entity.getIsReady();

		boolean enabled = isSiteEnabled();
		boolean isbeta = isBeta();
		boolean visibility = entity.getSiteSearchVisibility() == null || entity.getSiteSearchVisibility() ? true : entity.getSiteSearchVisibility();
		if (enabled && (!isready || !visibility))
			return Status.Invisible.name();
		else if (enabled && isbeta)
			return Status.Beta.name();
		else if (enabled)
			return Status.Supported.name();
		else if (isready && isbeta)
			return Status.UnsupportedBeta.name();
		else if (isready)
			return Status.Unsupported.name();
		else if (!isready)
			return Status.Unavailable.name();

		return "";
	}

	private boolean isSiteEnabled() {
		long cobrandId = getSubrandId();
		Criteria selectCriteria = new Criteria().andIn(new Criteria(SumInfoDefCat.class).join(SumInfoDefCat_.sumInfo)
				.add(new Criteria().and(SumInfo_.isReady, Boolean.TRUE).and(SumInfo_.siteId, entity.getSiteId())).join(SumInfoDefCat_.defCat).join(DefCat_.defTab).and(DefTab_.cobrandId, cobrandId)
				.and(DefTab_.isReady, Boolean.TRUE).and(DefCat_.isReady, Boolean.TRUE).and(SumInfoDefCat_.isReady, Boolean.TRUE));
		List<SumInfo> list = SumInfo.DAO.select(selectCriteria);
		if (list != null && !list.isEmpty())
			return true;
		return false;
	}

	private boolean isBeta() {
		boolean beta = false;
		List<SumInfo> sumInfos = getSumInfos();
		if (sumInfos != null && !sumInfos.isEmpty()) {
			int count = 0;
			for (SumInfo sumInfo : sumInfos) {
				if (sumInfo.isIsBeta())
					count++;
				else
					break;
			}
			if (count == sumInfos.size())
				return true;
		}

		return beta;
	}

	public String getRowLastUpdatedExt() {
		Date lastUpdated = entity.getRowLastUpdated();
		for (SumInfo sumInfo : getSumInfos()) {
			Date sumInfoLastUpdated = null;
			if (lastUpdated == null || (sumInfoLastUpdated = sumInfo.getRowLastUpdated()) != null && sumInfoLastUpdated.after(lastUpdated))
				lastUpdated = sumInfoLastUpdated;
		}
		if (lastUpdated != null) {
			SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			return df.format(lastUpdated);
		}
		return null;
	}
	
	public String getRowCreatedExt() {
		Date created = entity.getRowCreated();
		if (created != null) {
			SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			return df.format(created);
		}
		return null;
	}

	

	public String getBaseUrlExt() {
		return getBaseUrlExt(getPrimaryLocale(), true);
	}

	private String getBaseUrlExt(Locale locale, boolean isLocalReq) {
		if (isLocalReq)
			return getMessageForKey(PREFIX, DBMCConstants.SITE_BASE_URL_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale);
		else
			return getMessageForKey(PREFIX, DBMCConstants.SITE_BASE_URL_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale, null);
	}

	private Locale getPrimaryLocale() {
		primaryLocale = getLocaleForLocaleId(entity.getPrimaryLocaleId());
		if (primaryLocale == null)
			primaryLocale = NO_PRIMARY_LOCALE;
		return primaryLocale != NO_PRIMARY_LOCALE ? primaryLocale : null;
	}

	public List<SumInfoExt> getSumInfoExts() {
		sumInfoExts = new ArrayList<SumInfoExt>();
		for (SumInfo sumInfo : getAllSumInfosForSite()) {
			SumInfoExt sumInfoExt = new SumInfoExt();
			sumInfoExt.init(sumInfo, serviceContext);
			sumInfoExts.add(sumInfoExt);
		}
		return sumInfoExts;
	}

	private List<SumInfo> getSumInfos() {
		List<SumInfo> list = new ArrayList<SumInfo>();
		List<SumInfo> suminfos = getAllSumInfosForSite();
		for (SumInfo sumInfo : suminfos) {
			if (sumInfo.getIsReady())
				list.add(sumInfo);
		}
		return list;
	}

	public static Site getSiteForSiteId(long siteId) {
		return Site.DAO.get(siteId);
	}

	private List<SumInfo> getAllSumInfosForSite() {
		long siteId = entity.getSiteId();
		return SumInfo.DAO.get(SumInfo_.siteId, siteId);
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

	public static List<Long> getSiteIds() {
		List<Long> idsList = new ArrayList<Long>();
		String ids = SearchSiteLocalStorage.getSearchSiteCriteria().get("id");
		if (ids != null) {
			String[] split = ids.split(",");
			for (String id : split) {
				idsList.add(Long.valueOf(id));
			}
		}
		return idsList;
	}

	public static String getAgentname() {
		String name = SearchSiteLocalStorage.getSearchSiteCriteria().get("agentname");
		return name;
	}

	private static void loadSuminfoDisablementReasons() {
		String[] split = cisidDisReasons.split(",");
		for (String reasonId : split) {
			suminfoDisReasons.add(Long.valueOf(reasonId));
		}
	}

	public LocalizedSiteAttributes[] getLocalizedSiteAttributes() {
		Locale primaryLocale = getPrimaryLocale();
		Long defaultLocaleId = primaryLocale.getLocaleId();
		List<LocalizedSiteAttributes> result = new ArrayList<LocalizedSiteAttributes>();
		long siteId = entity.getSiteId();
		List<SiteSptdLocale> siteSptdLocale = SiteSptdLocale.DAO.get(SiteSptdLocale_.siteId, siteId);
		for (SiteSptdLocale siteLocale : siteSptdLocale) {
			if (defaultLocaleId == null || siteLocale.getLocaleId() != defaultLocaleId) {
				LocalizedSiteAttributes entry = new LocalizedSiteAttributes();
				Locale locale = getLocaleForLocaleId(siteLocale.getLocaleId());
				entry.setLocaleId(siteLocale.getLocaleId());
				entry.setCountryISOCode(getCountryExt(locale));
				entry.setLanguageISOCode(getLanguageExt(locale));
				entry.setName(getNameLocaleExt(locale));
				entry.setLoginUrl(getLoginUrlExt(locale, false));
				entry.setBaseUrl(getBaseUrlExt(locale, false));
				entry.setNameWithCountry(getCountryNameExt(locale));
				entry.setHelp(getHelp(locale, false));
				entry.setForgetPasswordURL(getForgetPasswordUrl(locale, false));
				entry.setLoginHelp(getLoginHelp(locale, false));
				result.add(entry);
			}
		}
		return result.toArray(new LocalizedSiteAttributes[result.size()]);
	}

	private boolean hasFavicon() {
		long siteId = entity.getSiteId();
		List<SumInfoSiteIconMap> sumInfoSiteIconMaps = SumInfoSiteIconMap.DAO.get(SumInfoSiteIconMap_.siteId, siteId);
		if (sumInfoSiteIconMaps != null && !sumInfoSiteIconMaps.isEmpty()) {
			for (SumInfoSiteIconMap sumInfoSiteIconMap : sumInfoSiteIconMaps) {
				if (sumInfoSiteIconMap.getSumInfoId() == null && sumInfoSiteIconMap.getFaviconBlob() != null) {
					return true;
				}
			}
		}

		return false;
	}

	public String getFavicon() {
		boolean isFaviconFound = false;
		StringBuffer targetURL = new StringBuffer(Context.getCdnUrl()).append("FAVICON").append("/");
		long siteId = entity.getSiteId();
		List<SumInfoSiteIconMap> sumInfoSiteIconMaps = SumInfoSiteIconMap.DAO.get(SumInfoSiteIconMap_.siteId, siteId);
		Map<String, String> targetMap = new HashMap<String, String>();
		if (sumInfoSiteIconMaps != null && !sumInfoSiteIconMaps.isEmpty()) {
			for (SumInfoSiteIconMap sumInfoSiteIconMap : sumInfoSiteIconMaps) {
				if (sumInfoSiteIconMap.getSumInfoId() == null && sumInfoSiteIconMap.isIsDisabled() == false) {
					StringBuilder builder = new StringBuilder();
					if (sumInfoSiteIconMap.getFaviconSVGBlob() != null) {
						builder.append("FAV_").append(entity.getSiteId()).append(".SVG");
						targetMap.put(SVG, builder.toString());
					}
					else if (sumInfoSiteIconMap.getFaviconBlob() != null) {
						builder.append("FAV_").append(entity.getSiteId()).append(".PNG");
						targetMap.put(NONSVG, builder.toString());
					}
				}
			}
		}
		if (targetMap.get(SVG) != null)
			targetURL.append(targetMap.get(SVG));
		else if (targetMap.get(NONSVG) != null)
			targetURL.append(targetMap.get(NONSVG));
		else
			targetURL.append("FAV_Default.PNG");

		return targetURL.toString();
	}

	public String getLogo() {
		StringBuffer targetURL = new StringBuffer(Context.getCdnUrl()).append("LOGO").append("/");
		Long primaryLocaleId = entity.getPrimaryLocaleId();
		Map<String, String> logoForLocale = getLogoForLocale(entity.getSiteId(), primaryLocaleId);
		if (logoForLocale.get(SVG) != null)
			targetURL.append(logoForLocale.get(SVG));
		else if (logoForLocale.get(NONSVG) != null)
			targetURL.append(logoForLocale.get(NONSVG));
		else
			targetURL.append("LOGO_Default.PNG");
		return targetURL.toString();
	}

	private boolean hasLogoImage() {
		long siteId = entity.getSiteId();
		Long primaryLocaleId = entity.getPrimaryLocaleId();
		if (primaryLocaleId != null) {
			List<FiLogo> fiLogos = FiLogo.DAO.get(FiLogo_.siteId, siteId);
			if (fiLogos != null && !fiLogos.isEmpty()) {
				for (FiLogo fiLogo : fiLogos) {
					if (fiLogo.getLocaleId() == primaryLocaleId.longValue() && !fiLogo.isIsDisabled())
						return true;
				}
			}

		}
		return false;
	}

	private String getImage(boolean isFavicon) {
		try {
			return isFavicon ? Context.getCdnUrl() + Long.valueOf(entity.getSiteId()).toString() + IMAGE_TYPE_FAVICON : Context.getCdnUrl() + Long.valueOf(entity.getSiteId()).toString()
					+ IMAGE_TYPE_LOGO;
		}
		catch (Exception e) {
			MessageController.log(FQCN, 509, "Error while loading image for siteId:" + entity.getSiteId(), MessageController.SERIOUS);
			return null;
		}
	}

	public String getHelp() {
		return getHelp(getPrimaryLocale(), true);
	}

	private String getHelp(Locale locale, boolean isLocalReq) {
		if (isLocalReq)
			return getMessageForKey(PREFIX, DBMCConstants.SITE_HELP_TEXT_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale);
		else
			return getMessageForKey(PREFIX, DBMCConstants.SITE_HELP_TEXT_BUNDLE_NAME, Long.valueOf(entity.getSiteId()).toString(), locale, null);
	}

	public String getMfaType() {
		String result = null;
		for (SumInfo sumInfo : getAllSumInfosForSite()) {
			if (entity.getIsReady()) {
				if (sumInfo.getIsReady()) {
					result = getMfaString(sumInfo);
					if (result != null)
						break;
				}
			}
			else {
				List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
				if (list != null && !list.isEmpty()) {
					SumInfoExtn sumInfoExtn = list.get(0);
					Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
					if (disbalementReasonId == null || !suminfoDisReasons.contains(disbalementReasonId)) {
						result = getMfaString(sumInfo);
						if (result != null)
							break;
					}
				}
			}
		}
		return result;
	}

	private String getMfaString(SumInfo sumInfo) {
		Long mfaTypeId = sumInfo.getMfaTypeId();
		String result = null;
		if (mfaTypeId != null && mfaTypeId > 0) {
			String mfaTypeName = MfaType.getEnumValueName(mfaTypeId);
			String description = getMfaTypeDescription(mfaTypeName);
			if (description != null) {
				result = description;
			}
		}
		return result;
	}

	public Boolean getIsOauthEnabledExt() {
		return entity.getIsOAuthEnabled() == null ? false : entity.getIsOAuthEnabled();
	}

	public String getLanguageIsoCodeExt() {
		Locale primaryLocale = getPrimaryLocale();
		if (primaryLocale != null && primaryLocale.getLanguage() != null) {
			Language language = getLanguageForLanguageId(primaryLocale.getLanguageId());
			return (language == null) ? null : language.getDescription();
		}
		return null;
	}

	public String getForgetPasswordUrl() {
		return getForgetPasswordUrl(getPrimaryLocale(), true);
	}

	private String getForgetPasswordUrl(Locale locale, boolean isLocalReq) {
		return getLoginForm() != null ? isLocalReq ? getMessageForKey(PREFIX, DBMCConstants.PASSWORD_HELP_URL_BUNDLE_NAME, Long.valueOf(getLoginForm().getLoginFormId()).toString(), locale)
				: getMessageForKey(PREFIX, DBMCConstants.PASSWORD_HELP_URL_BUNDLE_NAME, Long.valueOf(getLoginForm().getLoginFormId()).toString(), locale, null) : null;
	}

	public LoginForm getLoginForm() {
		List<SumInfo> sumInfos = getAllSumInfosForSite();
		if (sumInfos != null && !sumInfos.isEmpty()) {
			for (SumInfo sumInfo : sumInfos) {
				if (entity.getIsReady()) {
					if (sumInfo.getIsReady()) {
						loginForm = sumInfo.getLoginForm();
						break;
					}
				}
				else {
					List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
					if (list != null && !list.isEmpty()) {
						SumInfoExtn sumInfoExtn = list.get(0);
						Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
						if (disbalementReasonId == null || !suminfoDisReasons.contains(disbalementReasonId)) {
							loginForm = sumInfo.getLoginForm();
							break;
						}
					}
				}
			}
		}

		return loginForm;
	}

	public Boolean getIsAutoRefreshEnableExt() {
		Criteria cri = new Criteria();
		cri.add(SumInfo_.siteId, entity.getSiteId());
		List<SumInfo> allSumInfosForSite = SumInfo.DAO.select(cri);
		//List<SumInfo> allSumInfosForSite = getAllSumInfosForSite();
		for (SumInfo sumInfo : allSumInfosForSite) {
			if (entity.getIsReady()) {
				if (sumInfo.getIsReady()) {
					if (!sumInfo.isIsCacherunDisabled())
						return true;
				}
			}
			else {
				List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
				if (list != null && !list.isEmpty()) {
					SumInfoExtn sumInfoExtn = list.get(0);
					Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
					if (disbalementReasonId == null || !suminfoDisReasons.contains(disbalementReasonId)) {
						if (!sumInfo.isIsCacherunDisabled())
							return true;
					}
				}
			}
		}
		
		return false;
	}
	public Capability[] getCapability() {
		if (entity.getIsReady()) {
			ArrayList<Capability> capability = new ArrayList<Capability>();
			Capability capableSite = new Capability();
			ArrayList<String> container = new ArrayList<String>();
			long siteId = entity.getSiteId();
			List<SumInfo> sumInfos = SumInfo.DAO.get(SumInfo_.siteId, siteId, SumInfo_.isFtEnabled, true, SumInfo_.isReady, true);
			for (SumInfo sumInfo : sumInfos) {
				SumInfoExt sumInfoExt = new SumInfoExt();
				sumInfoExt.init(sumInfo, serviceContext);
				container.add(sumInfoExt.getContainerName());
			}
			if (container.size() > 0) {
				capableSite.setName("CHALLENGE_DEPOSIT_VERIFICATION");
				capableSite.setContainer(container.toArray(new String[container.size()]));
				capability.add(capableSite);
				return capability.toArray(new Capability[capability.size()]);
			}
		}
		return null;
	}

	public String getContainerAttributes() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		boolean addCont = false;
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			List<SumInfoExt> sumInfoExts2 = sortSumInfosBasedOnTagIdOrder(getSumInfoExts());
			List<Long> taglist = new ArrayList<Long>();
			List<Long> addedTaglist = new ArrayList<Long>();
			List<Long> suminfoList = new ArrayList<Long>();
			if (sumInfoExts2 != null && !sumInfoExts2.isEmpty()) {
				for (SumInfoExt sumInfoExt : sumInfoExts2) {
					if (sumInfoExt.getSumInfo().getIsReady()) {
						taglist.add(sumInfoExt.getSumInfo().getTagId());
					}
					suminfoList.add(sumInfoExt.getSumInfo().getSumInfoId());
				}
				List<Long> enabledSuminfoList = new ArrayList<Long>();
				if (!suminfoList.isEmpty()) {
					List<SumInfo> enabledSuminfos = getEnabledSuminfos(suminfoList);
					if (enabledSuminfos != null && !enabledSuminfos.isEmpty()) {
						for (SumInfo sumInfo : enabledSuminfos) {
							enabledSuminfoList.add(sumInfo.getSumInfoId());
						}
					}
				}
				int i = 0;
				for (SumInfoExt sumInfoExt : sumInfoExts2) {
					boolean addContainer = true;
					SumInfo sumInfo = sumInfoExt.getSumInfo();
					long tagId = sumInfoExt.getSumInfo().getTagId();
					if (addedTaglist.contains(tagId))
						continue;
					if (!sumInfoExt.getSumInfo().getIsReady()) {
						addContainer = false;
						if (!taglist.contains(tagId))
							addContainer = addDisabledContainer(sumInfoExt.getSumInfo().getSumInfoId());
					}
					if (addContainer) {
						if (i > 0)
							builder.append(",");
						i++;
						ObjectNode createObjectNode = mapper.createObjectNode();
						ContainerAttributes containerAttributes = new ContainerAttributes();
						containerAttributes.setId(sumInfo.getSumInfoId());
						String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
						if (userType != null && "tier2".equalsIgnoreCase(userType)) {
							containerAttributes.setAgentName(sumInfo.getClassName());
							AdditionalInformation[] additionalInformation = sumInfoExt.getAdditionalInformation();
							containerAttributes.setAdditionalInformation(additionalInformation);
						}
						containerAttributes.setIsAutoRefreshEnabled(sumInfoExt.isAutorefreshEnabled());
						
						String name = sumInfoExt.getName(getPrimaryLocale());
						containerAttributes.setName(replace(name, "", replaceChars));
						boolean contains = enabledSuminfoList.contains(Long.valueOf(sumInfo.getSumInfoId()));
						containerAttributes.setStatus(sumInfoExt.getSuminfoStatus(sumInfoExt, contains));
						containerAttributes.setNumberOfTransactionDays(sumInfoExt.getNumberOfTransactionDays());
						Date rowLastUpdated = sumInfo.getRowLastUpdated();
						if (rowLastUpdated != null) {
							SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
							df.setTimeZone(TimeZone.getTimeZone("UTC"));
							containerAttributes.setLastModified(df.format(rowLastUpdated));
						}
						String containerName = sumInfoExt.getContainerName();
						containerName = getCamelCapitalize(containerName);
						createObjectNode.put(containerName, mapper.readTree(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(containerAttributes)));
						String containeStr = createObjectNode.toString();
						int indexOf = containeStr.indexOf("{");
						containeStr = new StringBuilder(containeStr).replace(indexOf, indexOf + 1, "").toString();
						int ind = containeStr.lastIndexOf("}");
						if (ind >= 0)
							containeStr = new StringBuilder(containeStr).replace(ind, ind + 1, "").toString();
						builder.append(containeStr);
						addCont = true;
						addedTaglist.add(tagId);
					}
				}
			}

		}
		catch (Exception e) {
			MessageController.log(FQCN, 509, "Error while getting the container attributes:" + entity.getSiteId() + ExceptionUtils.getFullStackTrace(e), MessageController.SERIOUS);
		}
		builder.append("}");
		if (addCont)
			return builder.toString();
		else
			return null;
	}

	public String getLoginHelp() {
		return getLoginHelp(getPrimaryLocale(), true);
	}

	private String getLoginHelp(Locale locale, boolean isLocalReq) {
		return getLoginForm() != null ? isLocalReq ? getMessageForKey(PREFIX, DBMCConstants.LF_HELP_TEXT_BUNDLE_NAME, Long.valueOf(getLoginForm().getLoginFormId()).toString(), locale)
				: getMessageForKey(PREFIX, DBMCConstants.LF_HELP_TEXT_BUNDLE_NAME, Long.valueOf(getLoginForm().getLoginFormId()).toString(), locale, null) : null;
	}

	public AdditionalDataSet[] getAdditionalDataSet() {
		List<AdditionalDataSet> adsets = new ArrayList<AdditionalDataSet>();
		if (entity.getIsReady()) {
			List<AdditionalDataSetAttributes> attrs = new ArrayList<AdditionalDataSetAttributes>();
			Long id = entity.getSiteId();
			List<SumInfo> sumInfosIav = SumInfo.DAO.get(SumInfo_.siteId, id, SumInfo_.isIavFastSupprtd, true, SumInfo_.isReady, true);
			if (sumInfosIav != null && !sumInfosIav.isEmpty()) {
				List<AdditionalDataSetAttributes> accProfileAttr = addIavKeys(sumInfosIav);
				if (accProfileAttr != null && !accProfileAttr.isEmpty())
					attrs.addAll(accProfileAttr);
				if (!attrs.isEmpty()) {
					AdditionalDataSet adset = new AdditionalDataSet();
					adset.setName("ACCT_PROFILE");
					adset.setAdditionalDataSetAttributes(attrs.toArray((new AdditionalDataSetAttributes[attrs.size()])));
					adsets.add(adset);
				}
			}
			List<SumInfo> sumInfos = SumInfo.DAO.get(SumInfo_.siteId, entity.getSiteId(), SumInfo_.isReady, true);
			if (sumInfos != null && !sumInfos.isEmpty()) {
				List<AdditionalDataSetAttributes> docDownloadAttr = addDocDownloadAttributes(sumInfos);
				if (docDownloadAttr != null && !docDownloadAttr.isEmpty()) {
					AdditionalDataSet adset = new AdditionalDataSet();
					adset.setName("DOCUMENT");
					adset.setAdditionalDataSetAttributes(docDownloadAttr.toArray((new AdditionalDataSetAttributes[docDownloadAttr.size()])));
					adsets.add(adset);
				}
				List<AdditionalDataSetAttributes> investmentOptions = addInvestmentOptions(sumInfos);
				if (investmentOptions != null && !investmentOptions.isEmpty()) {
					AdditionalDataSet adset = new AdditionalDataSet();
					adset.setName("INVESTMENT_OPTIONS");
					adset.setAdditionalDataSetAttributes(investmentOptions.toArray((new AdditionalDataSetAttributes[investmentOptions.size()])));
					adsets.add(adset);
				}
			}
		}

		return adsets.size() > 0 ? adsets.toArray(new AdditionalDataSet[adsets.size()]) : null;
	}

	private List<AdditionalDataSetAttributes> addInvestmentOptions(List<SumInfo> sumInfosList) {
		List<AdditionalDataSetAttributes> attrs = new ArrayList<AdditionalDataSetAttributes>();
		List<SumInfoParamKey> sipk = SumInfoParamKey.DAO.get(SumInfoParamKey_.sumInfoParamKeyName, "com.yodlee.core.investments.investment_option_supported");
		Long sumInfoParamKeyId = sipk.get(0).getSumInfoParamKeyId();
		ArrayList<String> container = new ArrayList<String>();
		for (SumInfo sumInfo : sumInfosList) {
			long sumInfoId = sumInfo.getSumInfoId();
			SumInfoParamValue value = SumInfoParamValue.DAO.getSingle(SumInfoParamValue_.sumInfoId, sumInfoId, SumInfoParamValue_.sumInfoParamKeyId, sumInfoParamKeyId);
			if (value != null && value.getParamValue() != null && value.getParamValue().equalsIgnoreCase("true"))
				container.add(getContainerName(sumInfo.getTagId()));
		}
		if (!container.isEmpty()) {
			AdditionalDataSetAttributes attr = new AdditionalDataSetAttributes();
			attr.setContainer(container.toArray(new String[container.size()]));
			attrs.add(attr);
		}

		return attrs;

	}

	private List<AdditionalDataSetAttributes> addDocDownloadAttributes(List<SumInfo> sumInfos) {
		List<AdditionalDataSetAttributes> attrs = new ArrayList<AdditionalDataSetAttributes>();
		Map<Long, SumInfo> sumInfoMap = new HashMap<Long, SumInfo>();
		List<Long> suminfoIds = new ArrayList<Long>();
		List<DocTypeSuminfoConfig> docTypes = new ArrayList<DocTypeSuminfoConfig>();
		for (SumInfo sumInfo : sumInfos) {
			long sumInfoId = sumInfo.getSumInfoId();
			suminfoIds.add(sumInfoId);
			sumInfoMap.put(sumInfo.getSumInfoId(), sumInfo);
			List<DocTypeSuminfoConfig> list = DocTypeSuminfoConfig.DAO.get(DocTypeSuminfoConfig_.sumInfoId, sumInfoId);
			if (list != null && !list.isEmpty())
				docTypes.addAll(list);
		}
		Map<String, KeyValue> map = new HashMap<String, KeyValue>();
		for (DocTypeSuminfoConfig docTypeSuminfoConfig : docTypes) {
			if (docTypeSuminfoConfig.isIsDocDwnldEn()) {
				if (docTypeSuminfoConfig.getDocTypeId() == 1) {
					KeyValue keyValue = map.get("TAX");
					if (keyValue == null) {
						keyValue = new KeyValue();
					}
					List<KeyValue> valueList = keyValue.getValueList();
					if (valueList == null)
						valueList = new ArrayList<KeyValue>();
					KeyValue value = new KeyValue();
					value.setValue(getContainerName(sumInfoMap.get(
							docTypeSuminfoConfig.getSumInfoId()).getTagId()));
					short numdays = docTypeSuminfoConfig
							.getDwnldDurationInDays();
					value.setNumdays((int) numdays);
					valueList.add(value);
					keyValue.setValueList(valueList);
					map.put("TAX", keyValue);
				} else if (docTypeSuminfoConfig.getDocTypeId() == 2) {
					KeyValue keyValue = map.get("STATEMENT");
					if (keyValue == null) {
						keyValue = new KeyValue();
					}
					List<KeyValue> valueList = keyValue.getValueList();
					if (valueList == null)
						valueList = new ArrayList<KeyValue>();
					KeyValue value = new KeyValue();
					value.setValue(getContainerName(sumInfoMap.get(
							docTypeSuminfoConfig.getSumInfoId()).getTagId()));
					short numdays = docTypeSuminfoConfig
							.getDwnldDurationInDays();
					value.setNumdays((int) numdays);
					valueList.add(value);
					keyValue.setValueList(valueList);
					map.put("STATEMENT", keyValue);
				} else if (docTypeSuminfoConfig.getDocTypeId() == 3) {
					KeyValue keyValue = map.get("EBILL");
					if (keyValue == null) {
						keyValue = new KeyValue();
					}
					List<KeyValue> valueList = keyValue.getValueList();
					if (valueList == null)
						valueList = new ArrayList<KeyValue>();
					KeyValue value = new KeyValue();
					value.setValue(getContainerName(sumInfoMap.get(
							docTypeSuminfoConfig.getSumInfoId()).getTagId()));
					short numdays = docTypeSuminfoConfig
							.getDwnldDurationInDays();
					value.setNumdays((int) numdays);
					valueList.add(value);
					keyValue.setValueList(valueList);
					map.put("EBILL", keyValue);
				}
			}
		}
		if (!map.isEmpty()) {
			Set<Entry<String, KeyValue>> entrySet = map.entrySet();
			for (Entry<String, KeyValue> entry : entrySet) {
				String key = entry.getKey();
				KeyValue value = entry.getValue();
				AdditionalDataSetAttributes attr = new AdditionalDataSetAttributes();
				attr.setName(key);
				List<KeyValue> valueList = value.getValueList();
				List<String> container = new ArrayList<String>();
				JSONArray js = new JSONArray();
				for (KeyValue keyValue : valueList) {
					container.add(keyValue.getValue());
					if (keyValue.getNumdays() != null && keyValue.getNumdays() > 0) {
						JSONObject obj = new JSONObject();
						obj.put("noOfDays", keyValue.getNumdays());
						JSONObject obj1 = new JSONObject();
						String containerval = getCamelCapitalize(keyValue.getValue());
						obj1.put(containerval, obj);
						js.add(obj1);
					}
				}
				attr.setContainer(container.toArray(new String[container.size()]));
				if (js.size() > 0) {
					attr.setContainerAttribute(js);
				}
				attrs.add(attr);
			}
		}
		return attrs;
	}

	private List<AdditionalDataSetAttributes> addIavKeys(List<SumInfo> sumInfosList) {
		List<AdditionalDataSetAttributes> attrs = new ArrayList<AdditionalDataSetAttributes>();
		for (KeyValue key : ivaSuminfoKeys) {
			ArrayList<String> container = new ArrayList<String>();
			for (SumInfo sumInfo : sumInfosList) {
				long sumInfoId = sumInfo.getSumInfoId();
				Long keyId = key.getId();
				SumInfoParamValue value = SumInfoParamValue.DAO.getSingle(SumInfoParamValue_.sumInfoId, sumInfoId, SumInfoParamValue_.sumInfoParamKeyId, keyId);
				if (value != null && value.getParamValue() != null && value.getParamValue().equalsIgnoreCase(key.getValue()))
					container.add(getContainerName(sumInfo.getTagId()));
			}
			if (!container.isEmpty()) {
				AdditionalDataSetAttributes attr = new AdditionalDataSetAttributes();
				attr.setName(key.getName());
				attr.setContainer(container.toArray(new String[container.size()]));
				attrs.add(attr);
			}
		}
		return attrs;
	}

	public String getProviderCategory() {
		String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
		if (userType != null && "tier2".equalsIgnoreCase(userType)) {
			Long providerCategoryId = getProviderCategoryId();
			if (providerCategoryId != null) {
				return suminfoCategoryMap.get(providerCategoryId);
			}
		}
		return null;
	}
	private Long getProviderCategoryId() {
			for (SumInfo sumInfo : getAllSumInfosForSite()) {
				if (entity.getIsReady()) {
					if (sumInfo.getIsReady()) {
						List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
						if (list != null && !list.isEmpty()) {
							SumInfoExtn sumInfoExtn = list.get(0);
							if (sumInfoExtn.getSuminfoCategoryId() != null) {
								categorySuminfoId = sumInfo.getSumInfoId();
								return sumInfoExtn.getSuminfoCategoryId();
							}
						}
					}
				}
				else {
					List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
					if (list != null && !list.isEmpty()) {
						SumInfoExtn sumInfoExtn = list.get(0);
						Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
						if (disbalementReasonId == null || !suminfoDisReasons.contains(disbalementReasonId)) {
							if (sumInfoExtn.getSuminfoCategoryId() != null) {
								categorySuminfoId = sumInfo.getSumInfoId();
								return sumInfoExtn.getSuminfoCategoryId();
							}
						}
					}
				}
			}
		return null;
	}

	private boolean showProviderCategory() {
		Long providerCategoryId = getProviderCategoryId();
		if (providerCategoryId != null && !"".equalsIgnoreCase(providerCategoryId.toString())) {
			if (providerCategoryId.longValue() == 4l) {
				return false;
			}
			else if (providerCategoryId.longValue() == 1l) {
				return true;
			}
			if (providerCategoryId.longValue() == 5l) {
				Long cobId = getSubrandId();
				List<SumInfoCobExtn> list = SumInfoCobExtn.DAO.get(SumInfoCobExtn_.sumInfoId,categorySuminfoId,SumInfoCobExtn_.cobrandId, cobId,SumInfoCobExtn_.isDeleted,Boolean.FALSE);
				if (list == null || list.isEmpty())
					return false;
			}
			else {
				String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
				if (userType != null && "tier2".equalsIgnoreCase(userType)) {
					return true;
				}
				boolean siteEnabled = isSiteEnabled();
				return siteEnabled;
			}
		}
		return true;
	}

	private String getContainerName(long tagId) {
		Tag tag = SumInfoExt.tagIdToTagName.get(tagId);
		return tag.getTag();
	}

	public AdditionalInformation[] getAdditionalInformation() {
		List<AdditionalInformation> information = new ArrayList<AdditionalInformation>();
		if (!entity.getIsReady()) {
			AdditionalInformation info = new AdditionalInformation();
			info.setInformationType("DISABLEMENT");
			SiteExtn siteExtn = SiteExtn.DAO.getSingle(SiteExtn_.siteId, entity.getSiteId());
			if (siteExtn != null) {
				Long disbalementReasonId = siteExtn.getDisbalementReasonId();
				if (disbalementReasonId != null) {
					String reason = siteDisablementReasonMap.get(disbalementReasonId);
					if (reason != null)
						info.setDisabledReason(reason);
				}
			}
			String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
			if (userType != null && "tier2".equalsIgnoreCase(userType)) {
				Criteria cri = new Criteria();
				cri.add(Notes_.classificationId, 3l);
				cri.add(Notes_.subClassificationId, 15l);
				cri.add(Notes_.primaryKeyId, entity.getSiteId());
				cri.add(Notes_.isDisabled, false);
				List<Notes> list = Notes.DAO.select(cri);
				if (list != null && !list.isEmpty()) {
					Notes notes = list.get(0);
					info.setNotes(notes.getContent());
				}
			}
			information.add(info);
		}

		return information.size() > 0 ? information.toArray(new AdditionalInformation[information.size()]) : null;
	}

	public String getAuthTypeExt() {
		String authType = null;
		if (entity.getIsOAuthEnabled() == null ? false : entity.getIsOAuthEnabled()) {
			authType = "OAUTH";
		}
		else if (getMfaType() != null) {
			authType = "MFA_CREDENTIALS";
		}
		else {
			authType = "CREDENTIALS";
		}
		return authType;
	}

	public String getAggregationType() {
		String userType = SearchSiteLocalStorage.getSearchSiteCriteria().get("userType");
		if (userType != null && "tier2".equalsIgnoreCase(userType)) {
			Set<Long> aggrList = new HashSet<Long>();
			Long aggTypeId = null;
			for (SumInfo sumInfo : getAllSumInfosForSite()) {
				if (entity.getIsReady()) {
					if (sumInfo.getIsReady()) {
						if (sumInfo.getAggregationTypeId() != null) {
							Long aggType = sumInfo.getAggregationTypeId();
							aggrList.add(aggType);
						}
						else
							aggrList.add(2l);
					}
				}
				else {
					List<SumInfoExtn> list = SumInfoExtn.DAO.get(SumInfoExtn_.sumInfoId, sumInfo.getSumInfoId());
					if (list != null && !list.isEmpty()) {
						SumInfoExtn sumInfoExtn = list.get(0);
						Long disbalementReasonId = sumInfoExtn.getDisbalementReasonId();
						if (disbalementReasonId == null || !suminfoDisReasons.contains(disbalementReasonId)) {
							if (sumInfo.getAggregationTypeId() != null) {
								Long aggType = sumInfo.getAggregationTypeId();
								aggrList.add(aggType);
							}
							else
								aggrList.add(2l);
						}
					}
				}
			}
			if (aggrList.size() > 1) {
				BaseAggrType baseAggrType = BaseAggrType.DAO.get(3l);
				return baseAggrType.getBaseAggrType();
				}
			else if(aggrList.size() > 0) {
				aggTypeId = new ArrayList<Long>(aggrList).get(0);
				aggTypeId = BaseAggregationTypes.getBaseAggTypeId(aggTypeId);
				logger.info("base aggregationtype Id:"+aggTypeId);
				if(aggTypeId!=null)
				{
					BaseAggrType baseAggrType = BaseAggrType.DAO.get(aggTypeId);
					return baseAggrType.getBaseAggrType();
				}
			}

			return "SCREEN_SCRAPING";
		}
		return null;
	}

	private static Long getSubrandId() {
		String cobrandId = SearchSiteLocalStorage.getSearchSiteCriteria().get("COBRAND_ID");
		if (cobrandId != null)
			return Long.valueOf(cobrandId);
		else
			return ContextAccessorUtil.getContext().getCobrandId();
	}

	public String getCamelCapitalize(String value) {
		return value.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
	}

	private List<SumInfoExt> sortSumInfosBasedOnTagIdOrder(List<SumInfoExt> unorderedSis) {
		List<SumInfoExt> orderedSumInfo = new ArrayList<SumInfoExt>(1);
		List<SumInfoExt> dummyUnorderedSis = new ArrayList<SumInfoExt>(1);
		for (SumInfoExt si : unorderedSis)
			dummyUnorderedSis.add(si);

		for (Long tagId : tagIdOrderList) {
			for (SumInfoExt orderedSi : unorderedSis) {
				if (tagId.equals(orderedSi.getSumInfo().getTagId())) {
					orderedSumInfo.add(orderedSi);
					dummyUnorderedSis.remove(orderedSi);
				}
			}
		}
		if (dummyUnorderedSis != null && !dummyUnorderedSis.isEmpty()) {
			for (SumInfoExt si : dummyUnorderedSis)
				orderedSumInfo.add(si);
		}

		return orderedSumInfo;
	}
	 public enum ImageType {
			LR(1), SVG(2);
			private Integer tablePk;
			private ImageType(Integer tablePk) {
				this.tablePk = tablePk;
			}
		};

	private Map<String, String> getLogoForLocale(long siteId, long locale) {
		StringBuffer v1URL = null;
		StringBuffer v2URL = null;
		Map<String, String> targetMap = new HashMap<String, String>();
		Criteria cri=new Criteria();
		cri.add(FiLogo_.siteId, siteId);
		cri.add(FiLogo_.localeId, locale);
		cri.add(FiLogo_.isDisabled, false);
		//List<FiLogo> fiLogoList = FiLogo.DAO.get(FiLogo_.siteId, siteId, FiLogo_.localeId, locale, FiLogo_.isDisabled, false);
		List<FiLogo> fiLogoList = FiLogo.DAO.select(cri);
		if (fiLogoList != null) {
			for (FiLogo fiLogo : fiLogoList) {
				if (fiLogo.getSumInfoId() == null && fiLogo.getImageContent() != null && (Long) (fiLogo.getImageContent().getImageContentId()) != null) {
					Long imageType = fiLogo.getImageTypeId();
					if (imageType != null && imageType.equals(new Long(ImageType.SVG.tablePk))) {
						if (v2URL == null) {
							v2URL = new StringBuffer("LOGO_");
							v2URL.append(entity.getSiteId()).append("_").append(locale).append("_");
							v2URL.append(ImageType.SVG.tablePk).append(".SVG");
							targetMap.put(SVG, v2URL.toString());
						}
					}
					else {
						if (v1URL == null) {
							v1URL = new StringBuffer("LOGO_");
							v1URL.append(entity.getSiteId()).append("_").append(locale).append("_");
							v1URL.append(ImageType.LR.tablePk).append(".PNG");
							targetMap.put(NONSVG, v1URL.toString());

						}
					}

				}

			}
		}
		return targetMap;

	}
	public enum ProviderId implements IFilterOption {
		DEFAULT, PROVIDERID;

		@Override
		public String getName() {
			return name();
		}
	}

	public enum AgentName implements IFilterOption {
		DEFAULT, AGENTNAME;

		@Override
		public String getName() {
			return name();
		}
	}

	public static void setSiteFilterOption() {
		FilterOptionHandler.initFilterOptionTypeMap(Site.class, ProviderId.class, new OptionFilter(ProviderId.DEFAULT, new Criteria()), new OptionFilter(ProviderId.PROVIDERID, getSiteIdCriteria()));
	}

	public static void setAgentFilterOption() {
		FilterOptionHandler.initFilterOptionTypeMap(Site.class, AgentName.class, new OptionFilter(AgentName.DEFAULT, new Criteria()), new OptionFilter(AgentName.AGENTNAME, getAgentnameCriteria()));
	}

	public static Criteria getSiteIdCriteria() {
		return new Criteria().andIn(Site_.siteId, SiteExt_.siteIds);
	}

	public static Criteria getAgentnameCriteria() {
		Criteria cri = new Criteria().and(SumInfo_.className, SiteExt_.agentname).join(SumInfo_.site);
		return cri;
	}

	public static Criteria isreadyCriteria() {
		Criteria criteria = new Criteria().rightJoin(SiteExtn_.site).and(Site_.isReady, true).orIn(SiteExtn_.disbalementReasonId, siteDisList).and(Site_.isReady,false);
		return criteria;
	}
	public List<SumInfo> getEnabledSuminfos(List<Long> suminfoIds) {
		Long cobrandId = getSubrandId();
		Criteria selectCriteria = new Criteria().andIn(new Criteria(SumInfoDefCat.class).join(SumInfoDefCat_.sumInfo)
				.add(new Criteria().andIn(SumInfo_.sumInfoId, suminfoIds)).join(SumInfoDefCat_.defCat).join(DefCat_.defTab)
				.and(DefTab_.cobrandId, cobrandId).and(DefTab_.isReady, Boolean.TRUE).and(DefCat_.isReady, Boolean.TRUE).and(SumInfoDefCat_.isReady, Boolean.TRUE));
		List<SumInfo> list = SumInfo.DAO.select(selectCriteria);
		
		return list;
	}
	
	public static String replace(String original, String replace, String pattern) {
		StringBuffer sb = new StringBuffer();
		if (pattern == null)
			pattern = "[\\s]+";
		try {
			Pattern pt = Pattern.compile(pattern);
			Matcher m = pt.matcher(original);
			while (m.find()) {
				m.appendReplacement(sb, replace);
			}
			m.appendTail(sb);
		} catch (Exception e) {
			MessageController.log(FQCN, 249, "Replace exception:" + original + " Exception:" + ExceptionUtils.getFullStackTrace(e), MessageController.SERIOUS);
		}
		return sb.toString();
	}
}
