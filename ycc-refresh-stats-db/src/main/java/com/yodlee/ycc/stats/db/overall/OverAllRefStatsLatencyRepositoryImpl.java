/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.overall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.gson.Gson;
import com.yodlee.ycc.stats.db.util.RefreshStatDbUtil;
/**
 * 
 * @author knavuluri
 *
 */
public class OverAllRefStatsLatencyRepositoryImpl implements OverAllRefStatsLatencyCustomRepository {
	private static final Logger logger = LoggerFactory.getLogger(OverAllRefStatsLatencyRepositoryImpl.class);
	@Autowired
	@Qualifier("yccRefreshStatTemplate")
	MongoTemplate template;

	@Override
	public Long getJobId(String reportName) {
		return new RefreshStatDbUtil().getJobId(reportName, template);
	}

	@Override
	public String getStats(Long cobrandId, Long jobId, Date durationDate, Date durationOffset) {

		List<OverAllRefStatsLatency> overAllRefStats1 = new ArrayList<OverAllRefStatsLatency>();
		try {
			Query query = new Query();
			Criteria criteria = Criteria.where("cobrand_id").is(cobrandId).and("job_id").is(jobId);
			List<Criteria> criterias = new ArrayList<>();
			criterias.add(criteria);
			if (durationOffset != null && durationDate != null) {
				criterias.add(Criteria.where("timestamp").lt(durationDate));
				criterias.add(Criteria.where("timestamp").gt(durationOffset));
			}
			else if (durationOffset != null)
				criteria.and("timestamp").gt(durationOffset);
			
			Criteria criteria1 = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			query.addCriteria(criteria1);
			query.with(new Sort(Sort.Direction.DESC, "timestamp"));

			List<OverAllRefStatsLatency> overAllRefStats = template.find(query, OverAllRefStatsLatency.class);

			logger.debug("OverallStats for cobrandId:"+cobrandId+":"+overAllRefStats);
			
			for (OverAllRefStatsLatency overAllRefStatsLatency : overAllRefStats) {
				overAllRefStatsLatency.setId(null);
				overAllRefStats1.add(overAllRefStatsLatency);
			}
		}
		catch (Exception e) {
			logger.error("Error while accessing the cobrand over all stats:"+e);
		}
		Gson gson = new Gson();
		String json = gson.toJson(overAllRefStats1);
		json = "{\"results\":" + json + "}";
		return json;
	}

}
