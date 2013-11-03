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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.google.common.collect.Lists;
import com.myezteam.api.Team;
import com.myezteam.db.TeamDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/teams")
public class TeamResource {
  private final TeamDAO teamDAO;

  public TeamResource(TeamDAO teamDAO) {
    this.teamDAO = teamDAO;
  }

  @GET
  public List<Team> list(@Auth long userId) {
    try {
      // return teamDAO.findUsersTeams(userId);

      Set<Team> teams = new HashSet<Team>();
      teams.addAll(teamDAO.findTeamsUserOwns(userId));
      teams.addAll(teamDAO.findTeamsUserManages(userId));
      teams.addAll(teamDAO.findTeamsUserPlaysOn(userId));

      return Lists.newArrayList(teams.iterator());
    } catch (Throwable e) {
      e.printStackTrace();
      throw new WebApplicationException(e);
    }
  }
}
