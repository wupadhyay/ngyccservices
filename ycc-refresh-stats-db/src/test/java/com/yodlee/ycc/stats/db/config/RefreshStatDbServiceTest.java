/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.yodlee.ycc.stats.db.bean.RefreshStatsDbFilter;
import com.yodlee.ycc.stats.db.service.RefreshStatsDbService;

//@ContextConfiguration(locations = {"classpath:TestApplicationContext.xml",
//"classpath:dispatcher-servlet.xml"})
//
@EnableAutoConfiguration
@ComponentScan({"com.yodlee.ycc.stats.db"}) 
@ActiveProfiles(profiles= {"testng", "intgdev"})
public class RefreshStatDbServiceTest extends AbstractTestNGSpringContextTests{

	@Autowired
	RefreshStatsDbService service;
	
    @Test
    public void testLatencyReport() {
    	RefreshStatsDbFilter filter =new RefreshStatsDbFilter();
    	filter.setCobrandId(10000004l);
    	filter.setDuration("24h");
    	List<Long> siteIds=new ArrayList<>();
    	siteIds.add(123l);
    	filter.setSiteId(siteIds);
    	filter.setReportType("YCC_Overall_Refresh_Latency_Statistics_V1");
    	String refreshStatsData = service.getRefreshStatsData(filter);
    	System.out.println(refreshStatsData);
    	System.out.println("scan");
    }
    @Test
    public void testSiteOverallReport() {
    	RefreshStatsDbFilter filter =new RefreshStatsDbFilter();
    	//filter.setCobrandId(10000004l);
    	//filter.setDuration("4h");
    	List<Long> siteIds=new ArrayList<>();
    	siteIds.add(5l);
    	siteIds.add(2852l);
    	filter.setSiteId(siteIds);
    	filter.setReportType("YCC_Network_Sites_Statistics_V1");
    	String refreshStatsData = service.getRefreshStatsData(filter);
    	System.out.println(refreshStatsData);
    }
   
    @Test
    public void testAddOverallReport() {
    	RefreshStatsDbFilter filter =new RefreshStatsDbFilter();
    	filter.setCobrandId(10000004l);
    	filter.setDuration("7d");
    	List<Long> siteIds=new ArrayList<>();
    	//siteIds.add(5l);
    	//siteIds.add(2852l);
    	//filter.setSiteId(siteIds);
    	filter.setReportType("YCC_Add_Historic_Refresh_Latency_BreakDown_Statistics_V1");
    	String refreshStatsData = service.getRefreshStatsData(filter);
    	System.out.println(refreshStatsData);
    }
}
