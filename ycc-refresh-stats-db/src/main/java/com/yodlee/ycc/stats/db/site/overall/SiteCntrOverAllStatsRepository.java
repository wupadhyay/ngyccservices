/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.overall;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface SiteCntrOverAllStatsRepository extends MongoRepository<SiteCntrOverAllStats,Long>,SiteCntrOverAllStatsCustomRepository{
	
}
