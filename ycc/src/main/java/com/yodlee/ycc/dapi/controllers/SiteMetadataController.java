/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yodlee.domx.storage.SearchSiteLocalStorage;
import com.yodlee.framework.common.util.StringUtil;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.context.PersistenceContext;
import com.yodlee.nextgen.lucene.SiteSearchService;
import com.yodlee.nextgen.lucene.exceptions.InvalidSearchStringException;
import com.yodlee.ycc.dapi.bean.CobrandInfo;
import com.yodlee.ycc.dapi.constants.YSLConstants;
import com.yodlee.ycc.dapi.elasticsearch.ElasticQueryExecutor;
import com.yodlee.ycc.dapi.elasticsearch.ElasticSearchQueryBuilder;
import com.yodlee.ycc.dapi.elasticsearch.URLMatcher;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.exceptions.RefreshStatException;
import com.yodlee.ycc.dapi.utils.CobrandUtil;
import com.yodlee.ycc.dapi.utils.MiscUtil;

/**
 * 
 * @author knavuluri
 * 
 */
@Controller
@RequestMapping("/v1/providers")
public class SiteMetadataController extends MasterController {

	private static final Logger logger = LoggerFactory.getLogger(SiteMetadataController.class);

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String search(@RequestParam(value = "cobrandId", required = false) String cobrandId, @RequestParam(value = "providerId", required = false) String providerId,
			@RequestParam(value = "name", required = false) String name, @RequestParam(value = "agentName", required = false) String agentName,
			@RequestParam(value = "url", required = false) String url) throws ApiException,Exception {
		logger.debug("Invoking site search API");
		Object[] obj = new Object[] { cobrandId, providerId, name, agentName, url };
		logger.debug("Input parameters:cobrandId={},providerId={},name={},agentName={},url={}", obj);
		Date sDate = new Date();
		String cobId = (String) request.getAttribute("cobId");
		if (StringUtils.isEmpty(cobrandId)) {
			cobrandId = cobId;
		} else {

			if (!StringUtil.isNumber(cobrandId)) {
				throw new RefreshStatException("CobrandId is invalid");
			} else {
				CobrandUtil.validateCobrand(Long.valueOf(cobId), Long.valueOf(cobrandId));
			}
		}
		boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobId));
		if (!StringUtil.isNullOrEmpty(name) && name.length() < 3) {
			throw new RefreshStatException("Name should be more than 3 characters");
		}
		if (StringUtil.isNullOrEmpty(providerId) && StringUtil.isNullOrEmpty(name) && StringUtil.isNullOrEmpty(agentName) && StringUtil.isNullOrEmpty(url)) {
			throw new RefreshStatException("One of the parameter is required for search");
		}
		String providerResponse = null;

		try {
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("provider id distinct name status loginUrl baseUrl country containerNames isAutoRefreshEnabled aggregationType additionalInformation(all)");
			Map<String, String> map = new HashMap<String, String>();
			Set<Long> siteIds = new HashSet<Long>();
			if (!StringUtil.isNullOrEmpty(providerId)) {
				providerId = providerId.trim();
				siteIds.add(Long.valueOf(providerId));
				queryBuilder.append(" favicon logo primaryLanguageISOCode mfaType help languageISOCode countryISOCode lastModified createdDate forgetPasswordUrl authType providerCategory defaultName containerAttributes localizedProviderAttributes (all) capability(all)  loginForm (id forgetPasswordURL formType row id label help form fieldRowChoice field id name size maxLength type value isOptional prefix suffix valueEditable validation (regExp errorMsg) option (optionValue displayText)) additionalDataSet (all) ");
			}
			else {
				int primaryCount = 0;
				int searchCount = 0;
				boolean executeElasticSearch = false;
				//Elastic search
				boolean useElasticSearch = MiscUtil.getPropertyValue("com.yodlee.elasticsearch.enable", false).equals("true");
				logger.debug("Elastic Search boolean value "+useElasticSearch);
				if(useElasticSearch)
				{
					ElasticSearchQueryBuilder elasticSearchQueryBuilder = new ElasticSearchQueryBuilder();
					Set<Long> siteIdList1 = new HashSet<>();
					boolean hasValues = false;
					if (!StringUtil.isNullOrEmpty(name)) {
						name = name.trim();
						Set<Long> siteIdList = SiteSearchService.searchYccCobrandDisplayNameWithoutNoise(name.toLowerCase(), cobrandId);
						if (siteIdList != null && !siteIdList.isEmpty()) {
							siteIds.addAll(siteIdList);
							hasValues = true;
						}
							name = name.replaceAll("[!\"#$%&'()*\\\\+,-./:;<=>?@\\^_`{|}~\\[\\t\\]]","");
							String searchCompString = name.replaceAll("\\s+", "").toLowerCase();
							elasticSearchQueryBuilder.should("match_phrase", "site_display_name", name.toLowerCase(),false)
									.should("prefix", "site_display_name_comp", searchCompString,false);
							executeElasticSearch = true;
							 
					}
					if (!StringUtil.isNullOrEmpty(url)) {
							if (isURLMatch(url)) {
								String urlName = siteNameFromURL(url);
								urlName = urlName.replaceAll("[!\"#$%&'()*\\\\+,-./:;<=>?@\\^_`{|}~\\[\\t\\]]", "");
							elasticSearchQueryBuilder.should("prefix", "base_url", urlName.toLowerCase(),true)
										.should("prefix", "login_url", urlName.toLowerCase(),true);
							executeElasticSearch = true;

							}
					}
							if(executeElasticSearch)
							{
							String query =  elasticSearchQueryBuilder.build();
							logger.debug("Query formed-- "+query);
						 siteIdList1= ElasticQueryExecutor.executeQuery(query);
						 if (siteIdList1 != null && !siteIdList1.isEmpty()) {
								siteIds.addAll(siteIdList1);
								hasValues = true;
						 }
						if (hasValues)
							searchCount++;

						primaryCount++;
							}
					
					
					
				}
				else
				{
				if (!StringUtil.isNullOrEmpty(name)) {
					name = name.trim();
					Set<Long> siteIdList = SiteSearchService.searchYccCobrandDisplayNameWithoutNoise(name.toLowerCase(), cobrandId);
					boolean hasValues = false;
					if (siteIdList != null && !siteIdList.isEmpty()) {
						siteIds.addAll(siteIdList);
						hasValues = true;
					}
					Set<Long> siteIdList1 = SiteSearchService.searchYccDisplayNameWithoutNoise(name.toLowerCase());
					if (siteIdList1 != null && !siteIdList1.isEmpty()) {
						siteIds.addAll(siteIdList1);
						hasValues = true;
					}
					if (hasValues)
						searchCount++;

					primaryCount++;
				}
				if (!StringUtil.isNullOrEmpty(url)) {
					boolean hasValues = false;
					Set<Long> siteIdList = SiteSearchService.searchByYccBaseURL(url.toLowerCase());
					if (siteIdList != null && !siteIdList.isEmpty()) {
						if (!siteIds.isEmpty())
							siteIds.retainAll(siteIdList);
						else
							siteIds.addAll(siteIdList);
						hasValues = true;
					}
					Set<Long> loginSiteIdList = SiteSearchService.searchByYccLoginURL(url.toLowerCase());
					if (loginSiteIdList != null && !loginSiteIdList.isEmpty()) {
						if (!siteIds.isEmpty())
							siteIds.retainAll(loginSiteIdList);
						else
							siteIds.addAll(loginSiteIdList);
						hasValues = true;
					}
					if (hasValues)
						searchCount++;
					primaryCount++;
				}
				}
				if (primaryCount != searchCount)
					return "{}";

				boolean hasOtherCriteria = false;
				if (!StringUtil.isNullOrEmpty(agentName)) {
					map.put("agentname", agentName);
					queryBuilder.append(" agentName AGENTNAME");
					hasOtherCriteria = true;
				}
				if (siteIds.isEmpty() && !hasOtherCriteria) {
					return "{}";
				}
			}
			if (siteIds != null && !siteIds.isEmpty()) {
				String ids = StringUtils.join(siteIds, ",");
				map.put("id", ids);
				queryBuilder.append(" providerId PROVIDERID");
			}

			if (yodlee) {
				map.put(YSLConstants.USER_TYPE, "tier2");
			}
			else
				map.put(YSLConstants.USER_TYPE, "tier1");
			
			CobrandInfo cobrandInfoVO = CobrandUtil.getCobrandInfoVO(Long.valueOf(cobrandId));
			if (cobrandInfoVO != null && cobrandInfoVO.getChannelId() != null && cobrandInfoVO.getChannelId().longValue() > 0l)
				map.put(YSLConstants.COBRAND_ID, String.valueOf(cobrandInfoVO.getChannelId()));
			SearchSiteLocalStorage.SetSearchSiteCriteria(map);

			ContextAccessorUtil.setContext(Long.valueOf(cobrandId), 0l, null);
			
			logger.debug("Query for site:" + queryBuilder.toString());
			providerResponse = getResource(queryBuilder.toString());
			if (providerResponse != null && !providerResponse.equalsIgnoreCase("{}"))
				providerResponse = processResponse(providerResponse);
		}
		
		catch (Exception e) {
			logger.info("Input parameters:cobrandId={},providerId={},name={},agentName={},baseUrl={},loginUrl={}", obj);
			logger.error("Error while searching the sites:" + ExceptionUtils.getFullStackTrace(e));
			if(e instanceof InvalidSearchStringException)
				return "{}";
			throw e;
		}
		finally {
			SearchSiteLocalStorage.unSearchSiteCriteria();
			ContextAccessorUtil.unsetContext();
			PersistenceContext.getInstance().closeEntityManager("dom");
		}
		Date eDate = new Date();
		logger.info("Total time taken for site search API:" + (eDate.getTime() - sDate.getTime())  + " milliSec : Input parameters:cobrandId={},providerId={},name={},agentName={},baseUrl={},loginUrl={}", obj );
		logger.debug("Final response for site:" + providerResponse);
		return providerResponse == null ? "{}" : providerResponse;

	}
	
	@RequestMapping(value = "/search/provider", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String search(@RequestParam(value = "cobrandId", required = false) String cobrandId, @RequestParam(value = "providerIds", required = false) String providerIds,
			@RequestParam(value = "fieldName", required = false) String fieldName, @RequestParam(value = "name", required = false) String name) throws ApiException, Exception {
		logger.debug("Invoking site fav search API");
		Object[] obj = new Object[] { cobrandId, providerIds, fieldName };
		logger.debug("Input parameters:cobrandId={},providerIds={},fieldName={}", obj);
		Date sDate = new Date();
		String cobId = (String) request.getAttribute("cobId");

		if (StringUtils.isEmpty(cobrandId)) {
			cobrandId = cobId;
		}
		if (!StringUtil.isNumber(cobrandId)) {
			throw new RefreshStatException("CobrandId is invalid");
		}
		else {
			CobrandUtil.validateCobrand(Long.valueOf(cobId), Long.valueOf(cobrandId));
		}
		boolean yodlee = CobrandUtil.isYodlee(Long.valueOf(cobId));
		
		if (StringUtil.isNullOrEmpty(providerIds) && StringUtil.isNullOrEmpty(name)) {
			throw new RefreshStatException("One of the parameter is required for search");
		}
		String providerResponse = null;
		try {
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("provider id distinct name");
			if (fieldName != null) {
				String[] fields = fieldName.split(",");
				for (String field : fields) {
					queryBuilder.append(" " + field);
				}
			}
			Map<String, String> map = new HashMap<String, String>();
			Set<Long> siteIds = new HashSet<Long>();
			boolean useElasticSearch = MiscUtil.getPropertyValue("com.yodlee.elasticsearch.enable", false).equals("true");
			logger.debug("Elastic Search boolean value "+useElasticSearch);
			if (!StringUtil.isNullOrEmpty(name)) {
				if (name.length() < 3) {
					throw new RefreshStatException("Name should be more than 3 characters");
				}
				name = name.trim();
				Set<Long> siteIdList = SiteSearchService.searchYccCobrandDisplayNameWithoutNoise(name.toLowerCase(), cobrandId);
				if (siteIdList != null && !siteIdList.isEmpty()) {
					siteIds.addAll(siteIdList);
				}
				Set<Long> siteIdList1;
				if(useElasticSearch)
				{
					ElasticSearchQueryBuilder elasticSearchQueryBuilder = new ElasticSearchQueryBuilder();
					name = name.replaceAll("[!\"#$%&'()*\\\\+,-./:;<=>?@\\^_`{|}~\\[\\t\\]]","");
					String searchCompString = name.replaceAll("\\s+", "").toLowerCase();
					String query = elasticSearchQueryBuilder
							.should("match_phrase", "site_display_name", name.toLowerCase(),false)
							.should("prefix", "site_display_name_comp", searchCompString,false).build();
					logger.debug("--Query formed-- "+query);
					 siteIdList1 = ElasticQueryExecutor.executeQuery(query);
				}
				else
				{
				siteIdList1 = SiteSearchService.searchYccDisplayNameWithoutNoise(name.toLowerCase());
				}
				if (siteIdList1 != null && !siteIdList1.isEmpty()) {
					siteIds.addAll(siteIdList1);
				}
			}
			String ids = null;
			if (!siteIds.isEmpty()) {
				ids = StringUtils.join(siteIds, ",");
			}
			if (providerIds != null && !providerIds.isEmpty()) {
				ids = ids != null ? ids+"," + providerIds : providerIds;
			}
			if (ids == null)
				return "{}";
			map.put("id", ids);
			queryBuilder.append(" providerId PROVIDERID");

			if (yodlee) {
				map.put(YSLConstants.USER_TYPE, "tier2");
			}

			CobrandInfo cobrandInfoVO = CobrandUtil.getCobrandInfoVO(Long.valueOf(cobrandId));
			if (cobrandInfoVO != null && cobrandInfoVO.getChannelId() != null && cobrandInfoVO.getChannelId().longValue() > 0l)
				map.put(YSLConstants.COBRAND_ID, String.valueOf(cobrandInfoVO.getChannelId()));
			SearchSiteLocalStorage.SetSearchSiteCriteria(map);

			ContextAccessorUtil.setContext(Long.valueOf(cobrandId), 0l, null);
			
			logger.debug("Query for site:" + queryBuilder.toString());
			providerResponse = getResource(queryBuilder.toString());
			if (providerResponse != null && !providerResponse.equalsIgnoreCase("{}"))
				providerResponse = processResponse(providerResponse);
		}
		catch (Exception e) {
			logger.info("Input parameters:cobrandId={},providerIds={}", obj);
			logger.error("Error while searching the fav icon sites:" + ExceptionUtils.getFullStackTrace(e));
			if (e instanceof InvalidSearchStringException)
				return "{}";
			throw e;
		}
		finally {
			SearchSiteLocalStorage.unSearchSiteCriteria();
			ContextAccessorUtil.unsetContext();
			PersistenceContext.getInstance().closeEntityManager("dom");
		}
		Date eDate = new Date();
		logger.info("Total time taken for site fav API:" + (eDate.getTime() - sDate.getTime())  + " milliSec : cobrandId={},providerIds={},fieldName={}", obj );
		logger.debug("Final response for site fav:" + providerResponse);
		return providerResponse == null ? "{}" : providerResponse;
	}

	private String processResponse(String response) {
		JsonElement jsonElement = new JsonParser().parse(response);
		JsonElement providerElement = jsonElement.getAsJsonObject().get("provider");
		JsonArray providerArray = providerElement.getAsJsonArray();
		JsonArray nArray = new JsonArray();
		JsonObject nElement = new JsonObject();
		int size = providerArray.size();
		for (int i = 0; i < size; i++) {
			JsonElement curElement = providerArray.get(i);
			JsonObject currObj = curElement.getAsJsonObject();
			JsonElement nameEl = currObj.get("name");
			boolean addEl = true;
			if (nameEl != null) {
				String name = nameEl.getAsString();
				if (name != null && "-1".equals(name)) {
					addEl = false;
				}
			}
			if (addEl){
				JsonElement containerEl = currObj.get("containerAttributes");
				if (containerEl != null) {
					currObj.remove("containerAttributes");
					containerEl = new JsonParser().parse(containerEl.getAsString().replaceAll("\\\\", ""));
					currObj.add("containerAttributes", containerEl);
				}
				nArray.add(curElement);
			}
		}
		if (nArray.size() > 0)
			nElement.add("provider", nArray);
		return nElement.toString();
	}
	
	private boolean isURLMatch(String url) throws Exception {
		URLMatcher siteURLMatcher = null;
		try {
		
			siteURLMatcher = new URLMatcher();
			Matcher urlMatcher = siteURLMatcher.getMatcher();
			if (url != null && urlMatcher.reset(url.trim()).matches()) {
				return true;
			}
		} finally {
			if (siteURLMatcher != null) {
				siteURLMatcher.reset();
			}
		}
		return false;
	}

	private String siteNameFromURL(String url) throws Exception {
		URLMatcher siteURLMatcher = null;
		String siteName = url;
		try {
			
			siteURLMatcher = new URLMatcher();
			Matcher urlMatcher = siteURLMatcher.getMatcher();
			if (url != null && urlMatcher.reset(url.trim()).matches()) {
				siteName = urlMatcher.group("site");
				siteName = siteName.toLowerCase();
			}
		} finally {
			if (siteURLMatcher != null) {
				siteURLMatcher.reset();
			}
		}
		return siteName;
	}
}

