/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.subbrand;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.dom.entity.Cobrand;
import com.yodlee.dom.entity.Cobrand_;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.dao.DAO;
import com.yodlee.framework.runtime.dao.Record;
import com.yodlee.framework.runtime.shared.context.CobrandContextAccessor;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.framework.runtime.shared.dao.Enums.Op;
import com.yodlee.nextgen.jdbc.DBPoolInitInfo;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.yccdom.entity.CobrandEnvMap;
import com.yodlee.yccdom.entity.CobrandEnvMap_;
import com.yodlee.yccdom.entity.YccCobrand_;

/**
 * 
 * @author knavuluri
 * 
 */
@Configuration
@EnableScheduling
public class SubbrandPublishingListener {

	private static final Logger logger = LoggerFactory.getLogger(SubbrandPublishingListener.class);

	//@Scheduled(initialDelay = 3600000, fixedDelayString = "${config.subrrand.publish.interval}000")
	public void publish() {
		logger.debug("Invoking  the subbrand publishing:" + new Date());
		try {
			doElection(false);
		}
		catch (Exception e) {
			logger.error("Error while publishing sub-brands:" + ExceptionUtils.getFullStackTrace(e));
		}
		logger.debug("Subbrand publishing completed:" + new Date());
	}

	public static synchronized void doElection(boolean loadAll) {
		Criteria criteria = new Criteria();
		criteria.add(YccCobrand_.isChannel, Boolean.TRUE);
		criteria.add(CobrandEnvMap_.destinationEnvironment, "PRODUCTION");
		criteria.add(CobrandEnvMap_.destinationDb, "OLTP");
		criteria.join(YccCobrand_.yccCobrandId, CobrandEnvMap_.sourceId);
		criteria.select(CobrandEnvMap_.destinationId);
		criteria.select(CobrandEnvMap_.sourceId);
		Map<Long, Long> channelMap = new HashMap<Long, Long>();
		List<Record> channelList = DAO.selectRecords(CobrandEnvMap.class, criteria);
		List<Long> cobrandIds2 = DBPoolInitInfo.getCobrandIds();
		if (channelList != null && !channelList.isEmpty()) {
			for (Record fi : channelList) {
				long cobrandId = fi.getLong(1);
				long yccCobrandId = fi.getLong(2);
				if (cobrandIds2.contains(cobrandId))
					channelMap.put(yccCobrandId, cobrandId);
			}
		}
		logger.debug("Channel map for subbrand publish:"+channelMap);
		Criteria cri = new Criteria();
		cri.add(YccCobrand_.contentStatusId, 3l);
		cri.add(YccCobrand_.deploymentStatusId,1l);
		cri.or(YccCobrand_.deploymentStatusId,4l);// (1,4)
		cri.add(CobrandEnvMap_.destinationEnvironment, "PRODUCTION");
		cri.add(CobrandEnvMap_.destinationDb, "OLTP");
		cri.add(YccCobrand_.isChannel, Boolean.FALSE);
		if (!loadAll) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR, -1);
			Date date = cal.getTime();
			cri.add(YccCobrand_.rowLastUpdated, Op.gt, date);
		}
		cri.join(YccCobrand_.yccCobrandId, CobrandEnvMap_.sourceId);
		cri.select(CobrandEnvMap_.destinationId);
		cri.select(YccCobrand_.channelId);
		List<Record> list = DAO.selectRecords(CobrandEnvMap.class, cri);
		if (list != null && !list.isEmpty()) {
			for (Record fi : list) {
				long subbrandId = fi.getLong(1);
				long channelId = fi.getLong(2);
				Long channelCobrandId = channelMap.get(channelId);
				logger.debug("Loading Subbrand for subbrand publish:"+subbrandId);
				if (channelCobrandId != null) {
					try {
						ContextAccessorUtil.setContext(subbrandId, 0l, null);		
						CobrandContextAccessor.cobrandId = subbrandId;
						Criteria cobCri = new Criteria();
						cobCri.add(Cobrand_.cobrandId, subbrandId);
						cobCri.add(Cobrand_.cobrandStatusId, 1l);
						List<Cobrand> cobList = Cobrand.DAO.select(cobCri);
						if (cobList != null && !cobList.isEmpty()) {
							Cobrand cobrand = cobList.get(0);
							CobrandInfo cobInfo = new CobrandInfo();
							cobInfo.setCobrandId(Long.valueOf(cobrand.getCobrandId()));
							cobInfo.setCobrandStatusId(cobrand.getCobrandStatusId());
							cobInfo.setChannelId(cobrand.getChannelId());
							cobInfo.setCreated(cobrand.getCreated());
							cobInfo.setLastUpdated(cobrand.getLastUpdated());
							cobInfo.setIsChannel(cobrand.getIsChannel());
							cobInfo.setName(cobrand.getName());
							cobInfo.setIavEnabled(CobrandUtil.isIAVCobrand(subbrandId));
							cobInfo.setSlmrEnabled(CobrandUtil.isSLMRCobrand(subbrandId,channelId));
							cobInfo.setBalanceRefreshEnabled(CobrandUtil.pfmIavReport.get(subbrandId) != null ? CobrandUtil.pfmIavReport.get(subbrandId).equalsIgnoreCase("BALANCE_REFRESH") ? true : false : false);
							cobInfo.setIavCacheRefreshEnabled(CobrandUtil.pfmIavReport.get(subbrandId) != null ? CobrandUtil.pfmIavReport.get(subbrandId).equalsIgnoreCase("PFM_REFRESH") ? true : false : false);
							if (CobrandUtil.getCobrand(subbrandId) == null) {
								SubrandPublisher publisher = new SubrandPublisher();
								publisher.trigger(subbrandId);
							}
							CobrandUtil.addCobrand(subbrandId, cobInfo);
							DBPoolInitInfo.addSubrandChannel(cobrand.getCobrandId(), channelCobrandId);
						}
						logger.debug("Loaded Subbrand for subbrand publish:"+subbrandId);
					}
					catch (Exception e) {
						logger.error("Error while loading the sub-brand:" + subbrandId + " info from OLTP DB:" + ExceptionUtils.getFullStackTrace(e));
					}
					finally {
						ContextAccessorUtil.unsetContext();
						CobrandContextAccessor.cobrandId = null;
						PersistenceContext.getInstance().closeEntityManager("dom");
					}
				}

			}
		}
		//logger.debug("All the subbrands:" + CobrandUtil.cobrandMap);
		logger.debug("All the subbrands DB pool map:" + DBPoolInitInfo.getSubrandChannelMap());
	}

}
