/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.bean;

import java.util.List;

/**
 * 
 * @author knavuluri
 *
 */
public class GlobalMessageFilter {
	private Long globaleMessageId;
	private List<Long> siteIds;
	private Long cobrandId;
	private List<Long> statusIds;
	private boolean hasResolvedStatus;
	private boolean yodlee;

	public Long getGlobaleMessageId() {
		return globaleMessageId;
	}

	public void setGlobaleMessageId(Long globaleMessageId) {
		this.globaleMessageId = globaleMessageId;
	}

	public Long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public List<Long> getStatusIds() {
		return statusIds;
	}

	public void setStatusIds(List<Long> statusIds) {
		this.statusIds = statusIds;
	}

	public List<Long> getSiteIds() {
		return siteIds;
	}

	public void setSiteIds(List<Long> siteIds) {
		this.siteIds = siteIds;
	}

	public boolean isHasResolvedStatus() {
		if (statusIds != null && statusIds.contains(7l)) {
			statusIds.remove(7l);
			return true;
		}
		return hasResolvedStatus;
	}

	public void setHasResolvedStatus(boolean hasResolvedStatus) {
		this.hasResolvedStatus = hasResolvedStatus;
	}

	public boolean isYodlee() {
		return yodlee;
	}

	public void setYodlee(boolean yodlee) {
		this.yodlee = yodlee;
	}

	@Override
	public String toString() {
		return "GlobalMessageFilter [globaleMessageId=" + globaleMessageId + ", siteIds=" + siteIds + ", cobrandId=" + cobrandId + ", statusIds=" + statusIds
				+ ", hasResolvedStatus=" + hasResolvedStatus + ", yodlee=" + yodlee + "]";
	}

}
