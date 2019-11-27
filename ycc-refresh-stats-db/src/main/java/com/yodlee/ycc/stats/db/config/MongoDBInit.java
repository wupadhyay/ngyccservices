/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.stats.db.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.yodlee.config.ConfigServiceImpl;
import com.yodlee.config.NVPair;
import com.yodlee.crypto.enc.EncryptionException;
import com.yodlee.crypto.enc.IEncryption;
import com.yodlee.security.external.encryption.DBCredentialsEncryptionFactory;

public class MongoDBInit {
	private static final Logger logger = LoggerFactory.getLogger(MongoDBInit.class);
	
	@Autowired(required = true)
    @Qualifier(value = "ConfigService")
    ConfigServiceImpl configService;
	
	private final String CONFIG_MODULE_NAME = "MONGODB_POOL";
	private static final String PREFIX = "COM.YODLEE.DB.MONGO";
	private static boolean isMongodbPoolEnabled = false;
	private static List<MongodbInitInfo> mongodbdetails = new ArrayList<>();
	
	@Bean(name = "MongoDBService")
    public MongoDBInit initMongoInit() throws Exception {
        System.out.println("intiatign the initMongoInit #####################################");
		populateMongodbDetails();
        return this;
    }
	
	
	public boolean isMongoEnabled() {
		return isMongodbPoolEnabled;
	}
	
	private void populateMongodbDetails() {
		List<NVPair> configKeys = configService.getConfigKeys("CORE", configService.getCoreGroupId(), CONFIG_MODULE_NAME);
		
		for (NVPair config : configKeys) {
			if (config.getType() == NVPair.MULTI_VALUE && config.getKey().equalsIgnoreCase("MONGODB")) {
				List<String> dbvalues = config.getMultivalues();
				if(dbvalues == null) {
					dbvalues = config.getDefaultmultivalues();
				}
				for(String db : dbvalues) {
					MongodbInitInfo mongodb = new MongodbInitInfo();
					List<NVPair>configKeys1 = configService.getConfigKeys(config.getKey(), db);
					
					for(NVPair keyvalues : configKeys1) {
						String key = keyvalues.getKey();
						String value = keyvalues.getValue();
						if(key.equals(PREFIX + ".DBNAME")) {
							mongodb.setDbName(value);
							Set<String> dbNameList = mongodb.getDbNameList();
							if(dbNameList == null) 
								dbNameList = new HashSet<>();
							dbNameList.add(value);
							mongodb.setDbNameList(dbNameList);
						} else if(key.equals(PREFIX + ".SERVERNAME")) {
							mongodb.setServerName(value);
						} else if(key.equals(PREFIX + ".PORTNUMBER")) {
							mongodb.setPortNumber(Integer.parseInt(value));
						}  else if(key.equals(PREFIX + ".APPLICATIONPOOL.MAXCONNECTIONS")) {
							mongodb.setMaxConnections(Integer.parseInt(value));
						} else if(key.equals(PREFIX + ".DBUSER")) {
							IEncryption encryption = DBCredentialsEncryptionFactory.getInstance();
							String userName = "";
							int COUNT = 60;
							try {
								if (value.length() > COUNT) {
									userName = new String(encryption.decrypt(value));
								} else {
									userName = value;
								}
							} catch (EncryptionException e) {
								System.out.println(("Mongo DB credentials Decryption Failure for userName: " + e.getMessage()));
							}
							mongodb.setUserName(userName);
						} else if(key.equals(PREFIX + ".DBPASSWORD")) {
							IEncryption encryption = DBCredentialsEncryptionFactory.getInstance();
							String password = "";
							int COUNT = 60;
							try {
								if (value.length() > COUNT) {
									password = new String(encryption.decrypt(value));
								} else {
									password = value;
								}
							} catch (EncryptionException e) {
								System.out.println(("Mongo DB credentials Decryption Failure for password: " + e.getMessage()));
							}
							mongodb.setPassword(password);
						} else if(key.equals(PREFIX + ".APPLICATIONPOOL.CONNECTIONTIMEOUT")) {
							mongodb.setConnectionTimeout(Integer.parseInt(value));
						} else if(key.equals(PREFIX + ".APPLICATIONPOOL.SOCKETTIMEOUT")) {
							mongodb.setSocketTimeout(Integer.parseInt(value));
						} else if(key.equals(PREFIX + ".READPREF")) {
							mongodb.setReadPref(value);
						} else if(key.equals(PREFIX + ".WRITECONCERN")) {
							mongodb.setWriteconcern(value);
						} else if(key.equals(PREFIX + ".DBTYPE")) {
							mongodb.setDbType(value);
						} else if(key.equals(PREFIX + ".DBID")) {
							mongodb.setDbId(value);
						} else if(key.equals(PREFIX + ".REPLICASET")) {
							String replicas = value;
							Set<String> replicaset = mongodb.getReplicaset();
							if (replicas != null && !"".equals(replicas.trim()) && !"null".equalsIgnoreCase(replicas)) {
								replicaset = new HashSet<String>();
								String[] rep = replicas.split(",");
								for (String re : rep) {
									replicaset.add(re);
								}
							}
							mongodb.setReplicaset(replicaset);
							
						} else if(key.equals(PREFIX + ".COBRANDS")) {
							Set<Long> cobrandsSet = mongodb.getCobrandsList();
							if(cobrandsSet == null)
								cobrandsSet = new HashSet<>();
							
							if (value != null) {
								StringTokenizer st = new StringTokenizer(value, ",");
								if (st != null) {
									while (st.hasMoreTokens()) {
										String cobStr = st.nextToken();
										if (cobStr != null && cobStr.trim().length() != 0) {
											cobrandsSet.add(Long.valueOf(cobStr));
										}
									}
								}
							}
							mongodb.setCobrandsList(cobrandsSet);
							mongodb.setCobrandsArray(cobrandsSet.toArray(new Long[0]));
						} 
					}
					mongodbdetails.add(mongodb);

				}
			} else {
				if(config.getKey().equals("COM.YODLEE.DB.MONGO.DBPOOL.ENABLED")) {
					isMongodbPoolEnabled = Boolean.valueOf(config.getValue());
				}
			}
		}
	}
	public static List<MongodbInitInfo> getMongoDbdetails() {
		return mongodbdetails;
	}
	public static boolean isMongoPoolEnabled(){
		return isMongodbPoolEnabled;
	}
	public static MongodbInitInfo getYccRefreshStatDbDetails(){
		for (MongodbInitInfo info : mongodbdetails) {
				if(!info.getDbType().equals("YCCREFRESHSTAT"))
					continue;
				return info;
		}
		return null;
	}
	public static MongodbInitInfo getYccNotificationDbDetails(){
		for (MongodbInitInfo info : mongodbdetails) {
				if(!info.getDbType().equals("YCCNOTIFICATION"))
					continue;
				return info;
		}
		return null;
	}
	public static MongodbInitInfo getYccNotificationEmailDbDetails(){
		for (MongodbInitInfo info : mongodbdetails) {
				if(!info.getDbType().equals("YCCNOTIFICATION_EMAIL"))
					continue;
				return info;
		}
		return null;
	}
	
	public static MongoDbFactory getMongoDbFactory(MongodbInitInfo info) throws Exception {
		try {
			logger.info("userName:" + info.getUserName() + " :dbname:" + info.getDbName() + " :replica:" + info.getReplicaset() + " priserver:"
					+ info.getServerName() + " resdpref:" + info.getReadPref() + " conntout:" + info.getConnectionTimeout() + " :socktimout:"
					+ info.getSocketTimeout() + " maxconn:" + info.getMaxConnections());
			MongoCredential credential = MongoCredential.createCredential(info.getUserName(), info.getDbName(), info.getPassword().toCharArray());
			List<ServerAddress> serversList = new ArrayList<ServerAddress>();
			serversList.add(new ServerAddress(info.getServerName(), info.getPortNumber()));
			Set<String> replicaset = info.getReplicaset();
			if (replicaset != null && !replicaset.isEmpty()) {
				for (String replica : replicaset) {
					String[] rep = replica.split(":");
					serversList.add(new ServerAddress(rep[0], Integer.parseInt(rep[1])));
				}
			}
			ReadPreference preference = ReadPreference.nearest();
			if (info.getReadPref() != null) {
				try {
					Class<?> c = Class.forName("com.mongodb.ReadPreference");
					Method m = c.getMethod(info.getReadPref(), null);
					preference = (ReadPreference) m.invoke(null, new Object[0]);
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					logger.error("Read pref error:" + ExceptionUtils.getStackTrace(e));
				}
			}
			MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(info.getMaxConnections()).connectTimeout(info.getConnectionTimeout())
					.socketTimeout(info.getSocketTimeout()).readPreference(preference).build();
			MongoClient mongoClient = new MongoClient(serversList, Arrays.asList(credential), options);
			SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, info.getDbName());
			WriteConcern writeConcern = WriteConcern.valueOf("SAFE");
			if (info.getWriteconcern() != null)
				writeConcern = WriteConcern.valueOf(info.getWriteconcern().toUpperCase());
			simpleMongoDbFactory.setWriteConcern(writeConcern);
			logger.info("Simple factory:" + simpleMongoDbFactory);
			logger.info("Mongo client is created succesfully");
			return simpleMongoDbFactory;
		} catch (Exception e) {
			logger.error("Erro while creating the mongo factory", e);
		}
		return null;
	}
}
