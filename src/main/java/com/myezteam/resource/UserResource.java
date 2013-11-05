/**
 * TeamResource.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.myezteam.api.User;
import com.myezteam.db.mysql.UserDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/users")
public class UserResource {
  private final UserDAO userDAO;

  public UserResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @GET
  public User me(@Auth Long userId) {
    try {
      return userDAO.findById(userId);
    } catch (Throwable e) {
      e.printStackTrace();
      throw new WebApplicationException(e);
    }
  }
}
