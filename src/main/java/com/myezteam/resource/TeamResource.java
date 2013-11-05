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
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.google.common.base.Strings;
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Team;
import com.myezteam.api.User;
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
  public Team get(@Auth Long userId, @PathParam("id") Long teamId) {
    try {
      // verify user has access to team
      return teamACL.validateReadAccess(userId, teamId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @POST
  public void create(@Auth Long userId, @PathParam("id") Long teamId, Team team) {
    try {
      checkNotNull(teamId, "Team id is null");
      checkNotNull(team, "Team is empty");
      checkArgument(team.getId() == teamId, "Team id's don't match");
      checkArgument(false == Strings.isNullOrEmpty(team.getName()), "Name is empty");

      // verify user has access to team
      teamACL.validateWriteAccess(userId, teamId);
      teamController.save(team);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @PUT
  public void update(@Auth Long userId, Team team) {
    try {
      checkNotNull(team, "Team is empty");
      checkNotNull(team.getId(), "Team id is empty");
      checkArgument(false == Strings.isNullOrEmpty(team.getName()), "Name is empty");

      // verify user has access to team
      teamACL.validateWriteAccess(userId, team.getId());
      teamController.save(team);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/{id}/managers")
  public List<User> managers(@Auth Long userId, @PathParam("id") Long teamId) {
    try {
      checkNotNull(teamId, "Team id is empty");
      teamACL.validateOwner(userId, teamId);
      return teamController.getManagers(teamId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @POST
  @Path("/{id}/managers")
  public void addManagers(@Auth Long userId, @PathParam("id") Long teamId, List<Long> userIds) {
    try {
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userIds, "User ids is empty");
      teamACL.validateOwner(userId, teamId);
      teamController.addManagers(teamId, userIds);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @DELETE
  @Path("/{id}/managers/{user_id}")
  public void deleteManager(@Auth Long userId, @PathParam("id") Long teamId, @PathParam("user_id") Long managerId) {
    try {
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userId, "User id is empty");
      teamACL.validateOwner(userId, teamId);
      teamController.removeManager(teamId, managerId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }
}
