/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.service;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author knavuluri
 *
 */
@Component
public class MyBatisUtil {

	private static final Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);
	private static SqlSessionFactory factory;

	private MyBatisUtil() {
	}

	static {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			logger.error("Excpetion while creating session factory" + ExceptionUtils.getFullStackTrace(e));
			throw new RuntimeException(e.getMessage());
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return factory;
	}

	public static List selectList(String statement, Object filter, Connection connection) {
		List result = null;
		SqlSession sqlSession = null;
		logger.debug("Start execting the query:" + statement);
		Date sdate = new Date();
		DataSource dataSource = new SingleConnectionDataSource(connection, true);
		try {
			connection = dataSource.getConnection();
			sqlSession = factory.openSession(ExecutorType.REUSE, connection);
			result = sqlSession.selectList(statement, filter);
		} catch (Exception e) {
			logger.error("Excpetion while executing query:" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (sqlSession != null)
				sqlSession.close();
		}
		Date edate = new Date();
		logger.info("Total time take for the query " + statement + " execution :" + (edate.getTime() - sdate.getTime()) + " milli sec");
		logger.debug("Completed execting the query:" + statement);
		return result;
	}
}