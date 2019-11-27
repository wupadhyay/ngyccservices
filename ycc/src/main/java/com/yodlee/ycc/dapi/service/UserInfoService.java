/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.ycc.dapi.bean.CobrandEmailInfo;
import com.yodlee.ycc.dapi.bean.UserInfo;
import com.yodlee.ycc.dapi.bean.UserInfoResult;

@Service
public class UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
	
	@Autowired
	private HttpServletRequest request;
	
	public String getUserInformation(String cobrandList) throws Exception {
		logger.info("Inside getUserInformation");
		String response = null;
		Set<UserInfoResult> cobList = null;
		String query = "select distinct email,cobrand_id as cobrandId from user_info where cobrand_id in ("
				+ cobrandList + ")";
		String cobId = (String) request.getAttribute("cobId");
		logger.debug("Cobrand Id from request in UserInfoService-"+cobId+" long value for same-"+Long.valueOf(cobId));
		try {
			ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, null);
			Connection connection = PersistenceContext.getInstance().getConnection("yccdom");
			logger.info("Inside getUserInformation to get connection--");
			DataSource dataSource = new SingleConnectionDataSource(connection, true);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			cobList = new HashSet<UserInfoResult>();
			cobList = new HashSet<>(
					jdbcTemplate.query(query, new BeanPropertyRowMapper<>(UserInfoResult.class)));
			logger.info("Coblist here is printing--" + cobList.toString());

		} catch (Exception e) {
			logger.error("Error while searching the notifications:" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			ContextAccessorUtil.unsetContext();
			PersistenceContext.getInstance().closeEntityManager("yccdom");
		}
		HashMap<Long, List<String>> cobEmailMap = new HashMap<>();

		for (UserInfoResult siteAlertUserInfo : cobList) {
			Long cobrandId = siteAlertUserInfo.getCobrandId();
			String email = siteAlertUserInfo.getEmail();
			List<String> emailList = null;
			if (cobEmailMap.containsKey(cobrandId)) {
				emailList = cobEmailMap.get(cobrandId);
				emailList.add(email);
				cobEmailMap.put(cobrandId, emailList);
			} else {
				emailList = new ArrayList<>();
				emailList.add(email);
				cobEmailMap.put(cobrandId, emailList);
			}
		}
		CobrandEmailInfo cobEmailInfo = new CobrandEmailInfo();
		List<UserInfo> cobEmailList = new ArrayList<>();
		for (Long cobrandId : cobEmailMap.keySet()) {
			UserInfo siteAlertUserInfo = new UserInfo();
			siteAlertUserInfo.setCobrandId(cobrandId);
			siteAlertUserInfo.setEmailList(cobEmailMap.get(cobrandId));
			cobEmailList.add(siteAlertUserInfo);
		}
		cobEmailInfo.setUsers(cobEmailList);
		response = convertObjToJson(cobEmailInfo);
		logger.debug("Response to be returned--" + response);
		return response;
	}
		
	public static String convertObjToJson(Object obj) {
		ObjectMapper converter = new ObjectMapper();
		String response = null;
		try {
			response = converter.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("Illegal request paramaters are passed: " + e.getMessage());
		}
		return response;

	}

}
