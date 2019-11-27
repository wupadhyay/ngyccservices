/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yodlee.dom.entity.CobParam;
import com.yodlee.dom.entity.CobParam_;
import com.yodlee.dom.entity.Cobrand;
import com.yodlee.dom.entity.ParamKey_;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.ICobrandContext;
import com.yodlee.nextgen.authentication.AuthenticationException;
import com.yodlee.nextgen.authentication.exceptions.basic.InternalAuthenticationException;
import com.yodlee.nextgen.cobrand.ctx.defn.CoBrandContextService;
import com.yodlee.nextgen.ctx.defn.ContextServices;
import com.yodlee.nextgen.ctx.defn.exceptions.ContextServiceException;
import com.yodlee.nextgen.session.SessionServices;
import com.yodlee.nextgen.session.exceptions.SessionException;
import com.yodlee.nextgen.vo.Credentials;
import com.yodlee.nextgen.vo.context.CoBrandContext;
import com.yodlee.nextgen.vo.credentials.BasicAuthenticationCreds;
import com.yodlee.nextgen.vo.request.ServiceRequest;
import com.yodlee.restbridge.RequestHandler;
import com.yodlee.restbridge.RequestHandlerFactory;
import com.yodlee.restbridge.RestBridgeType;
//import com.yodlee.core.exceptions.DataEncryptionException;
import com.yodlee.ycc.dapi.exceptions.ApiException;

/**
 * 
 * @author knavuluri
 * 
 * 
 */

@Controller
@RequestMapping("/v1/cobrand")
@Api(value = "COBRAND")
public class Authenticate extends MasterController {
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	/**
	 * The Following Method is used for CoBrand Login. The CoBrand Login is the
	 * first API Call to be made. For API interactions.
	 * 
	 * The Controller is mapped to the HTTP endpoint :
	 * http://localhost:8080/services/{coBrand}/dapi/authenticate/coblogin
	 * 
	 * @param cobrandLogin
	 * @param cobrandPassword
	 * @param model
	 * @return
	 */

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "Cobrand Login", notes = "<font size='2px' color='grey' face='Georgia,serif'>" + " The cobrand login service authenticates a cobrand.<br/> "
			+ " Cobrand session in the response includes the cobrand session token (cobSession) <br/>" + " which is used in subsequent API calls like registering or signing in the user. <br/>"
			+ " The cobrand session token expires every 100 minutes. This service can be <br/> " + " invoked to create a new cobrand session token. <br/><br/> " + " <b>Sample Input</b>" + "<pre>"
			+ "cobrandName: yodlee<br/>" + "cobrandParam : <br/>" + "{<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;\"cobrand\": " + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br/>"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"cobrandLogin\": \"yodlee_10000004\",<br/>" +

			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"cobrandPassword\": \"yodlee123\",<br/>" +

			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"locale\": \"en_US\"<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>" + "}<br/>" + " </pre> " + " <br/> " + " <b>Sample Response</b>" + "<pre>" + "{"
			+ "	\"cobrandId\" : 10000004, <br/>" + "	\"applicationId\" : \"17CBE222A42161A5GG896M47CF4C1A00\", <br/>" + "	\"session\" : {<br/>"
			+ "			\"cobSession\": \"06142010_0:4044d058bb39ae11f52584f11189f75bba5\" <br/>" + "	},<br/>" + "	\"locale\" : \"en_CA\"<br/>" + "}" + " </pre> " + " <br/> " + "</font>"
			+ "<br/><br/><br/><br/>")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Y003 : Account is locked, contact Yodlee customer care"), @ApiResponse(code = 401, message = "Y001 : User name and password required"),
			@ApiResponse(code = 400, message = "Y800 : Invalid value for cobrandParam") })
	public @ResponseBody
	CoBrandContext authenticateCoBrand(@ApiParam(name = "cobrandParam", value = "cobrandParam(Mandatory)", required = false, defaultValue = "") @RequestBody(required = true) String cobrandParam)
			throws MalformedURLException, ContextServiceException, SessionException, AuthenticationException, ApiException {

		String cobrandLoginLocale = null;
		String coBrandLogin = null;
		String coBrandPassword = null;

		if (cobrandParam != null && !cobrandParam.isEmpty()) {
			RequestHandler requestHandler = RequestHandlerFactory.getInstance(RestBridgeType.JSON);
			HashMap<String, String> restRequestMap = requestHandler.getRestRequest("input", cobrandParam);
			if (restRequestMap == null)
				throw new ApiException("Y800", new Object[] { "cobrandParam" });

			cobrandLoginLocale = restRequestMap.get("cobrand.locale");
			coBrandLogin = restRequestMap.get("cobrand.cobrandLogin");
			coBrandPassword = restRequestMap.get("cobrand.cobrandPassword");

		}
		if (StringUtil.isNullOrEmpty(coBrandLogin) || StringUtil.isNullOrEmpty(coBrandPassword)) {
			throw new AuthenticationException("Y001", "UserName/Password can not be empty!!!");
		}

		String serverIPAddress = request.getHeader("X-FORWARDED-FOR");

		if (serverIPAddress == null) {
			serverIPAddress = request.getRemoteAddr();
		}
		

		ICobrandContext cc = getCobrandContext(request);
		long cobrandId = cc.getCobrandId();
		
		ContextAccessorUtil.setContext(cobrandId, 0l, cc.getApplicationId());
		ContextAccessorUtil.setContextAccessor(cobrandId, 0l, cc.getApplicationId());

		if (cobrandLoginLocale != null) {
			// if(!valiateLocale(cobrandLoginLocale,cobrandId)){
			// throw new ApiException("Y800",new Object[]{"locale"});
			// }

		}

		ContextServices contextService = CoBrandContextService.getCoBrandContextService();
		Credentials credentials = new BasicAuthenticationCreds(coBrandLogin, coBrandPassword);
		ServiceRequest serviceRequest = new ServiceRequest(cc.getCobrandId(), cc.getApplicationId(), credentials, cobrandLoginLocale, null);
		serviceRequest.setIpAddress(serverIPAddress);
		CoBrandContext coBrandContext = (CoBrandContext) contextService.createContext(serviceRequest);
		// loadLocale(coBrandContext.getSession().getExternalSessionID(), null,
		// null, cobrandLoginLocale, null);

		return coBrandContext;

	}

	@ApiOperation(value = "Cobrand Logout", notes = "<font size='2px' color='grey' face='Georgia,serif'><br/>" + "The cobrand logout service is used to log out the cobrand. <br/>"
			+ "This service does not return a response. The HTTP response code is 204 (Success with no content).<br/><br/>" + "<b>Sample Input</b>" + " <pre>" + "	cobrandName  : yodlee <br/>"
			+ " </pre> " + " <br/> " + "</font>")
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
	public @ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ResponseBody
	void logoutCoBrand() throws MalformedURLException, ContextServiceException, SessionException, InternalAuthenticationException, ApiException {

		ICobrandContext cc = getCobrandContext(request);
		long cobrandId = cc.getCobrandId();

		ContextAccessorUtil.setContext(cobrandId, 0l, cc.getApplicationId());
		ContextAccessorUtil.setContextAccessor(cobrandId, 0l, cc.getApplicationId());


		boolean logoutCoBrand = SessionServices.logoutCoBrand(request.getAttribute("cobSession").toString(), cobrandId);

		if (!logoutCoBrand) {
			throw new InternalAuthenticationException("Y010", "Invalid Session");
		}
		else {
			logger.debug("Cobrand Logout is successful");
		}

	}

	@RequestMapping(value = "/locale", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getLocale() throws ApiException {
		logger.debug("Calling cobrand locale API");
		String cobId = (String) request.getAttribute("cobId");
		String locale = null;
		try {
		
			ContextAccessorUtil.setContext(Long.valueOf(cobId), 0l, null);
			ContextAccessorUtil.setContextAccessor(Long.valueOf(cobId), 0l, null);


			Criteria criteria = new Criteria();
			criteria.add(CobParam_.cobrandId, Long.valueOf(cobId));
			criteria.join(CobParam_.paramKeyId, ParamKey_.paramKeyId);
			criteria.add(ParamKey_.paramKeyName, "COM.YODLEE.COBRAND.LOCALE.DEFAULT");
			List<CobParam> cobParam = CobParam.DAO.select(criteria);
			if (cobParam != null && !cobParam.isEmpty()) {
				CobParam parm = cobParam.get(0);
				locale = parm.getParamValue();
			}
		}
		catch (Exception e) {
			logger.error("Error while accesing the cobrand locale API:" + ExceptionUtils.getFullStackTrace(e));
		}
		finally {
			ContextAccessorUtil.unsetContext();
		}
		if (locale != null) {
			String json = "{\"cobrand\":{\"locale\":\"" + locale + "\"}}";
			logger.debug("Locale json:" + json);
			return json;
		}
		logger.debug("End cobrand locale API");
		return "{}";
	}

}