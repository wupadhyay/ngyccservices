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

public class FailureEntity {

    public final String type;
    public String volume;
    public  String rate;
    public List<ErrorCodeBreakUp> breakups;
   

    @JsonCreator
    public FailureEntity(@JsonProperty("type") String type, @JsonProperty("volume") String volume, @JsonProperty("rate") String rate,
    		@JsonProperty("breakups") List<ErrorCodeBreakUp> breakups){
        this.type = type;
        this.volume = volume;
        this.rate = rate;
        this.breakups = breakups;
    }
    
    public FailureEntity(String type, String techErrorVolume,
			String techErrorPer) {
		this(type,techErrorVolume,techErrorPer,null);
	}

	public void setBreakups(List<ErrorCodeBreakUp> breakups) {
		this.breakups = breakups;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FailureEntity [type=");
		builder.append(type);
		builder.append(", volume=");
		builder.append(volume);
		builder.append(", rate=");
		builder.append(rate);
		builder.append(", breakups=");
		builder.append(breakups);
		builder.append("]");
		return builder.toString();
	}
    
    


}
