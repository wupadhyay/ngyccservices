/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.dom.tx.TransactionMgmt;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.ycc.dapi.bean.CobrandAcl;
import com.yodlee.ycc.dapi.bean.CobrandAclInfo;

@Service
public class CobrandService {
	
	private static final Logger logger = LoggerFactory.getLogger(CobrandService.class);

	public String getCobrandDetails(String cobrand, String acl) {
		List<String> cobrandList = Arrays.asList(cobrand.split("\\s*,\\s*"));
		List<CobrandAcl> finalCobrandList = new ArrayList<>();
		for (String cobId : cobrandList) {
			Long cobrandId = Long.valueOf(cobId);
			List<CobrandAcl> cobDetail = null;
			TransactionMgmt txn = null;
			try {
				ContextAccessorUtil.setContext(cobrandId, 0l, null);
				txn = new TransactionMgmt();
				txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, cobrandId);

				JdbcTemplate jdbcTemplate = txn.getJdbcTemplate();
				String query = "select acl_value,cobrand_id as cobrandId from cobrand_acl_value where param_acl_id="
						+ acl + " and cobrand_id=" + cobrandId;
				cobDetail = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(CobrandAcl.class));
				finalCobrandList.add(cobDetail.get(0));
			} catch (Exception e) {
				logger.error("Exception in getAllSrStatuses " + e.getMessage());
			} finally {
				txn.endTransaction(false);
				logger.info("LoaDataUtil::getAllSrStatuses the transaction");
				ContextAccessorUtil.unsetContext();
			}
		}
		CobrandAclInfo cobInfo = new CobrandAclInfo();
		cobInfo.setCobrands(finalCobrandList);
		String response = convertObjToJson(cobInfo);
		return response;
	}
	
	public static String convertObjToJson(Object obj) {
		ObjectMapper converter = new ObjectMapper();
		String response = null;
		try {
			response = converter.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("Illegal request paramaters are passed: " + e.getMessage());
		}
		return response;

	}
	
	@PostConstruct
	public void init() {
		logger.info(" CobrandService CobrandServiceCobrandService ----- Postconstruct");
	}
}
