/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.overall;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface OverAllRefStatsLatencyRepository extends MongoRepository<OverAllRefStatsLatency,Long>,OverAllRefStatsLatencyCustomRepository{
	
	@Query("{timestamp :{'$gte':?2},cobrand_id:?1 , job_id:?0   }")
	List<OverAllRefStatsLatency> getOverAllRefStats(long jobId,long cobrandId, Date fromTime);
	
	@Query("{cobrand_id:?1, job_id: ?0  }")
	List<OverAllRefStatsLatency> getOverAllRefStats(long jobId,long cobrandId);
	
	@Query("{job_id: ?0,cobrand_id:?1 ,$limit:?3 }")
	List<OverAllRefStatsLatency> getOverAllRefStats(long jobId,long cobrandId,long limit);

}
