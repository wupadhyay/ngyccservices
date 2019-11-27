/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.elasticsearch;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * @author
 *  apunekar
 */

public class ElasticSearchQueryBuilder {
	


	/** The final query. */
	private JsonObject finalQuery;
	
	/** The should array. */
	private JsonArray shouldArray_url;
	
	private JsonArray shouldArray_name;
	
	private JsonArray mustArray;

	/**
	 * Instantiates a new elastic search query builder.
	 */
	public ElasticSearchQueryBuilder() {
		initializeQuery();
	}



	/**
	 * Initialize query.
	 */
	private void initializeQuery() {

		shouldArray_name = new JsonArray();
		JsonObject shouldObjName = new JsonObject();
		shouldObjName.add("should", shouldArray_name);
		
		JsonObject boolObjName = new JsonObject();
		boolObjName.add("bool", shouldObjName);
		
		shouldArray_url = new JsonArray();
		JsonObject shouldObjUrl = new JsonObject();
		shouldObjUrl.add("should", shouldArray_url);
		
		JsonObject boolObjUrl = new JsonObject();
		boolObjUrl.add("bool", shouldObjUrl);
				
		mustArray = new JsonArray();
		mustArray.add(boolObjUrl);
		mustArray.add(boolObjName);
		JsonObject mustObj = new JsonObject();
		mustObj.add("must", mustArray);
		
		JsonObject boolObj = new JsonObject();
		boolObj.add("bool", mustObj);

		finalQuery = new JsonObject();
		finalQuery.add("query", boolObj);

	}


	

	/**
	 * Should.
	 *
	 * @param criteriaName the criteria name
	 * @param key the key
	 * @param value the value
	 * @return the elastic search query builder
	 */
	public ElasticSearchQueryBuilder should(String criteriaName, String key, String value,boolean isUrlPassed ) {

		if (getRespectiveArray(isUrlPassed) != null) {
			JsonObject crit = new JsonObject();
			crit.add(criteriaName, getCriteria(key, value));
			getRespectiveArray(isUrlPassed).add(crit);
		}

		return this;
	}

	/**
	 * Gets the criteria.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the criteria
	 */
	public JsonObject getCriteria(String key, String value) {
		JsonObject queryObj = new JsonObject();
		queryObj.addProperty(key, value);
		return queryObj;
	}
	
	/**
	 * Resets the query to have initial data.
	 *
	 * @return the elastic search query builder
	 */
	public ElasticSearchQueryBuilder reset()
	{
		initializeQuery();
		return this;
	}
	

	/**
	 * Returns final query formed
	 *
	 * @return the string
	 */
	public String build() {
		if (finalQuery != null ) {
			return finalQuery.toString();
		}
		return "";
	}
	
	private JsonArray getRespectiveArray(boolean isUrlPassed)
	{
		if(isUrlPassed)
			return shouldArray_url;
		else
			return shouldArray_name;
	}
	


}
