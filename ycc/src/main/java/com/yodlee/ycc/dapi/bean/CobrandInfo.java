/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.bean;

import java.util.List;


/**
 * 
 * @author knavuluri
 *
 */
public class CobrandInfo implements Comparable<CobrandInfo>{
	private Long cobrandId;
	private Long cobrandStatusId;
	private String name;
	private String loginName;
	private String loginNameAsucase;
	private String password;
	private long created;
	private long lastUpdated;
	private boolean isCacherunDisabled;
	private Long channelId;
	private Boolean isChannel;
	private boolean isYodlee;
	private boolean iavEnabled;
	private boolean slmrEnabled;
	private boolean balanceRefreshEnabled;
	private boolean iavCacheRefreshEnabled;
	private List<CobrandInfo> subbrands;
	private boolean emailTriggerEnabled;
	
	public boolean isEmailTriggerEnabled() {
		return emailTriggerEnabled;
	}
	public void setEmailTriggerEnabled(boolean emailTriggerEnabled) {
		this.emailTriggerEnabled = emailTriggerEnabled;
	}
	public Long getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}
	public Long getCobrandStatusId() {
		return cobrandStatusId;
	}
	public void setCobrandStatusId(Long cobrandStatusId) {
		this.cobrandStatusId = cobrandStatusId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginNameAsucase() {
		return loginNameAsucase;
	}
	public void setLoginNameAsucase(String loginNameAsucase) {
		this.loginNameAsucase = loginNameAsucase;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public long getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public boolean isCacherunDisabled() {
		return isCacherunDisabled;
	}
	public void setCacherunDisabled(boolean isCacherunDisabled) {
		this.isCacherunDisabled = isCacherunDisabled;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Boolean getIsChannel() {
		return isChannel;
	}
	public void setIsChannel(Boolean isChannel) {
		this.isChannel = isChannel;
	}
	public boolean isYodlee() {
		return isYodlee;
	}
	public void setYodlee(boolean isYodlee) {
		this.isYodlee = isYodlee;
	}
	public boolean isIavEnabled() {
		return iavEnabled;
	}
	public void setIavEnabled(boolean iavEnabled) {
		this.iavEnabled = iavEnabled;
	}
	public boolean isSlmrEnabled() {
		return slmrEnabled;
	}
	public void setSlmrEnabled(boolean slmrEnabled) {
		this.slmrEnabled = slmrEnabled;
	}
	public boolean isBalanceRefreshEnabled() {
		return balanceRefreshEnabled;
	}
	public void setBalanceRefreshEnabled(boolean balanceRefreshEnabled) {
		this.balanceRefreshEnabled = balanceRefreshEnabled;
	}
	
	public boolean isIavCacheRefreshEnabled() {
		return iavCacheRefreshEnabled;
	}
	public void setIavCacheRefreshEnabled(boolean iavCacheRefreshEnabled) {
		this.iavCacheRefreshEnabled = iavCacheRefreshEnabled;
	}
	@Override
	public String toString() {
		return "CobrandInfo [cobrandId=" + cobrandId + ", cobrandStatusId=" + cobrandStatusId + ", name=" + name + ", loginName=" + loginName + ", loginNameAsucase=" + loginNameAsucase
				+ ", password=" + password + ", created=" + created + ", lastUpdated=" + lastUpdated + ", isCacherunDisabled=" + isCacherunDisabled + ", channelId=" + channelId + ", isChannel="
				+ isChannel + ", isYodlee=" + isYodlee + ", iavEnabled=" + iavEnabled + ", slmrEnabled=" + slmrEnabled + ", balanceRefreshEnabled=" + balanceRefreshEnabled + ", iavCacheRefreshEnabled="
				+ iavCacheRefreshEnabled + ",Subbrands="+subbrands+"]";
	}
	

	@Override
	public int compareTo(CobrandInfo cobInfo) {
		if (cobInfo != null)
			return this.getCobrandId().compareTo(cobInfo.getCobrandId());
		return 0;
	}
	public List<CobrandInfo> getSubbrands() {
		return subbrands;
	}
	public void setSubbrands(List<CobrandInfo> subbrands) {
		this.subbrands = subbrands;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cobrandId == null) ? 0 : cobrandId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CobrandInfo other = (CobrandInfo) obj;
		if (cobrandId == null) {
			if (other.cobrandId != null)
				return false;
		}
		else if (!cobrandId.equals(other.cobrandId))
			return false;
		return true;
	}

}
