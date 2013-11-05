/**
 * ApiKeyAuthenticator.java
 * webservices
 * 
 * Created by jeremy on Nov 5, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.auth;

import java.util.List;
import com.google.common.collect.Lists;


/**
 * @author jeremy
 * 
 */
public class ApiKeyAuthenticator {
  public static class ApiKeyException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApiKeyException(String message) {
      super(message);
    }
  }

  private static final List<String> API_KEYS = Lists.newArrayList(
      "9c0ba686-e06c-4a2c-821b-bae2a235fd3d"
      );

  public void validateApiKey(String apiKey) throws ApiKeyException {
    if (false == API_KEYS.contains(apiKey)) { throw new ApiKeyException("Invalid api key"); }
  }
}
