/*
* Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.tests.utils;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;
import com.yodlee.ycc.tests.controller.BaseTestNGController;

/**
 * 
 * @author knavuluri
 *
 */
public class NetworkStatUtilTest extends RefreshStatCommonUtilTest {
	
	@Mock
	RefreshStatsDbService dbService;
	
	@Spy
	private RefreshStatsHandler handler;

	@InjectMocks
	RefreshStatUtil rutil;

	@Resource(name = "testNeworkStatsData")
	protected Map<String, Object> testnetworkRefreshData;

	
	@Test
	public void testNetworkRefreshFor24H() {
		String overallData = (String) testnetworkRefreshData.get("networkRefreshData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkRefreshApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("REFRESH");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT24H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	
	@Test
	public void testNetworkRefreshFor12H() {
		String overallData = (String) testnetworkRefreshData.get("networkRefreshData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkRefreshApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("REFRESH");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT12H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	
	@Test
	public void testNetworkRefreshFor4H() {
		String overallData = (String) testnetworkRefreshData.get("networkRefreshData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkRefreshApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("REFRESH");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT4H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	
	@Test
	public void testNetworkIavFor24H() {
		String overallData = (String) testnetworkRefreshData.get("networkIavData");
		String expectedResponse = (String) testnetworkRefreshData.get("newworkIavApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT24H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	@Test
	public void testNetworkIavFor12H() {
		String overallData = (String) testnetworkRefreshData.get("networkIavData");
		String expectedResponse = (String) testnetworkRefreshData.get("newworkIavApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT12H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	@Test
	public void testNetworkIavFor4H() {
		String overallData = (String) testnetworkRefreshData.get("networkIavData");
		String expectedResponse = (String) testnetworkRefreshData.get("newworkIavApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT4H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	
	@Test
	public void testNetworkAddAccountFor24H() {
		String overallData = (String) testnetworkRefreshData.get("networkAddData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkAddApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT24H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	@Test
	public void testNetworkAddAccountFor12H() {
		String overallData = (String) testnetworkRefreshData.get("networkAddData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkAddApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT12H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
	@Test
	public void testNetworkAddAccountFor4H() {
		String overallData = (String) testnetworkRefreshData.get("networkAddData");
		String expectedResponse = (String) testnetworkRefreshData.get("networkAddApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("All");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setProviderIds("5,643,2852");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		filter.setDuration("PT4H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}
}
