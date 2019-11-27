/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.ycc.dapi.utils.FinancialDataUtil;

/**
 * 
 * @author bkumar1 
 * Description: This is the scheduler class to update the 
 * 				cache at regular intervals.
 */

@Configuration
@EnableScheduling
public class FinancialDataScheduler {
	private static final Logger logger = LoggerFactory.getLogger(FinancialDataScheduler.class);

	/*@Scheduled(initialDelay = 0, fixedDelay = 30000000)
	private static void loadAllFirms() {
        logger.info("Loading all firms started");
		FinancialDataUtil.loadAllFirms();
		logger.info("Completed loading all firms");
	}*/
}
