/*
 * Copyright (c) 2015 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */
/**
 * 
 */
package com.yodlee.ycc.dapi.utils;

import java.net.InetAddress;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yodlee.config.ConfigServiceImpl;

/**
 * Generates unique reference id to use for tracking an call
 * @author sprashan
 *
 */
public class UniqueTrackingIdGenerator {
	
	private static final char[] ALPHABETS_ARRAY;
	private static final ThreadLocal<Random> threadLocal = new ThreadLocal<Random>();

	private static final UniqueTrackingIdGenerator _instance = new UniqueTrackingIdGenerator();

	private int instanceId;
	private String ipLastOctet = null;

	public static UniqueTrackingIdGenerator getInstance() {
		return _instance;
	}

	private UniqueTrackingIdGenerator() {
		this.ipLastOctet = getIpAddress();
	}
	
	static {
		StringBuffer sb = new StringBuffer();
		for (char c = 'a'; c <= 'z'; c++) {
			sb.append(c);
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			sb.append(c);
		}
		ALPHABETS_ARRAY = sb.toString().toCharArray();
	}


	/**
	 * Tracking id format: <R><TIME><R><I><R><IP4><R> where <R> - Random
	 * alphabet of length 1(ex., x) <TIME> - Time in Milliseconds <I> - Instance
	 * id of the application(1 for instance one.) <IP4> - Last part of ip
	 * address of the instance(125 for ip 172.17.18.125)
	 * 
	 * Example tracking id: b1430181702118Y15w100U 1430181702118 milliseconds,
	 * instance 15 and ip ending with .100
	 * 
	 * @return
	 */
	public String generateTrackingId() {

		Random r = threadLocal.get();
		if (r == null) {
			// create new random number generator using default generated seed
			r = new Random();
			threadLocal.set(r);
		}
		
		long now = System.currentTimeMillis();

	    String uniqueId = String.format("%s%d%s%d%s%s%s", ALPHABETS_ARRAY[r.nextInt(ALPHABETS_ARRAY.length)], now,
				ALPHABETS_ARRAY[r.nextInt(ALPHABETS_ARRAY.length)], this.getInstanceId(), 
				ALPHABETS_ARRAY[r.nextInt(ALPHABETS_ARRAY.length)], this.getIpLastOctet(),
				ALPHABETS_ARRAY[r.nextInt(ALPHABETS_ARRAY.length)]);

	    return uniqueId;
	}

	/**
	 * get local ip address, and extract last octet if ip address is
	 * 172.17.80.125, then output is 125
	 * 
	 * @return
	 */
	private static String getIpAddress() {

		String ipLastOctet = null;
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			ipLastOctet = ip.substring(ip.lastIndexOf('.') + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipLastOctet;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public String getIpLastOctet() {
		return ipLastOctet;
	}

	public void setIpLastOctet(String ipLastOctet) {
		this.ipLastOctet = ipLastOctet;
	}
}