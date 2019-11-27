/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.interceptor.TransactionAttribute;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yodlee.dom.tx.TransactionMgmt;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
//import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.ycc.dapi.bean.Holding;
import com.yodlee.ycc.dapi.bean.Holding360;
import com.yodlee.ycc.dapi.bean.ItemAccCacheTransaction;
import com.yodlee.ycc.dapi.bean.ItemAccountHolding;
import com.yodlee.ycc.dapi.bean.ItemAccountInfo;
import com.yodlee.ycc.dapi.bean.ItemAccountTransaction;
import com.yodlee.ycc.dapi.bean.Transaction;
import com.yodlee.ycc.dapi.bean.Transaction360;
import com.yodlee.ycc.dapi.task.SplunkOutput;
import com.yodlee.ycc.dapi.task.SplunkReportExecutor;

public final class ReconDataAccessor {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ReconDataAccessor.class);
	private static HashMap<String, String> hadoopCobrand = null;
	private static HashMap<String, String> extractCobrand = null;
	private static HashMap<String, String> latestHadoopDate = null;
	public static HashMap<String, String> latestExtractDate = null;
	public static HashMap<String, String> previousExtractDate = null;
	
	/*
	 * The following method is used to retrieve Item Account Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<ItemAccountInfo>
	 */
	public static List<ItemAccountInfo> getItemAccountDetails (final Long cobId, final Long itemAccountId, 
			final String asOfDate){
		//updateCobrandInfo();
		List<ItemAccountInfo> itemAccountInfos = null;
		Long confirmedItemAccountID = itemAccountId;
		
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
		
		logger.debug("getItemAccountDetails:Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
        	logger.debug("ReconService.getItemAccountHoldings : Retrieving latest hadoop date");
    		String hadoopCobrandName = ReconDataAccessor.getCobrandName(cobId.toString(), "hadoop");
    		String latestHadoopDate = null;
    		try{
    			latestHadoopDate = asOfDate != null ? asOfDate : ReconDataAccessor.latestHadoopDate.get(hadoopCobrandName);
                if(latestHadoopDate == null){
                	logger.info("LatestHadoopDate Found Empty :: Revaluating");
                	latestHadoopDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestHadoopDate(hadoopCobrandName),"timestamp");
                	logger.info("Revaluated latest Hadoop date : "+ latestHadoopDate);
                }
    		}catch(Exception ex){
    			logger.error("Scheduler Error, not able to create cache for Latest Hadoop Date : "+ExceptionUtils.getFullStackTrace(ex));
    			latestHadoopDate = ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestHadoopDate(hadoopCobrandName),"timestamp");
    			logger.info("Refetching Latest Hadoop Date : "+latestHadoopDate);
    		}
    		
    		JdbcTemplate template = txn.getJdbcTemplate();
               // Try confirms if the itemAccountId is aggregated or reconciled and assigns aggregatedItemAccountId to confirmedItemAccountID
               try{
            	   Long itemId = null;
                   Long itemIdStatus = null;
                   Object[] args = new Object[] { itemAccountId };
                   itemId = template.queryForObject(MiscUtil.getQuery("itemAccountGetPrefKey"), args, Long.class);
                   Object[] args1 = new Object[] { itemId };
                   itemIdStatus = template.queryForObject(MiscUtil.getQuery("itemAccountCheckAccountStatus"), args1, Long.class);
                   if(itemIdStatus == 7){
                	   confirmedItemAccountID = itemId; 
                   }
               // 1 reconciled // 7 aggregated
               }catch (Exception e){
            	   logger.info("No prefKeyValue for itemAccountID :"+ itemAccountId);
               }
               
               itemAccountInfos = template.query(MiscUtil.getQuery("item_account_details") , 
            		   new BeanPropertyRowMapper<ItemAccountInfo>(ItemAccountInfo.class), new Long [] {confirmedItemAccountID, confirmedItemAccountID, confirmedItemAccountID});
               logger.debug("getItemAccountDetails:Ending the transaction");
               for(ItemAccountInfo itemAccInfo :itemAccountInfos){
            	  
            	   itemAccInfo.setLatestHadoopDate(latestHadoopDate);
               }
               return itemAccountInfos;
        } catch (Exception e) {
               logger.error("Exception while getting the item_account_details:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               //ContextAccessorUtil.unsetContext();
               ContextAccessorUtil.unsetContext();
        }
		return null;
	}

	/*
	 * The following method is used to retrieve Holding Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<Holding>
	 */
	public static List<Holding> getHoldingDetails (final Long cobId, final Long itemAccountId){
		
		List<Holding> holdings = null;
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getHoldingDetails :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate(); 
               holdings = template.query(MiscUtil.getQuery("holding_details") , 
            		   new BeanPropertyRowMapper<Holding>(Holding.class), new Long [] { itemAccountId });
               logger.debug("getHoldingDetails: Ending the transaction");
               return holdings;
        } catch (Exception e) {
               logger.error("Exception while getting the hodling_details:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
              ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	/*
	 * The following method is used to retrieve Transaction Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<Transaction>
	 */
	public static List<Transaction> getTransactionDetails (final Long cobId, final Long itemAccountId, String latestExtractDate,
			String previousExtractDate){
		
		List<Transaction> transactions = null;
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getTransactionDetails :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
        	String transactionQuery =  MiscUtil.getQuery("transaction_details").replaceAll("dateLatest", latestExtractDate)
        			.replaceAll("datePrevious", previousExtractDate); 
               JdbcTemplate template = txn.getJdbcTemplate(); 
               transactions = template.query(transactionQuery , 
            		   new BeanPropertyRowMapper<Transaction>(Transaction.class), new Long [] { itemAccountId });
               logger.debug("getTransactionDetails: Ending the transaction");
               return transactions;
        } catch (Exception e) {
               logger.error("Exception while getting the transaction_details:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	
	
	/*
	 * The following method is used to retrieve Aggregated Account Holding Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<ItemAccount360>
	 */
	public static List<ItemAccountHolding> getItemAccountAggHoldings(final Long cobId, 
			final Long itemAccountId, final Long recAccountId, String requestType){
		
		List<ItemAccountHolding> itemAccountHoldings = null;
		setCobrandContext(cobId);	
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getItemAccountAggHoldings :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate();
               MapSqlParameterSource parameters = new MapSqlParameterSource();
   			   parameters.addValue("id", itemAccountId);
   			   NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
               if("aggregated".equals(requestType)){
            	   parameters.addValue("id", itemAccountId);
            	   }
               else{
            	   parameters.addValue("id", recAccountId);
            	   }
               itemAccountHoldings = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountAggHoldings"),parameters, new BeanPropertyRowMapper(ItemAccountHolding.class));
               logger.debug("getItemAccountAggHoldings: Ending the transaction");
               return itemAccountHoldings;
        } catch (Exception e) {
               logger.error("Exception while getting the getItemAccountAggHoldings:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
              ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	
	/*
	 * The following method is used to retrieve Aggregated Account Transaction Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<ItemAccount360>
	 */
	public static List<ItemAccountTransaction> getItemAccountAggTransactions(final Long cobId, 
			final Long itemAccountId, final Long recAccountId, String requestType, String latestExtractDate,
			String previousExtractDate){
		
		List<ItemAccountTransaction> itemAccountTransaction = null;
		setCobrandContext(cobId);	
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getItemAccountAggTransactions :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
        	
        		JdbcTemplate template = txn.getJdbcTemplate();
    			MapSqlParameterSource parameters = new MapSqlParameterSource();
    			//parameters.addValue("id", itemAccountId);
    			parameters.addValue("dateLatest", latestExtractDate);
    			parameters.addValue("datePrevious", previousExtractDate);
    			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
    			if("aggregated".equals(requestType)){
    				parameters.addValue("id", itemAccountId);
    				//itemAccountTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountAggTransactions"), parameters,new BeanPropertyRowMapper(ItemAccountTransaction.class));
    			}
    			else{
    				parameters.addValue("id", recAccountId);
    				//itemAccountTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountReconTransactions"), parameters,new BeanPropertyRowMapper(ItemAccountTransaction.class));
    			}
    			itemAccountTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountAggTransactions"), parameters,new BeanPropertyRowMapper(ItemAccountTransaction.class));
    			logger.debug("getItemAccountAggTransactions: Ending the transaction");
               return itemAccountTransaction;
        } catch (Exception e) {
               logger.error("Exception while getting the getItemAccountAggTransactions:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	        }
		return null;
	}
	
	
	/*
	 * The following method is used to retrieve Aggregated Account Cash Transaction Details,
	 * from DB for Account360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * 
	 * @return List<ItemAccount360>
	 */
	public static List<ItemAccCacheTransaction> getItemAccAggCashTransactions(final Long cobId, 
			final Long itemAccountId, final Long recAccountId, String requestType, String latestExtractDate, String previousExtractDate){
		
		List<ItemAccCacheTransaction> itemAccCacheTransaction = null;
		setCobrandContext(cobId);	
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getItemAccAggCashTransactions :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {    
        		JdbcTemplate template = txn.getJdbcTemplate();
    			MapSqlParameterSource parameters = new MapSqlParameterSource();
    			//parameters.addValue("id", itemAccountId);
    			parameters.addValue("dateLatest", latestExtractDate);
    			parameters.addValue("datePrevious", previousExtractDate);
    			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
    			if("aggregated".equals(requestType)){
    				parameters.addValue("id", itemAccountId);
    				//itemAccCacheTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountAggCacheTransactions"), parameters,new BeanPropertyRowMapper(ItemAccCacheTransaction.class));	
    			}else{
    				parameters.addValue("id", recAccountId);
    				//itemAccCacheTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountReconCashTransactions"), parameters,new BeanPropertyRowMapper(ItemAccCacheTransaction.class));
    			}
    			itemAccCacheTransaction = namedParameterJdbcTemplate.query(MiscUtil.getQuery("itemAccountAggCacheTransactions"), parameters,new BeanPropertyRowMapper(ItemAccCacheTransaction.class));
    			logger.debug("getItemAccAggCashTransactions: Ending the transaction");
               return itemAccCacheTransaction;
        } catch (Exception e) {
               logger.error("Exception while getting the getItemAccAggCashTransactions:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	  
        }
		return null;
	}
	
	
	/*
	 * The following method is used to retrieve Holding360 Aggregated Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * @param holdingId
	 * 
	 * @return List<Holding360>
	 */
	public static List<Holding360> getHolding360Aggregated(final Long cobId, 
			final Long itemAccountId, final Long holdingId){
		
		List<Holding360> holding360Aggregated = null;
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getHolding360Aggregated :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate(); 
               holding360Aggregated = template.query(MiscUtil.getQuery("holding360_aggregated_data") , 
            		   new BeanPropertyRowMapper<Holding360>(Holding360.class),new Long [] {itemAccountId, holdingId});
               logger.debug("getHolding360Aggregated: Ending the transaction");
               return holding360Aggregated;
        } catch (Exception e) {
               logger.error("Exception while getting the getHolding360Aggregated:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	/*
	 * The following method is used to retrieve Holding360 Reconciled Details,
	 * from DB for Holding360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * @param holdingId
	 * 
	 * @return List<Holding360>
	 */
	public static List<Holding360> getHolding360Reconciled(final Long cobId, 
			final Long recAccountId, final Long holdingId){
		
		List<Holding360> holding360Reconciled = null;
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getHolding360Reconciled :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate(); 
               holding360Reconciled = template.query(MiscUtil.getQuery("holding360_reconciled_data") , 
            		   new BeanPropertyRowMapper<Holding360>(Holding360.class),new Long [] {recAccountId, holdingId});
               logger.debug("getHolding360Reconciled: Ending the transaction");
               return holding360Reconciled;
        } catch (Exception e) {
               logger.error("Exception while getting the getHolding360Reconciled:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	/*
	 * The following method is used to retrieve Transaction360 Aggregated Details,
	 * from DB for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * @param holdingId
	 * 
	 * @return List<Transaction360>
	 */
	public static List<Transaction360> getTransaction360Aggregated(final Long cobId, 
			final Long itemAccountId, final Long transactionId){
		
		List<Transaction360> transaction360Aggregated = null;
		//ContextAccessorUtil.setContext(cobId, 0, null);
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getTransaction360Aggregated :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate(); 
               transaction360Aggregated = template.query(MiscUtil.getQuery("transaction360_aggregated_data") , 
            		   new BeanPropertyRowMapper<Transaction360>(Transaction360.class),new Long [] {itemAccountId, transactionId});
               logger.debug("getTransaction360Aggregated: Ending the transaction");
               return transaction360Aggregated;
        } catch (Exception e) {
               logger.error("Exception while getting the getTransaction360Aggregated:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
               ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	/*
	 * The following method is used to retrieve Holding360 Reconciled Details,
	 * from DB for Transaction360 view
	 * 
	 * @param itemAccoundId
	 * @param cobId
	 * @param transactionId
	 * 
	 * @return List<Transaction360>
	 */
	public static List<Transaction360> getTransaction360Reconciled(final Long cobId, 
			final Long recAccountId, final Long transactionId){
		
		List<Transaction360> transaction360Reconciled = null;
		setCobrandContext(cobId);
		TransactionMgmt txn = new TransactionMgmt();
        logger.debug("getTransaction360Reconciled :Beginning the transaction");
        txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobId));
        try {
               JdbcTemplate template = txn.getJdbcTemplate(); 
               transaction360Reconciled = template.query(MiscUtil.getQuery("transaction360_reconciled_data") , 
            		   new BeanPropertyRowMapper<Transaction360>(Transaction360.class),new Long [] {recAccountId, transactionId});
               logger.debug("getTransaction360Reconciled: Ending the transaction");
               return transaction360Reconciled;
        } catch (Exception e) {
               logger.error("Exception while getting the Transaction360Reconciled:" + ExceptionUtils.getFullStackTrace(e));
        }finally{
               txn.endTransaction(false);   
              ContextAccessorUtil.unsetContext();
        	
        }
		return null;
	}
	
	/*
	 * The following method is used to connect to Splunk Server,
	 * and retrieve extarct and hadoop information
	 * 
	 * @param query
	 * 
	 * 
	 * @return String
	 */
	public static String getSplunkData(String query) throws Exception{

		logger.debug("getSplunkData : retrieved data from splunk");
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

		SplunkReportExecutor reportExecutor  = new SplunkReportExecutor(null,null, query, "recon", "1000",false);
		Future<SplunkOutput> future = executor.submit(reportExecutor);

		String splunkResp=null;
		SplunkOutput oneOutPut =future.get();
		if(!oneOutPut.getFailed()){
			splunkResp = oneOutPut.getSplunkResponse();
		}

		executor.shutdown();
		if(splunkResp==null){
			return null;
		}

		JsonElement jelement = new JsonParser().parse(splunkResp);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jsonArray = jobject.getAsJsonArray("results");

		if (jsonArray.size() <= 0) {
			logger.info("splunk json response is --> "+splunkResp);
			return null;
		}
		
		String response = new Gson().toJson(jsonArray);
		logger.debug("getSplunkData : response -->" + response);

		return response;
	}
	
	/*
	 * The following method is used to fetch query for ItemAccountExtracts
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getItemAccountExtracts(String latestExtractsDate, String itemAccountId, String cobrandName) throws Exception{
		
		logger.info("getItemAccountExtracts : ");
		String queryString = MiscUtil.getQuery("extracts_data"); 

		queryString = queryString.replaceAll("latestExtractsDate", latestExtractsDate);
		queryString = queryString.replaceAll("ITEM_ACCOUNT_ID", itemAccountId);
		queryString = queryString.replaceAll("cobrandName", cobrandName);
		
		return getSplunkData(queryString);
		
	}
	
	/*
	 * The following method is used to fetch query for ItemAccountHadoop
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getItemAccountHadoop(String latestHadoopDate, String itemAccountId, String hadoopCobrandName) throws Exception{

		logger.info("getItemAccountHadoop : ");
		String queryString = MiscUtil.getQuery("hadoop_data"); 

		queryString = queryString.replaceAll("latestHadoopDate", latestHadoopDate);
		queryString = queryString.replaceAll("Item_Account_Id", itemAccountId);
		queryString = queryString.replaceAll("hadoopCobrandName", hadoopCobrandName);
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for holding360_Extract
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getHolding360Hadoop(String holdingId, String hadoopCobrandName, String latestHadoopDate) throws Exception{

		logger.info("getHolding360Hadoop : ");
		String queryString = MiscUtil.getQuery("Holding360_hadoop_data"); 
		// todo utilize latestHadoopDate
		queryString = queryString.replaceAll("latestHadoopDate", latestHadoopDate);
		queryString = queryString.replaceAll("Holding_Id", holdingId);
		queryString = queryString.replaceAll("hadoopCobrandName", hadoopCobrandName);
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for holding360_Extract
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getHolding360Extract(String holdingId, String cobrandName, String itemAccountId, String latestExtractDate) throws Exception{

		logger.info("getHolding360Extract : ");
		String queryString = MiscUtil.getQuery("Holding360_extracts_data"); 
		// toDo utilise latestExtractDate
		queryString = queryString.replaceAll("latestExtractsDate", latestExtractDate);
		queryString = queryString.replaceAll("Holding_Id", holdingId);
		queryString = queryString.replaceAll("cobrandName", cobrandName);
		queryString = queryString.replaceAll("Item_Account_Id", itemAccountId);
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for holding360_Transaction
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getHolding360Transaction(String holdingId, String cobrandName, String itemAccountId, String latestExtractDate) throws Exception{

		logger.info("getHolding360Transaction : ");
		String queryString = MiscUtil.getQuery("Holding360_transactions_data"); 
		// todo Utilize latestExtractDate Date issue not changed the query as of now
		queryString = queryString.replaceAll("latestExtractsDate", latestExtractDate);
		queryString = queryString.replaceAll("Holding_Id", holdingId);
		queryString = queryString.replaceAll("cobrandName", cobrandName);
		queryString = queryString.replaceAll("Item_Account_Id", itemAccountId);
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for holding360_Extract
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getTransaction360Hadoop(String transactionId, String hadoopCobrandName, String latestHadoopDate) throws Exception{

		logger.info("getTransaction360Hadoop : ");
		String queryString = MiscUtil.getQuery("Transaction360_hadoop_data"); 
		// todo utilize latestHadoopdate   
		queryString = queryString.replaceAll("latestHadoopDate", latestHadoopDate);
		queryString = queryString.replaceAll("Transaction_Id", transactionId);
		queryString = queryString.replaceAll("hadoopCobrandName", hadoopCobrandName);
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for transaction360_Extract
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getTransaction360Extract(String transactionId, String cobrandName, String itemAccountId, String latestExtractDate) throws Exception{

		logger.info("getTransaction360Extract : ");
		String queryString = MiscUtil.getQuery("Transaction360_extracts_data"); 

		
		queryString = queryString.replaceAll("Transaction_Id", transactionId);
		queryString = queryString.replaceAll("cobrandName", cobrandName);
		queryString = queryString.replaceAll("Latest_Extract_Date", latestExtractDate);
		
		

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for LatestHadoopDate
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getLatestHadoopDate(String hadoopCobrandName) throws Exception{

		logger.info("getLatestHadoopDate : ");
		String queryString = MiscUtil.getQuery("latest_hadoop_date"); 

		queryString = queryString.replaceAll("hadoopCobrandName", hadoopCobrandName);

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for LatestExtractDate
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getLatestExtractsDate(String cobrandName) throws Exception{

		logger.info("getLatestExtractsDate : ");
		String queryString = MiscUtil.getQuery("latest_extracts_date"); 

		queryString = queryString.replaceAll("cobrandName", cobrandName);

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to fetch query for PreviousExtractDate
	 * from queries.json
	 * 
	 * @return String
	 */
	public static String getPreviousExtractsDate(String cobrandName, String extractDate) throws Exception{

		logger.info("getPreviousExtractsDate : ");
		String queryString = MiscUtil.getQuery("previous_extracts_date"); 

		queryString = queryString.replaceAll("cobrandName", cobrandName).replaceAll("extractDate", extractDate);

		return getSplunkData(queryString);
	}
	
	/*
	 * The following method is used to return timestamp value 
	 * from a supplied JSON
	 * 
	 * @return String
	 */
	public static String getTimestamp(String json, String timestamp){
		
		try{
			StringBuilder data = new StringBuilder(json);
			data.deleteCharAt(0);
			data.deleteCharAt(data.length()-1);
			String toJson = data.toString();
			JsonElement jelement = new JsonParser().parse(toJson);
			JsonObject jobject = jelement.getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entries = jobject.entrySet();
			for (Map.Entry<String, JsonElement> entry: entries) {
				
			    if(entry.getKey().equals(timestamp)){
			    	logger.info("Getting  Timestamp: ");
			    	return entry.getValue().getAsString();
			    	
			    }
			}
		}catch(Exception ex){
			logger.error("Exception while getting the timestamp :" + ExceptionUtils.getFullStackTrace(ex));
		}
		return null;
	}
	
	/*
	 * The following method converts yyyymmdd
	 * to mm-dd-yyyy
	 * 
	 * @return String
	 */
	public static String getDate(String latestDate) {

		if (latestDate.length() != 8) {
			return null;
		}
		String year = latestDate.substring(0, 4);
		String month = latestDate.substring(4, 6);
		String date = latestDate.substring(6, 8);

		return month + "-" + date + "-" + year;
	}
	
	/*
	 *The following method returns cobrandName 
	 *@param  String cobrandId
	 *@param  String fileType
	 *
	 *@return String
	 *
	 */
	public static String getCobrandName(String cobrandId, String fileType){
		
		String cobrandValue = null;
		
		if(fileType.equalsIgnoreCase("extract")){
			cobrandValue = extractCobrand.get(cobrandId);
		}else if(fileType.equalsIgnoreCase("hadoop")){
			cobrandValue = hadoopCobrand.get(cobrandId);
		}
		logger.info("CobrandId:"+cobrandId+" CobrandValue is:"+cobrandValue);
		return cobrandValue;
	}
	
	private static void setCobrandContext(Long cobrandId) {
		ContextAccessorUtil.setContext(cobrandId, 0l, null);		
	}
	
	
	
	/*
	 *The following method loads cobrandName 
	 *
	 *at server start up
	 */
	public static void loadCobrandInfo(){
		hadoopCobrand = new HashMap<String,String>();
		try{
		Properties propsHadoop = MiscUtil.loadProperties("hadoopCobrand.properties");
		Enumeration<Object> enuKeys = propsHadoop.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = propsHadoop.getProperty(key);
			hadoopCobrand.put(key,value);
		}
		logger.info("Hadoop Cobrand Load Success");
			}catch(Exception ex){
				logger.error("Exception while loading the hadoopCobrand :" + ExceptionUtils.getFullStackTrace(ex));
		}
		
		extractCobrand = new HashMap<String,String>();
		try{
		Properties propsExtract = MiscUtil.loadProperties("extractCobrand.properties");
		Enumeration<Object> enuExtractKeys = propsExtract.keys();
		while (enuExtractKeys.hasMoreElements()) {
			String key = (String) enuExtractKeys.nextElement();
			String value = propsExtract.getProperty(key);
			extractCobrand.put(key,value);
		}
		logger.info("Extract Cobrand Load Success");
			}catch(Exception ex){
				logger.error("Exception while loading the extractCobrand :" + ExceptionUtils.getFullStackTrace(ex));
		}
	}
	
	/*
	 *The following method loads Extract and Hadoop 
	 *
	 *dates at scheduled interval of 30mins
	 */
	
	public static void loadDates(){
		latestExtractDate = new HashMap<String,String>();
		try{
			for (Map.Entry<String, String> entry : extractCobrand.entrySet()) {
				String cobrandName = entry.getValue();
				latestExtractDate.put(cobrandName,ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestExtractsDate(cobrandName),"timestamp"));
				}
			logger.info("Latest Extrcat Date for all Cobrands Success");
		}catch(Exception ex){
			logger.error("Exception while loading the LatestExtractDate :" + ExceptionUtils.getFullStackTrace(ex));	
		}
		
		try{
			previousExtractDate = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : extractCobrand.entrySet()) {
				String cobrandName = entry.getValue();
				previousExtractDate.put(cobrandName,ReconDataAccessor.getTimestamp(ReconDataAccessor.getPreviousExtractsDate(cobrandName, latestExtractDate.get(cobrandName)),"timestamp"));
				}
			logger.info("Previous Extrcat Date for all Cobrands Success");
		}catch(Exception ex){
			logger.error("Exception while loading the PreviousExtractDate :" + ExceptionUtils.getFullStackTrace(ex));
		}
		
		try{
			latestHadoopDate = new HashMap<String,String>();
			for (Map.Entry<String, String> entry : hadoopCobrand.entrySet()) {
				String hadoopCobrandName = entry.getValue();
				latestHadoopDate.put(hadoopCobrandName,ReconDataAccessor.getTimestamp(ReconDataAccessor.getLatestHadoopDate(hadoopCobrandName),"timestamp"));
				}
			logger.info("Latest Hadoop Date for all Cobrands Success");
		}catch(Exception ex){
			logger.error("Exception while loading the LatestHadoopDate :" + ExceptionUtils.getFullStackTrace(ex));
		}
		
		
	}

}
