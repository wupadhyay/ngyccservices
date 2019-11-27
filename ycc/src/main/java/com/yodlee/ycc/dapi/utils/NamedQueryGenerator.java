/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

package com.yodlee.ycc.dapi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class NamedQueryGenerator {
	private static final Path restMasterLibrary = FileSystems.getDefault()
			//.getPath( "D:\\vp4\\razor\\core\\newarch\\modules\\dapi\\src\\restLibrary");
			.getPath("/opt/ctier/restLibrary");


	public static String getQuery(String resourceName, String container,
			String[] queryParams) throws IOException {
		String query = "";
		String resource = restMasterLibrary.toString() + File.separator
				+ resourceName;
		System.out.println("Fetching Query from resource = " + resource);
		Path queryPath = null;
		if (container != null && container.length() > 0)
			queryPath = FileSystems.getDefault().getPath(
					resource + File.separator + resourceName + "_" + container);
		else
			queryPath = FileSystems.getDefault().getPath(
					resource + File.separator + resourceName);
		try (BufferedReader reader = Files.newBufferedReader(queryPath,
				Charset.defaultCharset())) {
			String tmpQuery = "";
			System.out.print("Query =  ");
			while ((tmpQuery = reader.readLine()) != null) {
				System.out.println(tmpQuery);
				if (queryParams != null && queryParams.length > 0) {
					query = applyQueryCriteria(tmpQuery, queryParams);
				} else {
					query = tmpQuery;
					break;
				}
			}

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

	public static void main(String a[]) {
		try {
			// Test Case 1 - transactions for all containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("transactions", null, null);
			// Test Case 2 - transactions for bank no critera specified all
			// defaults enabled.
			NamedQueryGenerator.getQuery("transactions", "bank", null);
			// Test Case 3 - transactions for credits containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("transactions", "credits", null);
			// Test Case 4 - transactions for investment containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("transactions", "Investment", null);
			// Test Case 5 - transactions for insurance containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("transactions", "Insurance", null);
			// Test Case 6 - transactions for bills containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("transactions", "bills", null);
			// Test Case 7 - transactions for all containers for all Days
			String[] queryParam7 = new String[] { "lastDays all" };
			System.out.println(NamedQueryGenerator.getQuery("transactions",
					null, queryParam7));
			// Test Case 7 - transactions for all containers for all Days
			String[] queryParam8 = new String[] { "lastDays all",
					"where baseType eq DEBIT" };
			System.out.println(NamedQueryGenerator.getQuery("transactions",
					null, queryParam8));
			// Test Case 8 - transactions for all containers for last 90 Days
			// and baseType is DEBIT
			String[] queryParam9 = new String[] { "las90days",
					"where baseType eq CREDIT" };
			System.out.println(NamedQueryGenerator.getQuery("transactions",
					"credits", queryParam9));

			// Test Case 9 - accounts for all
			NamedQueryGenerator.getQuery("accounts", null, null);
			// Test Case 10 - transactions for bank no critera specified all
			// defaults enabled.
			NamedQueryGenerator.getQuery("accounts", "bank", null);
			// Test Case 11 - transactions for credits containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("accounts", "credits", null);
			// Test Case 12 - transactions for investment containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("accounts", "Investment", null);
			// Test Case 13 - transactions for insurance containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("accounts", "Insurance", null);
			// Test Case 14 - transactions for bills containers no critera
			// specified all defaults enabled.
			NamedQueryGenerator.getQuery("accounts", "bills", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}