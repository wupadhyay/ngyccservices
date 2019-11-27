/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author knavuluri
 * 
 */
@Component
public class SplunkUtil {

	private static final Logger logger = LoggerFactory.getLogger(SplunkUtil.class);

	private static String REST_URL = "https://splunkapi.yodlee.com/";

	private static String USER_NAME = "knavuluri";

	private static String PASSWORD = "test@123";
	
	private static int maxRetryCount = 30;
	
	private static int minRetryCount = 10;
	
	private static long maxSleepTime= 5000;
	
	private static long minSleepTime= 1000;

	private static String splunkSession;

	static {
		USER_NAME = MiscUtil.getPropertyValue("config.splunk.username", true);
		PASSWORD = MiscUtil.getPropertyValue("config.splunk.password", true);
		REST_URL = MiscUtil.getPropertyValue("config.splunk.url", false);
		maxRetryCount = Integer.valueOf(MiscUtil.getPropertyValue("com.yodlee.app.maxRetryCount", false));
		minRetryCount = Integer.valueOf(MiscUtil.getPropertyValue("com.yodlee.app.minRetryCount", false));
		maxSleepTime = Long.valueOf(MiscUtil.getPropertyValue("com.yodlee.app.maxSleepTime", false));
		minSleepTime = Long.valueOf(MiscUtil.getPropertyValue("com.yodlee.app.minSleepTime", false));
	}

	private static final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}
	};

	public static synchronized  void login() {
		if (splunkSession == null) {
			String url = REST_URL + "services/auth/login";
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("username", USER_NAME);
			params.add("password", PASSWORD);
			String response = restPost(url, params);
			splunkSession = response.substring(response.indexOf("<sessionKey>") + 12, response.lastIndexOf("</sessionKey>")).trim();
			logger.debug("splunk session:" + splunkSession);
		}
	}

	public static String restPost(String url, MultiValueMap<String, String> params) {
		HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", "Splunk " + splunkSession);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
		return response.getBody().toString();
	}

	private  String get(String url, String content) throws Exception{
		StringBuffer strLine = new StringBuffer();
		logger.info("url "+ url + " content "+content);
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
			URL u = new URL(url);
			HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
			if (uc instanceof HttpsURLConnection) {
				((HttpsURLConnection) uc).setHostnameVerifier(HOSTNAME_VERIFIER);
			}
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			uc.setRequestProperty("Authorization", "Splunk " + splunkSession);
			uc.setRequestMethod("GET");
			if (content != null && !"".equalsIgnoreCase(content.trim())) {
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(uc.getOutputStream()), true);
				pw.print(content);
				pw.flush();
				pw.close();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				strLine.append(output);
			}
			uc.disconnect();
		} catch (Exception e) {
			logger.error("Error while invoking the splunk get call: "+content +" Exception:"+ExceptionUtils.getFullStackTrace(e));
			throw e;
		}

		if (strLine.length() > 0)
			return strLine.toString();
		logger.error("couldnt get response for http method GET url "+url);
		return null;
	}

	private  String post(String url, String content) throws Exception{
		StringBuffer strLine = new StringBuffer();
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
			URL u = new URL(url);
			HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
			if (uc instanceof HttpsURLConnection) {
				((HttpsURLConnection) uc).setHostnameVerifier(HOSTNAME_VERIFIER);
			}
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			uc.setRequestProperty("Authorization", "Splunk " + splunkSession);
			uc.setRequestMethod("POST");
			if (content != null && !"".equalsIgnoreCase(content.trim())) {
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(uc.getOutputStream()), true);
				pw.print(content);
				pw.flush();
				pw.close();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
				strLine.append(output);
			}
			uc.disconnect();
		} catch (Exception e) {
			logger.error("Error while invoking the splunk post call: "+content +" Exception:"+ExceptionUtils.getFullStackTrace(e));
			throw e;
		}

		if (strLine.length() > 0)
			return strLine.toString();
		logger.error("couldnt get response for http method POST url "+url);
		return null;

	}

	public String executeSplunkServices(final String endpoint, String queryString, String joinString, String count, boolean reload) throws Exception {
		String response = null;
		try {
			if (splunkSession == null)
				login();
			String sid = getSid(endpoint, false, queryString, joinString);
			logger.debug("Splunk sid is:" + sid);
			if (sid == null)
				throw new RuntimeException("sid is null");
			if (count == null)
				count = "100000";
			String url = REST_URL + "services/search/jobs/" + sid + "/results?output_mode=json&count=" + count;
			response = get(url, null);
		}
		catch (Exception e) {
			logger.error("Reportname:" + endpoint + " Error while getting the sid:" + ExceptionUtils.getFullStackTrace(e));
			if (e.getMessage().contains("401")) {
				splunkSession = null;
				logger.info("Reportname:" + endpoint + " Invoking login");
				login();
				logger.info("Reportname:" + endpoint + " Login completed");
				if (!reload)
					return executeSplunkServices(endpoint, queryString, joinString, count, true);
			}
			else {
				throw new RuntimeException("Oops some issue at our end");
			}
		}
		return response;
	}

	public  String getSid(String endpoint, boolean reload, String queryString,String joinString) throws Exception{
		String sid = null;
		try {
			String url = REST_URL + "services/search/jobs?output_mode=json";
			StringBuilder builder = new StringBuilder();
			if(joinString != null && joinString.equalsIgnoreCase("recon"))
			{
				builder.append(queryString);
			}else{
				builder.append("search=|loadjob savedsearch=\"admin:search:"+ endpoint + "\"");
				if (queryString != null) {
					if (joinString != null) {
						//builder.append(joinString);
						builder.append("| where " + queryString);
					}
					//builder.append("| where " + queryString);
					builder.append(joinString);
				}
			}
			logger.info("content:" + builder.toString());
			
			String response = post(url, builder.toString());
			if (response != null) {
				sid = response.substring(response.indexOf(":") + 2, response.lastIndexOf("\""));
			} else {
				logger.info("Sid is null .So throwing the exception.");
				throw new RuntimeException("Oops some issue at our end");
			}
			logger.debug("Sid is:" + sid);
			String jobstatus = getJobstatus(endpoint,sid, 0);
			logger.debug("Final Job status is:" + jobstatus);
			if (jobstatus == "1")
				throw new RuntimeException("Splunk invocation exception");
		} catch (Exception e) {
			logger.error("Reportname:"+endpoint+" Error while getting the sid:" + ExceptionUtils.getFullStackTrace(e));
			throw e;

		}
		return sid;
	}

	public String getJobstatus(String endpoint, String sid, int count) throws Exception {
		String response = get(REST_URL + "services/search/jobs/" + sid, null);
		String done = response.substring(response.indexOf("<s:key name=\"isDone\">") + 21, response.indexOf("<s:key name=\"isDone\">") + 22);
		String status = "";
		logger.info("Report Name:" + endpoint + " Sid:" + sid + " Job Status for the request:" + done);
		if (done.equals("1")) {
			status = response.substring(response.indexOf("<s:key name=\"isFailed\">") + 23, response.indexOf("<s:key name=\"isFailed\">") + 24);
			return status;
		}
		else {
			if (count > maxRetryCount) {
				logger.error("Exceeded max retry count   of "+ maxRetryCount +" times retrival of job status");
				throw new RuntimeException("Exceeded max retry count of "+ maxRetryCount + " while fetching splunk data for sid "+sid );
			}
			try {
				if (count < minRetryCount)
					Thread.sleep(minSleepTime);
				else
					Thread.sleep(maxSleepTime);
			}
			catch (InterruptedException e) {
				logger.error("Error while getting the job status:" + ExceptionUtils.getFullStackTrace(e));
			}
			status = getJobstatus(endpoint, sid, ++count);
		}
		return status;
	}
	
	public static void main(String args[])
	{
	    System.out.println("Splunk main..");
		SplunkUtil splunkUtil = new SplunkUtil();
		SplunkUtil.login();
	}

}
