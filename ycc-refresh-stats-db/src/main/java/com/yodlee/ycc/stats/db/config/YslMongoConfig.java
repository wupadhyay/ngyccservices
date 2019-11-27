/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({MongoDBInit.class})
@Profile("ysl")
public class YslMongoConfig {
	private static final Logger logger = LoggerFactory.getLogger(YslMongoConfig.class);
	@Autowired(required = true)
    @Qualifier(value = "MongoDBService")
	MongoDBInit mongodbInit;

	private MongoDbFactory mongoDbFactory() throws Exception {
		try {
			logger.info("Mongo pool is enabled:" + MongoDBInit.isMongoPoolEnabled());
			if (MongoDBInit.isMongoPoolEnabled()) {
				MongodbInitInfo info = MongoDBInit.getYccRefreshStatDbDetails();
				logger.info("userName:" + info.getUserName() + " :dbname:" + info.getDbName() + " :replica:" + info.getReplicaset() + " priserver:"
						+ info.getServerName() + " resdpref:" + info.getReadPref() + " conntout:" + info.getConnectionTimeout() + " :socktimout:"
						+ info.getSocketTimeout() + " maxconn:" + info.getMaxConnections());
				MongoDbFactory mongoDbFactory = MongoDBInit.getMongoDbFactory(info);
				return mongoDbFactory;
			}
		} catch (Exception e) {
			logger.error("Erro while creating the mongo factory", e);
		}
		return null;
	}

	public @Bean(name = "yccRefreshStatTemplate")
	MongoTemplate mongoTemplate() throws Exception {
		logger.info("Creating the mongo template");
		MongoDbFactory mongoDbFactory = mongoDbFactory();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		logger.info("Created the mongo template");
		logger.info("Mongo template db:"+mongoTemplate.getDb());
		return mongoTemplate;
		
	}
	
	
}
