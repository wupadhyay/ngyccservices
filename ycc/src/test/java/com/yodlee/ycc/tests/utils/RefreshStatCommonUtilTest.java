/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.tests.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;

import com.yodlee.ycc.tests.controller.BaseTestNGController;

/**
 * 
 * @author knavuluri
 *
 */
public class RefreshStatCommonUtilTest extends BaseTestNGController {

	protected static String dataCobrandId;
	
	protected static String channelDataCobrandId;

	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
		qaProfile = getQaProfile();
		String key = "refreshTestDetails";
		if (qaProfile)
			key = "qarefreshTestDetails";
		
		Object object = testBasicYccData.get(key);
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String, Object> mapObject = mapper.readValue(object.toString(),
					new TypeReference<Map<String, Object>>() {
					});
			LinkedHashMap<String, String> refreshDetails = (LinkedHashMap<String, String>) mapObject.get("refreshDetails");
			dataCobrandId = refreshDetails.get("cobrandId");
			cobrandId = refreshDetails.get("logdinCobrandId");
			channelDataCobrandId = refreshDetails.get("channelId");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
