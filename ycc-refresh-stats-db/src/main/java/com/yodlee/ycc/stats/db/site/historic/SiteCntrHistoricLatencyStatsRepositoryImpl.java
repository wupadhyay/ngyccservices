/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.historic;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.ycc.stats.db.util.RefreshStatDbUtil;

/**
 * 
 * @author knavuluri
 * 
 */
public class SiteCntrHistoricLatencyStatsRepositoryImpl implements SiteCntrHistoricLatencyStatsCustomRepository {
	private static final Logger logger = LoggerFactory.getLogger(SiteCntrHistoricLatencyStatsRepositoryImpl.class);
	@Autowired
	@Qualifier("yccRefreshStatTemplate")
	MongoTemplate template;

	@Override
	public Long getJobId(String reportName) {
		return new RefreshStatDbUtil().getJobId(reportName, template);
	}

	@Override
	public String getStats(Long cobrandId, Long jobId, Date durationDate, Date durationOffset, Long siteId) {

		List<SiteCntrHistoricLatencyStats> siteHistAllRefStats1 = new ArrayList<SiteCntrHistoricLatencyStats>();
		try {
			Query query = new Query();
			Criteria criteria = Criteria.where("cobrand_id").is(cobrandId).and("job_id").is(jobId).and("site_id").is(siteId);
			List<Criteria> criterias = new ArrayList<>();
			criterias.add(criteria);
			if (durationOffset != null && durationDate != null) {
				criterias.add(Criteria.where("timestamp").lte(durationDate));
				criterias.add(Criteria.where("timestamp").gte(durationOffset));
			}
			else if (durationOffset != null)
				criteria.and("timestamp").gte(durationOffset);

			Criteria criteria1 = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			query.addCriteria(criteria1);
			query.with(new Sort(Sort.Direction.DESC, "timestamp"));

			List<SiteCntrHistoricLatencyStats> siteHistRefStats = template.find(query, SiteCntrHistoricLatencyStats.class);

			logger.debug("Cobrand Id:" + cobrandId + "Site Id:" + siteId + " Site historic stats:" + siteHistRefStats);

			for (SiteCntrHistoricLatencyStats overAllRefStatsLatency : siteHistRefStats) {
				overAllRefStatsLatency.setId(null);
				SimpleDateFormat pstFormat = new SimpleDateFormat("yyyy/MM/dd");
				pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));
				overAllRefStatsLatency.setTimeStampNew(pstFormat.format(overAllRefStatsLatency.getTimestamp()));
				
				siteHistAllRefStats1.add(overAllRefStatsLatency);
			}
		}
		catch (Exception e) {
			logger.error("Error while accessing the Site historic stats for site Id" + siteId + " :" + e);
		}
		Gson gson = new Gson();
		String json = gson.toJson(siteHistAllRefStats1);
		json = "{\"results\":" + json + "}";
		json= RefreshStatDbUtil.processResponse(json);
		return json;
	}

}
