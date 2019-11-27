/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

package com.yodlee.ycc.dapi.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yodlee.framework.runtime.util.SimpleTypeUtil;

public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String DATE_TIME_FORMAT_PATTERN_EXT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static boolean checkDates(String fromDate,String toDate) throws ParseException {
		
		Date date1 = sdf.parse(fromDate);
    	Date date2 = sdf.parse(toDate);

    	Calendar cal1 = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
    	cal1.setTime(date1);
    	cal2.setTime(date2);
    	
    	if(cal1.after(cal2)) {
    		return false;
    	} 
    	
    	return true;
	}
	
	public static boolean checkFromDate(String fromDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		Date date1 = sdf.parse(fromDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		int daysInBetween= (int) ((currentDate.getTime() - date1.getTime())/(24*60*60*1000));
		
		if(daysInBetween>365)
			return false;
		
		if(daysInBetween<0)
			return false;
		
		return true;
	}
	
	public static boolean checkToDate(String toDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		
		Date date1 = sdf.parse(toDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		if(cal1.after(cal)) { //Future Date
			return true;
		} else {
			cal.setTime(new Date());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE,-365);
			
			if(cal.getTime().equals(date1))
				return true;
			
			if(cal.after(cal1)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String getDateFromRestEpoch(String json,
			String[] toReplaceArray, String start) {
		int startIndex = 0;
		String convartedDate;
		while (startIndex != -1) {
			startIndex = json.indexOf(start, startIndex + 1);
			if (startIndex == -1)
				continue;
			for (String toReplace : toReplaceArray) {
				int first = json.indexOf(toReplace, startIndex);
				if (first < 0) { 
					// When attribute not present, goes to next attribue
					continue;
				}
				int second = json.indexOf(",", first);
				int secondCurly = json.indexOf("}", first);
				String toCompile;
				String replace;
				if (!json.substring(first + toReplace.length() + 2).startsWith(
						"\"")) {
					String date;
					if (second == -1 | secondCurly < second) {
						second = json.indexOf("}", first);
						date = json.substring(first + toReplace.length() + 2,
								second);
					} else
						date = json.substring(first + toReplace.length() + 2,
								second);
					// System.out.println("date= "+date+" "+getDateFromEpoch(Long.parseLong(date),
					// false));
					int size = date.length();
					// System.out.println(size);
					convartedDate = getDateFromEpoch(Long.parseLong(date), true);
					replace = "\"" + toReplace + "\":\"" + convartedDate + "\"";
					toCompile = "\"" + toReplace + "\":[0-9]{" + size + "}";
				} else {
					String date;
					if (second == -1 | secondCurly < second) {
						second = json.indexOf("}", first);
						date = json.substring(first + toReplace.length() + 3,
								second - 1);
					} else
						date = json.substring(first + toReplace.length() + 3,
								second - 1);

					// System.out.println("date= "+date+ " "
					// +getDateFromEpoch(Long.parseLong(date), false));
					int size = date.length();
					System.out.println(size);
					convartedDate = getDateFromEpoch(Long.parseLong(date), true);
					replace = "\"" + toReplace + "\":\"" + convartedDate + "\"";
					toCompile = "\"" + toReplace + "\":\"[0-9]{" + size + "}\"";

				}
				String dummyUrl = json.substring(startIndex);
				Pattern pattern = Pattern.compile(toCompile);
				Matcher matcher = pattern.matcher(json.substring(startIndex));
				// Check if pattern find
				if (matcher.find()) {
					json = json.replaceFirst(toCompile, replace);
				}
			}
		}
		return json;
	}
	
	
	
	public static String getDateFromEpoch(long epochDate, boolean isTimeZoneReqd){
		long seconds = epochDate;
		Date date = new Date(seconds * 1000);
		SimpleDateFormat formatter = null;
		 
		if(isTimeZoneReqd) {
			formatter =  new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
		} else {
			formatter =  new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);;
		}
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String format = formatter.format(date);
		return format.toString();
	}
	
	
	public static String formatDateToISO(Date date) {
		if (date == null) {
			return null;
		}
		
		return sdf.format(date);
	}

	public static Date convertStringToDate(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date format = formatter.parse(date);
		return format;
	}
	public static String convertDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String format = formatter.format(date);
		return format.toString();
	}
}
