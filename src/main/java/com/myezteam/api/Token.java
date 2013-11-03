/**
 * Token.java
 * webservices
 * 
 * Created by jeremy on Nov 2, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@DynamoDBTable(tableName = "tokens")
@JsonSnakeCase
public class Token {
  @JsonProperty
  private String token;
  @JsonProperty
  private Long userId;
  @JsonProperty
  private Long created;

  public Token() {}

  public Token(String token, long userId) {
    this.token = token;
    this.userId = userId;
  }

  /**
   * @param token2
   */
  public Token(String token) {
    this.token = token;
  }

  @DynamoDBHashKey(attributeName = "token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @DynamoDBAttribute(attributeName = "user_id")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @DynamoDBAttribute(attributeName = "created")
  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }
}
