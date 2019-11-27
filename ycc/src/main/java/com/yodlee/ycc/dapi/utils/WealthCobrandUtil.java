/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.google.gson.Gson;
import com.yodlee.ycc.dapi.bean.CobrandInfo;

@Component
public class WealthCobrandUtil {

	private static final Logger logger = LoggerFactory.getLogger(WealthCobrandUtil.class);
	private static Map<Long, CobrandInfo> wealthcobrandMap = new ConcurrentHashMap<Long, CobrandInfo>();

	private static Date startDate = new Date();

	/*
	 * static { String cobinterval =
	 * MiscUtil.getPropertyValue("com.yodlee.app.reconCobcacheInterval", false); if
	 * (cobinterval != null) cobRefreshDuration = Long.valueOf(cobinterval);
	 * logger.info("Loading all the wealth recon enabled cobrands");
	 * loadAllWealthCobrands();
	 * logger.info("Completed loading all the wealth recon enabled cobrands"); }
	 */

	@PostConstruct
	public void init() {
		logger.info("Loading all the cobrands");
		loadAllWealthCobrands();
		logger.info("Completed loading all the cobrands");
	}

	// @Scheduled(initialDelay =0, fixedDelayString =
	// "${com.yodlee.app.reconCobcacheInterval}000")3600000
	@Scheduled(fixedDelayString = "${com.yodlee.app.reconCobcacheInterval:86400000}", initialDelayString = "${com.yodlee.app.reconCobcacheInterval:0}")
	public void reload() {
		logger.info("Loading all the cobrands");
		loadAllWealthCobrands();
		logger.info("Completed loading all the cobrands");
	}

	private static void loadAllWealthCobrands() {
		// TODO Auto-generated method stub
		synchronized (WealthCobrandUtil.class) {

			Map<Long, CobrandInfo> cobrandMap = CobrandUtil.getCobrandMap();
			logger.info("size of cobrands " + cobrandMap.size());
			for (Long cobrandId : cobrandMap.keySet()) {
				try {
					ContextAccessorUtil.setContext(cobrandId, 0l, null);
					if (isWealthReconEnabled(cobrandId, null)) {
						logger.info("wealth recon is enabled for" + cobrandId);
						CobrandInfo cobInfo = cobrandMap.get(cobrandId);
						wealthcobrandMap.put(cobrandId, cobInfo);
					}
				} catch (Exception e) {
					logger.error("Error while getting wealth cobrand" + ExceptionUtils.getFullStackTrace(e));
				} finally {
					ContextAccessorUtil.unsetContext();
					PersistenceContext.getInstance().closeEntityManager("dom");
				}
			}
			logger.info("wealth recon is enabled for" + wealthcobrandMap.keySet());

		}
	}

	public static String getAllWealthReconEnabledCobrands() {
		String json = null;
		Gson gson = new Gson();
		if (wealthcobrandMap == null || wealthcobrandMap.isEmpty()) {
			logger.info("wealth cobrand map is empty");
			loadAllWealthCobrands();
		}
		if (!wealthcobrandMap.isEmpty()) {
			json = gson.toJson(wealthcobrandMap.values());
			json = "{\"reconCobrandInfo\":" + json + "}";
		}
		return json;
	}

	public static boolean isWealthReconEnabled(Long cobrandId, Long channelId) {
		return CobrandUtil.getParamValue(cobrandId, 6300l, channelId);
	}

}
