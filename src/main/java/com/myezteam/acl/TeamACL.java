/**
 * TeamACL.java
 * webservices
 * 
 * Created by jeremy on Nov 4, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.acl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import com.myezteam.api.Team;
import com.myezteam.db.TeamController;


/**
 * @author jeremy
 * 
 */
public class TeamACL {
  private final TeamController teamController;

  public TeamACL(TeamController teamController) {
    this.teamController = teamController;
  }

  public Team validateReadAccess(Long userId, Long teamId) throws ExecutionException, AclException {
    Team team = new Team(teamId);
    for (List<Team> teams : teamController.getUsersTeams(userId).values()) {
      if (teams.contains(team)) { return teams.get(teams.indexOf(team)); }
    }
    throw new AclException("Not authorized");
  }

  public Team validateWriteAccess(Long userId, Long teamId) throws ExecutionException, AclException {
    Team team = new Team(teamId);
    List<Team> ownerTeams = teamController.getTeamsUserOwns(userId);
    if (ownerTeams.contains(team)) { return ownerTeams.get(ownerTeams.indexOf(team)); }
    List<Team> managerTeams = teamController.getTeamsUserManages(userId);
    if (managerTeams.contains(team)) { return managerTeams.get(managerTeams.indexOf(team)); }
    throw new AclException("Not authorized");
  }

  public Team validateOwner(Long userId, Long teamId) throws ExecutionException, AclException {
    Team team = new Team(teamId);
    List<Team> ownerTeams = teamController.getTeamsUserOwns(userId);
    if (ownerTeams.contains(team)) { return ownerTeams.get(ownerTeams.indexOf(team)); }
    throw new AclException("Not authorized");
  }
}
