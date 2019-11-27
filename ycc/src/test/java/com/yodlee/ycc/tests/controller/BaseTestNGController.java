/*
* Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yodlee.ycc.dapi.context.ApplicationContextProvider;
/**
 * 
 * @author knavuluri
 *
 */
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
//@ActiveProfiles(profiles= {"testng", "local"})
@ActiveProfiles(profiles= {"testng", "qa"})
public class BaseTestNGController extends AbstractTestNGSpringContextTests {
	
    static protected MockMvc mockMvc;

    protected static String cobsession = "";


    protected static String usersession = "";
   
    protected static String cobrandId;
    
    protected static Long memId;
    
    protected static String appId;
   
    protected static boolean qaProfile;

    @Resource(name = "testBasicYCCData")
    protected Map<String, Object> testBasicYccData;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static HashMap<String, MockMvc> mockMap = new HashMap<String, MockMvc>();

    public static MockMvc getMockInstance() {
        MockMvc mockMvcObj = null;
        mockMvcObj = mockMap.get("mock");
        return mockMvcObj;
    }

    @Test(testName = "setup")
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        mockMvc = BaseTestNGController.getMockInstance();
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                    .build();
            mockMap.put("mock", mockMvc);
        }
        qaProfile = getQaProfile();
        String key = "testCobrandDetails";
        if (qaProfile)
        	key = "qatestCobrandDetails";
        Object object = testBasicYccData.get(key);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> mapObject = mapper.readValue(object.toString(),
                    new TypeReference<Map<String, Object>>() {
                    });
			LinkedHashMap<String, String> cobrandDetails = (LinkedHashMap<String, String>) mapObject.get("cobrandDetail");
			cobrandId = cobrandDetails.get("cobrandId");
			appId = cobrandDetails.get("appId");
            System.out.println("cobrandId ::::: " + cobrandId);
            System.out.println("appId ::::: " + appId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @AfterTest(alwaysRun = true)
	public void tearDown() throws Exception {
//		ResultActions result = this.mockMvc.perform(post("/v1/user/logout").servletPath("/v1/user/logout").header("Authorization", "{{cobSession=" + cobsession + ",userSession="+usersession+",cobrandId=10000004,appId=17CBE222A42161A3FF450E47CF4C1A00}"))
//				.andExpect(status().is(204)).andDo(print());
//
//		ResultActions result1 = this.mockMvc.perform(post("/v1/cobrand/logout").servletPath("/v1/cobrand/logout").header("Authorization", "{cobSession=" + cobsession + ",cobrandId=10000004,appId=17CBE222A42161A3FF450E47CF4C1A00}"))
//				.andExpect(status().is(204)).andDo(print());
	}

    @Test(testName = "testCobrandLogin", dependsOnMethods = "setUp")
    public void testCobrandLogin() throws Exception {
        
        String json = "{" + "                           \"cobrand\" :{"
                + "                        \"cobrandLogin\" : \"yodlee_10000004\","
                + "                        \"cobrandPassword\" :\"yodlee123\" " + "}}";
                
               
    	 String json1 = "{" + "                           \"cobrand\" :{"
                 + "                        \"cobrandLogin\" : \"yodlee\","
                 + "                        \"cobrandPassword\" :\"yodlee\" " + "}}";
    	 
		String key = "cobrandCredentials";
		if (qaProfile)
			key = "qacobrandCredentials";
		Object object = testBasicYccData.get(key);
        ResultActions result = this.mockMvc
                .perform(
                        post("/v1/cobrand/login")
                        	.header("Authorization", "{cobrandId="+cobrandId+",appId="+appId+"}")
                                .servletPath("/v1/cobrand/login")
                                .content(object.toString()))
                .andExpect(status().isOk()).andDo(print());

        MvcResult cobSessions = result.andReturn();
        String jsonBody = cobSessions.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> mapObject = mapper.readValue(jsonBody,
                    new TypeReference<Map<String, Object>>() {
                    });
            LinkedHashMap<String, String> cobJson = (LinkedHashMap<String, String>) mapObject
                    .get("session");
            cobsession = cobJson.get("cobSession");
            System.out.println("cobsession ::::: " + cobsession);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   @Test(testName = "userLogin", dependsOnMethods = "testCobrandLogin")
    public void testUserLogin() throws Exception {
	   String key = "userCredentials";
		if (qaProfile)
			key = "qauserCredentials";
		Object object = testBasicYccData.get(key);
        ResultActions result = this.mockMvc
                .perform(
                        post("/v1/user/login")
                                .header("Authorization", "{cobSession=" + cobsession + ",cobrandId="+cobrandId+",appId="+appId+"}")
                                .servletPath("/v1/user/login")
                                .content(object.toString()))
                .andExpect(status().isOk()).andDo(print());

        MvcResult userSessions = result.andReturn();

        String jsonBody = userSessions.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> mapObject = mapper.readValue(jsonBody,
                    new TypeReference<Map<String, Object>>() {
                    });
            //Fix for Test case fail Start
            JsonElement rootElement = new JsonParser().parse(jsonBody);

            JsonElement userElement = rootElement.getAsJsonObject().get("user");

            JsonElement sessionElement = userElement.getAsJsonObject().get("session");

            JsonElement userSessionElement = sessionElement.getAsJsonObject().get("userSession");
            usersession = userSessionElement.toString().replace("\"", "").trim();
            JsonElement memIdEl = userElement.getAsJsonObject().get("id");
            Number asNumber = memIdEl.getAsNumber();
			memId = asNumber.longValue();
			//cobrandId = Long.valueOf("99");
            //Fix for Test case fail End
            System.out.println("cobsession ::::: " + cobsession);
            System.out.println("usersession ::::: " + usersession);
            System.out.println("memId ::::: " + memId);
            System.out.println("cobrandId ::::: " + cobrandId);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   public static boolean getQaProfile() {
		Environment env = ApplicationContextProvider.getContext().getEnvironment();
		String[] activeProfiles = env.getActiveProfiles();
		if(activeProfiles != null && Arrays.asList(activeProfiles).contains("qa")) {
			return true;
		} 
		return false;
	}

   
  }
