/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.site.overall;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author knavuluri
 * 
 */
@Document(collection = "site_cntr_overall_stats")
public class SiteCntrOverAllStats {

	@Id
	private Object id;
	private long job_id;
	private Date timestamp;
	private long cobrand_id;
	private long site_id;
	private long sum_info_id;
	private long tag_id;
	private String avg_latency;
	private String site_dependent_errors;
	private String successful_refreshes;
	private String technical_errors;
	private String total_refreshes;
	private String user_dependent_errors;
	private String timestamp_of_insert;
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

	@Override
	public String toString() {
		return "SiteCntrOverAllStats [id=" + id + ", job_id=" + job_id + ", timestamp=" + timestamp + ", cobrand_id=" + cobrand_id + ", site_id=" + site_id + ", sum_info_id=" + sum_info_id
				+ ", tag_id=" + tag_id + ", avg_latency=" + avg_latency + ", site_dependent_errors=" + site_dependent_errors + ", successful_refreshes=" + successful_refreshes + ", technical_errors="
				+ technical_errors + ", total_refreshes=" + total_refreshes + ", user_dependent_errors=" + user_dependent_errors + ", timestamp_of_insert=" + timestamp_of_insert + ",timeStampNew="+timeStampNew+"]";
	}

	public String getTimeStampNew() {
		return timeStampNew;
	}

	public void setTimeStampNew(String timeStampNew) {
		this.timeStampNew = timeStampNew;
	}

}
