/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessEntity {

    public  String volume;
    public  String rate;
 
    
    @JsonCreator
    public SuccessEntity(@JsonProperty("volume") String volume, @JsonProperty("rate") String rate){
        this.volume = volume;
        this.rate = rate;
    }


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SuccessEntity [volume=");
		builder.append(volume);
		builder.append(", rate=");
		builder.append(rate);
		builder.append("]");
		return builder.toString();
	}
    

}
