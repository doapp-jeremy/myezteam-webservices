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
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.myezteam.api.Team;
import com.myezteam.api.User;
import com.myezteam.db.TeamController;
import com.myezteam.db.mysql.UserDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/users")
public class UserResource extends BaseResource {
  private final UserDAO userDAO;
  private final TeamController teamController;

  public UserResource(UserDAO userDAO, TeamController teamController) {
    this.userDAO = userDAO;
    this.teamController = teamController;
  }

  @GET
  public User me(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      return userDAO.findById(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @POST
  public User create(@QueryParam(API_KEY) String apiKey, User user) {
    try {
      checkApiKey(apiKey);
      String email = checkNotNull(user.getEmail(), "Email is required");
      checkNotNull(user.getPassword(), "Password is required");
      User existingUser = userDAO.findByEmail(email);
      if (existingUser != null) { throw new Exception("A user with email " + email + " already exists"); }
      userDAO.createUser(user);
      Long newId = userDAO.getLastInsertId();
      return new User(newId, email, user.getFirstName(), user.getLastName());
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @PUT
  public void update(@Auth Long userId, @QueryParam(API_KEY) String apiKey, User user) {
    try {
      checkApiKey(apiKey);
      checkNotNull(user.getId(), "User id is null");
      checkArgument(userId.equals(user.getId()), "Can not update the profile of another user");
      userDAO.updateUser(user);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/friends")
  public List<User> friends(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);

      Set<Long> teamIds = new HashSet<Long>();
      String teamIdsString = "";

      for (Team team : teamController.getTeamsUserManages(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }
      for (Team team : teamController.getTeamsUserOwns(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }
      for (Team team : teamController.getTeamsUserPlaysOn(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }

      // this is a hack until I can figure out the correct jdbi IN () call
      return userDAO.findUsersOnTeams(teamIdsString);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }
}
