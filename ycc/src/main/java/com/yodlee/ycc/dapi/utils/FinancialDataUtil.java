/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.interceptor.TransactionAttribute;

import com.google.gson.Gson;
import com.yodlee.dom.tx.TransactionMgmt;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.security.external.encryption.IEncryption;
import com.yodlee.security.external.encryption.ItemSummaryEncryptionFactory;
import com.yodlee.ycc.dapi.bean.AccountInfo;
import com.yodlee.ycc.dapi.bean.FirmInfo;
import com.yodlee.ycc.dapi.bean.PositionInfo;
import com.yodlee.ycc.dapi.bean.TransactionInfo;
import com.yodlee.ycc.dapi.extentions.CommonExtn;

/**
 * 
 * @author bkumar1 
 * Description: This is the Util which handles the requests 
 * 				from the clientDashboard.
 */

public class FinancialDataUtil {
	private final static Logger logger = LoggerFactory.getLogger(FinancialDataUtil.class);
	public static Map<String, List<FirmInfo>> cobrandFirms = new ConcurrentHashMap<String, List<FirmInfo>>();
	private final static String wealthCobrands=MiscUtil.getPropertyValue("ACCOUNT_INFORMATION_COBRANDS", false);
	public static final String DATE_TIME_FORMAT_PATTERN_EXT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static void loadAllFirms() {
		try{
			logger.info("Loading all the firms");
			List<String> cobrandList = new ArrayList<String>(Arrays.asList(wealthCobrands.split(",")));
			for (String cobrandId : cobrandList) {
				getFirms(cobrandId);
				logger.debug("Loaded firms of Cobrand:"+cobrandId);
			}
			logger.info("Completed loading all the firms");
		} catch (Exception e) {
			logger.error("Exception in loadAllFirms:" + ExceptionUtils.getFullStackTrace(e));
		}		
	}

	/*
	 * this method returns firm names for a particular cobrand provided as
	 * input.
	 */
	public static String loadAllFirmsForCobrand(String cobID) {
		String result = new String();
		if (!cobrandFirms.containsKey(cobID)) {
			getFirms(cobID);
		}
		if (!cobrandFirms.isEmpty() && cobrandFirms.containsKey(cobID)) {
			List<FirmInfo> firmInfo = cobrandFirms.get(cobID);
			Gson gson = new Gson();
			result = "{\"firms\":" + gson.toJson(firmInfo) + "}";
			logger.debug("getFirms result:" + result);
		}
		return result;
	}

	private static void setCobrandContext(String cobrandId) {
		 ContextAccessorUtil.setContext(Long.valueOf(cobrandId), 0, null);
	}

	public static void getFirms(String cobrandId) {
		logger.debug("loading for cobrand: " + cobrandId);
		List<FirmInfo> firmInfo = null;
		setCobrandContext(cobrandId);
		TransactionMgmt txn = new TransactionMgmt();
		logger.debug("getFirms:Beginning the transaction");
		txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobrandId));
		try {
			JdbcTemplate template = txn.getJdbcTemplate();
			firmInfo = template.query(MiscUtil.getQuery("firm_details"), new BeanPropertyRowMapper(FirmInfo.class),cobrandId);
			cobrandFirms.put(cobrandId, firmInfo);
		} catch (Exception e) {
			logger.error("Exception while getting the firm details:" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			txn.endTransaction(false);
			logger.debug("getFirms:Ending the transaction");
			ContextAccessorUtil.unsetContext();
		}
	}

	private static ExecutorService executor = Executors.newCachedThreadPool();

	/*
	 * this method returns all the accounts information for a particular firm
	 * provided as input.
	 */
	public static String getAccountsForFirm(final String firmId, String cobrandId, String requestType) {
		List<AccountInfo> accountInfo = null;
		List<AccountInfo> bankAccountInfo = null;
		setCobrandContext(cobrandId);
		TransactionMgmt txn = new TransactionMgmt();
		logger.debug("getAccountsForFirm:Beginning the transaction");
		txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobrandId));
		try {
			final JdbcTemplate template = txn.getJdbcTemplate();
			if ("display".equals(requestType)) {
				template.setMaxRows(1500);
			}
			Future<List<AccountInfo>> future1 = executor.submit(new Callable<List<AccountInfo>>() {
				@Override
				public List<AccountInfo> call() throws Exception {
					return template.query(MiscUtil.getQuery("investment_account_details"),new BeanPropertyRowMapper(AccountInfo.class), firmId);
				}
			});

			Future<List<AccountInfo>> future2 = executor.submit(new Callable<List<AccountInfo>>() {
				@Override
				public List<AccountInfo> call() throws Exception {
					return template.query(MiscUtil.getQuery("bank_account_details"),new BeanPropertyRowMapper(AccountInfo.class), firmId);
				}
			});
			
			accountInfo = future1.get();
			bankAccountInfo = future2.get();
			if (accountInfo!=null){
				accountInfo.addAll(bankAccountInfo);
			}
			else{
				accountInfo=bankAccountInfo;
			}
			if ("display".equals(requestType) && accountInfo != null && accountInfo.size() > 1500) {
				java.util.Collections.shuffle(accountInfo);
				accountInfo = accountInfo.subList(0, 1499);
			}
			logger.debug("getAccountsForFirm:Starting decryption of accounts");
			 IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			//IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			long startTime = System.currentTimeMillis();
			CommonExtn ext=new CommonExtn();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (AccountInfo account : accountInfo) {
				try {
					if (account.getResponse() != null) {
						String errDescValue = account.getResponse().equals("0") ? "SUCCESS": MiscUtil.getPropertyValue(account.getResponse(), false);
						account.setRefreshStatus(errDescValue);
					}
					if(account.getSiteId()!=null){
						account.setFiName(ext.getSiteName(account.getSiteId()));
					}
					if(account.getLastRefreshAttempt()!=null){
						Date date = new Date(Long.parseLong(account.getLastRefreshAttempt()) * 1000);
						account.setLastRefreshAttempt(formatter.format(date));	
					}
					if(account.getLastSuccessfulRefresh()!=null){
						Date date = new Date(Long.parseLong(account.getLastSuccessfulRefresh()) * 1000);
						account.setLastSuccessfulRefresh(formatter.format(date));	
					}
					if (account.getAccountNumber() != null) {
						try{
							account.setAccountNumber(new String(encryption.decrypt(account.getAccountNumber()), "UTF-8"));
						}catch (Exception e) {
							logger.debug("Error in decryption" + e.getMessage());
						}
					}
					if (account.getAccountName() != null) {
						try{
						account.setAccountName(new String(encryption.decrypt(account.getAccountName()), "UTF-8"));
						}catch (Exception e) {
							logger.debug("Error in decryption:" + e.getMessage());
						}
					}
				} catch (Exception e) {
					logger.debug("Error in accountInfo population:" + ExceptionUtils.getFullStackTrace(e));
				}
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.debug("getAccountsForFirm:Ending decryption of accounts.Time taken : " + elapsedTime);
		} catch (Exception e) {
			logger.error("Exception while getting the account details:" + ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		} finally {
			txn.endTransaction(false);
			logger.debug("getAccountsForFirm:Ending the transaction");
			ContextAccessorUtil.unsetContext();
		}
		Gson gson = new Gson();
		String result = "{\"accounts\":" + gson.toJson(accountInfo) + "}";
		logger.debug("getAccountsForFirm result:" + result);
		return result;
	}

	/*
	 * this method returns all the position information for a particular
	 * accounts provided as input.
	 */
	public static String getPositionsForAccounts(List<Long> selectedAccounts, String cobrandId, String requestType) {
		List<PositionInfo> positionInfo = null;
		setCobrandContext(cobrandId);
		TransactionMgmt txn = new TransactionMgmt();
		logger.debug("getPositionsForAccounts:Beginning the transaction");
		txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobrandId));
		try {
			JdbcTemplate template = txn.getJdbcTemplate();
			String query = MiscUtil.getQuery("position_details");
			if ("display".equals(requestType)) {
				template.setMaxRows(1500);
			}
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", selectedAccounts);
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
			positionInfo = namedParameterJdbcTemplate.query(query, parameters,new BeanPropertyRowMapper(PositionInfo.class));
			logger.debug("getPositionsForAccounts:Starting decryption of accounts");
			 IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			//IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			long startTime = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (PositionInfo info : positionInfo) {
				try {
					if(info.getAsOfDate()!=null){
						Date date = new Date(Long.parseLong(info.getAsOfDate()) * 1000);
						info.setAsOfDate(formatter.format(date));	
					}
					if (info.getAccountNumber() != null) {
						info.setAccountNumber(new String(encryption.decrypt(info.getAccountNumber()), "UTF-8"));
					}
					if (info.getAccountName() != null) {
						info.setAccountName(new String(encryption.decrypt(info.getAccountName()), "UTF-8"));
					}
				} catch (Exception e) {
					logger.debug("Error in decryption:" + e.getMessage());
				}
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.debug("getPositionsForAccounts:Ending decryption of accounts.Time taken : " + elapsedTime);
		} catch (Exception e) {
			logger.error("Exception while getting the position details of accounts:" + ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		} finally {
			txn.endTransaction(false);
			logger.debug("getPositionsForAccounts:Ending the transaction");
			ContextAccessorUtil.unsetContext();
		}
		Gson gson = new Gson();
		String result = "{\"positionInfo\":" + gson.toJson(positionInfo) + "}";
		logger.debug("getPositionsForAccount result:" + result);
		return result;
	}

	/*
	 * this method returns all the transaction information for a particular
	 * accounts provided as input.
	 */
	public static String getTransactionsForAccounts(List<Long> selectedAccounts, String cobrandId, String requestType) {
		List<TransactionInfo> transactionInfo = null;
		List<TransactionInfo> bankTransactionInfo = null;
		setCobrandContext(cobrandId);
		TransactionMgmt txn = new TransactionMgmt();
		logger.debug("getTransactionsForAccounts:Beginning the transaction");
		txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobrandId));
		try {
			JdbcTemplate template = txn.getJdbcTemplate();
			if ("display".equals(requestType)) {
				template.setMaxRows(1500);
			}
			final MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", selectedAccounts);
			final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
			
			Future<List<TransactionInfo>> future1 = executor.submit(new Callable<List<TransactionInfo>>() {
				@Override
				public List<TransactionInfo> call() throws Exception {
					return namedParameterJdbcTemplate.query(MiscUtil.getQuery("investment_transaction_details"), parameters,new BeanPropertyRowMapper(TransactionInfo.class));
				}
			});

			Future<List<TransactionInfo>> future2 = executor.submit(new Callable<List<TransactionInfo>>() {
				@Override
				public List<TransactionInfo> call() throws Exception {
					return namedParameterJdbcTemplate.query(MiscUtil.getQuery("bank_transaction_details"), parameters,new BeanPropertyRowMapper(TransactionInfo.class));
				}
			});
			
			transactionInfo=future1.get();
			bankTransactionInfo=future2.get();
			if (transactionInfo!=null){
				transactionInfo.addAll(bankTransactionInfo);
			}
			else{
				transactionInfo=bankTransactionInfo;
			}
			if ("display".equals(requestType) && transactionInfo != null && transactionInfo.size() > 1500) {
				java.util.Collections.shuffle(transactionInfo);
				transactionInfo = transactionInfo.subList(0, 1499);
			}
			
			logger.debug("getTransactionsForAccounts:Starting decryption of accounts");
			 IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			//IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			long startTime = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (TransactionInfo info : transactionInfo) {
				try {
					if (info.getTransactionDate() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date transDate = sdf.parse(info.getTransactionDate());
						info.setTransactionDate(formatter.format(transDate));
					}
					if (info.getAccountNumber() != null) {
						info.setAccountNumber(new String(encryption.decrypt(info.getAccountNumber()), "UTF-8"));
					}
				} catch (Exception e) {
					logger.debug("Error in decryption:" + e.getMessage());
				}
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.debug("getTransactionsForAccounts:Ending decryption of accounts.Time taken : " + elapsedTime);
		} catch (Exception e) {
			logger.error("Exception while getting the transaction details of accounts:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		} finally {
			txn.endTransaction(false);
			logger.debug("getTransactionsForAccounts:Ending the transaction");
			ContextAccessorUtil.unsetContext();
		}
		Gson gson = new Gson();
		String result = "{\"transactionInfo\":" + gson.toJson(transactionInfo) + "}";
		logger.debug("getTransactionsForAccounts result:" + result);
		return result;
	}

	public static String getAccountsForProvider(String cobrandId, String requestType, List<Long> accountIds) {
		logger.info("FinancialDataUtil - getAccountsForProvider(String cobrandId, String requestType, List<Long> accountIds) - STARTED");
		List<AccountInfo> accountInfo = null;
		List<AccountInfo> bankAccountInfo = null;
		setCobrandContext(cobrandId);
		TransactionMgmt txn = new TransactionMgmt();
		logger.info("getAccountsForProvider : Beginning the transaction");
		txn.beginTransaction(TransactionAttribute.PROPAGATION_REQUIRES_NEW, Long.valueOf(cobrandId));
		try {
			final JdbcTemplate template = txn.getJdbcTemplate();
			if ("display".equals(requestType)) {
				template.setMaxRows(1500);
			}
			final MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", accountIds);
			final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
			logger.info("Before calling future 1");
			Future<List<AccountInfo>> future1 = executor.submit(new Callable<List<AccountInfo>>() {
				@Override
				public List<AccountInfo> call() throws Exception {
					logger.info("QUERY FOR investment_account_details_non_tamrac :"+MiscUtil.getQuery("investment_account_details_non_tamrac"));
					return namedParameterJdbcTemplate.query(MiscUtil.getQuery("investment_account_details_non_tamrac"), parameters,new BeanPropertyRowMapper(AccountInfo.class));
				}
			});
			logger.info("After calling future 1");
			Future<List<AccountInfo>> future2 = executor.submit(new Callable<List<AccountInfo>>() {
				@Override
				public List<AccountInfo> call() throws Exception {
					logger.info("QUERY FOR bank_account_details_non_tamrac :"+MiscUtil.getQuery("bank_account_details_non_tamrac"));
					return namedParameterJdbcTemplate.query(MiscUtil.getQuery("bank_account_details_non_tamrac"), parameters,new BeanPropertyRowMapper(AccountInfo.class));
				}
			});
			logger.info("After calling future 2");
			accountInfo = future1.get();
			bankAccountInfo = future2.get();
			if (accountInfo!=null){
				accountInfo.addAll(bankAccountInfo);
			}
			else{
				accountInfo=bankAccountInfo;
			}
			if ("display".equals(requestType) && accountInfo != null && accountInfo.size() > 1500) {
				java.util.Collections.shuffle(accountInfo);
				accountInfo = accountInfo.subList(0, 1499);
			}
			logger.debug("getAccountsForProvider : Starting decryption of accounts");
			 //IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessor.getCobrandContext());
			IEncryption encryption = ItemSummaryEncryptionFactory.getInstance(ContextAccessorUtil.getContext());
			long startTime = System.currentTimeMillis();
			CommonExtn ext=new CommonExtn();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_EXT);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			for (AccountInfo account : accountInfo) {
				try {
					if (account.getResponse() != null) {
						String errDescValue = account.getResponse().equals("0") ? "SUCCESS": MiscUtil.getPropertyValue(account.getResponse(), false);
						account.setRefreshStatus(errDescValue);
					}
					if(account.getSiteId()!=null){
						account.setFiName(ext.getSiteName(account.getSiteId()));
					}
					if(account.getLastRefreshAttempt()!=null){
                        Date date = new Date(Long.parseLong(account.getLastRefreshAttempt()) * 1000);
                        account.setLastRefreshAttempt(formatter.format(date));      
					}
					if(account.getLastSuccessfulRefresh()!=null){
                        Date date = new Date(Long.parseLong(account.getLastSuccessfulRefresh()) * 1000);
                        account.setLastSuccessfulRefresh(formatter.format(date));      
					}
					if (account.getAccountNumber() != null) {
						try{
							account.setAccountNumber(new String(encryption.decrypt(account.getAccountNumber()), "UTF-8"));
						}catch (Exception e) {
							logger.error("getAccountsForProvider : Error in decryption" + e.getMessage());
						}
					}
					if (account.getAccountName() != null) {
						try{
						account.setAccountName(new String(encryption.decrypt(account.getAccountName()), "UTF-8"));
						}catch (Exception e) {
							logger.error("getAccountsForProvider : Error in decryption:" + e.getMessage());
						}
					}
				} catch (Exception e) {
					logger.error("getAccountsForProvider : Error in accountInfo population:" + ExceptionUtils.getFullStackTrace(e));
				}
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			logger.info("getAccountsForProvider : Ending decryption of accounts.Time taken : " + elapsedTime);
			logger.info("FinancialDataUtil - getAccountsForProvider(String cobrandId, String requestType, List<Long> accountIds) - ENDED");
		} catch (Exception e) {
			logger.error("getAccountsForProvider : Exception while getting the account details:" + ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		} finally {
			txn.endTransaction(false);
			logger.error("getAccountsForProvider : Ending the transaction");
//			 ThreadLocalStorage.unset();
		}
		Gson gson = new Gson();
		String result = "{\"accounts\":" + gson.toJson(accountInfo) + "}";
//		logger.info("getAccountsForProvider result : " + result);
		return result;
	}
}
