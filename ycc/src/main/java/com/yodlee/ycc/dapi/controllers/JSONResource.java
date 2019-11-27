/*
 * Copyright (c) 2012 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yodlee.framework.common.log.MessageController;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.ICobrandContext;
import com.yodlee.framework.service.build.builder.RootBuilder.ResultStyle;
import com.yodlee.framework.service.process.Processor;
import com.yodlee.nextgen.exceptions.core.InvalidYodleeServiceQueryException;
import com.yodlee.nextgen.session.exceptions.InvalidSessionException;

@Controller
@RequestMapping("/freeform")
public class JSONResource extends MasterController {
	
	private static final String FQCN = JSONResource.class.getName();
	
    public static boolean isSdmRequired = true;
    public static boolean isPositionalResult = true;
    public static final String ROOT = "/root";
    private static final String VIEW = "view\\";
    public static final String JSON = "json";
    public static final String QUERY = "query";
    public static final String USE = "use";
    
    private static final String YODLEE_LIB = "yodlee/";
    private static final String STYLE = "style";


    @Autowired
	private HttpServletRequest request;
    
    @RequestMapping(value = "/processRequest",method = RequestMethod.POST,produces = "application/json")
    public @ResponseBody String postResource(
    		@RequestParam(value = "query", required = true) String query,@RequestParam(value = "style", required = false) String style
    		) throws InvalidSessionException, InvalidYodleeServiceQueryException, MalformedURLException, IllegalArgumentException {
    	
    	
    	if(null==query || query.equals("")) {
    		throw new InvalidYodleeServiceQueryException("Invalid Yodlee Service Query");
    	}
    	
    	MessageController.log(FQCN, 101, "Process Request Method-->Query"+query, MessageController.STATUS);

    	 ICobrandContext validateContext = null;//processContext();
    	
    	try {

    		MultivaluedMap params = new MultivaluedMapImpl();
    		params.add("query", query);

    		ResultStyle resultStyle = null;

    		if(style!=null) {
    			resultStyle = ResultStyle.valueOf(style.toUpperCase());
    		}

    		boolean debugEnabled = Boolean.getBoolean("com.yodlee.debugJDBC");
    		long lMethodCallStartTime = 0;
    		long lMethodCallEndTime = 0;
    		long apiSequenceNumber = 0;
    		if(debugEnabled) {
    			if (lMethodCallStartTime == 0) {
    				lMethodCallStartTime = System.currentTimeMillis();
    			}
    			apiSequenceNumber = sqlTrace.logApiStart(query, lMethodCallStartTime);
    		}

    		String process = Processor.process("",query, params, null, false, false, 
    				resultStyle, 0, true);

    		if(debugEnabled) {
    			if (lMethodCallEndTime == 0) {
    				lMethodCallEndTime =System.currentTimeMillis();
    			}
    			sqlTrace.logApiEnd(query, lMethodCallStartTime, lMethodCallEndTime, apiSequenceNumber);
    		}
    		return process;

    	} finally {
            ContextAccessorUtil.unsetContext();            
        }
    }

   
}
