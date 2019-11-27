/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.historic;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 * @author knavuluri
 *
 */
public interface SiteCntrHistoricLatencyStatsRepository extends MongoRepository<SiteCntrHistoricLatencyStats,Long>,SiteCntrHistoricLatencyStatsCustomRepository{
	
}
