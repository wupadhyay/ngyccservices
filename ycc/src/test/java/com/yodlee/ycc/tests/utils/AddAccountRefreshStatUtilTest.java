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
import org.mockito.Spy;
import org.testng.annotations.Test;

import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.CobrandStats;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsService;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;

public class AddAccountRefreshStatUtilTest extends RefreshStatCommonUtilTest {

	
	@Mock
	RefreshStatsDbService dbService;
	
	@Spy
	private RefreshStatsHandler handler;

	@InjectMocks
	RefreshStatUtil refreshStatUtil;
	
	@Resource(name = "testAddAccountRefreshData")
	protected Map<String, Object> testAddAccountRefreshData;
	
	

	
	@Test
	public void testAddAccountTopVolumeFor24H() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolume");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolumeAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setNumRecords("18");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setYodlee(true);
		filter.setDuration("PT24H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
	}
	
	@Test
	public void testAddAccountTopVolumeFor12H() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolume");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolumeAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setNumRecords("18");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setYodlee(true);
		filter.setDuration("PT12H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
	}
	
	@Test
	public void testAddAccountTopVolumeFor4H() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolume");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopVolumeAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setNumRecords("18");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setYodlee(true);
		filter.setDuration("PT4H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
	}
	
	@Test
	public void testAddAccountTopFailureFor24H()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailure");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailureAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT24H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
		
		
	}
	
	@Test
	public void testAddAccountTopFailureFor12H()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailure");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailureAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT12H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
		
		
	}
	
	@Test
	public void testAddAccountTopFailureFor4H()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailure");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailureAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop(YSLConstants.SPLUNK_TOP_FAILURE);
		filter.setYodlee(true);
		filter.setDuration("PT4H");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
		
		
	}
	
	
	
	@Test
	public void testAddAccount5DData()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefresh5dData");
		String expectedResponse = (String) testAddAccountRefreshData.get("AddAccountRefresh5dDataAPIResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P5D");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
		
		
		
	}
	
	@Test
	public void testAddAccountProviderWithoutTop()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountRefreshTopFailure");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		try
		{
			String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);

		}
		catch(Exception e)
		{
		assertEquals(e.getMessage(),"Top or ProviderId(s) is required");
		}
	}
	
	@Test
	public void testADD24hrLatencyBreakup() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountlatencyBreakUp24hr");
		String expectedRestResponse = (String) testAddAccountRefreshData.get("AddAccountlatencyBreakUp24hrAPIResponse");
		expectedRestResponse = expectedRestResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsService refreshStatsService;
		refreshStatsService = Mockito.spy(new CobrandStats());
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P12H");
		filter.setYodlee(true);
		filter.setInclude("latencybreakup");
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		when(handler.getHandler(anyString())).thenReturn(refreshStatsService);
		Mockito.doReturn(true).when(refreshStatsService).validateRequestParams(filter);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		assertThat(refreshStatsData, equalTo(expectedRestResponse));

		
	}
	
	@Test
	public void testADD7DHistoric() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccount7DWithHistoric");
		String summaryMongoDBResponse = (String) testAddAccountRefreshData.get("AddAccount24hrHistoric");
		String expectedRESTApiResponse = (String) testAddAccountRefreshData.get("AddAccount7DHistoricAPIResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");;
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P7D");
		filter.setInclude("historic");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class)))
				.thenReturn(cobhist).thenReturn(summaryMongoDBResponse);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
	//	System.out.println("response:" + refreshStatsData);
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));

	}
	
	@Test
	public void testAddAccountErrorCode()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccount24HRErrorCode");
		String expectedRESTApiResponse = (String) testAddAccountRefreshData.get("AddAccount24HRErrorCodeAPIResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		assertThat(refreshStatsData, equalTo(expectedRESTApiResponse));
		
		
	}
	

	
	@Test
	public void testAddAccountProviderHistoric5Day()
	{
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountProviderHistoric5D");
		String expectedRESTApiResponse = (String) testAddAccountRefreshData.get("AddAccountProviderHistoric5DAPIResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P5D");
		filter.setProviderIds("5");
		filter.setInclude(YSLConstants.INCLUDE_HISTORIC);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedRESTApiResponse);
		
		
	}
	
	@Test
	public void testAddAccountProviderIds() {
		
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountProviderIds");
		String expectedRESTApiResponse = (String) testAddAccountRefreshData.get("AddAccountProviderIdsAPIResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedRESTApiResponse);
	}

	@Test
	public void testAddAccountProviderIdswithHistoric() {
		String cobhist = (String) testAddAccountRefreshData.get("AddAccountProviderIdsWithHistoric");
		String summaryResponse = (String) testAddAccountRefreshData.get("AddAccountProviderIdsWithHistoric24Hr");
		String expectedRESTApiResponse = (String) testAddAccountRefreshData.get("AddAccountProviderIdsWithHistoricAPIResponse");
		expectedRESTApiResponse = expectedRESTApiResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(dataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("ADD");
		filter.setLogdinCobrandId(cobrandId);
		filter.setProviderIds("5");
		filter.setInclude(YSLConstants.INCLUDE_HISTORIC);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist).thenReturn(summaryResponse);
		String refreshStatsData = refreshStatUtil.getRefreshStatsData(filter);
		refreshStatsData = refreshStatsData.replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedRESTApiResponse);
	}

}
