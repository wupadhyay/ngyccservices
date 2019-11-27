/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.historic;

import java.util.Date;
/**
 * 
 * @author knavuluri
 *
 */
public interface SiteCntrHistoricLatencyStatsCustomRepository {
 public Long getJobId(String reportName);
 public String getStats(Long cobrandId,Long jobId,Date durationDate, Date durationOffSetDate,Long siteId);
}
