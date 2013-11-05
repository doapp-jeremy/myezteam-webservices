/**
 * AclException.java
 * webservices
 * 
 * Created by jeremy on Nov 4, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.acl;

/**
 * @author jeremy
 * 
 */
public class AclException extends Exception {
  /**
   * @param string
   */
  public AclException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 1L;

}
