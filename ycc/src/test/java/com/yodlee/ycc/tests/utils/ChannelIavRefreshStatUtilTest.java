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

import java.util.Map;

import javax.annotation.Resource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.RefreshStatsFilter;
import com.yodlee.ycc.dapi.splunkresponseformat.refresh.handler.RefreshStatsHandler;
import com.yodlee.ycc.dapi.utils.RefreshStatUtil;
import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;

public class ChannelIavRefreshStatUtilTest extends RefreshStatCommonUtilTest {

	@Mock
	RefreshStatsDbService dbService;
	
	@Spy
	private RefreshStatsHandler handler;

	@InjectMocks
	RefreshStatUtil rutil;

	@Resource(name = "testIavChannelRefreshData")
	protected Map<String, Object> testChannelRefreshData;
	

	@Test
	public void testChannelIavOverAll24hrRefreshes() {
		String overallData = (String) testChannelRefreshData.get("refreshIav24hrCobrandData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIav24hrApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setInclude("channel");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("response:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "").replaceAll(" ", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavOverAll24hrHistoricRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIav24hrCobrandData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIav24hrHistoryApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setInclude("historic,channel");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("Overallhistresponse:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavCobrand15DHistoricRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIav15DHistoricData");
		String overallData = (String) testChannelRefreshData.get("refreshIav24hrCobrandData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIav15dHistoryApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P15D");
		filter.setInclude("historic,channel");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist).thenReturn(overallData);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("histresponse:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavRefreshTopVolumeRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavTopVolumeData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavTopVolumeApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop("TOP_VOLUME");
		// filter.setDuration("P15D");
		filter.setInclude("channel");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("topVolume:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavRefreshTopFailureRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavTopFailureData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavTopFailureApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setTop("TOP_FAILURE");
		// filter.setDuration("P15D");
		filter.setInclude("channel");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("topFailure:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavRefreshSiteOverAll24hrRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSite24HrData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavSite24HrApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		// filter.setTop("TOP_FAILURE");
		filter.setDuration("PT24H");
		filter.setInclude("channel");
		filter.setProviderIds("5");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("site24hr:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavRefreshSiteHistoric15DRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSite15DData");
		String summary = (String) testChannelRefreshData.get("refreshIavSite24HrData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavSite15DApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P15D");
		filter.setInclude(YSLConstants.INCLUDE_HISTORIC + "," + YSLConstants.INCLUDE_CONTAINER + "," + YSLConstants.CONSOLIDATE_STATS);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist).thenReturn(summary);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("site15D:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	@Test
	public void testChannelIavRefreshSiteHistoric15DLatencyRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSite15DData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavSite15DLatencyApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("P15D");
		filter.setInclude(YSLConstants.INCLUDE_HISTORIC + "," + YSLConstants.INCLUDE_CONTAINER + "," + YSLConstants.INCLUDE_LATENCY_BREAKUP + "," + YSLConstants.CONSOLIDATE_STATS);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		System.out.println("site15DLatency:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData, expectedResponse);
	}

	 @Test
	public void testChannelIavRefreshSiteErrorcode() {
		String errorCode = (String) testChannelRefreshData.get("refreshIavSiteErrorCodeData");
		String overall = (String) testChannelRefreshData.get("refreshIavSite24HrData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavSiteErrorcodeApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("PROVIDER");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setInclude(YSLConstants.INCLUDE_ERROR_CODE + "," + YSLConstants.CONSOLIDATE_STATS);
		filter.setProviderIds("5");
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(overall).thenReturn(errorCode);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println("siteErrorCode:" + refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
	}

	@Test
	public void testChannelIavRefreshCobrandWithNumRecordsRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSiteErrorCodeData");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setNumRecords("20");
		filter.setInclude(YSLConstants.CONSOLIDATE_STATS);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		try {
			String refreshStatsData = rutil.getRefreshStatsData(filter);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "NumRecords should be empty");
		}
	}
	@Test
	public void testChannelIavRefreshCobrandWithTopRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSiteErrorCodeData");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		filter.setTop(YSLConstants.SPLUNK_TOP_VOLUME);
		filter.setInclude(YSLConstants.CONSOLIDATE_STATS);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		try {
			String refreshStatsData = rutil.getRefreshStatsData(filter);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Top should be empty");
		}
	}
	@Test
	public void testChannelIavRefreshCobrandWithSiteErroCodeRefreshes() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavSiteErrorCodeData");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		//filter.setNumRecords("20");
		filter.setInclude(YSLConstants.CONSOLIDATE_STATS+","+YSLConstants.INCLUDE_ERROR_CODE);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		try {
			String refreshStatsData = rutil.getRefreshStatsData(filter);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "include should not contain errorcode");
		}
	}
	@Test
	public void testChannelIavRefreshWithCobrandErroCode() {
		String cobhist = (String) testChannelRefreshData.get("refreshIavCobrandErrorcodeData");
		String expectedResponse = (String) testChannelRefreshData.get("refreshIavCobrandErrorcodeApiResponse");
		RefreshStatsFilter filter = new RefreshStatsFilter();
		filter.setCobrandId(channelDataCobrandId);
		filter.setGroupBy("COBRAND");
		filter.setReportType("IAV");
		filter.setLogdinCobrandId(cobrandId);
		filter.setDuration("PT24H");
		//filter.setNumRecords("20");
		filter.setInclude(YSLConstants.CONSOLIDATE_STATS+","+YSLConstants.INCLUDE_ERROR_CODE);
		filter.setYodlee(true);
		when(dbService.getRefreshStatsData(any(RefreshStatsDbFilter.class))).thenReturn(cobhist);
		String refreshStatsData = rutil.getRefreshStatsData(filter);
		//System.out.println(refreshStatsData);
		expectedResponse = expectedResponse.replaceAll("\\r|\\n|\\t", "");
		assertEquals(refreshStatsData,expectedResponse );
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
