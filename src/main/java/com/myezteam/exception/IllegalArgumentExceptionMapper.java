/**
 * InvalidEntityExceptionMapper.java
 * push-service
 * 
 * Created by jeremy on Aug 15, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * @author jeremy
 * 
 */
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
   */
  @Override
  public Response toResponse(IllegalArgumentException e) {
    return Response.status(400)
        .type(MediaType.APPLICATION_JSON)
        .entity(new Error(e))
        .build();
  }

}
