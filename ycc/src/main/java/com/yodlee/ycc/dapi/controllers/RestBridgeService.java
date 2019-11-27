/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.yodlee.restbridge.exceptions.RestBridgeException;

public interface RestBridgeService {
	
	String processRestBridgeRequest()throws HttpException, IOException, RestBridgeException;

}
