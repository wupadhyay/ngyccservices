/*
 * Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.dapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yodlee.ycc.dapi.exceptions.ApiException;
import com.yodlee.ycc.dapi.service.ReconService;

@Controller
@RequestMapping("/v1/reconciliation")
public class ReconController extends MasterController{
	
	private static final Logger logger = LoggerFactory.getLogger(ReconController.class);
	
	@Autowired
	private ReconService reconService;
	
	@RequestMapping(value = "/itemAccountDetails", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountDetails(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "asOfDate", required = false)String asOfDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountDetails");
		logger.info("itemAccountDetails input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		final String ret = this.reconService.getItemAccountDetails(Long.valueOf(itemAccountId), Long.valueOf(cobrandId), asOfDate);
		logger.debug("Completed  reconService.getItemAccountDetails");
		return ret;
	}
	
	@RequestMapping(value = "/itemAccountHoldings", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountHoldings(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "asOfDate", required = false)String asOfDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountHoldings");
		logger.info("itemAccountHoldings input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" asOfDate: "+asOfDate);
		final String ret = this.reconService.getItemAccountHoldings(Long.valueOf(itemAccountId), Long.valueOf(cobrandId), asOfDate);
		logger.debug("Completed  reconService.getItemAccountHoldings");
		return ret;
	}
	
	@RequestMapping(value = "/itemAccountTransactions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountTransactions(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate,
			@RequestParam(value = "previousExtractDate", required = false) String previousExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountTransactions");
		logger.info("itemAccountTransactions input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		final String ret = this.reconService.getItemAccountTransactions(Long.valueOf(itemAccountId), Long.valueOf(cobrandId), 
				latestExtractDate, previousExtractDate);
		logger.debug("Completed  reconService.getItemAccountTransactions");
		return ret;
	}
	
	@RequestMapping(value = "/itemAccountExtracts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountExtracts(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountExtracts");
		logger.info("itemAccountExtracts input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		final String ret = this.reconService.getItemAccountExtracts(Long.valueOf(itemAccountId), Long.valueOf(cobrandId), latestExtractDate);
		logger.debug("Completed  reconService.getItemAccountExtracts");
		return ret;
	}
	
	@RequestMapping(value = "/itemAccountHadoop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountHadoop(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "latestHadoopDate", required = false) String latestHadoopDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountHadoop");
		logger.info("itemAccountHadoop input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		final String ret = this.reconService.getItemAccountHadoop(Long.valueOf(itemAccountId), Long.valueOf(cobrandId), latestHadoopDate);
		logger.debug("Completed  reconService.getItemAccountHadoop");
		return ret;
	}
	
	
	@RequestMapping(value = "/itemAcc360Holdings", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountAggHoldings(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "recAccountId", required = false) String recAccountId,
			@RequestParam(value = "requestType", required = false) String requestType) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountAggHoldings");
		logger.info("itemAccountAggHoldings input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		if(recAccountId == null){
			recAccountId = "-1";
			logger.debug("itemAccountAggHoldings : Setting recAccountId to -1");
		}
		final String ret = this.reconService.getItemAccountAggHoldings(Long.valueOf(itemAccountId), Long.valueOf(recAccountId), Long.valueOf(cobrandId),requestType);
		logger.debug("Completed  reconService.getItemAccountAggHoldings");
		return ret;
	}
	
	@RequestMapping(value = "/itemAcc360Transactions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccountAggTransactions(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "recAccountId", required = false) String recAccountId,
			@RequestParam(value = "requestType", required = false) String requestType,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate,
			@RequestParam(value = "previousExtractDate", required = false) String previousExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccountAggTransactions");
		logger.info("itemAccountAggTransactions input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		if(recAccountId == null){
			recAccountId = "-1";
			logger.debug("itemAccountAggHoldings : Setting recAccountId to -1");
		}
		final String ret = this.reconService.getItemAccountAggTransactions(Long.valueOf(itemAccountId), Long.valueOf(recAccountId), Long.valueOf(cobrandId),requestType,latestExtractDate,
				previousExtractDate);
		logger.debug("Completed  reconService.getItemAccountAggTransactions");
		return ret;
	}
	
	@RequestMapping(value = "/itemAcc360CashTransactions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getItemAccAggCashTransactions(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "recAccountId", required = false) String recAccountId,
			@RequestParam(value = "requestType", required = false) String requestType,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate,
			@RequestParam(value = "previousExtractDate", required = false) String previousExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getItemAccAggCashTransactions");
		logger.info("itemAccAggCashTransactions input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId);
		if(recAccountId == null){
			recAccountId = "-1";
			logger.debug("itemAccountAggHoldings : Setting recAccountId to -1");
		}
		final String ret = this.reconService.getItemAccAggCashTransactions(Long.valueOf(itemAccountId), Long.valueOf(recAccountId), Long.valueOf(cobrandId), requestType,
				latestExtractDate, previousExtractDate);
		logger.debug("Completed  reconService.getItemAccAggCashTransactions");
		return ret;
	}

	@RequestMapping(value = "/holding360Aggregated", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getHolding360Aggregated(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "holdingId", required = false) String holdingId) throws ApiException,Exception {
		logger.debug("Invoking reconService.getHolding360Aggregated");
		logger.info("holding360Aggregated input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" holdingId:"+holdingId);
		final String ret = this.reconService.getHolding360Aggregated(Long.valueOf(itemAccountId), Long.valueOf(holdingId), 
				Long.valueOf(cobrandId));
		logger.debug("Completed  reconService.getHolding360Aggregated");
		return ret;
	}
	
	@RequestMapping(value = "/holding360Reconciled", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getHolding360Reconciled(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "recAccountId", required = false) String recAccountId,
			@RequestParam(value = "holdingId", required = false) String holdingId) throws ApiException,Exception {
		logger.debug("Invoking reconService.getHolding360Reconciled");
		logger.info("holding360Reconciled input params: "+"recAccountId: "+recAccountId+" cobrandId: "+cobrandId+" holdingId:"+holdingId);
		final String ret = this.reconService.getHolding360Reconciled(Long.valueOf(recAccountId), Long.valueOf(holdingId), 
				Long.valueOf(cobrandId));
		logger.debug("Completed  reconService.getHolding360Reconciled");
		return ret;
	}
	
	@RequestMapping(value = "/holding360Hadoop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getHolding360Hadoop(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "holdingId", required = false) String holdingId,
			@RequestParam(value = "latestHadoopDate", required = false) String latestHadoopDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getHolding360Hadoop");
		logger.info("holding360Hadoop input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" holdingId:"+holdingId);
		final String ret = this.reconService.getHolding360Hadoop(Long.valueOf(itemAccountId), Long.valueOf(holdingId), 
				Long.valueOf(cobrandId), latestHadoopDate);
		logger.debug("Completed  reconService.getHolding360Hadoop");
		return ret;
	}
	
	@RequestMapping(value = "/holding360Extracts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getHolding360Extract(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "holdingId", required = false) String holdingId,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getHolding360Extract");
		logger.info("holding360Extracts input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" holdingId:"+holdingId);
		final String ret = this.reconService.getHolding360Extract(Long.valueOf(itemAccountId), Long.valueOf(holdingId), 
				Long.valueOf(cobrandId), latestExtractDate);
		logger.debug("Completed  reconService.getHolding360Extract");
		return ret;
	}
	
	@RequestMapping(value = "/holding360Transactions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getHolding360Transaction(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "holdingId", required = false) String holdingId,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getHolding360Transaction");
		logger.info("holding360Transactions input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" holdingId:"+holdingId);
		final String ret = this.reconService.getHolding360Transaction(Long.valueOf(itemAccountId), Long.valueOf(holdingId), 
				Long.valueOf(cobrandId), latestExtractDate);
		logger.debug("Completed  reconService.getHolding360Transaction");
		return ret;
	}
	
	@RequestMapping(value = "/transaction360Aggregated", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getTransaction360Aggregated(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "transactionId", required = false) String transactionId) throws ApiException,Exception {
		logger.debug("Invoking reconService.getTransaction360Aggregated");
		logger.info("transaction360Aggregated input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" transactionId:"+transactionId);
		final String ret = this.reconService.getTransaction360Aggregated(Long.valueOf(itemAccountId), Long.valueOf(transactionId), 
				Long.valueOf(cobrandId));
		logger.debug("Completed  reconService.getTransaction360Aggregated");
		return ret;
	}
	
	@RequestMapping(value = "/transaction360Hadoop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getTransaction360Hadoop(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "transactionId", required = false) String transactionId,
			@RequestParam(value = "latestHadoopDate", required = false) String latestHadoopDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getTransaction360Hadoop");
		logger.info("transaction360Hadoop input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" transactionId:"+transactionId);
		final String ret = this.reconService.getTransaction360Hadoop(Long.valueOf(itemAccountId), Long.valueOf(transactionId), 
				Long.valueOf(cobrandId), latestHadoopDate);
		logger.debug("Completed  reconService.getTransaction360Hadoop");
		return ret;
	}
	
	@RequestMapping(value = "/transaction360Extracts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getTransaction360Extracts(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "itemAccountId", required = false) String itemAccountId,
			@RequestParam(value = "transactionId", required = false) String transactionId,
			@RequestParam(value = "latestExtractDate", required = false) String latestExtractDate) throws ApiException,Exception {
		logger.debug("Invoking reconService.getTransaction360Extracts");
		logger.info("transaction360Extracts input params: "+"itemAccountId: "+itemAccountId+" cobrandId: "+cobrandId+" transactionId:"+transactionId);
		final String ret = this.reconService.getTransaction360Extract(Long.valueOf(itemAccountId), Long.valueOf(transactionId), 
				Long.valueOf(cobrandId), latestExtractDate);
		logger.debug("Completed  reconService.getTransaction360Extracts");
		return ret;
	}
	
	@RequestMapping(value = "/transaction360Reconciled", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String getTransaction360Reconciled(@RequestParam(value = "cobrandId", required = false) String cobrandId, 
			@RequestParam(value = "recAccountId", required = false) String recAccountId,
			@RequestParam(value = "transactionId", required = false) String transactionId) throws ApiException,Exception {
		logger.debug("Invoking reconService.getTransaction360Reconciled");
		logger.info("transaction360Reconciled input params: "+"recAccountId: "+recAccountId+" cobrandId: "+cobrandId+" transactionId:"+transactionId);
		final String ret = this.reconService.getTransaction360Reconciled(Long.valueOf(recAccountId), Long.valueOf(transactionId), 
				Long.valueOf(cobrandId));
		logger.debug("Completed  reconService.getTransaction360Reconciled");
		return ret;
	}

}
