/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@Import(SpringRefreshDbConfig.class)
@PropertySource({"classpath:prod-application.properties"})
@Profile("nonyslprod")
public class NonYslProdSpringMongoConfig {
//	@Value("${spring.data.mongodb.database}")
//	String dbName = null;

     
    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.password}")
    private String password;
    
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
  public MongoClient mongoClient() throws Exception {
	  mongoClientUri = new MongoClientURI(uri);
	  List<ServerAddress> seeds = new ArrayList<>();
	  List<String> urls = mongoClientUri.getHosts();
	  for (String url : urls) {
		  String[] ip = url.split(":");
		seeds.add(new ServerAddress(ip[0], Integer.parseInt(ip[1])));
	}
	  
	  List<MongoCredential> credentialList = new ArrayList<>();
	  System.out.println("uri"+mongoClientUri + " uri "+uri);
	  credentialList.add(mongoClientUri.getCredentials());
	return new MongoClient(seeds,credentialList);
}
	
	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient(),mongoClientUri.getDatabase());
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
