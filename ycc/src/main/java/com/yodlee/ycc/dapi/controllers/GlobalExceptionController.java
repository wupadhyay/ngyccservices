/*
 * Copyright (c) 2012 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import springfox.documentation.annotations.ApiIgnore;

import com.yodlee.config.ConfigServiceImpl;
import com.yodlee.core.exceptions.InsufficientPrivilegeException;
import com.yodlee.framework.runtime.dao.DAOException;
import com.yodlee.internal.user.UserStateChangedException;
import com.yodlee.internal.user.UserSuspendedException;
import com.yodlee.internal.user.UserUnregisteredException;
import com.yodlee.nextgen.authentication.AuthenticationException;
import com.yodlee.nextgen.authentication.SAMLAuthenticationException;
import com.yodlee.nextgen.cobrand.ctx.exceptions.InternalContextDefinitionException;
import com.yodlee.nextgen.ctx.defn.exceptions.ContextServiceException;
import com.yodlee.nextgen.exceptions.core.InvalidYodleeServiceQueryException;
import com.yodlee.nextgen.jdbc.JdbcServiceException;
import com.yodlee.nextgen.lucene.exceptions.LuceneException;
import com.yodlee.nextgen.session.exceptions.InvalidSessionException;
import com.yodlee.nextgen.session.exceptions.SessionException;
import com.yodlee.nextgen.util.LogCollectorUtil;
import com.yodlee.restbridge.RestBridgeUtil;
import com.yodlee.restbridge.exceptions.RestBridgeException;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.exceptions.HttpForbiddenException;
import com.yodlee.ycc.dapi.exceptions.HttpMethodNotAllowedException;
import com.yodlee.ycc.dapi.exceptions.HttpNotImplementedException;
import com.yodlee.ycc.dapi.exceptions.PageNotFoundException;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.exceptions.UnauthorizedException;
import com.yodlee.ycc.dapi.exceptions.UserAuthenticationException;
import com.yodlee.ycc.dapi.utils.UniqueTrackingIdGenerator;
import com.yodlee.ycc.notification.db.exception.NotificationException;
import com.yodlee.ycc.notification.db.exception.UserContactInfoException;

@ControllerAdvice
public class GlobalExceptionController {
	private static Properties prop = new Properties();
	static {
		InputStream input = null;
		try {
			//get properties file from project classpath
			input = RestBridgeUtil.class.getClassLoader().getResourceAsStream("errorMessages.properties");

			prop.load(input);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public String getErrorMessages(String code,Object[] arguments) {
		MessageFormat form = new MessageFormat(prop.getProperty(code));
		return form.format(arguments).trim();
	}
	
	public void populateRefCode(JsonExceptionInfo exceptionInfo) {
		exceptionInfo.setReferenceCode(LogCollectorUtil.getRefId());
		LogCollectorUtil.destroy();
	}
	
	@ExceptionHandler(LuceneException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(LuceneException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y400");
		exceptionInfo.setErrorMessage(getErrorMessages("Y400", new Object[]{ex.getErrorMessage()}));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(IOException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(IOException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y400");
		exceptionInfo.setErrorMessage(getErrorMessages("Y400", new Object[]{ex.getMessage()}));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}

	@ExceptionHandler(JdbcServiceException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(JdbcServiceException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), null)); 
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(RestBridgeException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(RestBridgeException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(UnsupportedOperationException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(getErrorMessages("Y901", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(DAOException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(DAOException ex) {
		System.out.println(ex.toString());
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y902");
		exceptionInfo.setErrorMessage(getErrorMessages("Y902", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(IllegalStateException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y806");//Invalid Input
		exceptionInfo.setErrorMessage(getErrorMessages("Y806", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(RuntimeException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y902");
		exceptionInfo.setErrorMessage(getErrorMessages("Y902", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(TypeMismatchException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y806");//Invalid input
		exceptionInfo.setErrorMessage(getErrorMessages("Y806", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleAuthenticationException(AuthenticationException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(SAMLAuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleSAMLAuthenticationException(SAMLAuthenticationException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage((ex.getArgs()!=null && ex.getArgs().length>0)?getErrorMessages(ex.getErrorCode(),ex.getArgs()):getErrorMessages(ex.getErrorCode(), null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(InternalContextDefinitionException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleAuthenticationException(InternalContextDefinitionException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UserStateChangedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleAuthenticationException(UserStateChangedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y004");
		exceptionInfo.setErrorMessage(getErrorMessages("Y004", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UserSuspendedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleAuthenticationException(UserSuspendedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y005");
		exceptionInfo.setErrorMessage(getErrorMessages("Y005", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UserUnregisteredException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleAuthenticationException(UserUnregisteredException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y006");
		exceptionInfo.setErrorMessage(getErrorMessages("Y006", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(ContextServiceException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleContextServiceException(ContextServiceException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	
	@ExceptionHandler(SessionException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleSessionException(SessionException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y010");
		exceptionInfo.setErrorMessage(getErrorMessages("Y010", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(InvalidSessionException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleInvalidSessionException(InvalidSessionException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y008");
		exceptionInfo.setErrorMessage(getErrorMessages("Y008", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(IllegalArgumentException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y400");
		exceptionInfo.setErrorMessage(msg==null?"Internal Server Error":getErrorMessages("Y400", new Object[]{ex.getMessage()}));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(NumberFormatException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(getErrorMessages("Y901", new Object[]{ex.getMessage()}));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}

	@ExceptionHandler(InvalidYodleeServiceQueryException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleInvalidYodleeServiceQueryException(InvalidYodleeServiceQueryException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(getErrorMessages("Y901", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(NoSuchRequestHandlingMethodException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y807");
		exceptionInfo.setErrorMessage("Resource not found "+msg);
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(HttpForbiddenException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(HttpForbiddenException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("403");
		exceptionInfo.setErrorMessage("Forbidden");
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(HttpMethodNotAllowedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(HttpMethodNotAllowedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("405");
		exceptionInfo.setErrorMessage("Method Not Allowed");
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(HttpNotImplementedException.class)
	@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(HttpNotImplementedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("501");
		exceptionInfo.setErrorMessage("Not Implemented");
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(PageNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(PageNotFoundException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y807");
		exceptionInfo.setErrorMessage("Resource not found");
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(UnauthorizedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("401");
		exceptionInfo.setErrorMessage("Unauthorized Access");
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(UserAuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleApiException(UserAuthenticationException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), ex.getArgs()));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(ApiException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleApiException(ApiException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode(ex.getErrorCode());
		exceptionInfo.setErrorMessage(getErrorMessages(ex.getErrorCode(), ex.getArgs()));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(HttpRequestMethodNotSupportedException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y807");
		exceptionInfo.setErrorMessage(getErrorMessages("Y807", new Object[]{msg}));
		String yodleeRefId = getUniqueReferenceTrackingID();
		LogCollectorUtil.init(-1, -1, null, yodleeRefId);
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@Autowired(required = true)
    @Qualifier(value = "ConfigService")
    ConfigServiceImpl config;
	private String getUniqueReferenceTrackingID(){
		UniqueTrackingIdGenerator uniqueTrackIDGenInstance = UniqueTrackingIdGenerator.getInstance();
        uniqueTrackIDGenInstance.setInstanceId(Integer.parseInt(config.getInstanceId()));
        return uniqueTrackIDGenInstance.generateTrackingId();
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(MissingServletRequestParameterException ex) {
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y400");
		exceptionInfo.setErrorMessage(getErrorMessages("Y400", new Object[]{msg}));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(Exception ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		String msg = ex.getMessage();
		exceptionInfo.setErrorCode("Y902");
		exceptionInfo.setErrorMessage(getErrorMessages("Y902", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	}
	@ExceptionHandler(InsufficientPrivilegeException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ApiIgnore
	public @ResponseBody
	JsonExceptionInfo handleException(InsufficientPrivilegeException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(getErrorMessages("Y901", null));
		populateRefCode(exceptionInfo);
		return exceptionInfo;
	
}

	@ExceptionHandler(RefreshStatException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody JsonExceptionInfo handleException(RefreshStatException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(ex.getMessage());
		populateRefCode(exceptionInfo);
		return exceptionInfo;

	}
	@ExceptionHandler(NotificationException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody JsonExceptionInfo handleException(NotificationException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(ex.getMessage());
		populateRefCode(exceptionInfo);
		return exceptionInfo;

	}
	@ExceptionHandler(UserContactInfoException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiIgnore
	public @ResponseBody JsonExceptionInfo handleException(UserContactInfoException ex) {
		ex.printStackTrace();
		JsonExceptionInfo exceptionInfo = new JsonExceptionInfo();
		exceptionInfo.setErrorCode("Y901");
		exceptionInfo.setErrorMessage(ex.getMessage());
		populateRefCode(exceptionInfo);
		return exceptionInfo;

	}
}
