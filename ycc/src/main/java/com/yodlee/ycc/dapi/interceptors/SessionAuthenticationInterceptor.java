/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.interceptors;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yodlee.common.UserType;
import com.yodlee.commons.context.ThreadLocalContext;
import com.yodlee.commons.context.UserContext;
import com.yodlee.dom.entity.Mem;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.internal.persistence.accessor.UserAccessor;
import com.yodlee.nextgen.authentication.AuthenticationException;
import com.yodlee.nextgen.session.exceptions.InvalidSessionException;
import com.yodlee.nextgen.validation.session.SessionValidationServices;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.ycc.dapi.utils.MiscUtil;
import com.yodlee.ycc.dapi.utils.RestClientUtil;
import com.yodlee.ycc.notification.util.NotificationUtil;
import com.yodlee.yccdom.entity.UserInfo;
import com.yodlee.yccdom.entity.UserInfo_;

/**
 * 
 * @author knavuluri
 * 
 */

public class SessionAuthenticationInterceptor implements HandlerInterceptor {

	private static final String FQCN = SessionAuthenticationInterceptor.class.getName();
	private static final Logger logger = LoggerFactory.getLogger(SessionAuthenticationInterceptor.class);

	private RestTemplate restTemplate;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String subPath = request.getServletPath().substring(1);
		logger.debug("Calling Session authentication for " + subPath);
		if (subPath.contains("cobrand/login") || subPath.contains("cobrand/logout"))
			return true;
		if (request.getAttribute("Bearer") != null) {
			String singularityDomain = MiscUtil.getPropertyValue("com.yodlee.ycc.singularity.domain", false);
			String singularityValidate = MiscUtil.getPropertyValue("com.yodlee.ycc.singularity.validate", false);
			String token = (String) request.getAttribute("Bearer");
			RestClientUtil.getInstance().callEndPoint(singularityDomain + singularityValidate, token, HttpMethod.POST);
		} else {
			String cobId = (String) request.getAttribute("cobId");
			String appId = (String) request.getAttribute("appId");
			try {
				ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, appId);
				if (subPath.contains("user/login")) {
					boolean cobsessionValid = SessionValidationServices.validateExternalCoBrandSessionToken(
							request.getAttribute("cobSession").toString(), Long.valueOf(cobId));
					if (!cobsessionValid)
						throw new InvalidSessionException(FQCN, 50, "Invalid Cobrand Session");
				} else {
					validateUserSession(request, appId.trim(), Long.valueOf(cobId.trim()));
				}
			} finally {
				ContextAccessorUtil.unsetContext();
				PersistenceContext.getInstance().closeEntityManager("dom");
			}
			if (!subPath.contains("notification/provider")
					&& (subPath.contains("notification") || subPath.contains("usercontactinfo"))) {
				Long memId = (Long) request.getAttribute("memId");
				getUserInfo(cobId, memId);
			}
		}
		logger.debug("Session is valid for " + subPath);
		return true;
	}

	private void getUserInfo(String cobrandId, Long memId) {
		try {
			ContextAccessorUtil.setContext(Long.valueOf(cobrandId), 0l, null);
			UserInfo userInfo = UserInfo.DAO.getSingle(UserInfo_.memId, memId);
			if (userInfo != null) {
				boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobrandId));
				UserContext userContext = new UserContext();
				userContext.setEmail(userInfo.getEmail());
				userContext.setFirstName(userInfo.getFirstName());
				userContext.setLastName(userInfo.getLastName());
				userContext.setMemId(userInfo.getMemId());
				userContext.setPhone(userInfo.getPhone());
				userContext.setUserName(userInfo.getUserName());
				userContext.setUserTypeId(userInfo.getUserTypeId());
				userContext.setYodlee(yodlee);
				userContext.setCobrandId(userInfo.getCobrandId());
				ThreadLocalContext.setUserInfoContext(userContext);
			}
		} catch (Exception e) {
			logger.error("Error while getting the user info from SR DB:" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			ContextAccessorUtil.unsetContext();
			PersistenceContext.getInstance().closeEntityManager("yccdom");
		}
	}

	public void validateUserSession(HttpServletRequest request, String appId, Long cobrandId) throws Exception {
		long memId = 0;
		try {
			memId = SessionValidationServices
					.validateExternalUserSessionToken(request.getAttribute("userSession").toString(), cobrandId, appId);
			request.setAttribute("memId", memId);
			logger.info("User login stats:" + request.getAttribute("userSession").toString() + "," + cobrandId + ","
					+ memId);
		} catch (Exception e) {
			logger.error("Error while validating the user session:" + ExceptionUtils.getFullStackTrace(e));
			throw new InvalidSessionException(FQCN, 50, "Invalid User Session");
		}
		if (memId <= 0) {
			throw new InvalidSessionException(FQCN, 50, "Invalid User Session");
		}
		Mem mem = UserAccessor.getUserByUserId(cobrandId, memId);
		if (mem.getUserTypeId() != UserType.SUPPORT_USER.getUserTypeId()) {
			throw new InvalidSessionException(FQCN, 50, "Invalid User Session");
		}
		request.setAttribute("memId", memId);
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
	
	
}
