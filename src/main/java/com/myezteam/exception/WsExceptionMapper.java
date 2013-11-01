package com.myezteam.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class WsExceptionMapper implements ExceptionMapper<Throwable> {

  public Response toResponse(Throwable e) {
    e.printStackTrace();
    int responseCode = 500;
    try
    {
      Object entity = getEntityFromThrowable(e);
      responseCode = getResponseCodeFromThrowable(e);
      return Response.status(responseCode).type(MediaType.APPLICATION_JSON).entity(entity).build();
    } catch (Throwable t) {
      Object entity = getEntityFromThrowable(t);
      return Response.status(responseCode).type(MediaType.APPLICATION_JSON).entity(entity).build();
    }

  }

  private int getResponseCodeFromThrowable(Throwable e) {
    int result = 500;

    if (e instanceof ConstraintViolationException) {
      result = 422;
    }
    else if (e instanceof WebApplicationException) {
      Response response = ((WebApplicationException) e).getResponse();
      result = response.getStatus();
    }
    return result;
  }

  private Object getEntityFromThrowable(Throwable e) {
    Object result = e.getMessage();
    if (e instanceof ConstraintViolationException) {
      result = new WsValidationError((ConstraintViolationException) e);
    }
    else if (e instanceof ExceptionInInitializerError)
    {
      result = new WsError(((ExceptionInInitializerError) e).getException().getMessage());
    }
    else {
      Throwable cause = e.getCause();
      if (cause instanceof WsException) {
        result = new WsError((WsException) cause);
      }
      else if (cause != null)
      {
        result = new WsError(cause);
      }
      else {
        result = new WsError(e.getMessage());
      }
    }
    return result;
  }
}
