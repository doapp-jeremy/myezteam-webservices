/**
 * TokenAuthenticator.java
 * webservices
 * 
 * Created by jeremy on Nov 2, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.auth;

import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Optional;
import com.myezteam.api.Token;
import com.myezteam.db.mysql.TokenDAO;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;


/**
 * @author jeremy
 * 
 */
public class TokenAuthenticator implements Authenticator<String, Long> {

  private final TokenDAO tokenDAO;

  private final Map<String, Long> userIdCache = new HashMap<String, Long>();

  public TokenAuthenticator(TokenDAO tokenDAO) {
    this.tokenDAO = tokenDAO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yammer.dropwizard.auth.Authenticator#authenticate(java.lang.Object)
   */
  @Override
  public Optional<Long> authenticate(String credentials) throws AuthenticationException {
    Long userId = userIdCache.get(credentials);
    if (userId != null) {
      return Optional.of(userId);
    }
    else {
      Token token = tokenDAO.getToken(credentials);
      if (token != null) {
        userId = token.getUserId();
        userIdCache.put(credentials, userId);
        return Optional.of(userId);
      }
    }
    return null;
  }

}
