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

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.google.common.base.Strings;
import com.myezteam.api.User;
import com.myezteam.db.UserDAO;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {
  private final UserDAO userDAO;

  public AuthResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @POST
  @Path("/login")
  public User login(Map<String, String> body) {
    try {
      checkArgument(false == Strings.isNullOrEmpty(body.get("email")), "email is required");
      checkArgument(false == Strings.isNullOrEmpty(body.get("password")), "password is required");
      User user = userDAO.authenticate(body.get("email"), body.get("password"));
      if (user == null) { throw new Exception("Invalid login"); }
      return user;
    } catch (Throwable e) {
      e.printStackTrace();
      throw new WebApplicationException(e);
    }
  }
}
