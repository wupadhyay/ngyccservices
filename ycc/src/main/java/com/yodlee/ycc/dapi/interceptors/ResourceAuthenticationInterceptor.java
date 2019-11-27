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
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yodlee.nextgen.authentication.AuthenticationException;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
/**
 * 
 * @author knavuluri
 *
 */
public class ResourceAuthenticationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ResourceAuthenticationInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String subPath = request.getServletPath().substring(1);
		logger.info("Invoking the API :" + subPath);
		logger.info("Request Header---"+request.getHeader("Authorization"));
		try {
			if (request.getHeader("Authorization") == null)
				throw new AuthenticationException("Y007", "Authorization header missing");
			String[] tokens = ResourceAuthenticationInterceptor.convert(request.getHeader("Authorization"));
			Map<String, String> accessTokens = new HashMap<>();
			if (tokens.length > 0) {
				if (tokens.length > 5)
					throw new ApiException("Y008");
				for (String accessToken : tokens) {
					String[] split = accessToken.split("=");
					if (split.length > 1) {
						accessTokens.put(split[0].trim(), split[1].trim());
					}
				}
			}
			if (accessTokens.size() == 0) {
				throw new AuthenticationException("Y009", "Session tokens missing");
			}
			if (accessTokens.get("Bearer") !=null && accessTokens.get("Bearer").contains("#singularity")) {
				logger.info("Inside to check for token"+accessTokens.toString());
				if(!accessTokens.containsKey("cobrandId"))
					throw new AuthenticationException("Y008", "CobrandId is missing in authorization header");
				if (accessTokens.get("cobrandId") == null || accessTokens.get("cobrandId").trim().equals(""))
					throw new AuthenticationException("Y008", "CobrandId should not be null");
				request.setAttribute("Bearer", accessTokens.get("Bearer"));
				request.setAttribute("cobId", accessTokens.get("cobrandId"));
			} else {
				if (accessTokens.get("cobrandId") == null || accessTokens.get("cobrandId").trim().equals(""))
					throw new AuthenticationException("Y008", "CobrandId is missing in the header");
				if (accessTokens.get("appId") == null || accessTokens.get("appId").trim().equals(""))
					throw new AuthenticationException("Y008", "ApplicationId is missing in the header");
				request.setAttribute("cobId", accessTokens.get("cobrandId"));
				request.setAttribute("appId", accessTokens.get("appId"));
				if (accessTokens.get("cobSession") != null)
					request.setAttribute("cobSession", accessTokens.get("cobSession"));

				if (subPath.contains("cobrand/login"))
					return true;

				if (subPath.contains("/user/login") || subPath.contains("cobrand/logout")) {
					if (accessTokens.get("cobSession") == null || accessTokens.get("cobSession").length() < 1)
						throw new AuthenticationException("Y008", "cobSession missing");
					if (subPath.contains("cobrand/logout"))
						return true;
				} else {
					if (accessTokens.get("userSession") == null || accessTokens.get("userSession").length() < 1)
						throw new AuthenticationException("Y008", "userSession missing");
				}
				CobrandInfo cobrandInfo = CobrandUtil.getCobrandInfoVO(Long.valueOf(accessTokens.get("cobrandId")));
				if (cobrandInfo == null)
					throw new AuthenticationException("Y008", "CobrandId is missing in the DBpool");
				request.setAttribute("userSession", accessTokens.get("userSession"));
			}
		} catch (Exception e) {
			logger.error("Eror while accesing the API:" + subPath + ":" + e.getMessage()
					+ ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		return true;
	}

	private static String[] convert(String token) throws Exception {
		if (token == null || token.length() < 1)
			throw new AuthenticationException("Y009", "Session tokens missing");
		Pattern p = Pattern.compile("[\\{\\}\\,]++");
		String[] split = p.split(token);
		return split;
	}

}
