/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.subbrand;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yodlee.security.init.SecurityInitFromDB;

/**
 * 
 * @author knavuluri
 * 
 */
public class SubrandPublisher {

	private static final Logger logger = LoggerFactory.getLogger(SubrandPublisher.class);

	@Autowired(required = true)
	@Qualifier(value = "SecurityInitFromDB")
	SecurityInitFromDB securityInitFromDB;

	public int trigger(Long subrandId) throws Exception {
		try {
			logger.debug("Loading the security keys for subbrand:" + subrandId);
			securityInitFromDB.reLoadKeys(Long.valueOf(subrandId));
			logger.debug("Completed loading the security keys for subbrand:" + subrandId);
		}
		catch (Exception e) {
			logger.error("Error while loading the keystore for the subbrand:" + subrandId + " " + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		return 1;
	}
}