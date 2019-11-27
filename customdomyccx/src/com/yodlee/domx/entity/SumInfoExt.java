/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.domx.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yodlee.dom.entity.DefCat_;
import com.yodlee.dom.entity.DefTab_;
import com.yodlee.dom.entity.Locale;
import com.yodlee.dom.entity.LoginForm;
import com.yodlee.dom.entity.Notes;
import com.yodlee.dom.entity.Notes_;
import com.yodlee.dom.entity.SumInfo;
import com.yodlee.dom.entity.SumInfoDefCat;
import com.yodlee.dom.entity.SumInfoDefCat_;
import com.yodlee.dom.entity.SumInfoParamKey;
import com.yodlee.dom.entity.SumInfoParamKey_;
import com.yodlee.dom.entity.SumInfoParamValue;
import com.yodlee.dom.entity.SumInfoParamValue_;
import com.yodlee.dom.entity.SumInfoRowDelParam;
import com.yodlee.dom.entity.SumInfoRowDelParam_;
import com.yodlee.dom.entity.SumInfoSptdLocale;
import com.yodlee.dom.entity.SumInfoSptdLocale_;
import com.yodlee.dom.entity.SumInfo_;
import com.yodlee.dom.entity.Tag;
import com.yodlee.dom.entity.TagRowDelParam;
import com.yodlee.dom.entity.TagRowDelParam_;
import com.yodlee.domx.base.ServiceBaseExt;
import com.yodlee.domx.entity.SiteExt.Status;
import com.yodlee.domx.utils.DBMCConstants;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.service.metamodel.ServiceMetamodelHelper;

/**
 * 
 * @author knavuluri
 * 
 */
public class SumInfoExt extends ServiceBaseExt<SumInfo> {

	private static List<Tag> tagsSupported;
	public static HashMap<Long, Tag> tagIdToTagName;
	private static Set<String> tagsNotSupported = new HashSet<String>();
	protected static final String PWD_HELP_URL_BUNDLE_NAME = "db.password_help_url.login_form";
	protected static final String LOGIN_URL_BUNDLE_NAME = "db.login_url.login_form";
	private static final String SUM_INFO_BUNDLE = "db.display_name.sum_info";
	public static Long directTransferSupportedParamKeyId;
	public static Long MFAInstantUpdateOnlyParamKeyId;
	private static final String PREFIX = "com.yodlee";
	private SiteExt siteExt;

	static {
		loadTags();
	}

	private static void loadTags() {
		List<Tag> tags = Tag.DAO.get();
		tagsSupported = tags;
		tagIdToTagName = new HashMap<Long, Tag>();
		for (Tag tagData : tagsSupported) {
			tagIdToTagName.put(tagData.getTagId(), tagData);
			tagsNotSupported.add(tagData.getTag());
		}
	}

	public SumInfoExt() {
		super(SumInfo.class);
	}

	@Override
	public boolean isCacheable() {
		return true;
	}

	public SumInfo getSumInfo() {
		return entity;
	}

	public SiteExt getSiteExt() {
		if (siteExt == null) {
			siteExt = new SiteExt();
			siteExt.initParent(parentBinding, ancestorAccessor, ancestorCriteria, isUseSdkDaoProxy);
			siteExt.init(entity.getSite(), null);
		}
		return siteExt;
	}

	public String getLoginUrlExt(Locale locale, boolean isLocalReq) {
		String result = null;
		if (!(entity.getLoginForm() == null || entity.getLoginForm().getLoginFormId() == 0)) {
			if (isLocalReq) {
				result = getMessageForKey(PREFIX, DBMCConstants.LOGIN_URL_BUNDLE_NAME, entity.getLoginFormId().toString(), locale);
			}
			else {
				result = getMessageForKey(PREFIX, DBMCConstants.LOGIN_URL_BUNDLE_NAME, entity.getLoginFormId().toString(), locale, locale);
			}
			if (result != null) {
				return result;
			}
			else {
				Long loginFormId = entity.getLoginFormId();
				String loginUrl = null;
				LoginForm loginform = LoginForm.DAO.get(loginFormId);
				if (loginform != null) {
					loginUrl = loginform.getLoginUrl();
					return loginUrl;
				}
			}
		}
		return null;
	}

	public String getContainerName() {
		Tag tag = tagIdToTagName.get(entity.getTagId());
//		String tagName = (tag.getTag() == null) ? null : ((ServiceMetamodelHelper.getContainerExtName(tag.getTag()) == null) ? tag.getTag()
//				: (ServiceMetamodelHelper.getContainerExtName(tag.getTag())));

		return tag.getTag();
	}

	@Override
	public SumInfoSptdLocale getDftSptdLocale() {
		return getSingle(SumInfoSptdLocale_.isPrimary, true);
	}

	@Override
	public List<SumInfoSptdLocale> getSptdLocales() {
		return get(SumInfoSptdLocale.class);
	}

	public long getServiceId() {
		return id;
	}

	public Boolean isAutorefreshEnabled() {
		return entity.isIsCacherunDisabled();
	}

	public String getName(Locale locale) {
		String name = getMessageForKey(PREFIX, SUM_INFO_BUNDLE, Long.valueOf(entity.getSumInfoId()).toString(), locale, locale);
		if (name == null)
			name = getMessageForKey(PREFIX, SUM_INFO_BUNDLE, Long.valueOf(entity.getSumInfoId()).toString(), locale);
		return name;
	}

	public String getStatus(Long cobrandId) {
		boolean isready = entity.getIsReady() == null ? false : entity.getIsReady();
		boolean enabled = isSuminfoEnabled(cobrandId);
		boolean isbeta = entity.isIsBeta();
		if (enabled && !isready)
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

		return null;
	}

	public boolean isSuminfoEnabled(Long cobrandId) {
		Criteria selectCriteria = new Criteria().andIn(new Criteria(SumInfoDefCat.class).join(SumInfoDefCat_.sumInfo)
				.add(new Criteria().and(SumInfo_.isReady, Boolean.TRUE).and(SumInfo_.sumInfoId, entity.getSumInfoId())).join(SumInfoDefCat_.defCat).join(DefCat_.defTab)
				.and(DefTab_.cobrandId, cobrandId).and(DefTab_.isReady, Boolean.TRUE).and(DefCat_.isReady, Boolean.TRUE).and(SumInfoDefCat_.isReady, Boolean.TRUE));
		List<SumInfo> list = SumInfo.DAO.select(selectCriteria);
		if (list != null && !list.isEmpty())
			return true;
		return false;
	}

	public Long getNumberOfTransactionDays() {
		long sumInfoId = entity.getSumInfoId();
		long tagId = entity.getTagId();
		List<SumInfoRowDelParam> sdParamList = SumInfoRowDelParam.DAO.get(SumInfoRowDelParam_.sumInfoId, sumInfoId);
		TagRowDelParam tagRowDelParam = TagRowDelParam.DAO.getSingle(TagRowDelParam_.tagId, tagId);
		Long maxTxnSelectionDuration = null;
		if (sdParamList != null && !sdParamList.isEmpty()) {
			SumInfoRowDelParam sumInfoRowDelParam = sdParamList.get(0);
			maxTxnSelectionDuration = sumInfoRowDelParam.getMaxTxnSelectionDuration();
		}
		if (maxTxnSelectionDuration == null && tagRowDelParam != null) {
			maxTxnSelectionDuration = tagRowDelParam.getMaxTxnSelectionDuration();
		}
		if (maxTxnSelectionDuration == null) {
			SumInfoParamKey rtnParamKey = SumInfoParamKey.DAO.getSingle(SumInfoParamKey_.sumInfoParamKeyName, "com.yodlee.contentservice.transactions.max_txn_available_duration");
			if (rtnParamKey != null) {
				String sumInfoParamMaxTxnDuration = rtnParamKey.getDefaultValue();
				Long sumInfoParamKeyId = rtnParamKey.getSumInfoParamKeyId();
				SumInfoParamValue rtnParamValue = SumInfoParamValue.DAO.getSingle(SumInfoParamValue_.sumInfoId, sumInfoId, SumInfoParamValue_.sumInfoParamKeyId, sumInfoParamKeyId);
				if (rtnParamValue != null) {
					String paramValue = rtnParamValue.getParamValue();
					maxTxnSelectionDuration = Long.parseLong(paramValue);
				}
				else
					maxTxnSelectionDuration = Long.parseLong(sumInfoParamMaxTxnDuration);
			}
		}

		return maxTxnSelectionDuration;
	}

	public AdditionalInformation[] getAdditionalInformation() {
		List<AdditionalInformation> information = new ArrayList<AdditionalInformation>();
		if (!entity.getIsReady()) {
			Criteria cri = new Criteria();
			cri.add(Notes_.classificationId, 1l);
			cri.add(Notes_.subClassificationId, 16l);
			cri.add(Notes_.primaryKeyId, entity.getSumInfoId());
			cri.add(Notes_.isDisabled, false);
			List<Notes> list = Notes.DAO.select(cri);
			if (list != null && !list.isEmpty()) {
				Notes notes = list.get(0);
				AdditionalInformation info = new AdditionalInformation();
				info.setInformationType("DISABLEMENT");
				if (notes.getContent() != null)
					info.setNotes(SiteExt.replace(notes.getContent(), "", SiteExt.replaceChars));
				information.add(info);
			}
		}

		return information.size() > 0 ? information.toArray(new AdditionalInformation[information.size()]) : null;
	}
	public String getSuminfoStatus(SumInfoExt sumInfoExt, boolean enabled)
	  {
	    SumInfo sumInfo = sumInfoExt.getSumInfo();
	    boolean isready = (sumInfo.getIsReady() == null) ? false : sumInfo.getIsReady().booleanValue();
	    boolean isbeta = sumInfo.isIsBeta();
	    if ((enabled) && (!isready))
	      return SiteExt.Status.Invisible.name();
	    if ((enabled) && (isbeta))
	      return SiteExt.Status.Beta.name();
	    if (enabled)
	      return SiteExt.Status.Supported.name();
	    if ((isready) && (isbeta))
	      return SiteExt.Status.UnsupportedBeta.name();
	    if (isready)
	      return SiteExt.Status.Unsupported.name();
	    if (!isready) {
	      return SiteExt.Status.Unavailable.name();
	    }
	    return null;
	  }

}
