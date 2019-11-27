/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public  final class Info {
    public  String id;
    public  String name;
    public  String lastModified;
    public  Summary summary;
    public List<Details> details;
    @JsonIgnore
    private Float perWithOutUar;

    @JsonCreator
    public Info(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("lastModified") String lastModified, @JsonProperty("summary") Summary summary, @JsonProperty("details") List<Details> details){
        this.id = id;
        this.name = name;
        this.lastModified = lastModified;
        this.summary = summary;
        this.details = details;
    }


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Info [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", lastModified=");
		builder.append(lastModified);
		builder.append(", summary=");
		builder.append(summary);
		builder.append(", details=");
		builder.append(details);
		builder.append("]");
		return builder.toString();
	}


	public Float getPerWithOutUar() {
		return perWithOutUar;
	}


	public void setPerWithOutUar(Float perWithOutUar) {
		this.perWithOutUar = perWithOutUar;
	}
    
    
}
