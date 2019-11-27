/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.service.CobrandService;
import com.yodlee.ycc.dapi.utils.CobrandUtil;

/**
 * 
 * @author knavuluri
 * 
 */

@Controller
@RequestMapping("/v1/cobrandInfo")
public class CobrandController extends MasterController {

	private static final Logger logger = LoggerFactory.getLogger(CobrandController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private CobrandUtil cobrandUtil;
	
	@Autowired
	private CobrandService cobrandService;

	@RequestMapping(value = "/cobrands", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String getAllCobrands() throws Exception

	{
		logger.info("Invoking the getAllCobrands API");
 		Date sDate = new Date();
		logger.debug("Invoking cobrands API");
		String allCobrands = null;
		try {
			String cobId = (String) request.getAttribute("cobId");
			boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobId));
			if (!yodlee)
				throw new RefreshStatException("You are not allowed to access this API");
			allCobrands = CobrandUtil.getAllCobrands();
		}
		catch (Exception e) {
			logger.error("Getting all the cobrands:" + ExceptionUtils.getFullStackTrace(e));
		}
		logger.debug("Cobrands Response:" + allCobrands);
		Date eDate = new Date();
 		logger.info("Total time taken for getAllCobrands API:" + (eDate.getTime() - sDate.getTime()) / 1000 + " sec");
 		return allCobrands != null ? allCobrands : "{}";
	}

	@RequestMapping(value = "/cobrand", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String getCobrand(@RequestParam(value = "cobrandId", required = false) String cobrandId) throws Exception {
		logger.debug("Invoking cobrand API");
		logger.debug(" Input parameter cobrandId=" + cobrandId);
		String cobrandInfo = null;
		try {
			if (!StringUtils.isNotBlank(cobrandId)) {
				throw new RefreshStatException("Please provide the cobrandId");
			}
			else if (!StringUtil.isNumber(cobrandId)) {
				throw new RefreshStatException("CobrandId is invalid");
			}
			String cobId = (String) request.getAttribute("cobId");
			Long logdinCobrandId = Long.valueOf(cobId);
			CobrandUtil.validateCobrand(logdinCobrandId, Long.valueOf(cobrandId));
			cobrandInfo = cobrandUtil.getCobrandInfo(Long.valueOf(cobrandId.trim()));
			if (cobrandInfo == null)
				throw new RefreshStatException("CobrandId is invalid");
		}
		catch (Exception e) {
			logger.error("Getting the cobrand info:" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
		logger.debug("Cobrand response:" + cobrandInfo);
		return cobrandInfo != null ? cobrandInfo : "{}";
	}
	
	@RequestMapping(value = "/cobrandacl/{acl}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getCobrandDetails(@PathVariable(value = "acl") String acl,
			@RequestParam(value = "cobrandId", required = true) String cobrandId) {

		logger.info("Get User Info Data:");
		Date sDate = new Date();
		String cobrandACLInfoResponse = null;
		cobrandACLInfoResponse = cobrandService.getCobrandDetails(cobrandId, acl);
		Date eDate = new Date();
		logger.info("Total time taken for the notification create API:" + (eDate.getTime() - sDate.getTime())
				+ " millisec");

		return cobrandACLInfoResponse;
	}

}
