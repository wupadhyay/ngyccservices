/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.overall;

import java.util.Date;

public interface OverAllRefStatsLatencyCustomRepository {
 public Long getJobId(String reportName);
 public String getStats(Long cobrandId,Long jobId,Date durationDate, Date durationOffSetDate);
}
