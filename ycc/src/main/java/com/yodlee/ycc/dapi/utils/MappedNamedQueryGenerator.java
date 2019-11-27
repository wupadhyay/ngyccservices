/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

package com.yodlee.ycc.dapi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yodlee.nextgen.exceptions.core.InvalidYodleeServiceQueryException;
import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.rest.api.queries.PreCannedQueryMap;

public class MappedNamedQueryGenerator 
{

	public static String getQuery(String resourceName, String container,
			String[] queryParams) 
	{
		String query = "";
		String preProcessedQuery = "";
		if (container != null && container.length() > 0)
		{
			System.out.println("Fetching Query for resource = " + resourceName + " & container is = " + container);
			preProcessedQuery = PreCannedQueryMap.preCannedQueryMap.get(resourceName + "_" + container);
			if(preProcessedQuery == null) return null;
		}
		else
			preProcessedQuery = PreCannedQueryMap.preCannedQueryMap.get(resourceName);
		System.out.println("preProcessedQuery =  " + preProcessedQuery);
		
		if (queryParams != null && queryParams.length > 0) 
		{
		query = applyQueryCriteria(preProcessedQuery, queryParams);
		} else 
		{
					query = preProcessedQuery;
		}
		return query;
	}
	
	private static String applyQueryCriteria(String query, String[] queryParams) {
		StringBuilder tmpQuery = new StringBuilder(query);
		for (String s : queryParams) {
			tmpQuery.append(" " + s);
		}
		return new String(tmpQuery);
	}
	
	private static void getAccounts(Long id, String container) throws InvalidYodleeServiceQueryException, ApiException
	{
		Map<String,String> requestMap = new HashMap<String,String>();
		if(id !=null)
		requestMap.put("id", id.toString());
		if(container!=null)
		requestMap.put("container", container);
		System.out.println("In Accounts Controller");
		String query = "";
		List<String> queryParams= new ArrayList<String>();
		if(requestMap.size() > 0)
		{
			if(!(requestMap.containsKey("id") || requestMap.containsKey("container")))
				throw new InvalidYodleeServiceQueryException("Invalid param");
			if (id !=null  && container != null && id.longValue() > 0)
			{
				System.out.println("Found id = " + id);
				queryParams.add("where id eq " + id.longValue());
			}
			else if (container == null) 
				throw new InvalidYodleeServiceQueryException("param [container] needed with [id]");
			System.out.println(" requestParamterMap" + requestMap.toString() + ", size = " + requestMap.size() );
			if(container != null && container.length() > 1)
				container = container.toLowerCase();	
			String[] queryCriteria = queryParams.toArray(new String[queryParams.size()]);
			
			query = MappedNamedQueryGenerator.getQuery("accounts", container,queryCriteria);
		}
		else
			query = MappedNamedQueryGenerator.getQuery("accounts", null,null);
		System.out.println(" Generated query = " + query);
		if(query == null) throw new ApiException("Y800", new Object[] {"container"});
	}
	
	private static void getTransactions(String container, String baseType, String merchant, Long accountId, String fromDate, String toDate, String category, Long skip, Long top) throws InvalidYodleeServiceQueryException, ApiException
	{
		Map requestMap = new HashMap();
		if(container !=null)
		requestMap.put("container", container.toString());
		if(baseType !=null)
			requestMap.put("baseType", baseType.toString());
		if(merchant !=null)
			requestMap.put("merchant", merchant.toString());
		if(accountId !=null)
			requestMap.put("accountId", accountId.longValue());
		if(fromDate !=null)
			requestMap.put("fromDate", fromDate.toString());
		if(toDate !=null)
			requestMap.put("toDate", toDate.toString());
		if(category !=null)
			requestMap.put("category", category.toString());
		if(skip !=null)
			requestMap.put("skip", skip.longValue());
		if(top !=null)
			requestMap.put("top", top.longValue());
		System.out.println("In Transaction Controller");
		String query = "";
		if(requestMap.size() > 0)
		{
			// Check if the incoming Parameters are in the list above, since we do not have a restful model hardcode these rules.
			if(!(requestMap.containsKey("baseType") 	|| 
				 requestMap.containsKey("container") ||
				 requestMap.containsKey("merchant") 	||
				 requestMap.containsKey("accountId") ||
				 requestMap.containsKey("fromDate")  ||
				 requestMap.containsKey("toDate")    ||
				 requestMap.containsKey("category")  ||
				 requestMap.containsKey("skip")      ||
				 requestMap.containsKey("top")		))
				throw new ApiException("Y901");
			
			System.out.println(" Found Query Params ! Creating query Param List" + requestMap.entrySet().toString());	
			List<String> queryParams= new ArrayList<String>();
			if (accountId != null && accountId.longValue() > 0) 
			{
				System.out.println("Found accountId = " + accountId.toString());
				queryParams.add("where account.id eq " + accountId);
			} 
			if (baseType != null && baseType.length() > 0 && (baseType.equals("DEBIT") || baseType.equals("CREDIT") || baseType.equals("UNKNOWN")))
			{
				System.out.println("Found baseType = " + baseType);
				queryParams.add("where baseType eq " + baseType.trim() + " ");	
			}
			if (merchant != null && merchant.length() > 0)
			{
				System.out.println("Found merchant  = " + merchant);
				if (queryParams.size() >= 1)
					queryParams.add("and merchantName eq " + merchant );
				else
					queryParams.add("where merchantName eq " + merchant.trim() + " ");
				
			}
			if (fromDate != null && fromDate.toString().length() > 0)
			{
				System.out.println("Found fromDate = " + fromDate);
				if (queryParams.size() >= 1)
					queryParams.add("and date le " + fromDate + " ");
				else
					queryParams.add("where date le " + fromDate + " ");
			}
			if (category != null && category.length() > 0)
			{
				System.out.println("Found category = " + category);
				if (queryParams.size() >= 1)
					queryParams.add("and category eq " + category );
				else
					queryParams.add("where category eq " + category.trim() + " ");
			}
			if (toDate != null && toDate.toString().length() > 0)
			{
				System.out.println("Found toDate = " + toDate);
				if (queryParams.size() >= 1)
					queryParams.add("and date ge " + toDate + " ");
				else
					queryParams.add("where date ge " + toDate + " ");
			}
			if (skip !=null && skip > 0 && skip < 500)
			{
				System.out.println("Found skip = " + skip);
				queryParams.add("skip " + skip*2 + " ");
				
			}
			if (top !=null && top > 0 && top <= 500)
			{
				System.out.println("Found top = " + top);
				queryParams.add("top " + top*2 + " ");
				//response.addHeader("Link", request.getRequestURI().toString()+"?&skip=" + top + ";rel=next");
			}
			if(container != null && container.length() > 1)
				container = container.toLowerCase();
			String[] queryCriteria = queryParams.toArray(new String[queryParams.size()]);
			query = MappedNamedQueryGenerator.getQuery("transactions", container,queryCriteria);
		}
		else
		{
			System.out.println(" No Query Params !");	
			query = MappedNamedQueryGenerator.getQuery("transactions", null, null);
		}
		System.out.println(" Generated query = " + query);
		if(query == null) throw new ApiException("Y800", new Object[] {"Container"});
	}
	
	
	public static void main(String a[]) {
		new PreCannedQueryMap();
		try {
			// Test Case 1 - transactions for all containers no critera
			// specified all defaults enabled.
			/*MappedNamedQueryGenerator.getQuery("transactions", null, null);
			// Test Case 2 - transactions for bank no critera specified all
			// defaults enabled.
			MappedNamedQueryGenerator.getQuery("transactions", "bank", null);
			// Test Case 3 - transactions for credits containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("transactions", "credits", null);
			// Test Case 4 - transactions for investment containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("transactions", "investment", null);
			// Test Case 5 - transactions for insurance containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("transactions", "insurance", null);
			// Test Case 6 - transactions for bills containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("transactions", "bills", null);
			// Test Case 7 - transactions for all containers for all Days
			String[] queryParam7 = new String[] { "lastDays all" };
			System.out.println(MappedNamedQueryGenerator.getQuery("transactions",
					null, queryParam7));
			// Test Case 7 - transactions for all containers for all Days
			String[] queryParam8 = new String[] { "lastDays all",
					"where baseType eq DEBIT" };
			System.out.println(MappedNamedQueryGenerator.getQuery("transactions",
					null, queryParam8));
			// Test Case 8 - transactions for all containers for last 90 Days
			// and baseType is DEBIT
			String[] queryParam9 = new String[] { "las90days",
					"where baseType eq CREDIT" };
			System.out.println(MappedNamedQueryGenerator.getQuery("transactions",
					"credits", queryParam9));*/

			// Test Case 9 - accounts for all
			/*MappedNamedQueryGenerator.getQuery("accounts", null, null);
			// Test Case 10 - transactions for bank no critera specified all
			// defaults enabled.
			MappedNamedQueryGenerator.getQuery("accounts", "bank", null);
			// Test Case 11 - transactions for credits containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("accounts", "credits", null);
			// Test Case 12 - transactions for investment containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("accounts", "investment", null);
			// Test Case 13 - transactions for insurance containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("accounts", "insurance", null);
			// Test Case 14 - transactions for bills containers no critera
			// specified all defaults enabled.
			MappedNamedQueryGenerator.getQuery("accounts", "bills", null);*/
			
			/*MappedNamedQueryGenerator.getAccounts(null, "bank");
			MappedNamedQueryGenerator.getAccounts(null, null);
			try {
				MappedNamedQueryGenerator.getAccounts(209427l, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MappedNamedQueryGenerator.getAccounts(null, "bills");
			MappedNamedQueryGenerator.getAccounts(null,"loga");*/
			
			MappedNamedQueryGenerator.getTransactions("bank",null, null, null, null, null, null, null, null);
			MappedNamedQueryGenerator.getTransactions("credits",null, null, null, null, null, null, null, null);
			MappedNamedQueryGenerator.getTransactions(null, null, null, null, null, null, null, null, null);
			try {
				MappedNamedQueryGenerator.getTransactions("bank", "DEBIT", null, null, null, null, null, null, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MappedNamedQueryGenerator.getTransactions("investment", "CREDIT", "unkown", null, null, null, null, 0l, 100l);
			MappedNamedQueryGenerator.getTransactions("insurance", "UNKNOWN", "Target", null, "2014-07-30", "2014-05-01", null, 10l, 30l);
			MappedNamedQueryGenerator.getTransactions("bank", "CREDIT", null, null, null, null, "Unknown", 9l, 15l);
			MappedNamedQueryGenerator.getTransactions("bank", "UNKNOWN", null, null, null, null, "Uncategorgized", null, 15l);
			//MappedNamedQueryGenerator.getAccounts(null, "bills");
			//MappedNamedQueryGenerator.getAccounts(null,"loga");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
