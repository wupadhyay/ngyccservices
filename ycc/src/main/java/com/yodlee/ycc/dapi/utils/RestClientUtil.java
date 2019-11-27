/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.yodlee.nextgen.authentication.AuthenticationException;

/**
 * 
 * @author wupadhyay 
 * 
 * 				
 */

public final class RestClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(RestClientUtil.class);
	private static RestClientUtil restClient = new RestClientUtil();

	private RestTemplate restTemplate;

	private RestClientUtil() {

	}

	public static RestClientUtil getInstance() {
		return restClient;
	}

	public void callEndPoint(String url, String token, HttpMethod requestType) throws Exception {
		ResponseEntity<Object> response = null;
		response = getResponse(url, token, requestType);
		if (response != null) {
			switch (response.getStatusCode()) {
			case OK:
				logger.info("Successful Authentication");
				break;
			case REQUEST_TIMEOUT:
				throw new AuthenticationException("Y008", "Request Timeout");
			case UNAUTHORIZED:
				throw new AuthenticationException("Y009", "Invalid Token");
			default:
				break;
			}
		} else {
			throw new AuthenticationException("Y008", "Invalid Token");
		}

	}

	public ResponseEntity<Object> getResponse(String url, String token, HttpMethod requestType) {
		HttpEntity<Object> httpEntity = new HttpEntity<>(getHttpHeaders(token));
		restTemplate = new RestTemplate();
		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.exchange(url, requestType, httpEntity, Object.class);
		} catch (RestClientException e) {
			logger.error("Got Exception {}", e);
		}
		return response;
	}

	HttpHeaders getHttpHeaders(String token) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (token != null) {
			if (token.contains("Bearer")) {
				httpHeaders.add("Authorization", token);
			} else {
				httpHeaders.add("Authorization", "Bearer " + token);
			}
		}
		httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
		return httpHeaders;
	}

}
