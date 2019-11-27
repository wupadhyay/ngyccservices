/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.ycc.dapi.mybatis.bean.Notification;
import com.yodlee.ycc.dapi.mybatis.bean.UserInfo;

/**
 * 
 * @author knavuluri
 *
 */
public class NotificationTypeHandler extends BaseTypeHandler<List<Notification>> {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationTypeHandler.class);
	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<Notification> parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Notification> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		ResultSet resultSet = (ResultSet) rs.getObject(columnName);
		List<Notification> notifications = new ArrayList<Notification>();
		if (resultSet != null) {
			while (resultSet.next()) {
				Notification notification = new Notification();
				notification.setMessage(resultSet.getString("EXTERNAL_UPDATE_LOG"));
				Date date = resultSet.getDate("ROW_LAST_UPDATED");
				if (date != null) {
					logger.debug("Epoch Date for notification update:"+date.getTime());
					df.setTimeZone(TimeZone.getTimeZone("UTC"));
					notification.setLastupdated(df.format(date));
				}
				
				notifications.add(notification);
			}
		}
		return notifications;
	}

	@Override
	public List<Notification> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notification> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
