/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.pengine.recurringtransaction.ReconsiledItemAccountTransactionsData;
import com.yodlee.ycc.dapi.bean.Account360;
import com.yodlee.ycc.dapi.bean.Holding;
import com.yodlee.ycc.dapi.bean.Holding360;
import com.yodlee.ycc.dapi.bean.ItemAccCacheTransaction;
import com.yodlee.ycc.dapi.bean.ItemAccountHolding;
import com.yodlee.ycc.dapi.bean.ItemAccountTransaction;
import com.yodlee.ycc.dapi.bean.ItemAccountInfo;
import com.yodlee.ycc.dapi.bean.ReconDataInfo;
import com.yodlee.ycc.dapi.bean.Transaction;
import com.yodlee.ycc.dapi.bean.Transaction360;
import com.yodlee.ycc.dapi.controllers.ReconController;
import com.yodlee.ycc.dapi.task.SplunkOutput;
import com.yodlee.ycc.dapi.task.SplunkReportExecutor;
import com.yodlee.ycc.dapi.utils.MiscUtil;
import com.yodlee.ycc.dapi.utils.ReconDataAccessor;

@Service
public class ReconService {
	
	

	private static final Logger logger = LoggerFactory.getLogger(ReconService.class);
	

	
	/*
	 * The following method is used to retrieve Account Details
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountDetails(final Long itemAccoundId, final Long cobrandId, final String asOfDate) throws Exception{
		logger.debug("ReconService.getItemAccountDetails : Retriving ItemAccountDetails.");
		final List<ItemAccountInfo> itemAccounts = ReconDataAccessor.getItemAccountDetails(cobrandId, itemAccoundId, asOfDate);
		
		final Account360 account360 = new Account360();
		account360.setItemAccountId(itemAccoundId)
		.setItemAccountInfos(itemAccounts);
		
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String ret = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountDetails : final json string :"+ret);
		return ret;
	}
	
	
	/*
	 * The following method is used to retrieve Holding Details
	 *
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountHoldings(final Long itemAccoundId, final Long cobrandId, String asOfDate) throws Exception{
		logger.debug("ReconService.getItemAccountHoldings : Retriving Holding Details.");
		final List<Holding> holdings = ReconDataAccessor.getHoldingDetails(cobrandId, itemAccoundId);
		
		String cobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "extract");
		logger.debug("ReconService.getItemAccountHoldings : Retrieving latest extracts date");
		String latestExtractDate = null;
		try{
			 latestExtractDate = asOfDate != null ? asOfDate : 
				 ReconDataAccessor.latestExtractDate.get(cobrandName);
			if(latestExtractDate == null){
				logger.info("Latest Extarct date found empty :: Revaluating");
				latestExtractDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestExtractsDate(cobrandName),"timestamp");
				logger.info("Latest Extarct date Refetched :"+ latestExtractDate);
			}
		}catch(Exception ex){
			logger.error("Scheduler Error, not able to create cache for Latest Extarct Date : "+ExceptionUtils.getFullStackTrace(ex));
			latestExtractDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestExtractsDate(cobrandName),"timestamp");
			logger.info("Refetching Latest extract Date : "+latestExtractDate);
		}
		
		logger.debug("ReconService.getItemAccountHoldings : Retrieving previous extracts date");
		String previousExtractDate = null;
		try{
			previousExtractDate = asOfDate != null ? 
					ReconDataAccessor.getTimestamp(ReconDataAccessor.getPreviousExtractsDate(cobrandName, latestExtractDate),"timestamp")
					: ReconDataAccessor.previousExtractDate.get(cobrandName);
				if(previousExtractDate == null){
					previousExtractDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getPreviousExtractsDate(cobrandName, latestExtractDate),"timestamp");
				}
			
		}catch(Exception ex){
			logger.error("Scheduler Error, not able to create cache for Previous Extarct Date : "+ExceptionUtils.getFullStackTrace(ex));
			previousExtractDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getPreviousExtractsDate(cobrandName, latestExtractDate),"timestamp");
			logger.info("Refetching previous extract Date : "+previousExtractDate);
		}
		
		
		final Account360 account360 = new Account360();
		account360.setItemAccountId(itemAccoundId)
		.setLatestExtractDate(latestExtractDate)
		.setPreviousExtractDate(previousExtractDate)
		.setHoldings(holdings);
		
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String ret = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountHoldings : final json string :"+ret);
		return ret;
	}
	
	
	/*
	 * The following method is used to retrieve Transaction Details
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountTransactions(final Long itemAccoundId, final Long cobrandId, String latestExtractDate, 
			String previousExtractDate) throws Exception{
		
		logger.debug("ReconService.getItemAccountTransactions : Retriving Transactions.");
		final List<Transaction> transactions = ReconDataAccessor.getTransactionDetails(cobrandId, itemAccoundId, 
				latestExtractDate, previousExtractDate);
		
		final Account360 account360 = new Account360();
		account360.setItemAccountId(itemAccoundId)
		.setTransactions(transactions);
		
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String ret = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountTransactions : final json string :"+ret);
		return ret;
		
	}
	
	/*
	 * The following method is used to retrieve Extract Details,
	 * from Splunk Server against specified itemAccoundId for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountExtracts(final Long itemAccoundId, final Long cobrandId, String latestExtractDate) throws Exception{
		//String cobrandName ="ENV_PRD"; //test data
		String cobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "extract");
		logger.debug("ReconService.getItemAccount360 : Retrieving latest extracts date");
		//String latestExtractsDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestExtractsDate(cobrandName),"timestamp");
		
		logger.debug("ReconService.getItemAccount360 : Retriving Extracts.");
		String itemAccountExtracts = ReconDataAccessor.getItemAccountExtracts(latestExtractDate, itemAccoundId+"",cobrandName);
		if(itemAccountExtracts != null){
			itemAccountExtracts = itemAccountExtracts.replace("as_of_date", ReconDataAccessor.getDate(latestExtractDate));
			}
		
		final Account360 account360 = new Account360();
		account360.setExtracts(itemAccountExtracts);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String extracts = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountExtracts : final json string :"+extracts);
		logger.debug("ReconService.getItemAccountExtracts : Complete ReconDataAccessor to get data from Extracts.");
		
		return extracts;
	}
	
	/*
	 * The following method is used to retrieve Hadoop Details,
	 * from Splunk Server against specified itemAccoundId for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountHadoop(final Long itemAccoundId, final Long cobrandId, String latestHadoopDate) throws Exception{
		
		//String hadoopCobrandName = "envestnet_prod"; // test data
		String hadoopCobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "hadoop");
		/*logger.debug("ReconService.getItemAccount360 : Retrieving latest hadoop date");
		String latestHadoopDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestHadoopDate(hadoopCobrandName),"timestamp");*/
		
		logger.debug("ReconService.getItemAccount360 : Retriving Hadoop.");
		String itemAccountHadoop = ReconDataAccessor.getItemAccountHadoop(latestHadoopDate, itemAccoundId+"", hadoopCobrandName);
		if(itemAccountHadoop != null){
			itemAccountHadoop = itemAccountHadoop.replace("as_of_date", ReconDataAccessor.getDate(latestHadoopDate));
			}
		
		final Account360 account360 = new Account360();
		account360.setHadoop(itemAccountHadoop);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String hadoop = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountHadoop : final json string :"+hadoop);
		logger.debug("ReconService.getItemAccountHadoop : Complete ReconDataAccessor to get data from Extracts.");
		
		return hadoop;
		}
	
	
	
	
	/*
	 * The following method is used to retrieve Aggregated Account Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountAggHoldings(final Long itemAccoundId, final Long recAccountId, Long cobrandId, String requestType) throws Exception{
		
		logger.debug("ReconService.getItemAccountAggHoldings : Retriving Aggregated.");
		List<ItemAccountHolding> itemAccountHoldings = ReconDataAccessor.getItemAccountAggHoldings(cobrandId, itemAccoundId, recAccountId, requestType);
		logger.debug("ReconService.getItemAccountAggHoldings : Retrived Aggregated.");
		
		final Account360 account360 = new Account360();
		account360.setOltpHoldings(itemAccountHoldings);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String aggregated = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountAggregated : Complete ReconDataAccessor to get data for Aggregated.");
		return aggregated;
		
	}
	
	/*
	 * The following method is used to retrieve Aggregated transaction Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccountAggTransactions(final Long itemAccoundId, final Long recAccountId, final Long cobrandId, String requestType, String latestExtractDate,
			String previousExtractDate) throws Exception{
		
		logger.debug("ReconService.getItemAccountAggTransactions : Retriving Aggregated.");
		List<ItemAccountTransaction> itemAccountAggTranscations = ReconDataAccessor.getItemAccountAggTransactions(cobrandId, itemAccoundId, recAccountId, requestType, 
				latestExtractDate, previousExtractDate);
		logger.debug("ReconService.getItemAccountAggTransactions : Retrived Aggregated.");
		
		final Account360 account360 = new Account360();
		account360.setOltpTransactions(itemAccountAggTranscations);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String aggregated = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccountAggTransactions : Complete ReconDataAccessor to get data for Aggregated.");
		return aggregated;
		
	}
	
	
	/*
	 * The following method is used to retrieve Aggregated transaction Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getItemAccAggCashTransactions(final Long itemAccoundId, final Long recAccountId, final Long cobrandId, String requestType, 
			String latestExtractDate, String previousExtractDate) throws Exception{
		
		logger.debug("ReconService.getItemAccAggCacheTransactions : Retriving Aggregated.");
		List<ItemAccCacheTransaction> itemAccAggCacheTranscations = ReconDataAccessor.getItemAccAggCashTransactions(cobrandId, itemAccoundId,
				recAccountId, requestType, 
				latestExtractDate, previousExtractDate);
		logger.debug("ReconService.getItemAccAggCacheTransactions : Retrived Aggregated.");
		
		final Account360 account360 = new Account360();
		account360.setOltpCashTransactions(itemAccAggCacheTranscations);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String aggregated = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getItemAccAggCacheTransactions : Complete ReconDataAccessor to get data for Aggregated.");
		return aggregated;
		
	}

	
	/*
	 * The following method is used to retrieve Aggregated Account Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getHolding360Aggregated(final Long itemAccountId, final Long holdingId, final Long cobrandId) throws Exception{
		
		logger.debug("ReconService.getItemAggregated : Retriving Holding360 Aggregated.");
		List<Holding360> holding360Aggregated = ReconDataAccessor.getHolding360Aggregated(cobrandId, itemAccountId, holdingId);
		logger.debug("ReconService.getItemAggregated : Retrived Holding360 Aggregated.");
		
		final Account360 account360 = new Account360();
		account360.setHolding360Aggregated(holding360Aggregated);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String holdingAggregated = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Aggregated : Complete ReconDataAccessor to get data for Holding360Aggregated.");
		return holdingAggregated;
		
	}
	
	/*
	 * The following method is used to retrieve Reconciled Account Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getHolding360Reconciled(final Long recAccountId, final Long holdingId, final Long cobrandId) throws Exception{
		
		logger.debug("ReconService.getHolding360Reconciled : Retriving Holding360 Reconciled.");
		List<Holding360> holding360Reconciled = ReconDataAccessor.getHolding360Reconciled(cobrandId, recAccountId, holdingId);
		logger.debug("ReconService.getHolding360Reconciled : Retrived Holding360 Reconciled.");
		
		final Account360 account360 = new Account360();
		account360.setHolding360Reconciled(holding360Reconciled);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String holdingReconciled = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Reconciled : Complete ReconDataAccessor to get data for Holding360Reconciled.");
		return holdingReconciled;
		
	}
	
	/*
	 * The following method is used to retrieve Hadoop Account Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getHolding360Hadoop(final Long itemAccountId, final Long holdingId, final Long cobrandId, String latestHadoopDate) throws Exception{
		
		//String hadoopCobrandName = "envestnet_prod";
		String hadoopCobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "hadoop");
		logger.debug("ReconService.getHolding360Hadoop : Retriving Holding360 Hadoop.");
		String holdingHadoop = ReconDataAccessor.getHolding360Hadoop(holdingId+"", hadoopCobrandName, latestHadoopDate);
		logger.debug("ReconService.getHolding360Hadoop : Retrived Holding360 Hadoop.");
		final Account360 account360 = new Account360();
		account360.setHolding360Hadoop(holdingHadoop);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String holding360Hadoop = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Hadoop : Complete ReconDataAccessor to get data for getHolding360Hadoop.");
		return holding360Hadoop;
		
	}
	
	/*
	 * The following method is used to retrieve Extract Account Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getHolding360Extract(final Long itemAccountId, final Long holdingId, final Long cobrandId, String latestExtractDate) throws Exception{
		
		//String cobrandName = "ENV_PRD";
		String cobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "extract");
		logger.debug("ReconService.getHolding360Extract : Retriving Holding360 Extract.");
		String holdingExtract= ReconDataAccessor.getHolding360Extract(holdingId+"", cobrandName, itemAccountId+"", latestExtractDate);
		logger.debug("ReconService.getHolding360Extract : Retrived Holding360 Extract.");
		
		final Account360 account360 = new Account360();
		account360.setHolding360Extract(holdingExtract);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String holding360Extract = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Extract : Complete ReconDataAccessor to get data for getHolding360Extract.");
		return holding360Extract;
		
	}
	
	/*
	 * The following method is used to retrieve Transaction Details,
	 * from Splunk for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getHolding360Transaction(final Long itemAccountId, final Long holdingId, final Long cobrandId, String latestExtractDate) throws Exception{
		
		//String cobrandName = "ENV_PRD";
		String cobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "extract");
		logger.debug("ReconService.getHolding360Transaction : Retriving Holding360 Transaction.");
		String holdingTransaction= ReconDataAccessor.getHolding360Transaction(holdingId+"", cobrandName, itemAccountId+"", latestExtractDate);
		logger.debug("ReconService.getHolding360Transaction : Retrived Holding360 Transaction.");
		
		final Account360 account360 = new Account360();
		account360.setHolding360Transaction(holdingTransaction);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String holding360Transaction = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Transaction : Complete ReconDataAccessor to get data for getHolding360Transaction.");
		return holding360Transaction;
		
	}
	

	/*
	 * The following method is used to retrieve Aggregated Account Details,
	 * from DB for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param transactionId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getTransaction360Aggregated(final Long itemAccountId, final Long transactionId, final Long cobrandId) throws Exception{
		
		logger.debug("ReconService.getTransaction360Aggregated : Retriving Transaction360 Aggregated.");
		List<Transaction360> transaction360Aggregated = ReconDataAccessor.getTransaction360Aggregated(cobrandId, itemAccountId, transactionId);
		logger.debug("ReconService.getTransaction360Aggregated : Retrived Transaction360 Aggregated.");
		
		final Account360 account360 = new Account360();
		account360.setTransaction360Aggregated(transaction360Aggregated);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String transactionAggregated = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getTransaction360Aggregated : Complete ReconDataAccessor to get data for Transaction360Aggregated.");
		return transactionAggregated;
		
	}
	
	/*
	 * The following method is used to retrieve Hadoop Account Details,
	 * from Hadoop for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param holdingId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getTransaction360Hadoop(final Long itemAccountId, final Long transactionId, final Long cobrandId, String latestHadoopDate) throws Exception{
		
		//String hadoopCobrandName = "envestnet_prod";
		String hadoopCobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "hadoop");
		logger.debug("ReconService.getTransaction360Hadoop : Retriving Transaction360 Hadoop.");
		String transactionHadoop = ReconDataAccessor.getTransaction360Hadoop(transactionId+"", hadoopCobrandName, latestHadoopDate);
		logger.debug("ReconService.getTransaction360Hadoop : Retrived Transaction360 Hadoop.");
		
		final Account360 account360 = new Account360();
		account360.setTransaction360Hadoop(transactionHadoop);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String transaction360Hadoop = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getHolding360Hadoop : Complete ReconDataAccessor to get data for getTransaction360Hadoop.");
		return transaction360Hadoop;
		
	}
	
	/*
	 * The following method is used to retrieve Extract Account Details,
	 * from DB for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param transactionId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getTransaction360Extract(final Long itemAccountId, final Long transactionId, final Long cobrandId, String latestExtractDate) throws Exception{
		
		//String cobrandName = "ENV_PRD";
		String cobrandName = ReconDataAccessor.getCobrandName(cobrandId.toString(), "extract");
		logger.debug("ReconService.getTransaction360Extract : Retriving Transaction360 Extract.");
		String transactionExtract= ReconDataAccessor.getTransaction360Extract(transactionId+"", cobrandName, itemAccountId+"", latestExtractDate);
		logger.debug("ReconService.getTransaction360Extract : Retrived Transaction360 Extract.");
		
		final Account360 account360 = new Account360();
		account360.setTransaction360Extract(transactionExtract);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String transaction360Extract = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getTransaction360Extract : Complete ReconDataAccessor to get data for getTransaction360Extract.");
		return transaction360Extract;
		
	}
	
	/*
	 * The following method is used to retrieve Reconciled Account Details,
	 * from DB for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param transactionId
	 * @param cobrandId
	 * 
	 * @return String
	 */
	public String getTransaction360Reconciled(final Long recAccountId, final Long transactionId, final Long cobrandId) throws Exception{
		
		logger.debug("ReconService.getTransaction360Reconciled : Retriving Transaction360 Reconciled.");
		List<Transaction360> transaction360Reconciled = ReconDataAccessor.getTransaction360Reconciled(cobrandId, recAccountId, transactionId);
		logger.debug("ReconService.getTransaction360Reconciled : Retrived Transaction360 Reconciled.");
		
		final Account360 account360 = new Account360();
		account360.setTransaction360Reconciled(transaction360Reconciled);
		final ReconDataInfo reconDataInfo = new  ReconDataInfo();
		reconDataInfo.setAccount360(account360);
		final String transactionReconciled = toJSON(reconDataInfo);
		
		logger.debug("ReconService.getTransaction360Reconciled : Complete ReconDataAccessor to get data for Transaction360Reconciled.");
		return transactionReconciled;
		
	}
	
		
	/*
	 * The following method is used to create JSON string
	 * from supplied ReconDataInfo object
	 * 
	 * @param ReconDataInfo
	 * 
	 * @return String
	 */
	private static String toJSON(ReconDataInfo reconDataInfo) {
		
		final Gson gson = new Gson();
		String json = null;
		if(reconDataInfo == null || reconDataInfo.getAccount360() == null)
			return "{}";

		json = gson.toJson(reconDataInfo);
		json = "{\"reconDataInfo\":" + json + "}";
		return json;
	}

	
}
