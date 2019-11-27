/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  final class Message {
	public String type;
    public String text;

    @JsonCreator
    public  Message(@JsonProperty("type") String type,
    		@JsonProperty("text") String text){
    	this.type=type;
    	this.text=text;
    	
    }
}