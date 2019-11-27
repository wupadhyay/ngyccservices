/*
* Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.dapi.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CacheControlInterceptor extends HandlerInterceptorAdapter {

	private static final String FQCN = CacheControlInterceptor.class.getName();
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		response.setHeader("Pragma","no-cache" );
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
	}
}
