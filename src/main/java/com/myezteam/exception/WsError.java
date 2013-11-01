/**
 * ReadfulError.java
 * readful-ws
 * 
 * Created by jeremy on Jun 12, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.exception;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author jeremy
 * 
 */
public class WsError {
  @JsonProperty
  private final String status = "error";

  @JsonProperty
  private final int code;

  @JsonProperty
  private final String message;

  /**
   * @param errors
   */
  public WsError(String message, int code) {
    this.message = message;
    this.code = code;
  }

  public WsError(String message) {
    this(message, 0);
  }

  /**
   * 
   */
  public WsError(Throwable t) {
    this(t.getMessage(), WsExceptionMessage.UNKNOWN_ERROR.getCode());
  }

  public WsError(WsException e) {
    this(e.getMessage(), e.getWsExceptionMessage().getCode());
  }

}
