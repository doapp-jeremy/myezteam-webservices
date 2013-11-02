/**
 * WebApplicationExceptionMapper.java
 * push-service
 * 
 * Created by jeremy on Aug 15, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.log4j.Logger;
import com.amazonaws.services.cloudfront.model.InvalidArgumentException;


/**
 * @author jeremy
 * 
 */
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
  static final Logger log = Logger.getLogger(WebApplicationExceptionMapper.class);

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
   */
  @Override
  public Response toResponse(WebApplicationException e) {
    log.warn(e);
    log.info("Exception: " + e.getMessage());
    e.printStackTrace();
    Throwable t = e.getCause();
    if (t != null) {
      t.printStackTrace();
      if ((t instanceof IllegalArgumentException) || (t instanceof InvalidArgumentException)) {
        return Response.status(400)
            .type(MediaType.APPLICATION_JSON)
            .entity(new WsError(t))
            .build();
      }
      else {
        return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new WsError(t)).build();
      }
    }
    return Response.status(500)
        .type(MediaType.APPLICATION_JSON)
        .entity(new WsError(e))
        .build();
  }

}
