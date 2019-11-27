/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.yodlee.ycc.dapi.mybatis.bean.Cobrand;

/**
 * 
 * @author knavuluri
 *
 */
public class CobrandTypeHandler extends BaseTypeHandler<List<Cobrand>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<Cobrand> parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Cobrand> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		ResultSet resultSet = (ResultSet) rs.getObject(columnName);
		List<Cobrand> cobrands = new ArrayList<Cobrand>();
		if (resultSet != null) {
			while (resultSet.next()) {
				Cobrand cob = new Cobrand();
				cob.setCobrandId(resultSet.getLong(1));
				cobrands.add(cob);
			}
		}
		return cobrands;
	}

	@Override
	public List<Cobrand> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cobrand> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
