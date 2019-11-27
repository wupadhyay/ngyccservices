/*
* Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * @author knavuluri
 *
 */
public class RefreshStatsControllerTest extends BaseTestNGController {
	public static String dataCobrandId;

	private String relativepath = "/v1/cobrefresh/stats";

	private String channelId = null;

	@BeforeClass
	public void prepareData() {
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
			LinkedHashMap<String, String> filter = (LinkedHashMap<String, String>) mapObject.get("refreshDetails");
			dataCobrandId = filter.get("cobrandId");
			channelId = filter.get("channelId");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(testName = "testRefreshStatsWithCobrand", dependsOnMethods = "testUserLogin")
	public void testRefreshStatsWithCobrand() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get(relativepath).servletPath(relativepath).param("groupBy", "COBRAND").param("cobrandId", dataCobrandId).param("reportType", "REFRESH")
						.param("duration", "PT24H").param("durationOffset", "PT24H").param("include", "historic")
						.header("Authorization", "{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

	}

	@Test(testName = "testRefreshStatsWithCobrandIav", dependsOnMethods = "testUserLogin")
	public void testRefreshStatsWithCobrandIav() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get(relativepath).servletPath(relativepath).param("groupBy", "COBRAND").param("cobrandId", dataCobrandId).param("reportType", "IAV")
						.header("Authorization", "{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

	}

	@Test(testName = "testRefreshStatsWithGroupByProvider", dependsOnMethods = "testUserLogin")
	public void testRefreshStatsWithGroupByProvider() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get(relativepath).servletPath(relativepath).param("groupBy", "PROVIDER").param("cobrandId", dataCobrandId).param("reportType", "REFRESH")
						.param("top", "TOP_VOLUME").param("numRecords", "20")
						.header("Authorization", "{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

	}

	@Test(testName = "testRefreshStatsWithGroupByProviderWithId", dependsOnMethods = "testUserLogin")
	public void testRefreshStatsWithGroupByProviderWithId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get(relativepath).servletPath(relativepath).param("groupBy", "PROVIDER").param("cobrandId", dataCobrandId).param("reportType", "REFRESH")
						.param("providerIds", "5")
						.header("Authorization", "{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

	}

	@Test(testName = "testRefreshStatsWithConsolidateby", dependsOnMethods = "testUserLogin")
	public void testRefreshStatsWithConsolidatedby() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get(relativepath).servletPath(relativepath).param("groupBy", "COBRAND").param("reportType", "REFRESH").param("consolidatedBy", "channel")
						.param("cobrandId", channelId)
						.header("Authorization", "{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

	}

}
