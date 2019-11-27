/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.dom.entity.CobParam;
import com.yodlee.dom.entity.CobParam_;
import com.yodlee.dom.entity.Cobrand;
import com.yodlee.dom.entity.CobrandProduct;
import com.yodlee.dom.entity.CobrandProduct_;
import com.yodlee.dom.entity.ProductCatalog;
import com.yodlee.framework.common.util.To;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.shared.context.CobrandContextAccessor;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.nextgen.jdbc.DBPoolInitInfo;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.context.ApplicationContextProvider;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.subbrand.SubbrandPublishingListener;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;
import com.yodlee.yccdom.entity.FiRepository;
import com.yodlee.yccdom.entity.FiRepository_;

/**
 * 
 * @author knavuluri
 * 
 */
@Service
public class CobrandUtil {

	private static final Logger logger = LoggerFactory.getLogger(CobrandUtil.class);
	
	@Autowired
	RefreshStatsDbService dbService;

	private static Map<Long, CobrandInfo> cobrandMap = new ConcurrentHashMap<Long, CobrandInfo>();
	
	private static Map<Long, CobrandInfo> consolidatedCobrandMap = new ConcurrentHashMap<Long, CobrandInfo>();
	
	public static Map<Long, String> pfmIavReport = new ConcurrentHashMap<Long, String>();
	
	private static Long yodleeCobrandId = 10000004l;
	
	private static long CATier2Cobrand = 10008768;
	
	@Value("${com.yodlee.email.trigger}")
	private  boolean emailTriggerEnabled;

	@PostConstruct
	public void init(){
		logger.info("Loading all the cobrands");
		loadAllCobrands();
		logger.info("Completed loading all the cobrands");
	}
	@Scheduled(initialDelayString ="${config.subrrand.publish.interval}000", fixedDelayString = "${config.subrrand.publish.interval}000")
	public void reload(){
		logger.info("Loading all the cobrands");
		loadAllCobrands();
		logger.info("Completed loading all the cobrands");
	}
	public static Map<Long, CobrandInfo> getCobrandMap()
	{
		return cobrandMap;
	}
	public static List<Cobrand> getCobrands() {
		List<Cobrand> list = new ArrayList<Cobrand>();
		try {
			Map<String, List<Long>> conext = CobrandContextAccessor.getConext();
			Set<Entry<String, List<Long>>> entrySet = conext.entrySet();
			for (Entry<String, List<Long>> entry : entrySet) {
				List<Long> value = entry.getValue();
				for (Long cobrandId : value) {
					try {
						ContextAccessorUtil.setContext(cobrandId, 0l, null);
						CobrandContextAccessor.cobrandId = cobrandId;
						Cobrand cobrand = Cobrand.DAO.get(cobrandId);
						if (cobrand != null && cobrand.getCobrandStatusId() != null && cobrand.getCobrandStatusId().equals(1l)) {
							list.add(cobrand);
						}
					} catch (Throwable e) {
						logger.error("Exception while loading cobrands:" + ExceptionUtils.getFullStackTrace(e));
					} finally {
						ContextAccessorUtil.unsetContext();
						CobrandContextAccessor.cobrandId = null;
						PersistenceContext.getInstance().closeEntityManager("dom");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception while loading cobrands:" + ExceptionUtils.getFullStackTrace(e));
			CobrandContextAccessor.cobrandId = null;
		}
		try {
			ApplicationContextProvider.getContext().getBean(CobrandUtil.class).loadPfmIAVReport();
			SubbrandPublishingListener.doElection(true);
		} catch (Exception e) {
			logger.error("Exception while loading subbrands:" + ExceptionUtils.getFullStackTrace(e));
		}
		return list;
	}

	public static void loadAllCobrands() {
		synchronized (CobrandUtil.class) {
				boolean isYodlee  = false;
				logger.info("Loading the cobrands");
				List<Cobrand> allCobrands = getCobrands();
				if (allCobrands != null && !allCobrands.isEmpty()) {
					for (Cobrand cobrand : allCobrands) {
						List<Long> cobrandIds2 = DBPoolInitInfo.getCobrandIds();
						Long cobrandId = Long.valueOf(cobrand.getCobrandId());
						Long channelId = cobrand.getChannelId();
						if (cobrandIds2.contains(cobrandId) || (channelId != null && cobrandIds2.contains(channelId))) {
							if (cobrand.getCobrandStatusId() != null && cobrand.getCobrandStatusId().equals(1l)) {
								CobrandInfo cobInfo = new CobrandInfo();
								cobInfo.setCobrandId(cobrand.getCobrandId());
								cobInfo.setCobrandStatusId(cobrand.getCobrandStatusId());
								cobInfo.setChannelId(cobrand.getChannelId());
								cobInfo.setCreated(cobrand.getCreated());
								cobInfo.setLastUpdated(cobrand.getLastUpdated());
								cobInfo.setIsChannel(cobrand.getIsChannel());
								cobInfo.setName(cobrand.getName());
								cobInfo.setCacherunDisabled(cobrand.isIsCacherunDisabled());
								try{
									ContextAccessorUtil.setContext(cobrandId, 0l, null);
									cobInfo.setIavEnabled(isIAVCobrand(cobrandId));
									cobInfo.setSlmrEnabled(isSLMRCobrand(cobrandId,channelId));
									cobInfo.setBalanceRefreshEnabled(pfmIavReport.get(cobrandId) != null ? pfmIavReport.get(cobrandId).equalsIgnoreCase("BALANCE_REFRESH") ? true : false : false);
									cobInfo.setIavCacheRefreshEnabled(pfmIavReport.get(cobrandId) != null ? pfmIavReport.get(cobrandId).equalsIgnoreCase("PFM_REFRESH") ? true : false : false);
								}catch(Exception e){
									logger.error("Exception while getting the cobrand info:"+ExceptionUtils.getFullStackTrace(e));
								}finally{
									ContextAccessorUtil.unsetContext();
									PersistenceContext.getInstance().closeEntityManager("dom");
								}
								
								if (!isYodlee) {
									
								if (cobrand.getCobrandId() == yodleeCobrandId.longValue()
										|| cobrand.getCobrandId() == CATier2Cobrand) {
									cobInfo.setYodlee(true);
									isYodlee = true;
									yodleeCobrandId = cobrand.getCobrandId();
								}
									/*Criteria c = new Criteria();
									c.add(FiRepository_.cobrandId, To.long_(cobrand.getCobrandId()));
									List<FiRepository> fis = null;
									try {
										fis = FiRepository.DAO.select(c);
										if (fis != null && fis.size() > 0) {
											FiRepository fiRepository = fis.get(0);
											cobInfo.setYodlee(fiRepository.isIsYodleeCobrand());
											if (fiRepository.isIsYodleeCobrand()) {
												isYodlee = true;
												yodleeCobrandId = fiRepository.getCobrandId();
											}
										}
									}
									catch (Exception e) {
										logger.error("Error while getting the Firepository:" + ExceptionUtils.getFullStackTrace(e));
									}*/
								}
								cobrandMap.put(cobrand.getCobrandId(), cobInfo);
								if (cobrand.getChannelId() == null)
									consolidatedCobrandMap.put(cobrand.getCobrandId(), cobInfo);
							}
						}
					}
					Collection<CobrandInfo> values = cobrandMap.values();
					for (CobrandInfo cobrandInfo : values) {
						Long channelId = cobrandInfo.getChannelId();
						if (channelId != null) {
							CobrandInfo cobrandInfo2 = consolidatedCobrandMap.get(channelId);
							if (cobrandInfo2 != null) {
								List<CobrandInfo> subbrandList = cobrandInfo2.getSubbrands();
								if (subbrandList == null)
									subbrandList = new ArrayList<CobrandInfo>();
								subbrandList.add(cobrandInfo);
								cobrandInfo2.setSubbrands(subbrandList);
								if(cobrandInfo.isIavCacheRefreshEnabled())
									cobrandInfo2.setIavCacheRefreshEnabled(true);
								if(cobrandInfo.isBalanceRefreshEnabled())
									cobrandInfo2.setBalanceRefreshEnabled(true);
								consolidatedCobrandMap.put(channelId, cobrandInfo2);
							}
							else
								logger.info("Channel Id " + channelId + " is not available in the list while preparing the subbrand list");
						}
						
					}

				}
		}
	}

	public void loadPfmIAVReport() {
		try {
			logger.debug("Loading YCC_PFM_IAV_IDENTIFIER reoprt");
			RefreshStatsDbFilter refreshStatsDbFilter = new RefreshStatsDbFilter();
			refreshStatsDbFilter.setReportType(YSLConstants.YCC_PFM_IAV_IDENTIFIER);
			String pfmReport= dbService.getRefreshStatsData(refreshStatsDbFilter);
			logger.info("YCC_PFM_IAV_IDENTIFIER response:" + pfmReport);
			if (pfmReport != null) {
				JsonElement jsonElement = new JsonParser().parse(pfmReport.toString());
				JsonElement pfmReportElement = jsonElement.getAsJsonObject().get("results");
				JsonArray notificationArray = pfmReportElement.getAsJsonArray();
				int size = notificationArray.size();
				for (int i = 0; i < size; i++) {
					JsonObject jsonObject = notificationArray.get(i).getAsJsonObject();
					String cobId = jsonObject.get("cobrand_id").getAsString();
					String value = jsonObject.get("iav_cob_type").getAsString();
					pfmIavReport.put(Long.valueOf(cobId), value);
				}
			}
			logger.debug("Loaded YCC_PFM_IAV_IDENTIFIER reoprt");
		}
		catch (Exception e) {
			logger.error("Error while getting the data from YCC_PFM_IAV_IDENTIFIER splunk report:" + ExceptionUtils.getFullStackTrace(e));
		}

	}

	public static String getAllCobrands() {
		String json = null;
		Gson gson = new Gson();
		if (consolidatedCobrandMap == null || consolidatedCobrandMap.isEmpty()) {
			loadAllCobrands();
		}
		if (!consolidatedCobrandMap.isEmpty()) {
			json = gson.toJson(consolidatedCobrandMap.values());
			json = "{\"cobrandInfo\":" + json + "}";
		}
	//	logger.debug("Cobrand Json =" + json);
		return json;
	}

	public static boolean isYodlee(Long cobrandId) {
		if (cobrandMap == null || cobrandMap.isEmpty()) {
			loadAllCobrands();
		}
		CobrandInfo cobrandInfo = cobrandMap.get(cobrandId);
		if (cobrandInfo != null)
			return cobrandInfo.isYodlee();
		return false;
	}

	public static boolean isChannel(Long cobrandId) {
		if (cobrandMap == null || cobrandMap.isEmpty()) {
			loadAllCobrands();
		}
		CobrandInfo cobrandInfo = cobrandMap.get(cobrandId);
		if (cobrandInfo != null)
			return cobrandInfo.getIsChannel();
		return false;
	}
	public  String getCobrandInfo(Long cobrandId) {
		String json = null;
		CobrandInfo cobrandInfo = getCobrandInfoVO(cobrandId);
		if (cobrandInfo != null) {
			if(cobrandInfo.getIsChannel() != null && cobrandInfo.getIsChannel()){
				cobrandInfo = consolidatedCobrandMap.get(cobrandId);
			}
			cobrandInfo.setEmailTriggerEnabled(emailTriggerEnabled);
			Gson gson = new Gson();
			json = gson.toJson(cobrandInfo);
			json = "{\"cobrandInfo\":" + json + "}";
		}

		return json;
	}
	public static List<CobrandInfo> getSubbrandList(Long cobrandId) {
		CobrandInfo cobrandInfo = getCobrandInfoVO(cobrandId);
		if (cobrandInfo != null) {
			if(cobrandInfo.getIsChannel() && cobrandInfo.getIsChannel()){
				cobrandInfo = consolidatedCobrandMap.get(cobrandId);
			}
		}
		return cobrandInfo.getSubbrands();
	}
	public static CobrandInfo getCobrandInfoVO(Long cobrandId) {
		if (cobrandMap == null || cobrandMap.isEmpty()) {
			loadAllCobrands();
		}
		CobrandInfo cobrandInfo = cobrandMap.get(cobrandId);
		return cobrandInfo;
	}
	
	public static String getCobrandName(Long cobrandId) {
		String cobrandName = "";
		CobrandInfo cobrandInfo = getCobrandInfoVO(cobrandId);
		if (cobrandInfo != null) {
			cobrandName = cobrandInfo.getName();
		}
		return cobrandName;
	}

	public static boolean isIAVCobrand(Long cobrandId) {
		boolean iavEnabled = false;
		try {
			Criteria criteria = new Criteria();
			criteria.add(CobrandProduct_.cobrandId, cobrandId);
			List<CobrandProduct> cobProductList = CobrandProduct.DAO.select(criteria);
			logger.debug("For cobrand :"+cobrandId+" Cobrand product List:"+cobProductList);
			if (cobProductList != null)
				logger.debug(" Cobrand:"+cobrandId+" Cobrand product List:" + cobProductList.size());
			ArrayList<Long> pidList = new ArrayList<Long>();
			for (int i = 0; i < cobProductList.size(); i++) {
				pidList.add(cobProductList.get(i).getProductCatalogId());
			}
			logger.debug(" Cobrand:"+cobrandId+" Cobrand product List:" + pidList);
			if ((pidList.contains(ProductCatalog.getId("IAV")) || pidList.contains(ProductCatalog.getId("ACCOUNT_VERIFICATION")))) {
				iavEnabled = true;
			}
		}
		catch (Exception e) {
			logger.error("Error while getting the iav cobrandInfo:" + ExceptionUtils.getFullStackTrace(e));
		}
		return iavEnabled;
	}

	public static boolean isSLMRCobrand(Long cobrandId,Long channelId) {
		return getParamValue(cobrandId, 5949l,channelId);
	}
	   
	public static void addCobrand(Long cobrandId, CobrandInfo cobrandInfo) {
		if (cobrandId != null && cobrandInfo != null)
			cobrandMap.put(cobrandId, cobrandInfo);
	}

	public static CobrandInfo getCobrand(Long cobrandId) {
		if (cobrandId != null)
			return cobrandMap.get(cobrandId);
		return null;
	}

	public static boolean getParamValue(Long cobrandId, Long paramkeyId, Long channelId) {
		boolean keyEnabled = false;
		try {
			Criteria criteria = new Criteria();
			criteria.add(CobParam_.cobrandId, cobrandId);
			criteria.add(CobParam_.paramKeyId, paramkeyId);
			List<CobParam> list = CobParam.DAO.select(criteria);
			if (list != null && !list.isEmpty()) {
				for (CobParam cobParam : list) {
					if (cobParam.getParamValue().equalsIgnoreCase("TRUE"))
						keyEnabled = true;
				}
			}
			else if (channelId != null) {
				Criteria criteria1 = new Criteria();
				criteria1.add(CobParam_.cobrandId, channelId);
				criteria1.add(CobParam_.paramKeyId, paramkeyId);
				List<CobParam> list1 = CobParam.DAO.select(criteria1);
				if (list1 != null && !list1.isEmpty()) {
					for (CobParam cobParam : list1) {
						if (cobParam.getParamValue().equalsIgnoreCase("TRUE"))
							keyEnabled = true;
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("Error while getting the Parm key " + paramkeyId + " cobrandInfo:" + ExceptionUtils.getFullStackTrace(e));
		}
		return keyEnabled;
	}

	public static boolean validateSubbrand(Long channelId,Long subbrandId){
		List<CobrandInfo> subbrandList = getSubbrandList(channelId);
		if (subbrandList != null && !subbrandList.isEmpty()) {
			CobrandInfo info = new CobrandInfo();
			info.setCobrandId(Long.valueOf(subbrandId));
			if (!subbrandList.contains(info))
				throw new RefreshStatException("CobrandId is invalid");
		}
		else
			throw new RefreshStatException("CobrandId is invalid");
		return true;
	}

	public static boolean validateCobrand(Long logdinCobrandId, Long cobrandId) {
		CobrandInfo cobrandInfo = getCobrandInfoVO(Long.valueOf(cobrandId));
		if (cobrandInfo == null)
			throw new RefreshStatException("CobrandId is invalid");
		if (logdinCobrandId.longValue() != Long.valueOf(cobrandId).longValue()) {
			boolean yodlee = CobrandUtil.isYodlee(logdinCobrandId);
			if (!yodlee) {
				boolean channel = CobrandUtil.isChannel(logdinCobrandId);
				if (channel) {
					validateSubbrand(logdinCobrandId, Long.valueOf(cobrandId));
				}
				else
					throw new RefreshStatException("CobrandId is invalid");
			}
		}

		return true;
	}

	public static Long getYodleeCobrandId() {
		return yodleeCobrandId;
	}
}
