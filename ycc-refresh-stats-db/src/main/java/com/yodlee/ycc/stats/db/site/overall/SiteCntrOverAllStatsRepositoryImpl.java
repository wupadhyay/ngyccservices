/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.overall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
public class SiteCntrOverAllStatsRepositoryImpl implements SiteCntrOverAllStatsCustomRepository {
	private static final Logger logger = LoggerFactory.getLogger(SiteCntrOverAllStatsRepositoryImpl.class);
	@Autowired
	@Qualifier("yccRefreshStatTemplate")
	MongoTemplate template;

	@Override
	public Long getJobId(String reportName) {
		return new RefreshStatDbUtil().getJobId(reportName, template);
	}

	@Override
	public String getStats(Long cobrandId, Long jobId, Date durationDate, Date durationOffset, Long siteId) {

		List<SiteCntrOverAllStats> overAllRefStats1 = new ArrayList<SiteCntrOverAllStats>();
		try {
			Query query = new Query();
			Criteria criteria = Criteria.where("cobrand_id").is(cobrandId).and("job_id").is(jobId).and("site_id").is(siteId);
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

			List<SiteCntrOverAllStats> overAllRefStats = template.find(query, SiteCntrOverAllStats.class);

			logger.debug("Cobrand Id:" + cobrandId + "Site Id:" + siteId + " Site Over all stats:" + overAllRefStats);

			for (SiteCntrOverAllStats overAllRefStatsLatency : overAllRefStats) {
				overAllRefStatsLatency.setId(null);
				SimpleDateFormat pstFormat = new SimpleDateFormat("yyyy/MM/dd HH");
				pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));
				overAllRefStatsLatency.setTimeStampNew(pstFormat.format(overAllRefStatsLatency.getTimestamp()));
				
				overAllRefStats1.add(overAllRefStatsLatency);
			}
		}
		catch (Exception e) {
			logger.error("Error while accessing the Site over all stats for site Id" + siteId + " :" + e);
		}
		Gson gson = new Gson();
		String json = gson.toJson(overAllRefStats1);
		json = "{\"results\":" + json + "}";
		json= RefreshStatDbUtil.processResponse(json);
		return json;
	}

}
