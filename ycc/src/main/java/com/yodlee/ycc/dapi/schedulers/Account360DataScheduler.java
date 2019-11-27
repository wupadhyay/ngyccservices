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


import com.yodlee.ycc.dapi.utils.ReconDataAccessor;

/**
 * 
 * @author ggayanwardhan 
 * Description: This is the scheduler class to update the 
 * 				cache with extract and hadoop date at regular intervals.
 */

@Configuration
@EnableScheduling
public class Account360DataScheduler {
	private static final Logger logger = LoggerFactory.getLogger(Account360DataScheduler.class);

	/*@Scheduled(initialDelay = 200000, fixedDelay = 1800000)
	private static void loadAllFirms() {
        logger.info("Loading Extract and Hadoop Date started");
		ReconDataAccessor.loadDates();
		logger.info("Loading Extract and Hadoop Date Completed");
	}*/
}
