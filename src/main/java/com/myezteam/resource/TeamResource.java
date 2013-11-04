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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.myezteam.api.Team;
import com.myezteam.db.TeamDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/teams")
public class TeamResource {
  private final TeamDAO teamDAO;

  public TeamResource(TeamDAO teamDAO) {
    this.teamDAO = teamDAO;
  }

  @GET
  public List<Team> player(@Auth long userId) {
    try {
      return teamDAO.findTeamsUserPlaysOn(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/owner")
  public List<Team> owner(@Auth long userId) {
    try {
      return teamDAO.findTeamsUserOwns(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/manager")
  public List<Team> manager(@Auth long userId) {
    try {
      return teamDAO.findTeamsUserManages(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/all")
  public Map<String, List<Team>> all(@Auth long userId) {
    try {
      Map<String, List<Team>> teams = new HashMap<String, List<Team>>();
      teams.put("owner", teamDAO.findTeamsUserOwns(userId));
      teams.put("manager", teamDAO.findTeamsUserManages(userId));
      teams.put("player", teamDAO.findTeamsUserPlaysOn(userId));

      return teams;
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }
}
