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
}
