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
import com.myezteam.api.Player;
import com.myezteam.api.Team;
import com.myezteam.api.User;
import com.myezteam.db.TeamController;


/**
 * @author jeremy
 * 
 */
public class TeamControllerMysql extends TeamController {
  private final TeamDAO teamDAO;
  private final PlayerDAO playerDAO;

  public TeamControllerMysql(TeamDAO teamDAO, PlayerDAO playerDAO) {
    this.teamDAO = teamDAO;
    this.playerDAO = playerDAO;
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
  public Team save(Team team) {
    if (null == team.getId()) {
      teamDAO.create(team);
      // this is a hack until I can figure out how to get the id of the newly generated team
      return teamDAO.getLastCreatedTeam(team.getOwnerId());
    }
    else {
      teamDAO.update(team);
    }
    return team;
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

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#addPlayer(java.lang.Long, com.myezteam.api.Player)
   */
  @Override
  public void addPlayer(Long teamId, Player player) {
    playerDAO.addPlayer(teamId, player);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#getPlayers(java.lang.Long)
   */
  @Override
  public List<Player> getPlayers(Long teamId) {
    return playerDAO.getPlayersForTeam(teamId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#removePlayer(java.lang.Long, java.lang.Long)
   */
  @Override
  public void removePlayer(Long teamId, Long playerId) {
    playerDAO.removePlayer(teamId, playerId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#updatePlayerType(java.lang.Long, java.lang.Long,
   * java.lang.Long)
   */
  @Override
  public void updatePlayerType(Long teamId, Long playerId, Long playerTypeId) {
    playerDAO.updatePlayerType(teamId, playerId, playerTypeId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#getOwnerOfTeam(java.lang.Long)
   */
  @Override
  public User getOwnerOfTeam(Long teamId) {
    return teamDAO.getOwner(teamId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.myezteam.db.TeamController#deleteTeam(java.lang.Long)
   */
  @Override
  public void deleteTeam(Long teamId) {
    teamDAO.deleteTeam(teamId);
  }
}
