/*
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 */
package com.yodlee.domx.entity;

import java.util.HashMap;
import java.util.Map;

public enum BaseAggregationTypes {

    aggTypeId_1(1l,2l),
	aggTypeId_2(2l,2l),
	aggTypeId_3(3l,1l),
    aggTypeId_4(4l,1l),
    aggTypeId_5(5l,1l),
    aggTypeId_6(6l,1l),
    aggTypeId_7(7l,1l),
    aggTypeId_8(8l,1l),
    aggTypeId_9(9l,1l),
    aggTypeId_10(10l,1l),
    aggTypeId_11(11l,1l),
    aggTypeId_12(12l,3l),
    aggTypeId_13(13l,3l),
    aggTypeId_14(14l,3l),
    aggTypeId_15(15l,3l),
    aggTypeId_16(16l,3l),
    aggTypeId_17(17l,4l),
    aggTypeId_18(18l,4l),
    aggTypeId_19(19l,5l),
    aggTypeId_20(20l,5l),
    aggTypeId_21(21l,4l),
    aggTypeId_22(22l,5l);

    private final Long aggTypeId;
    private final Long baseAggTypeId;
    private static Map<Long, Long> baseAggMap;

    private BaseAggregationTypes(Long aggTypeId, Long baseAggTypeId) {
        this.aggTypeId = aggTypeId;
        this.baseAggTypeId = baseAggTypeId;
    }
    public Long getAggtype() {
        return aggTypeId;
    }

    public Long getBaseaggtype() {
        return baseAggTypeId;
    }


    public static Long getBaseAggTypeId(Long aggType) {
        if (baseAggMap == null) {
            initializeMapping();
        }
        if (baseAggMap.containsKey(aggType)) {
            return baseAggMap.get(aggType);
        }
        return null;
    }

    private static void initializeMapping() {
    	baseAggMap = new HashMap<Long, Long>();
        for (BaseAggregationTypes s : BaseAggregationTypes.values()) {
        	baseAggMap.put(s.aggTypeId, s.baseAggTypeId);
        }
    }


	
}
