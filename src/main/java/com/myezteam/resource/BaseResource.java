/**
 * ApiKeyResource.java
 * webservices
 * 
 * Created by jeremy on Nov 5, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.resource;

import com.myezteam.auth.ApiKeyAuthenticator;
import com.myezteam.auth.ApiKeyAuthenticator.ApiKeyException;


/**
 * @author jeremy
 * 
 */
public abstract class BaseResource {
  public static final String API_KEY = "api_key";
  public static final String API_KEY_PARAM = "{" + API_KEY + "}";
  private final ApiKeyAuthenticator apiKeyAuthenticator;

  protected BaseResource() {
    this.apiKeyAuthenticator = new ApiKeyAuthenticator();
  }

  protected void checkApiKey(String apiKey) throws ApiKeyException {
    apiKeyAuthenticator.validateApiKey(apiKey);
  }
}
