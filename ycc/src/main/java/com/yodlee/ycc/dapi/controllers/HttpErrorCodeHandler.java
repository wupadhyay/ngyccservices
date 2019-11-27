/*
* Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.dapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.yodlee.ycc.dapi.exceptions.HttpForbiddenException;
import com.yodlee.ycc.dapi.exceptions.HttpMethodNotAllowedException;
import com.yodlee.ycc.dapi.exceptions.HttpNotImplementedException;
import com.yodlee.ycc.dapi.exceptions.PageNotFoundException;
import com.yodlee.ycc.dapi.exceptions.UnauthorizedException;

@Controller
@RequestMapping("/errorCode/{httpCode}")
public class HttpErrorCodeHandler {

	@ApiIgnore
	@RequestMapping(produces = "application/json")
	public @ResponseBody void  handleHttpErrorCode(@PathVariable String httpCode) throws Exception 
	{
		if(httpCode.equals("404")) {
			throw new PageNotFoundException("Resource not found");
		} else if(httpCode.equals("401")) {
			throw new UnauthorizedException("Unauthorized");
		} else if(httpCode.equals("403")) {
			throw new HttpForbiddenException("Forbidden");
		} else if(httpCode.equals("405")) {
			throw new HttpMethodNotAllowedException("Method not allowed");
		} else if(httpCode.equals("500")) {
			throw new Exception("Internal Server Error");
		}  else if(httpCode.equals("501")) {
			throw new HttpNotImplementedException("Not implemented");
		}
		 
	}
	
}
