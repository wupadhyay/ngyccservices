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

/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
public class Latency {

    public  String min;
    public  String max;
    public  String avg;
    public  List<LatencyBreakup> breakups;

    @JsonCreator
    public Latency(@JsonProperty("min") String min, @JsonProperty("max") String max, 
    		@JsonProperty("avg") String avg, @JsonProperty("breakups") List<LatencyBreakup> breakups){
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.breakups = breakups;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Latency [min=");
		builder.append(min);
		builder.append(", max=");
		builder.append(max);
		builder.append(", avg=");
		builder.append(avg);
		builder.append(", breakups=");
		builder.append(breakups);
		builder.append("]");
		return builder.toString();
	}

    

}
