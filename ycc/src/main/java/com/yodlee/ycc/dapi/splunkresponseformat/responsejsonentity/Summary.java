/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Summary {

    public  String totalVolume;
    public  SuccessEntity success;
    public  List<FailureEntity> failure;
    public  Latency latency;
    public  List<ContainerStat> containerStats;
    public  PartialSuccessEntity partialSuccess;
    
   
    
    String getTotalVolume() {
		return totalVolume;
	}



	void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}



	SuccessEntity getSuccess() {
		return success;
	}



	void setSuccess(SuccessEntity success) {
		this.success = success;
	}



	List<FailureEntity> getFailure() {
		return failure;
	}



	void setFailure(List<FailureEntity> failure) {
		this.failure = failure;
	}



	Latency getLatency() {
		return latency;
	}



	void setLatency(Latency latency) {
		this.latency = latency;
	}



	List<ContainerStat> getContainerStats() {
		return containerStats;
	}



	void setContainerStats(List<ContainerStat> containerStats) {
		this.containerStats = containerStats;
	}



	PartialSuccessEntity getPartialSuccess() {
		return partialSuccess;
	}



	void setPartialSuccess(PartialSuccessEntity partialSuccess) {
		this.partialSuccess = partialSuccess;
	}



	@JsonCreator
    public Summary(@JsonProperty("totalVolume") String totalVolume, @JsonProperty("success") SuccessEntity success, @JsonProperty("failure") List<FailureEntity> failure, @JsonProperty("latency") Latency latency, @JsonProperty("containerStats") List<ContainerStat> containerStats,@JsonProperty("partialSuccess") PartialSuccessEntity partialSuccess){
        this.totalVolume = totalVolume;
        this.success = success;
        this.failure = failure;
        this.latency = latency;
        this.containerStats = containerStats;
        this.partialSuccess = partialSuccess;
    }



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Summary [totalVolume=");
		builder.append(totalVolume);
		builder.append(", success=");
		builder.append(success);
		builder.append(", failure=");
		builder.append(failure);
		builder.append(", latency=");
		builder.append(latency);
		builder.append(", containerStats=");
		builder.append(containerStats);
		builder.append(", partialSuccess=");
		builder.append(partialSuccess);
		builder.append("]");
		return builder.toString();
	}
    
    


}
