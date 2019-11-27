/*
 * Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */

package com.yodlee.ycc.rest.api.queries;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PreCannedQueryMap {

	public static Map<String, String> preCannedQueryMap;

	public PreCannedQueryMap()
	{
		System.out.println(" PRE-CANNED QUERIES LOADED INTO MEMORY");
		preCannedQueryMap = new HashMap<String,String>();
		//preCannedQueryMap1 = new HashMap<Integer,String>();
		//Accounts_Summary
		//String clause = "queryClause";
		preCannedQueryMap.put("accounts_summary","account providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext availableBalance ext currentBalance accountType ext amountDue dueDate ext minimumAmountDue ext availableCash ext availableCredit ext totalCashLimit ext totalCreditLine ext cash ext marginBalance ext cashValue  maturityDate term interestRate ext originalLoanAmount primaryRewardUnit isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_bank","account bank providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext availableBalance isManual ext currentBalance accountType  maturityDate term interestRate holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_creditcard","account creditcard providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext amountDue  dueDate ext minimumAmountDue accountType ext availableCash ext availableCredit ext totalCashLimit ext totalCreditLine isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_bill","account bill providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext amountDue  dueDate ext minimumAmountDue accountType isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_insurance","account Insurance providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext amountDue dueDate ext minimumAmountDue accountType ext cashValue isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_investment","account Investment providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName accountType ext cash ext marginBalance isManual holderProfile refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_reward", "account reward providerAccountId accountName accountStatus accountNumber isAsset container id lastUpdated nickname providerId providerName primaryRewardUnit isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_summary_loan","account loan providerAccountId accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext amountDue  dueDate accountType ext minimumAmountDue maturityDate term ext originalLoanAmount interestRate ext availableCredit isManual holderProfile refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		//401k
		
		preCannedQueryMap.put("retirement_plan","account investment id where accountType eq _401K and hasInvestmentOption eq true ?  investmentPlan  providerId providerName asOfDate planName planNumber id lastUpdated returnAsOfDate feesAsOfDate investmentOption holdingType cusipNumber description isin id  ext price priceAsOfDate sedol symbol inceptionDate inceptionToDateReturn yearToDateReturn grossExpenseRatio netExpenseRatio ext grossExpenseAmount ext netExpenseAmount ext historicReturns ");
		
		//Accounts_Detail
		preCannedQueryMap.put("accounts_details_bank","account bank providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName isManual ext availableBalance ext currentBalance accountType classification interestRate ext maturityAmount  maturityDate term holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_details_creditcard","account creditcard providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName isManual ext amountDue apr ext availableCash ext availableCredit  dueDate lastPaymentDate ext ext lastPaymentAmount ext minimumAmountDue accountType classification ext runningBalance ext totalCashLimit ext totalCreditLine holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_details_bill","account bill providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName ext amountDue  dueDate  lastPaymentDate ext lastPaymentAmount ext minimumAmountDue accountType isManual holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_details_insurance","account Insurance providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName isManual accountType ext amountDue  dueDate lastPaymentDate ext lastPaymentAmount ext minimumAmountDue ext annuityBalance  expirationDate ext cashValue policyStatus homeInsuranceType lifeInsuranceType accountType ext faceAmount holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_details_investment","account Investment providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance ext lastEmployeeContributionAmount lastEmployeeContributionDate container id lastUpdated nickname providerId providerName isManual accountType ext availableLoan ext 401kLoan ext annuityBalance ext cash classification ext marginBalance ext moneyMarketBalance ext totalUnvestedBalance ext totalVestedBalance ext shortBalance holderProfile  refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		preCannedQueryMap.put("accounts_details_reward", "account reward providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset container id lastUpdated nickname providerId providerName  isManual classification enrollmentDate primaryRewardUnit currentLevel nextLevel holderProfile ? refreshInfo statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled rewardBalance description balance balanceType units");
		preCannedQueryMap.put("accounts_details_loan","account loan providerAccountId status NONDELETED accountName accountStatus accountNumber isAsset ext balance container id lastUpdated nickname providerId providerName isManual accountType ext amountDue  dueDate ext minimumAmountDue  maturityDate term ext originalLoanAmount interestRate  lastPaymentDate ext lastPaymentAmount ext availableCredit ext escrowBalance ext principalBalance ext recurringPayment ext totalCreditLimit classification holderProfile refreshInfo (statusCode statusMessage lastRefreshed lastRefreshAttempt nextRefreshScheduled)");
		//transactions
		preCannedQueryMap.put("transactions","transaction id ? categoryLevel all container ext amount baseType transactionCategoryId category ext description holdingDescription type isManual ext interest ext principal date transactionDate postDate settleDate memo status cusipNumber ext price quantity symbol postingOrder accountId");
        preCannedQueryMap.put("transactions_bank","transaction bank ? id categoryLevel all container ext amount baseType transactionCategoryId category ext description date postDate transactionDate postingOrder isManual memo status accountId");
        preCannedQueryMap.put("transactions_creditcard","transaction creditcard ? id categoryLevel all container ext amount baseType transactionCategoryId category ext description date postDate transactionDate postingOrder isManual memo status accountId");
        preCannedQueryMap.put("transactions_insurance","transaction Insurance ? id categoryLevel all container ext amount baseType transactionCategoryId category ext description date postDate transactionDate postingOrder isManual memo status accountId");
        preCannedQueryMap.put("transactions_investment","transaction Investment ? id categoryLevel all container ext amount baseType type transactionCategoryId category ext description holdingDescription date transactionDate settleDate postingOrder isManual memo status cusipNumber ext price quantity symbol accountId");
        preCannedQueryMap.put("transactions_loan","transaction loan ? id categoryLevel all container ext amount baseType transactionCategoryId category ext description date transactionDate postDate postingOrder isManual memo status ext interest ext principal postingOrder accountId");
        //transaction categories
        preCannedQueryMap.put("transaction_categories_cobrand", "userTransactionCategory system id source category type parentCategoryId where regionId eq 1 and not isSmallBusinessCategory");
        preCannedQueryMap.put("transaction_categories_user", "userTransactionCategory system id source category type parentCategoryId where regionId eq 1 and not isSmallBusinessCategory");
         
        preCannedQueryMap.put("transactionCount","transaction ?  total count id as count");
		
        /*preCannedQueryMap.put("transactions_geo_disabled","transaction id ? container ext amount baseType category ext description holdingDescription type isManual ext interest ext principal date transactionDate postDate settleDate memo status cusipNumber ext price quantity symbol postingOrder accountId merchant name");
        preCannedQueryMap.put("transactions_geo_disabled_bank","transaction bank ? id container ext amount baseType category ext description date postDate transactionDate postingOrder isManual memo status accountId merchant name");
        preCannedQueryMap.put("transactions_geo_disabled_creditcard","transaction creditcard ? id container ext amount baseType category ext description date postDate transactionDate postingOrder isManual memo status accountId merchant name");
       */
		//holdings
		preCannedQueryMap.put("holdings","holding id providerAccountId accountId contractQuantity ext costBasis couponRate cusipNumber currencyType description ext employeeContribution ext employerContribution exercisedQuantity expirationDate grantDate holdingType interestRate maturityDate optionType ext parValue ext price quantity ext spread ext strikePrice symbol term unvestedQuantity ext unvestedValue ext value vestedQuantity vestedSharesExercisable ext vestedValue vestingDate");
		preCannedQueryMap.put("holdings_asset_classification","holding id providerAccountId accountId contractQuantity ext costBasis couponRate cusipNumber currencyType description ext employeeContribution ext employerContribution exercisedQuantity expirationDate grantDate holdingType interestRate maturityDate optionType ext parValue ext price quantity ext spread ext strikePrice symbol term unvestedQuantity ext unvestedValue ext value vestedQuantity vestedSharesExercisable ext vestedValue vestingDate assetClassification (classificationType classificationValue allocation where vendorId eq ? ) ");
		preCannedQueryMap.put("holdings_asset_classification_summary","holding id accountId contractQuantity ext costBasis couponRate cusipNumber currencyType description ext employeeContribution ext employerContribution exercisedQuantity expirationDate grantDate holdingType interestRate maturityDate optionType ext parValue ext price quantity ext spread ext strikePrice symbol term unvestedQuantity ext unvestedValue ext value vestedQuantity vestedSharesExercisable ext vestedValue vestingDate country securitiesDomicile assetClassification classificationType classificationValue allocation");
		//Member
		preCannedQueryMap.put("member", "user id loginName email name first middleInitial last");
		preCannedQueryMap.put("member_info", "user id loginName email name (first middleInitial last) address address1 address2 state city zip country");
		//statements
		preCannedQueryMap.put("statements","statement id accountId statementDate billingPeriodStart billingPeriodEnd dueDate lastPaymentDate lastUpdated ext amountDue ext lastPaymentAmount isLatest");
		preCannedQueryMap.put("statements_creditcard","statement creditcard id accountId statementDate billingPeriodStart billingPeriodEnd dueDate lastPaymentDate lastUpdated ext minimumPayment ext newCharges apr cashApr ext amountDue ext lastPaymentAmount isLatest");
		preCannedQueryMap.put("statements_insurance","statement Insurance id accountId statementDate billingPeriodStart billingPeriodEnd dueDate lastPaymentDate lastUpdated ext amountDue ext lastPaymentAmount isLatest");
		preCannedQueryMap.put("statements_bill","statement bill id accountId statementDate billingPeriodStart billingPeriodEnd dueDate lastPaymentDate lastUpdated ext amountDue ext lastPaymentAmount isLatest");
		preCannedQueryMap.put("statements_loan","statement loan id accountId statementDate billingPeriodStart billingPeriodEnd dueDate lastPaymentDate lastUpdated ext minimumPayment ext newCharges ext interestAmount ext principalAmount ext loanBalance ext amountDue ext lastPaymentAmount isLatest");
		//Account data point
		preCannedQueryMap.put("daily_data_point","dailyAccountDataPoint ?  accountId isAsset date asOfDate ext balance dataSourceType");
		preCannedQueryMap.put("weekly_data_point","weeklyAccountDataPoint ?  accountId isAsset date asOfDate ext balance dataSourceType");
		preCannedQueryMap.put("monthly_data_point","monthlyAccountDataPoint ?  accountId isAsset date asOfDate ext balance dataSourceType");
		
		//Account data point for non-deleted account
		preCannedQueryMap.put("nondeleted_data_point_d","dailyAccountDataPoint ? status NONDELETED  accountId isAsset date asOfDate ext balance dataSourceType");
		preCannedQueryMap.put("nondeleted_data_point_w","weeklyAccountDataPoint ? status NONDELETED  accountId isAsset date asOfDate ext balance dataSourceType");
		preCannedQueryMap.put("nondeleted_data_point_m","monthlyAccountDataPoint ? status NONDELETED  accountId isAsset date asOfDate ext balance dataSourceType");
		//=============
		
		//Account data point
		preCannedQueryMap.put("daily_data_point_count","dailyAccountDataPoint ?  total count id as count");
		preCannedQueryMap.put("weekly_data_point_count","weeklyAccountDataPoint ?  total count id as count");
		preCannedQueryMap.put("monthly_data_point_count","monthlyAccountDataPoint ? total count id as count");
				
		//Account data point for non-deleted account
		preCannedQueryMap.put("nondeleted_data_point_d_count","dailyAccountDataPoint ? status NONDELETED  total count id as count");
		preCannedQueryMap.put("nondeleted_data_point_w_count","weeklyAccountDataPoint ? status NONDELETED  total count id as count");
		preCannedQueryMap.put("nondeleted_data_point_m_count","monthlyAccountDataPoint ? status NONDELETED total count id as count");
	}
}
