/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

package com.yodlee.ycc.dapi.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yodlee.ycc.dapi.utils.FinancialDataUtil;
import com.yodlee.ycc.dapi.utils.WealthCobrandUtil;

/**
 * 
 * @author bkumar1 
 * Description: This controller handles the requests from the
 *         		clientDashboard.
 */

@Controller
@RequestMapping("/v1/financialInfo")
public class FinancialDataController extends MasterController {
	private final Logger logger = LoggerFactory.getLogger(FinancialDataController.class);

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/firms", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getAllFirms(@RequestParam(value = "cobrandId", required = true) String cobrandId)
			throws Exception {
		logger.debug("Invoking firms API");
		String allFirms = null;
		try {
			allFirms = FinancialDataUtil.loadAllFirmsForCobrand(cobrandId);
		} catch (Exception e) {
			logger.error("Exception at getAllFirms :" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("getAllFirms Response:" + allFirms);
		return allFirms;
	}
	
	@RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getAllAccounts(@RequestParam(value = "firmId", required = true) String firmId,@RequestParam(value = "cobrandId", required = true) String cobrandId,@RequestParam(value = "requestType", required = true) String requestType)
			throws Exception {
		logger.debug("Invoking accounts API");
		String allAccounts = null;
		try {
			allAccounts = FinancialDataUtil.getAccountsForFirm(firmId,cobrandId,requestType);
		} catch (Exception e) {
			logger.error("Exception at getAllAccounts :" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("getAllAccounts Response:" + allAccounts);
		return allAccounts;
	}
	
	@RequestMapping(value = "/positions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getPositionDetails(@RequestParam(value = "selectedAccounts", required = true) List<Long> selectedAccounts,@RequestParam(value = "cobrandId", required = true) String cobrandId,@RequestParam(value = "requestType", required = true) String requestType)
			throws Exception {
		logger.debug("Invoking positions API");
		String positionInfo = null;
		try {
			positionInfo = FinancialDataUtil.getPositionsForAccounts(selectedAccounts,cobrandId,requestType);
		} catch (Exception e) {
			logger.error("Exception at getPositionsForAccounts :" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("getPositionDetails Response:" + positionInfo);
		return positionInfo;
	}
	
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getTransactionDetails(@RequestParam(value = "selectedAccounts", required = true) List<Long> selectedAccounts,@RequestParam(value = "cobrandId", required = true) String cobrandId,@RequestParam(value = "requestType", required = true) String requestType)
			throws Exception {
		logger.debug("Invoking positions API");
		String positionInfo = null;
		try {
			positionInfo = FinancialDataUtil.getTransactionsForAccounts(selectedAccounts,cobrandId,requestType);
		} catch (Exception e) {
			logger.error("Exception at getPositionsForAccounts :" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("getPositionDetails Response:" + positionInfo);
		return positionInfo;
	}
	
	@RequestMapping(value ="/wealthCobrands",method =RequestMethod.GET,produces ="application/json")
	public @ResponseBody
	String getAllWealthCobrands()
	{
		logger.debug("Fetching wealth recon cobrands");
		Date sDate = new Date();
		String allCobrands = null;
		try {
			allCobrands = WealthCobrandUtil.getAllWealthReconEnabledCobrands();
		}
		catch (Exception e) {
			logger.error("Getting all the wealth recon cobrands:" + ExceptionUtils.getFullStackTrace(e));
		}
		logger.debug("Cobrands Response:" + allCobrands);
		Date eDate = new Date();
		logger.info("Total time taken for getAllWealthReconEnabledCobrands API:" + (eDate.getTime() - sDate.getTime()) / 1000 + " sec");
		return allCobrands != null ? allCobrands : "{}";

	}
	
	/*
	 * cobrandid received is the cobrand id of the user who logs in the system.
			providerId is the cobrandId which is selected in the AccountInformation page for Non Tamrac user.
			
	 */
	@RequestMapping(value = "/envestnetAccounts", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getAllAccountsNonTamarac(@RequestParam(value = "providerId", required = true) String providerId,@RequestParam(value = "cobrandId", required = true) String cobrandId,@RequestParam(value = "requestType", required = true) String requestType,@RequestParam(value = "accountIds", required = true) List<Long> accountIds)
			throws Exception {
		logger.debug("Invoking getAllAccountsNonTamarac API For Non Tamrac Cobrands");
		String allAccounts = null;
		try {
			allAccounts = FinancialDataUtil.getAccountsForProvider(providerId,requestType,accountIds);
		} catch (Exception e) {
			logger.error("Exception at getAllAccountsNonTamarac :" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("getAllAccountsNonTamarac Response:" + allAccounts);
		return allAccounts;
	}
	

}
