/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.common.UserType;
import com.yodlee.dom.entity.CobParam;
import com.yodlee.dom.entity.CobParam_;
import com.yodlee.dom.entity.Mem;
import com.yodlee.dom.entity.MemPref;
import com.yodlee.dom.entity.MemPref_;
import com.yodlee.dom.entity.ParamKey_;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.ICobrandContext;
import com.yodlee.internal.persistence.accessor.MemPrefAccessor;
import com.yodlee.internal.persistence.accessor.UserAccessor;
import com.yodlee.nextgen.authentication.AuthenticationException;
import com.yodlee.nextgen.authentication.exceptions.basic.InternalAuthenticationException;
import com.yodlee.nextgen.ctx.defn.ContextServices;
import com.yodlee.nextgen.ctx.defn.exceptions.ContextServiceException;
import com.yodlee.nextgen.session.SessionServices;
import com.yodlee.nextgen.session.exceptions.SessionException;
import com.yodlee.nextgen.user.ctx.defn.UserContextService;
import com.yodlee.nextgen.validation.session.SessionValidationServices;
import com.yodlee.nextgen.vo.Credentials;
import com.yodlee.nextgen.vo.context.UserContext;
import com.yodlee.nextgen.vo.credentials.BasicAuthenticationCreds;
import com.yodlee.nextgen.vo.request.ServiceRequest;
import com.yodlee.restbridge.RequestHandler;
import com.yodlee.restbridge.RequestHandlerFactory;
import com.yodlee.restbridge.RestBridgeType;
import com.yodlee.ycc.dapi.bean.UserInfo;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.service.UserInfoService;
/**
 * 
 * @author knavuluri
 * 
 */
@Controller
@RequestMapping("/v1/user")
public class UsersController extends MasterController {

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserInfoService service;

	@RequestMapping(value = "/locale", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getLocale() throws ApiException {
		logger.debug("Calling user locale API");
		String cobId = (String) request.getAttribute("cobId");
		String memId = (String) request.getAttribute("memId");
		String locale = null;
		try {

			ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, null);

			Criteria criteria = new Criteria();
			criteria.add(MemPref_.memId, Long.valueOf(memId));
			criteria.add(MemPref_.prefKeyName, "COM.YODLEE.USER.LOCALE");
			List<MemPref> memPref = MemPref.DAO.select(criteria);
			if (memPref != null && !memPref.isEmpty()) {
				MemPref pref = memPref.get(0);
				locale = pref.getPrefKeyValue();
			}
			else {
				criteria = new Criteria();
				criteria.add(CobParam_.cobrandId, Long.valueOf(cobId));
				criteria.join(CobParam_.paramKeyId, ParamKey_.paramKeyId);
				criteria.add(ParamKey_.paramKeyName, "COM.YODLEE.COBRAND.LOCALE.DEFAULT");
				List<CobParam> cobParam = CobParam.DAO.select(criteria);
				if (cobParam != null && !cobParam.isEmpty()) {
					CobParam parm = cobParam.get(0);
					locale = parm.getParamValue();
				}
			}
		}
		catch (Exception e) {
			logger.error("Error while accesing the locale API:" + ExceptionUtils.getFullStackTrace(e));
		}
		finally {
			ContextAccessorUtil.unsetContext();
		}
		if (locale != null) {
			String json = "{\"userInfo\":{\"memId\":" + memId + ",\"locale\":\"" + locale + "\"}}";
			logger.debug("Locale json:" + json);
			return json;
		}
		logger.debug("End user locale API");
		return "{}";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String authenticateUser(@RequestBody(required = false) String userParam) throws ContextServiceException, AuthenticationException, SessionException, ApiException, HttpException, IOException,
			Exception {
		String yslresponse = null;
		try {
			String serverIPAddress = request.getHeader("X-FORWARDED-FOR");
			if (serverIPAddress == null) {
				serverIPAddress = request.getRemoteAddr();
			}

			String userLoginLocale = null;
			String cobId = (String) request.getAttribute("cobId");
			String appId = (String) request.getAttribute("appId");
			
			ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, appId);
			long cobrandId = Long.valueOf(cobId);
			RequestHandler requestHandler = RequestHandlerFactory.getInstance(RestBridgeType.JSON);
			HashMap<String, String> restRequestMap = null;

			String userLogin = null;
			String userPassword = null;
			if (userParam != null && !userParam.isEmpty()) {

				restRequestMap = requestHandler.getRestRequest("input", userParam);
				if (null == restRequestMap || restRequestMap.size() == 0) {
					throw new ApiException("Y800", new Object[] { "userParam" });
				}
				userLoginLocale = restRequestMap.get("user.locale");
				userLogin = restRequestMap.get("user.loginName");
				userPassword = restRequestMap.get("user.password");

				if (StringUtil.isNullOrEmpty(userLogin) || StringUtil.isNullOrEmpty(userPassword)) {
					throw new AuthenticationException("Y001", "loginName and password required");
				}

				if (userLoginLocale != null) {
					if (!valiateLocale(userLoginLocale, cobrandId)) {
						throw new ApiException("Y800", new Object[] { "locale" });
					}

				}

			}
			if (StringUtil.isNullOrEmpty(userLogin) || StringUtil.isNullOrEmpty(userPassword)) {
				throw new AuthenticationException("Y001", "loginName and password required");
			}

			UserContext userContext = null;
			ContextServices contextService = UserContextService.getUserContextService();
			Credentials credentials = new BasicAuthenticationCreds(userLogin, userPassword);
			ServiceRequest serviceRequest = new ServiceRequest(Long.valueOf(cobId), appId, credentials, null, userLoginLocale);
			serviceRequest.setIpAddress(serverIPAddress);
			serviceRequest.setUserType(UserType.SUPPORT_USER);
			serviceRequest.setCoBrandSessionToken(request.getAttribute("cobSession").toString());
			userContext = (UserContext) contextService.createContext(serviceRequest);
			String userSessionToken = userContext.getSession().getExternalSessionID();
			yslresponse = populateResponseForLoginApi(appId, userSessionToken, cobrandId, userLoginLocale);
		}
		catch (Exception e) {
			logger.error("Error while user login:" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}finally{
			ContextAccessorUtil.unsetContext();
		}
		return yslresponse != null ? yslresponse : "{}";
	}

	private String populateResponseForLoginApi(String appId, String userSessionToken, long cobrandId, String userLoginLocale) throws Exception {
		long memId = SessionValidationServices.validateExternalUserSessionToken(userSessionToken, cobrandId, appId);
		String response = null;
		try {
			ContextAccessorUtil.setContext(cobrandId, memId, appId);
			String user = getResource("user id loginName email name first middleInitial last");
			response = processResponseForLoginAndRegisterApi(user, userSessionToken, userLoginLocale);
		} catch (Exception e) {
			logger.error("Error while constructiing the response for login api:" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			ContextAccessorUtil.unsetContext();
		}
		return response;
	}

	private String processResponseForLoginAndRegisterApi(String user, String userSession, String loginLocale) {
		JsonElement userElement = new JsonParser().parse(user);
		JsonArray userArray = userElement.getAsJsonObject().get("user").getAsJsonArray();
		JsonElement userJson = userArray.get(0);

		JsonArray nameArray = userJson.getAsJsonObject().get("name").getAsJsonArray();
		JsonElement nameJson = nameArray.get(0);

		Long memId = Long.valueOf(userJson.getAsJsonObject().get("id").getAsLong());

		String preference = "";

		MemPref timeZone = MemPrefAccessor.getMemPrefForMemId(memId.longValue(), "com.yodlee.userprofile.TIMEZONE");
		JsonObject jsonObject = new JsonObject();
		if ((timeZone != null) && (!StringUtil.isNullOrEmpty(timeZone.getPrefKeyValue()))) {
			jsonObject.addProperty("timeZone", timeZone.getPrefKeyValue());
		}
		else {
			jsonObject.addProperty("timeZone", "PST");
		}
		preference = jsonObject.toString();

		if ((nameJson.isJsonObject()) && (nameJson.getAsJsonObject().entrySet().isEmpty())) {
			MemPref memPrefFirstName = MemPrefAccessor.getMemPrefForMemId(memId.longValue(), "personalInfo.FIRSTNAME");
			MemPref memPrefLastName = MemPrefAccessor.getMemPrefForMemId(memId.longValue(), "personalInfo.LASTNAME");

			if ((memPrefFirstName != null) && (!StringUtil.isNullOrEmpty(memPrefFirstName.getPrefKeyValue()))) {
				nameJson.getAsJsonObject().addProperty("first", memPrefFirstName.getPrefKeyValue());
			}
			if ((memPrefLastName != null) && (!StringUtil.isNullOrEmpty(memPrefLastName.getPrefKeyValue()))) {
				nameJson.getAsJsonObject().addProperty("last", memPrefLastName.getPrefKeyValue());
			}
		}
		JsonElement localeElement = nameJson.getAsJsonObject().remove("locale");
		if (nameJson.getAsJsonObject().entrySet().isEmpty()) {
			userJson.getAsJsonObject().remove("name");
		}

		JsonObject finalResponse = new JsonObject();
		if (userSession != null) {
			JsonObject attachUserSession = new JsonObject();
			attachUserSession.addProperty("userSession", userSession);
			userJson.getAsJsonObject().add("session", attachUserSession);

			JsonObject jsonObj = userJson.getAsJsonObject();
			jsonObj.remove("email");
		}

		JsonElement prefElement = new JsonParser().parse(preference);

		if ((localeElement != null) || (loginLocale != null)) {
			prefElement.getAsJsonObject().addProperty("locale", (loginLocale != null) ? loginLocale : localeElement.getAsString());
		}
		else {
			MemPref memPref = MemPrefAccessor.getMemPrefForMemId(memId.longValue(), "COM.YODLEE.USER.LOCALE");
			if (memPref != null) {
				String localeValue = memPref.getPrefKeyValue();
				prefElement.getAsJsonObject().addProperty("locale", (loginLocale != null) ? loginLocale : localeValue);
			}
		}
		userJson.getAsJsonObject().add("preferences", prefElement);
		finalResponse.add("user", userJson);
		return finalResponse.toString().replace("[", "").replace("]", "");
	}

	@RequestMapping(value = { "/logout" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST }, produces = { "application/json" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void logoutUser() throws MalformedURLException, ContextServiceException, SessionException, InternalAuthenticationException, ApiException {
		try {
			ICobrandContext cc = getCobrandContext(this.request);
			long cobrandId = cc.getCobrandId();
			ContextAccessorUtil.setContext(cobrandId, 0l, cc.getApplicationId());
			String userSessionToken = this.request.getAttribute("userSession").toString();
			boolean logoutUser = SessionServices.logoutUser(userSessionToken, cobrandId, cc.getApplicationId());
			if (!logoutUser)
				throw new InternalAuthenticationException("Y010", "Internal Exception");
		}
		catch (Exception e) {
			logger.error("Error while logging out the user:" + ExceptionUtils.getFullStackTrace(e));
		}finally{
			ContextAccessorUtil.unsetContext();
		}
	}

	@RequestMapping(value = { "/userinfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET }, produces = { "application/json" })
	
    @ResponseBody
    public String getUserInfo() throws MalformedURLException, SessionException, InternalAuthenticationException, ApiException {
           String userInfo = "{}";
           try {
        	   Long memId = (Long) request.getAttribute("memId");
        	   String cobId = (String) request.getAttribute("cobId");
        	   ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, null);
        	   Mem mem = UserAccessor.getUserByUserId(Long.valueOf(cobId), memId);
			if (mem != null) {
				UserInfo info = new UserInfo();
				info.setMemId(mem.getMemId());
				info.setLoginName(mem.getLoginName());
				info.setEmail(mem.getEmail());
				Gson gson = new Gson();
				String json = gson.toJson(info);
				userInfo = "{\"userInfo\":" + json + "}";
			}
        	   else
        	   {

        		   if(memId!=null)
        		   {
        			   userInfo = "{\"userInfo\":{\"memId\":" + memId + "}}";

        		   }
        	   }
           }
           catch (Exception e) {
                  logger.error("Error while getting the user info:" + ExceptionUtils.getFullStackTrace(e));
                  
           }
           finally {
        	   ContextAccessorUtil.unsetContext();
           }
           return userInfo;
    }
	
	@RequestMapping(value = "/userinfo/email", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getSiteUserInfo(@RequestParam(value = "cobrandIds", required=true) String cobrandList){
		
		logger.info("Get User Info Data:");
		Date sDate = new Date();
		String userInfoJson=null;
		try {
			userInfoJson = service.getUserInformation(cobrandList);
		} catch (Exception e) {
			logger.error("Error inside getSiteUserInfo is"+e);
		}
		Date eDate = new Date();
		logger.info("Total time taken for the notification create API:" + (eDate.getTime() - sDate.getTime())
				+ " millisec");
		
		return userInfoJson;
	}

	
}