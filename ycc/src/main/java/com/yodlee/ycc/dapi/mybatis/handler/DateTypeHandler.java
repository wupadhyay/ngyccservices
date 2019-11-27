package com.yodlee.ycc.dapi.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.ycc.dapi.controllers.SiteNotificationController;
/**
 * 
 * @author knavuluri
 *
 */
public class DateTypeHandler extends BaseTypeHandler<String> {

	private static final Logger logger = LoggerFactory.getLogger(DateTypeHandler.class);
	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		java.sql.Date date2 = rs.getDate(columnName);
		logger.debug("Default Date from DB epoch:"+date2.getTime());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (date2 != null)
			return df.format(date2);
		return null;
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
