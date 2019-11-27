/**
 * Copyright (c) 2017 Yodlee Incorporated. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Yodlee, Inc. Use is subject to license terms.
 *
 */

package com.yodlee.ycc.stats.db.config;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author knavuluri
 * 
 */
public class MongodbInitInfo implements Serializable{

	private Long[] cobrandsArray = null;

	private Set<Long> cobrandsList = null;

	private String dbName;

	private Set<String> dbNameList = null;

	private Map<String, Set<Long>> dbNameCobrandMap = null;

	private String serverName;

	private int portNumber;

	private String userName;

	private String password;

	private String userNameEnc;

	private String userPasswordEnc;

	private Set<String> replicaset;

	private String readPref;

	private String writeconcern;

	private int maxConnections;

	private int connectionTimeout;

	private int socketTimeout;

	private String dbType;

	private String dbId;

	public Long[] getCobrandsArray() {
		return cobrandsArray;
	}

	public void setCobrandsArray(Long[] cobrandsArray) {
		this.cobrandsArray = cobrandsArray;
	}

	public Set<Long> getCobrandsList() {
		return cobrandsList;
	}

	public void setCobrandsList(Set<Long> cobrandsList) {
		this.cobrandsList = cobrandsList;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Set<String> getDbNameList() {
		return dbNameList;
	}

	public void setDbNameList(Set<String> dbNameList) {
		this.dbNameList = dbNameList;
	}

	public Map<String, Set<Long>> getDbNameCobrandMap() {
		return dbNameCobrandMap;
	}

	public void setDbNameCobrandMap(Map<String, Set<Long>> dbNameCobrandMap) {
		this.dbNameCobrandMap = dbNameCobrandMap;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserNameEnc() {
		return userNameEnc;
	}

	public void setUserNameEnc(String userNameEnc) {
		this.userNameEnc = userNameEnc;
	}

	public String getUserPasswordEnc() {
		return userPasswordEnc;
	}

	public void setUserPasswordEnc(String userPasswordEnc) {
		this.userPasswordEnc = userPasswordEnc;
	}

	public Set<String> getReplicaset() {
		return replicaset;
	}

	public void setReplicaset(Set<String> replicaset) {
		this.replicaset = replicaset;
	}

	public String getReadPref() {
		return readPref;
	}

	public void setReadPref(String readPref) {
		this.readPref = readPref;
	}

	public String getWriteconcern() {
		return writeconcern;
	}

	public void setWriteconcern(String writeconcern) {
		this.writeconcern = writeconcern;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	

	
}
