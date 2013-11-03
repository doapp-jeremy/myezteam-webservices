/**
 * TokenDAO.java
 * webservices
 * 
 * Created by jeremy on Nov 2, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.myezteam.api.Token;


/**
 * @author jeremy
 * 
 */
public class TokenDAO {
  private final DynamoDBMapper dynamoDBMapper;

  public TokenDAO(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public Token getToken(String token) {
    return dynamoDBMapper.load(new Token(token));
  }

  public void saveToken(Token token) {
    dynamoDBMapper.save(token);
  }

}
