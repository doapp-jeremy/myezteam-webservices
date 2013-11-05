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

import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Team;
import com.myezteam.db.TeamController;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/teams")
public class TeamResource {
  private final TeamController teamController;
  private final TeamACL teamACL;

  public TeamResource(TeamController teamController, TeamACL teamACL) {
    this.teamController = teamController;
    this.teamACL = teamACL;
  }

  @GET
  public List<Team> player(@Auth Long userId) {
    try {
      return teamController.getTeamsUserPlaysOn(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/owner")
  public List<Team> owner(@Auth Long userId) {
    try {
      return teamController.getTeamsUserOwns(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/manager")
  public List<Team> manager(@Auth Long userId) {
    try {
      return teamController.getTeamsUserManages(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/all")
  public Map<String, List<Team>> all(@Auth Long userId) {
    try {
      return teamController.getUsersTeams(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/{id}")
  public Team get(@Auth long userId, @PathParam("{id}") long teamId) {
    try {
      // verify user has access to team
      return teamACL.valideRead(userId, teamId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }
}
