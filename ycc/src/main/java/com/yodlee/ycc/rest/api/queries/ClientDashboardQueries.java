/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.ycc.rest.api.queries;

public class ClientDashboardQueries {

	public static final String GET_FIRMS_FOR_CORBAND = "select distinct(firm_id_key) as firm_name from mem_pref_mv mpv, mem m where m.cobrand_id=? and mpv.mem_id=m.mem_id";
	
	public static final String GET_ACCOUNT_DETAILS_FOR_FIRM ="SELECT ina.item_account_id AS ACCOUNT_ID, "+
			 " ina.account_number AS ACCOUNT_NUMBER, "+
			 " ina.account_name AS ACCOUNT_NAME, "+
			 " nvl2(ina.total_balance,'$'||ina.total_balance,' ') AS TOTAL_BALANCE, "+
			 " to_char( cast(epoch_to_date(ci.last_update_attempt) as timestamp with local time zone), 'yyyy-mm-dd hh24:mi TZD') as LAST_REFRESH_ATTEMPT, "+
			 " to_char( cast(epoch_to_date(ci.last_successful_refresh_time) as timestamp with local time zone), 'yyyy-mm-dd hh24:mi TZD') as LAST_SUCCESSFUL_REFRESH, "+
			 " ci.code AS RESPONSE, "+
			 " dmc.value as FI_NAME, "+
			 " ina.account_holder as CLIENT_NAME "+
			 " FROM mem_pref_mv mpv, "+ 
			 " mem_item mi, "+
			 " investment_account ina, "+
			 " cache_info ci, "+
			 " sum_info si, "+
			 " db_message_catalog dmc "+
			 " WHERE mpv.mem_id=mi.mem_id "+
			 " AND mi.cache_item_id=ina.cache_item_id "+
			 " AND ina.cache_item_id=ci.cache_item_id "+
			 " AND ci.sum_info_id=si.sum_info_id "+
			 " AND si.mc_key=dmc.mc_key "+
			 " AND mpv.firm_id_key=? "+
			 " AND rownum<1500 " +
			 " UNION "+
			 " SELECT ba.item_account_id AS ACCOUNT_ID, "+
			 " ba.account_number AS ACCOUNT_NUMBER, "+
			 " ba.account_name AS ACCOUNT_NAME, "+
			 " nvl2(ba.current_balance,'$'||ba.current_balance,' ') AS TOTAL_BALANCE, " +
			 " to_char( cast(epoch_to_date(ci.last_update_attempt) as timestamp with local time zone), 'yyyy-mm-dd hh24:mi TZD') as LAST_REFRESH_ATTEMPT, "+
			 " to_char( cast(epoch_to_date(ci.last_successful_refresh_time) as timestamp with local time zone), 'yyyy-mm-dd hh24:mi TZD') as LAST_SUCCESSFUL_REFRESH, "+
			 " ci.code AS RESPONSE, "+
			 " dmc.value as FI_NAME, "+
			 " ba.account_holder as CLIENT_NAME "+
			 " FROM mem_pref_mv mpv, "+
			 " mem_item mi, "+
			 " bank_account ba, "+
			 " cache_info ci, "+
			 " sum_info si, "+
			 " db_message_catalog dmc "+
			 " WHERE mpv.mem_id=mi.mem_id "+
			 " AND mi.cache_item_id=ba.cache_item_id "+
			 " AND ba.cache_item_id=ci.cache_item_id "+
			 " AND ci.sum_info_id=si.sum_info_id "+
			 " AND si.mc_key=dmc.mc_key "+
			 " AND mpv.firm_id_key=? " +
			 " AND rownum<1500 ";
}
