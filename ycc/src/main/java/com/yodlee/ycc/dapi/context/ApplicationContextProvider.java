/**
 * 
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */

package com.yodlee.ycc.dapi.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author knavuluri
 *
 */
@Component
public class ApplicationContextProvider {

  private static ApplicationContext context;

  /**
   *
   */
  @Autowired
  public void setApplicationContext(final ApplicationContext context) {
    ApplicationContextProvider.context = context;
  }

  /**
   * @return the context
   */
  public static ApplicationContext getContext() {
    return ApplicationContextProvider.context;
  }

}
