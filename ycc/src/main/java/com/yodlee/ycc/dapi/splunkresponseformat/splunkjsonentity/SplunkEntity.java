/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.splunkresponseformat.splunkjsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SplunkEntity {
    public final boolean preview;
    public final long init_offset;
    public final Message messages[];
    public final Field fields[];
    public final Result results[];
    public boolean isPreview() {
		return preview;
	}

	public long getInit_offset() {
		return init_offset;
	}

	public Message[] getMessages() {
		return messages;
	}

	public Field[] getFields() {
		return fields;
	}

	public Result[] getResults() {
		return results;
	}

	public Highlighted getHighlighted() {
		return highlighted;
	}

	public final Highlighted highlighted;

    @JsonCreator
    public SplunkEntity(@JsonProperty("preview") boolean preview, @JsonProperty("init_offset") long init_offset, @JsonProperty("messages") Message[] messages, @JsonProperty("fields") Field[] fields, @JsonProperty("results") Result[] results, @JsonProperty("highlighted") Highlighted highlighted){
        this.preview = preview;
        this.init_offset = init_offset;
        this.messages = messages;
        this.fields = fields;
        this.results = results;
        this.highlighted = highlighted;
    }
}