/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.elasticsearch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLMatcher {
	
	//Note: Group name is 'site'
	public static String URL_MATCH_REGEX = "((http(s?)://)|(www\\.)|(http(s?)://www\\.)){1,2}(?<site>[A-Za-z\\.0-9/_-]+)(\\?\\S+)?";

	private Matcher urlMatcher;
	
	public URLMatcher() {
		this.urlMatcher = Pattern.compile(URL_MATCH_REGEX, Pattern.CASE_INSENSITIVE).matcher(""); ;
	}
	
	public Matcher getMatcher() {
		return urlMatcher;
	}
	
	public void reset() {
		urlMatcher.reset();
	}
}
