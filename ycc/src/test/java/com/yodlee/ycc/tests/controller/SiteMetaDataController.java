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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author knavuluri
 *
 */
//@Test(enabled=true)
public class SiteMetaDataController extends BaseTestNGController{
	public static String dataCobrandId;

	@BeforeClass
	public void prepareData() {
		qaProfile = getQaProfile();
		String key = "siteFilterDetails";
		if (qaProfile)
			key = "qasiteFilterDetails";

		Object siteFilterDetails = testBasicYccData.get(key);
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String, Object> mapObject = mapper.readValue(siteFilterDetails.toString(),
					new TypeReference<Map<String, Object>>() {
					});
			LinkedHashMap<String, String> filter = (LinkedHashMap<String, String>) mapObject.get("filter");
			dataCobrandId = filter.get("cobrandId");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test(testName = "testProviderSearchProviderId", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("providerId","16441")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
	}
	
	@Test(testName = "testProviderSearchProviderName", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderName() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("name","dag")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		System.out.println("response###########:"+response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		
	}
	@Test(testName = "testProviderSearchProviderUrl", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderUrl() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("url","http://64.14.28.129/dag/index.do")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		System.out.println("response###########:"+response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		
	}
	@Test(testName = "testProviderAgentName", dependsOnMethods = "testUserLogin")
	public void testProviderAgentName() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("agentName","DagBank")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		System.out.println("response###########:"+response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		
	}
	@Test(testName = "testProviderSearchProviderIdNoCobrandId", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderIdNoCobrandId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("providerId","16441")
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		
	}
	@Test(testName = "testProviderWrongProviderId", dependsOnMethods = "testUserLogin")
	public void testProviderWrongProviderId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("providerId","1000000")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		Assert.assertEquals(response, "{}");
		
	}
	@Test(testName = "testProviderSearchProviderIdBasicFields", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderIdBasicFields() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("providerId","16441")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
		Assert.assertTrue(response.contains("status"), "Status is missing in the response");
		Assert.assertTrue(response.contains("loginUrl"), "LoginUrl is missing in the response");
		Assert.assertTrue(response.contains("baseUrl"), "BaseUrl is missing in the response");
	}
	@Test(testName = "testProviderSearchProviderNameBasicFields", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderNameBasicFields() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("name","dag")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
		Assert.assertTrue(response.contains("status"), "Status is missing in the response");
		Assert.assertTrue(response.contains("loginUrl"), "LoginUrl is missing in the response");
		Assert.assertTrue(response.contains("baseUrl"), "BaseUrl is missing in the response");
		
	}
	
	@Test(testName = "testProviderSearchProviderIdBasicFields", dependsOnMethods = "testUserLogin")
	public void testProviderSearchProviderIdOtherFields() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search")
								.servletPath("/v1/providers/search")
								.param("providerId","16441")
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("isAutoRefreshEnabled"), "IsAutoRefreshEnabled is missing in the response");
		Assert.assertTrue(response.contains("aggregationType"), "AggregationType is missing in the response");
		Assert.assertTrue(response.contains("favicon"), "Status is missing in the response");
		Assert.assertTrue(response.contains("log"), "Logo is missing in the response");
		Assert.assertTrue(response.contains("authType"), "AuthType is missing in the response");
		Assert.assertTrue(response.contains("providerCategory"), "ProviderCategory is missing in the response");
		Assert.assertTrue(response.contains("containerNames"), "ContainerNames is missing in the response");
		Assert.assertTrue(response.contains("loginForm"), "LoginForm is missing in the response");
		Assert.assertTrue(response.contains("additionalDataSet"), "AdditionalDataSet is missing in the response");
	}
	
	@Test(testName = "testProviderQuickSearchProviderName", dependsOnMethods = "testUserLogin")
	public void testProviderQuickSearchProviderName() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search/provider")
								.servletPath("/v1/providers/search/provider")
								.param("name","dag")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
		
	}
	@Test(testName = "testProviderQuickSearchProviderIds", dependsOnMethods = "testUserLogin")
	public void testProviderQuickSearchProviderIds() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search/provider")
								.servletPath("/v1/providers/search/provider")
								.param("providerIds","5,16441")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
		
	}
	@Test(testName = "testProviderQuickSearchWrongProviderId", dependsOnMethods = "testUserLogin")
	public void testProviderQuickSearchWrongProviderId() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search/provider")
								.servletPath("/v1/providers/search/provider")
								.param("providerIds","-12")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		Assert.assertEquals(response, "{}");
	}
	@Test(testName = "testProviderQuickSearchProviderIdsNoCobrand", dependsOnMethods = "testUserLogin")
	public void testProviderQuickSearchProviderIdsNoCobrand() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search/provider")
								.servletPath("/v1/providers/search/provider")
								.param("providerIds","5,16441")
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
	}
	@Test(testName = "testProviderQuickSearchFieldName", dependsOnMethods = "testUserLogin")
	public void testProviderQuickSearchFieldName() throws Exception {
		ResultActions providerDetails = this.mockMvc
				.perform(
						get(
							"/v1/providers/search/provider")
								.servletPath("/v1/providers/search/provider")
								.param("providerIds","16441")
								.param("fieldName","logo")
								.param("cobrandId",dataCobrandId)
								.header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId="+cobrandId+",appId="+appId+"}"))
		.andExpect(status().isOk()).andDo(print());
		MvcResult providers = providerDetails.andReturn();
		String response = providers.getResponse().getContentAsString();
		JsonElement rootElement = new JsonParser().parse(response);
		JsonElement userElement = rootElement.getAsJsonObject().get("provider");
		Assert.assertNotNull(userElement);
		Assert.assertTrue(userElement.isJsonArray());
		Assert.assertTrue(response.contains("id"), "Id is missing in the response");
		Assert.assertTrue(response.contains("name"), "Name is missing in the response");
		
	}
}
