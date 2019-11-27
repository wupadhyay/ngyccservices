/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.jobstatus;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface JobStatusRepository extends MongoRepository<JobStatus,Long>{
	
	@Query("{job_id:?0, job_name: ?1, job_status:?2  }")
	JobStatus getJob(long jobId,String jobName,String jobStatus);

}
