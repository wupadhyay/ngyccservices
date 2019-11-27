package com.yodlee.yccdomx.base;

import java.util.List;
import java.util.Locale;

import com.yodlee.framework.runtime.shared.dom.IEntity;

/*
* Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
/*
 * knavuluri
 */
public abstract  class ServiceBaseExt<E extends IEntity> extends EntityBaseExt<E, Long> {
 	protected List<Locale> locales;
	

	protected ServiceBaseExt(Class<E> entityJavaType) {
		super(entityJavaType);
	}

	@Override
	public void init(E entity, Object context) {
		super.init(entity, context);
		
	}
	
}
