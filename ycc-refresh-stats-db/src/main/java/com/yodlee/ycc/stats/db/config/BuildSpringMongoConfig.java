/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.stats.db.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;

@Configuration
@Import(SpringRefreshDbConfig.class)
@PropertySource({"classpath:build-application.properties"})
@Profile("build")
public class BuildSpringMongoConfig {
//	@Value("${spring.data.mongodb.database}")
//	String dbName = null;

     
//    @Value("${spring.data.mongodb.host}")
//    private String mongoHost;
//
//    @Value("${spring.data.mongodb.port}")
//    private int mongoPort;
    
    @Value("${spring.data.mongodb.uri}")
    private String uri;
    
    private MongoClientURI mongoClientUri;

//    @Bean
//    public ValidatingMongoEventListener validatingMongoEventListener() {
//        return new ValidatingMongoEventListener(validator());
//    }

//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        return new LocalValidatorFactoryBean();
//    }

//    @Override
//    protected String getDatabaseName() {
//        return dbName;
//    }
//
//    @Override
//    public MongoClient mongo() throws Exception {
//        return new MongoClient(mongoHost,mongoPort);
//    }
	
	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		mongoClientUri = new MongoClientURI(uri);
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClientUri);
		return simpleMongoDbFactory;
	}

	public @Bean(name = "yccRefreshStatTemplate")
	MongoTemplate mongoTemplate() throws Exception {

		MongoDbFactory mongoDbFactory = mongoDbFactory();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		mongoTemplate.setReadPreference(mongoClientUri.getOptions().getReadPreference());
		mongoTemplate.setWriteConcern(mongoClientUri.getOptions().getWriteConcern());
		return mongoTemplate;

	}
	
	
}
