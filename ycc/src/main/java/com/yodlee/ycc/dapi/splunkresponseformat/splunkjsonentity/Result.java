/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
	public Result() {

	}

	@JsonProperty(value = "avg_latency")
	public String avg_latency;
	@JsonProperty(value = "cobrand_id")
	public String cobrand_id;
	@JsonProperty(value = "latency_0_20")
	public String latency_0_20;
	@JsonProperty(value = "latency_20_40")
	public String latency_20_40;
	@JsonProperty(value = "latency_40_60")
	public String latency_40_60;
	@JsonProperty(value = "latency_60_80")
	public String latency_60_80;
	@JsonProperty(value = "latency_80_100")
	public String latency_80_100;
	@JsonProperty(value = "latency_above_100")
	public String latency_above_100;
	@JsonProperty(value = "max_latency")
	public String max_latency;

	@JsonProperty(value = "min_latency")
	public String min_latency;
	@JsonProperty(value = "site_dependent_errors")
	public String site_dependent_errors;
	@JsonProperty(value = "site_id")
	public String site_id;
	@JsonProperty(value = "successful_refreshes")
	public String successful_refreshes;
	@JsonProperty(value = "sum_info_id")
	public String sum_info_id;
	@JsonProperty(value = "tag_id")
	public String tag_id;
	@JsonProperty(value = "technical_errors")
	public String technical_errors;
	@JsonProperty(value = "timestamp")
	public String timestamp;
	@JsonProperty(value = "timestamp_of_insert")
	public String timestamp_of_insert;
	@JsonProperty(value = "total_refreshes")
	public String total_refreshes;
	@JsonProperty(value = "user_dependent_errors")
	public String user_dependent_errors;
	public String epochhours_ago;
	@JsonProperty(value = "time_now")
	public String time_now;
	@JsonProperty(value = "time_from")
	public String time_from;
	@JsonProperty(value = "partial_success")
	public String partial_successful_refreshes;
	public String id;
	public String job_id;

	public String getAvg_latency() {
		return avg_latency;
	}

	public void setAvg_latency(String avg_latency) {
		this.avg_latency = avg_latency;
	}

	public String getCobrand_id() {
		return cobrand_id;
	}

	public void setCobrand_id(String cobrand_id) {
		this.cobrand_id = cobrand_id;
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

	public String getMax_latency() {
		return max_latency;
	}

	public void setMax_latency(String max_latency) {
		this.max_latency = max_latency;
	}

	public String getMin_latency() {
		return min_latency;
	}

	public void setMin_latency(String min_latency) {
		this.min_latency = min_latency;
	}

	public String getSite_dependent_errors() {
		return site_dependent_errors;
	}

	public void setSite_dependent_errors(String site_dependent_errors) {
		this.site_dependent_errors = site_dependent_errors;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getSuccessful_refreshes() {
		return successful_refreshes;
	}

	public void setSuccessful_refreshes(String successful_refreshes) {
		this.successful_refreshes = successful_refreshes;
	}

	public String getSum_info_id() {
		return sum_info_id;
	}

	public void setSum_info_id(String sum_info_id) {
		this.sum_info_id = sum_info_id;
	}

	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

	public String getTechnical_errors() {
		return technical_errors;
	}

	public void setTechnical_errors(String technical_errors) {
		this.technical_errors = technical_errors;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp_of_insert() {
		return timestamp_of_insert;
	}

	public void setTimestamp_of_insert(String timestamp_of_insert) {
		this.timestamp_of_insert = timestamp_of_insert;
	}

	public String getTotal_refreshes() {
		return total_refreshes;
	}

	public void setTotal_refreshes(String total_refreshes) {
		this.total_refreshes = total_refreshes;
	}

	public String getUser_dependent_errors() {
		return user_dependent_errors;
	}

	public void setUser_dependent_errors(String user_dependent_errors) {
		this.user_dependent_errors = user_dependent_errors;
	}

	public String getEpochhours_ago() {
		return epochhours_ago;
	}

	public void setEpochhours_ago(String epochhours_ago) {
		this.epochhours_ago = epochhours_ago;
	}

	public String getTime_now() {
		return time_now;
	}

	public void setTime_now(String time_now) {
		this.time_now = time_now;
	}

	public String getTime_from() {
		return time_from;
	}

	public void setTime_from(String time_from) {
		this.time_from = time_from;
	}

	public String getPartial_successful_refreshes() {
		return partial_successful_refreshes;
	}

	public void setPartial_successful_refreshes(String partial_successful_refreshes) {
		this.partial_successful_refreshes = partial_successful_refreshes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	@Override
	public String toString() {
		return "Result [avg_latency=" + avg_latency + ", cobrand_id=" + cobrand_id + ", latency_0_20=" + latency_0_20 + ", latency_20_40=" + latency_20_40 + ", latency_40_60=" + latency_40_60
				+ ", latency_60_80=" + latency_60_80 + ", latency_80_100=" + latency_80_100 + ", latency_above_100=" + latency_above_100 + ", max_latency=" + max_latency + ", min_latency="
				+ min_latency + ", site_dependent_errors=" + site_dependent_errors + ", site_id=" + site_id + ", successful_refreshes=" + successful_refreshes + ", sum_info_id=" + sum_info_id
				+ ", tag_id=" + tag_id + ", technical_errors=" + technical_errors + ", timestamp=" + timestamp + ", timestamp_of_insert=" + timestamp_of_insert + ", total_refreshes="
				+ total_refreshes + ", user_dependent_errors=" + user_dependent_errors + ", epochhours_ago=" + epochhours_ago + ", time_now=" + time_now + ", time_from=" + time_from
				+ ", partial_successful_refreshes=" + partial_successful_refreshes + ", id=" + id + ", job_id=" + job_id + "]";
	}

	
}
