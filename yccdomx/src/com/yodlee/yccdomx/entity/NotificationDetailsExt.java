/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.yccdomx.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONObject;

import com.yodlee.dom.entity.SumInfoSptdLocale;
import com.yodlee.domx.base.ServiceBaseExt;
import com.yodlee.framework.runtime.annotation.Derived;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.yccdom.entity.NotificationDetails;
import com.yodlee.yccdom.entity.UserInfo;
import com.yodlee.yccdom.entity.UserInfo_;
/**
 * 
 * @author knavuluri
 *
 */
public class NotificationDetailsExt extends ServiceBaseExt<NotificationDetails> {

	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	public NotificationDetailsExt() {
		super(NotificationDetails.class);
	}

	@Override
	public SumInfoSptdLocale getDftSptdLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SumInfoSptdLocale> getSptdLocales() {
		// TODO Auto-generated method stub
		return null;
	}
	@Derived(arguments = { "rowLastUpdated" })
	public String getLastUpdatedExt() {
		if (entity.getRowLastUpdated() != null)
			return df.format(entity.getRowLastUpdated());
		else
			return null;
	}
	@Derived(arguments = { "memId" })
	public String getUpdatedExt() {
//		Criteria cri=new Criteria();
//		cri.add(UserInfo_.memId,entity.getMemId());
//		cri.add(UserInfo_.cobrandId,10000004l);
//		cri.add(UserInfo_.userTypeId,2l);
//		List<UserInfo> userInfoList = UserInfo.DAO.select(cri);
//		//UserInfo userInfo = UserInfo.DAO.segetSingle(UserInfo_.memId, entity.getMemId());
//		if (userInfoList != null && !userInfoList.isEmpty()) {
//			UserInfo userInfo = userInfoList.get(0);
//			JSONObject obj1 = new JSONObject();
//			obj1.put("id", userInfo.getMemId());
//			obj1.put("loginName", userInfo.getUserName());
//			obj1.put("email", userInfo.getEmail());
//			obj1.put("first", userInfo.getFirstName());
//			obj1.put("last", userInfo.getLastName());
//			
//			return obj1.toString();
//		}
		return null;
	}

//	@Derived(arguments = { "memId" })
//	public UserInfoDetails getUserInfoDetails() {
//		UserInfo userInfo = UserInfo.DAO.getSingle(UserInfo_.memId, entity.getMemId());
//		if (userInfo != null) {
//			UserInfoDetails detail = new UserInfoDetails();
//			detail.setId(userInfo.getMemId());
//			detail.setLoginName(userInfo.getUserName());
//			detail.setEmail(userInfo.getEmail());
//			detail.setFirst(userInfo.getFirstName());
//			detail.setLast(userInfo.getLastName());
//			return detail;
//		}
//		return null;
//	}

}
