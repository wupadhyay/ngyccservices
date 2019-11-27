/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Map;

import javax.annotation.Resource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.CobrandStats;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.ProviderStats;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsService;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;
import com.yodlee.ycc.tests.controller.BaseTestNGController;


public class IAVRefreshStatUtilTest extends RefreshStatCommonUtilTest {

	@Mock
	RefreshStatsDbService dbService;
	@Mock 
	private RefreshStatsHandler handler;
	@InjectMocks
	RefreshStatUtil rutil;
	@Resource(name = "testRefresheshData")
	protected Map<String, Object> testRefreshesData;
	
	@Test
	public void testIav24hrDurationoffset() {

		String cobhist = (String) testRefreshesData.get("refresh24hrDurationOffsetCobrandData");
		String expectedRestResponse = (String) testRefreshesData.get("refresh24hrDurationOffsetCobrandDataExpectedRestResponse");
		expectedRestResponse = expectedRestResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new CobrandStats());
		
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("P24H");
		filter.setDurationOffset("P12H");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(cobhist);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRestResponse));

	}
	
	@Test
	public void testIav24hrLatencyBreakup() {

		String cobhist = (String) testRefreshesData.get("refresh24hrLatencyBreakup");
		String expectedRestResponse = (String) testRefreshesData.get("refresh24hrLatencyBreakupExpectedRestResponse");
		expectedRestResponse = expectedRestResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new CobrandStats());
		
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("P12H");
		filter.setYodlee(true);
		filter.setInclude("latencybreakup");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(cobhist);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRestResponse));

		
	}
	
	@Test
	public void testIav7DHistoric() {

		String cobhist = (String) testRefreshesData.get("refresh7DWithHistoric");
		String summaryMongoDBResponse = (String) testRefreshesData.get("refresh24hrLatencyBreakup");
		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refresh7DWithHistoricExpectedRestResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new CobrandStats());
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("99");
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("99");
		filter.setDuration("7d");
		filter.setInclude("historic");
		filter.setYodlee(true);
		
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(cobhist).thenReturn(summaryMongoDBResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}
	
	
	@Test
	public void testIavTopVolume() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopVolume");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopVolumeRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setDuration("PT24h");
		filter.setYodlee(true);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}

	@Test
	public void testIavTopVolumeFor12H() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopVolume");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopVolumeRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setDuration("PT12h");
		filter.setYodlee(true);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}

	
	@Test
	public void testIavTopVolumeFor4H() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopVolume");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopVolumeRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setDuration("PT4h");
		filter.setYodlee(true);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}

	
	@Test
	public void testIavTopfailureFor24H() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopFailure");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopFailureRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setNumRecords("18");
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT24h");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));
		
	}
	
	@Test
	public void testIavTopfailureFor12H() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopFailure");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopFailureRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setNumRecords("18");
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT12h");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));
		
	}
	
	@Test
	public void testIavTopfailureFor4H() {

		String mockResponse = (String) testRefreshesData
				.get("refreshesTopFailure");

		String expectedRESTApiResponse = (String) testRefreshesData
				.get("refreshTopFailureRestApiResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replace("REFRESH", "IAV").replaceAll(" ", "");
		
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setNumRecords("18");
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT4h");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));
		
	}
	
	@Test
	public void testProviderErrocodeFor4H() {

		String mockSummaryResponse = (String) testRefreshesData.get("refresh24hrProviderData");
		String mockErrorCodeResponse = (String) testRefreshesData.get("refreshSiteErrorCodeData");
		
		String expectedRESTApiResponse = (String) testRefreshesData.get("refreshSiteErrorCodeDataExpectedRestResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("4h");
        filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		//filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockSummaryResponse).thenReturn(mockErrorCodeResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}
	
	@Test
	public void testProviderErrocodeFor12H() {

		String mockSummaryResponse = (String) testRefreshesData.get("refresh24hrProviderData");
		String mockErrorCodeResponse = (String) testRefreshesData.get("refreshSiteErrorCodeData");
		
		String expectedRESTApiResponse = (String) testRefreshesData.get("refreshSiteErrorCodeDataExpectedRestResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("12h");
        filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		//filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockSummaryResponse).thenReturn(mockErrorCodeResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}
	
	@Test
	public void testProviderErrocodeFor24H() {

		String mockSummaryResponse = (String) testRefreshesData.get("refresh24hrProviderData");
		String mockErrorCodeResponse = (String) testRefreshesData.get("refreshSiteErrorCodeData");
		
		String expectedRESTApiResponse = (String) testRefreshesData.get("refreshSiteErrorCodeDataExpectedRestResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("24h");
        filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		//filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new ProviderStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(mockSummaryResponse).thenReturn(mockErrorCodeResponse);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}
	
	@Test
	public void testIavRefreshWithCobrandErroCode() {
		String cobhist = (String) testRefreshesData.get("refreshCobrandErrorcodeData");
		String expectedResponse = (String) testRefreshesData.get("refreshIavCobrandErrorcodeApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId("10000004");
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId("10000004");
		filter.setDuration("24h");
        filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE);
		filter.setYodlee(true);
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new CobrandStats());
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(cobhist);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
	}
	
	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeTest
	public void beforeTest() {
	}

	@AfterTest
	public void afterTest() {
	}

}
