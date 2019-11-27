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

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author knavuluri
 *
 */
public class NotificationControllerTest extends BaseTestNGController {

	@Test(testName = "testNotificationsWithStatus", dependsOnMethods = "testUserLogin")
	public void testNotificationWithStausIds() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("statuses", "4,5,6").param("cobrandId",cobrandId ).header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String jsonBody = providers.getResponse().getContentAsString();

		System.out.println("Notification response:" + jsonBody);
		JsonElement rootElement = new JsonParser().parse(jsonBody);

		JsonElement userElement = rootElement.getAsJsonObject().get("notification");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
	}

	@Test(testName = "testNotificationsWithProviderId", dependsOnMethods = "testUserLogin")
	public void testNotificationWithProviderIds() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "11").param("cobrandId",cobrandId ).header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String jsonBody = providers.getResponse().getContentAsString();

		System.out.println("Notification provider response:" + jsonBody);
		JsonElement rootElement = new JsonParser().parse(jsonBody);

		JsonElement userElement = rootElement.getAsJsonObject().get("notification");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());

	}

	@Test(testName = "testNotificationWithProviderIdsAndVerifyCategory", dependsOnMethods = "testUserLogin")
	public void testNotificationWithProviderIdsAndVerifyCategory() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "11").param("cobrandId",cobrandId ).header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

		System.out.println("Notification provider response:" + response);
		JsonElement rootElement = new JsonParser().parse(response);

		JsonElement userElement = rootElement.getAsJsonObject().get("notification");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("classification"), "classification is missing in the response");
		Assert.assertTrue(response.contains("category"), "category is missing in the response");

	}

	@Test(testName = "testNotificationWithProviderIdsAndVerifyCategory", dependsOnMethods = "testUserLogin")
	public void testNotificationWithProviderIdsAndVerifyImpactProvider() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "11").param("cobrandId",cobrandId ).header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

		System.out.println("Notification provider response:" + response);
		JsonElement rootElement = new JsonParser().parse(response);

		JsonElement userElement = rootElement.getAsJsonObject().get("notification");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("impactedProvider"), "impactedProvider is missing in the response");

	}

	@Test(testName = "testNotificationWithProviderIdsAndCobrandIdInvalid", dependsOnMethods = "testUserLogin")
	public void testNotificationWithProviderIdsAndCobrandIdInvalid() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "11").param("cobrandId", "99g").header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().is(500)).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

		System.out.println("Notification provider response:" + response);

	}

	@Test(testName = "testNotificationWithProviderIdsAndNoCobrandId", dependsOnMethods = "testUserLogin")
	public void testNotificationWithProviderIdsAndNoCobrandId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "5").header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();

		System.out.println("Notification provider response:" + response);
		JsonElement rootElement = new JsonParser().parse(response);

		JsonElement userElement = rootElement.getAsJsonObject().get("notification");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("impactedProvider"), "impactedProvider is missing in the response");

	}

	@Test(testName = "testNotificationWithWrongProviderId", dependsOnMethods = "testUserLogin")
	public void testNotificationWithWrongProviderId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(get("/v1/notification/provider/search").servletPath("/v1/notification/provider/search").param("providerIds", "10000000").header("Authorization",
						"{{cobSession=" + cobsession + ",userSession=" + usersession + ",cobrandId=" + cobrandId + ",appId=" + appId + "}"))
				.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		Assert.assertEquals(response, "{}");

	}
}
