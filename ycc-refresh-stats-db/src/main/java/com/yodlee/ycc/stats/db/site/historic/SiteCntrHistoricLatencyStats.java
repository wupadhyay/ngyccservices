/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.historic;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author knavuluri
 * 
 */
@Document(collection = "site_cntr_hist_latency")
public class SiteCntrHistoricLatencyStats {
	@Id
	private Object id;
	private long job_id;
	private Date timestamp;
	private long cobrand_id;
	private String total_refreshes;
	private String successful_refreshes;
	private String technical_errors;
	private String site_dependent_errors;
	private String user_dependent_errors;
	private String avg_latency;
	private String min_latency;
	private String max_latency;
	private String latency_0_20;
	private String latency_20_40;
	private String latency_40_60;
	private String latency_60_80;
	private String latency_80_100;
	private String latency_above_100;
	private String timestamp_of_insert;
	private long site_id;
	private long sum_info_id;
	private long tag_id;
	private String category;
	private String timeStampNew;
		
	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public long getJob_id() {
		return job_id;
	}

	public void setJob_id(long job_id) {
		this.job_id = job_id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public long getCobrand_id() {
		return cobrand_id;
	}

	public void setCobrand_id(long cobrand_id) {
		this.cobrand_id = cobrand_id;
	}

	public String getTotal_refreshes() {
		return total_refreshes;
	}

	public void setTotal_refreshes(String total_refreshes) {
		this.total_refreshes = total_refreshes;
	}

	public String getSuccessful_refreshes() {
		return successful_refreshes;
	}

	public void setSuccessful_refreshes(String successful_refreshes) {
		this.successful_refreshes = successful_refreshes;
	}

	public String getTechnical_errors() {
		return technical_errors;
	}

	public void setTechnical_errors(String technical_errors) {
		this.technical_errors = technical_errors;
	}

	public String getSite_dependent_errors() {
		return site_dependent_errors;
	}

	public void setSite_dependent_errors(String site_dependent_errors) {
		this.site_dependent_errors = site_dependent_errors;
	}

	public String getUser_dependent_errors() {
		return user_dependent_errors;
	}

	public void setUser_dependent_errors(String user_dependent_errors) {
		this.user_dependent_errors = user_dependent_errors;
	}

	public String getAvg_latency() {
		return avg_latency;
	}

	public void setAvg_latency(String avg_latency) {
		this.avg_latency = avg_latency;
	}

	public String getMin_latency() {
		return min_latency;
	}

	public void setMin_latency(String min_latency) {
		this.min_latency = min_latency;
	}

	public String getMax_latency() {
		return max_latency;
	}

	public void setMax_latency(String max_latency) {
		this.max_latency = max_latency;
	}

	public String getLatency_0_20() {
		return latency_0_20;
	}

	public void setLatency_0_20(String latency_0_20) {
		this.latency_0_20 = latency_0_20;
	}

	public String getLatency_20_40() {
		return latency_20_40;
	}

	public void setLatency_20_40(String latency_20_40) {
		this.latency_20_40 = latency_20_40;
	}

	public String getLatency_40_60() {
		return latency_40_60;
	}

	public void setLatency_40_60(String latency_40_60) {
		this.latency_40_60 = latency_40_60;
	}

	public String getLatency_60_80() {
		return latency_60_80;
	}

	public void setLatency_60_80(String latency_60_80) {
		this.latency_60_80 = latency_60_80;
	}

	public String getLatency_80_100() {
		return latency_80_100;
	}

	public void setLatency_80_100(String latency_80_100) {
		this.latency_80_100 = latency_80_100;
	}

	public String getLatency_above_100() {
		return latency_above_100;
	}

	public void setLatency_above_100(String latency_above_100) {
		this.latency_above_100 = latency_above_100;
	}

	public String getTimestamp_of_insert() {
		return timestamp_of_insert;
	}

	public void setTimestamp_of_insert(String timestamp_of_insert) {
		this.timestamp_of_insert = timestamp_of_insert;
	}

	public long getSite_id() {
		return site_id;
	}

	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}

	public long getSum_info_id() {
		return sum_info_id;
	}

	public void setSum_info_id(long sum_info_id) {
		this.sum_info_id = sum_info_id;
	}

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "SiteCntrHistoricLatencyStats [id=" + id + ", job_id=" + job_id + ", timestamp=" + timestamp + ", cobrand_id=" + cobrand_id + ", total_refreshes=" + total_refreshes
				+ ", successful_refreshes=" + successful_refreshes + ", technical_errors=" + technical_errors + ", site_dependent_errors=" + site_dependent_errors + ", user_dependent_errors="
				+ user_dependent_errors + ", avg_latency=" + avg_latency + ", min_latency=" + min_latency + ", max_latency=" + max_latency + ", latency_0_20=" + latency_0_20 + ", latency_20_40="
				+ latency_20_40 + ", latency_40_60=" + latency_40_60 + ", latency_60_80=" + latency_60_80 + ", latency_80_100=" + latency_80_100 + ", latency_above_100=" + latency_above_100
				+ ", timestamp_of_insert=" + timestamp_of_insert + ", site_id=" + site_id + ", sum_info_id=" + sum_info_id + ", tag_id=" + tag_id + ", category=" + category + ",timeStampNew="+timeStampNew+"]";
	}

	public String getTimeStampNew() {
		return timeStampNew;
	}

	public void setTimeStampNew(String timeStampNew) {
		this.timeStampNew = timeStampNew;
	}

}
