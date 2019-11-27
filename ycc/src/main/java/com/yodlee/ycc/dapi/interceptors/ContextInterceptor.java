/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Yodlee, Inc. Use is subject to license terms.
 *
 */
package com.yodlee.ycc.dapi.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yodlee.framework.runtime.shared.context.ContextProvider;
import com.yodlee.framework.runtime.shared.context.ContextProviderRegistry;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.ycc.notification.db.context.ThreadLocalContext;

/**
 * 
 * @author knavuluri
 * 
 */
public class ContextInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ContextInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler == null || !(handler instanceof HandlerMethod)) {
			return true;
		} else if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Object controller = handlerMethod.getBean();
			if (controller == null || !controller.getClass().getPackage().getName().startsWith("com.yodlee")) {
				return true;
			}
		}
		logger.debug("Start of preHandle::" + request.getRequestURI().toString());
		ContextProvider contextProvider = ContextProviderRegistry.getInstance().getContextProvider();
		contextProvider.initInstance();
		logger.debug("End of preHandle::" + request.getRequestURI().toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (handler == null || !(handler instanceof HandlerMethod)) {
			return;
		} else if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Object controller = handlerMethod.getBean();
			if (controller == null || !controller.getClass().getPackage().getName().startsWith("com.yodlee")) {
				return;
			}
		}
		ThreadLocalContext.unsetUserInfoContext();
		logger.debug("Start of postHandle::" + request.getRequestURI().toString());
		ContextProvider contextProvider = ContextProviderRegistry.getInstance().getContextProvider();
		contextProvider.cleanContext();
		PersistenceContext.getInstance().closeEntityManager("dom");
		logger.debug("End of postHandle::" + request.getRequestURI().toString());
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			logger.debug("Start of afterCompletion::" + request.getRequestURI().toString());
			ThreadLocalContext.unsetUserInfoContext();
			ContextProvider contextProvider = ContextProviderRegistry.getInstance().getContextProvider();
			contextProvider.cleanContext();
		} catch (Exception e) {
			logger.error("after completion error:" + ExceptionUtils.getStackTrace(e));
		} finally {
			PersistenceContext.getInstance().closeEntityManager("dom");
		}
		logger.debug("End of afterCompletion::" + request.getRequestURI().toString());

	}

}
