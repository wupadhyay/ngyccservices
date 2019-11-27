/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.ycc.dapi.mybatis.bean.GlobalMessage;
import com.yodlee.ycc.dapi.mybatis.bean.GlobalMessageFilter;
import com.yodlee.ycc.dapi.mybatis.bean.NotificationDetail;
import com.yodlee.ycc.dapi.mybatis.bean.Provider;
import com.yodlee.yccdom.entity.GlobalMessageStatus;
import com.yodlee.yccdom.entity.NotificationCategory;
import com.yodlee.yccdom.entity.NotificationIssue;

/**
 * 
 * @author knavuluri
 *
 */
@Service
public class SiteNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(SiteNotificationService.class);
	private static Map<Long, String> statusMap = new HashMap<>();
	private static Map<Long, String> categoryMap = new HashMap<>();
	private static Map<String, String> issueTypeMap = new HashMap<>();
	private static boolean metaDataLoaded = false;

	public static void buildMetadata() {
		logger.debug("Loading the Global message meta data");
		List<NotificationCategory> notificationCategory = NotificationCategory.DAO.select(new Criteria());
		if (notificationCategory != null && !notificationCategory.isEmpty()) {
			for (NotificationCategory notificationCategory2 : notificationCategory) {
				categoryMap.put(notificationCategory2.getNotificationCatId(), notificationCategory2.getNotificationCatName());
			}
		}
		List<NotificationIssue> notificationIssue = NotificationIssue.DAO.select(new Criteria());
		for (NotificationIssue notificationIssue2 : notificationIssue) {
			String notificationIssueName = notificationIssue2.getNotificationIssueName().replaceAll("\\s+", "");
			notificationIssueName = getCamelCapitalize(notificationIssueName);
			issueTypeMap.put(String.valueOf(notificationIssue2.getNotificationIssueId()), notificationIssueName);
		}
		List<GlobalMessageStatus> status = GlobalMessageStatus.DAO.select(new Criteria());
		if (status != null && !status.isEmpty()) {
			for (GlobalMessageStatus globalMessageStatus : status) {
				statusMap.put(globalMessageStatus.getId(), globalMessageStatus.getName());
			}
		}
		logger.debug("Completed loading the Global message meta data");
		metaDataLoaded = true;
	}

	public static String getNotifications(GlobalMessageFilter filter, Connection connection) {
		if (!metaDataLoaded)
			buildMetadata();
		List<GlobalMessage> messagesList = (List<GlobalMessage>) MyBatisUtil.selectList("globalMessageMapper.globalMessages", filter, connection);
		if (messagesList != null && !messagesList.isEmpty()) {
			logger.debug("Notification messages count:" + messagesList.size());

			for (GlobalMessage globalMessage : messagesList) {
				if (!filter.isYodlee()) {
					globalMessage.setImpactedCobrand(null);
				}
				globalMessage.setCategory(categoryMap.get(globalMessage.getCategoryId()));
				globalMessage.setStatus(statusMap.get(globalMessage.getStatusId()));
				String issueTypeIds = globalMessage.getIssueTypeIds();
				Provider provider = globalMessage.getProvider();
				if (provider != null) {
					ArrayList<Provider> pList = new ArrayList<>();
					pList.add(provider);
					globalMessage.setImpactedProvider(pList);
				}
				if (issueTypeIds != null) {
					List<String> result = Arrays.asList(issueTypeIds.split("\\s*,\\s*"));
					ArrayList<String> issueList = new ArrayList<>();
					for (String id : result) {
						String issue = issueTypeMap.get(id);
						issueList.add(issue);
					}
					if (!issueList.isEmpty()) {
						globalMessage.setIssueType(issueList);
					}
				}
			}
			logger.info("Total notifications for:" + filter + " :" + messagesList.size());
			NotificationDetail notification = new NotificationDetail();
			notification.setNotification(messagesList);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			try {
				String finalResponse = mapper.writeValueAsString(notification);
				return finalResponse;
			} catch (Exception e) {
				logger.error("Exception while converting notifications to Json:" + ExceptionUtils.getFullStackTrace(e));
			}
		}
		return null;
	}

	public static String getCamelCapitalize(String value) {
		return value.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
	}
}
