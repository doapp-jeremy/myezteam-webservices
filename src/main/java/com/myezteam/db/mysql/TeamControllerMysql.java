/**
 * TeamController.java
 * webservices
 * 
 * Created by jeremy on Nov 3, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.db.mysql;

import java.util.List;
import com.myezteam.api.Team;
import com.myezteam.api.User;
import com.myezteam.db.TeamController;


/**
 * @author jeremy
 * 
 */
public class TeamControllerMysql extends TeamController {
  private final TeamDAO teamDAO;

  public TeamControllerMysql(TeamDAO teamDAO) {
    this.teamDAO = teamDAO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#findTeamsUserOwns(java.lang.Long)
   */
  @Override
  protected List<Team> findTeamsUserOwns(Long userId) {
    return teamDAO.findTeamsUserOwns(userId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#findTeamsUserManages(java.lang.Long)
   */
  @Override
  protected List<Team> findTeamsUserManages(Long userId) {
    return teamDAO.findTeamsUserManages(userId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#findTeamsUserPlaysOn(java.lang.Long)
   */
  @Override
  protected List<Team> findTeamsUserPlaysOn(Long userId) {
    return teamDAO.findTeamsUserPlaysOn(userId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#save(com.myezteam.api.Team)
   */
  @Override
  public void save(Team team) {
    teamDAO.update(team);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#addManagers(java.lang.Long, java.util.List)
   */
  @Override
  public void addManagers(Long teamId, List<Long> userIds) {
    for (Long managerId : userIds) {
      teamDAO.addManager(teamId, managerId);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#getManagers(java.lang.Long)
   */
  @Override
  public List<User> getManagers(Long teamId) {
    return teamDAO.getManagers(teamId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#removeManager(java.lang.Long, java.lang.Long)
   */
  @Override
  public void removeManager(Long teamId, Long managerId) {
    teamDAO.removeManager(teamId, managerId);
  }
}
