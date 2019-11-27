/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories(basePackages ="com.yodlee.ycc.stats.db",mongoTemplateRef="yccRefreshStatTemplate")
@ComponentScan("com.yodlee.ycc.stats.db") 
public class SpringRefreshDbConfig {

}
