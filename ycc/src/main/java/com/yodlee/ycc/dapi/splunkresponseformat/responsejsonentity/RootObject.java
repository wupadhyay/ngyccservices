package com.yodlee.ycc.dapi.splunkresponseformat.responsejsonentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RootObject {
	private RefreshStats refreshStats;

	public RefreshStats getRefreshStats() {
		return this.refreshStats;
	}

	public void setRefreshStats(RefreshStats refreshStats) {
		this.refreshStats = refreshStats;
	}

	 @JsonCreator
	    public RootObject(@JsonProperty("refreshStats") RefreshStats refreshStats){
	        this.refreshStats = refreshStats;
	       
	    }
}
