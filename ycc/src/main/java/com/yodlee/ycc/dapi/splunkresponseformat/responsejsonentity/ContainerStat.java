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


public class ContainerStat {

    public final String id;
    public final String name;
    public final String container;
    public  String totalVolume;
    public final SuccessEntity success;
    public final  List<FailureEntity> failure;
    public final Latency latency;
    public final PartialSuccessEntity partialSuccess;

    @JsonCreator
    public ContainerStat(@JsonProperty("id") String id, @JsonProperty("name") String name,
    		@JsonProperty("container") String container, @JsonProperty("totalVolume") String totalVolume,
    		@JsonProperty("success") SuccessEntity success, @JsonProperty("failure") List<FailureEntity> failure, @JsonProperty("latency") Latency latency,@JsonProperty("partialSuccess") PartialSuccessEntity partialSuccess){
        this.id = id;
        this.name = name;
        this.container = container;
        this.totalVolume = totalVolume;
        this.success = success;
        this.failure = failure;
        this.latency = latency;
        this.partialSuccess = partialSuccess;
    }


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContainerStat [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", container=");
		builder.append(container);
		builder.append(", totalVolume=");
		builder.append(totalVolume);
		builder.append(", success=");
		builder.append(success);
		builder.append(", failure=");
		builder.append(failure);
		builder.append(", latency=");
		builder.append(latency);
		builder.append(", partialSuccess=");
		builder.append(partialSuccess);
		builder.append("]");
		return builder.toString();
	}
    
    

}
