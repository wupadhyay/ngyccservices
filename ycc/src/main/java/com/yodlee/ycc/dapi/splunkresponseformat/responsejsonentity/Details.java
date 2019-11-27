/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;



public final class Details {
    public final String date;
    public final Summary summary;
   

    @JsonCreator
    public Details(@JsonProperty("date") String date, @JsonProperty("summary") Summary summary){
        this.date = date;
        this.summary = summary;
    }


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Details [date=");
		builder.append(date);
		builder.append(", summary=");
		builder.append(summary);
		builder.append("]");
		return builder.toString();
	}

    
}

