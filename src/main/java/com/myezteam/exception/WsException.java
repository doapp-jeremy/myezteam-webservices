/**
 * ReadfulException.java
 * readful-data
 * 
 * Created by jeremy on May 6, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.exception;

/**
 * @author jeremy
 * 
 */
public class WsException extends Exception {

  private static final long serialVersionUID = 1L;
  private final WsExceptionMessage exceptionMessage;

  public WsException(WsExceptionMessage exceptionMessage) {
    super(exceptionMessage.getMessage());
    this.exceptionMessage = exceptionMessage;
  }

  public WsException(WsExceptionMessage exceptionMesssage, String message, Throwable t) {
    super(exceptionMesssage.getMessage() + ": " + message, t);
    this.exceptionMessage = exceptionMesssage;
  }

  public WsException(WsExceptionMessage exceptionMesssage, String message) {
    super(exceptionMesssage.getMessage() + ": " + message);
    this.exceptionMessage = exceptionMesssage;
  }

  public WsExceptionMessage getWsExceptionMessage() {
    return this.exceptionMessage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Throwable#toString()
   */
  @Override
  public String toString() {
    String message = getMessage();
    Throwable t = getCause();
    if (t != null) {
      message += ": " + t.getMessage();
    }
    return message;
  }
}
