/**
 * TeamController.java
 * webservices
 * 
 * Created by jeremy on Nov 4, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.myezteam.api.Email;
import com.myezteam.api.Player;
import com.myezteam.api.Team;
import com.myezteam.api.User;


/**
 * @author jeremy
 * 
 */
public abstract class TeamController {

  public Map<String, List<Team>> getUsersTeams(Long userId) throws ExecutionException {
    Map<String, List<Team>> teamsByPerm = new HashMap<String, List<Team>>();
    teamsByPerm.put("owner", getTeamsUserOwns(userId));
    teamsByPerm.put("manager", getTeamsUserManages(userId));
    teamsByPerm.put("player", getTeamsUserPlaysOn(userId));
    return teamsByPerm;
  }

  protected abstract List<Team> findTeamsUserOwns(Long userId);

  public List<Team> getTeamsUserOwns(Long userId) throws ExecutionException {
    return ownerTeams.get(userId);
  }

  private final LoadingCache<Long, List<Team>> ownerTeams = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
      .maximumSize(1000).build(new CacheLoader<Long, List<Team>>() {
        @Override
        public List<Team> load(Long userId) throws Exception {
          List<Team> teams = findTeamsUserOwns(userId);
          if (teams == null) {
            teams = new ArrayList<Team>();
          }
          return teams;
        }
      });

  protected abstract List<Team> findTeamsUserManages(Long userId);

  public List<Team> getTeamsUserManages(Long userId) throws ExecutionException {
    return managerTeams.get(userId);
  }

  private final LoadingCache<Long, List<Team>> managerTeams = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
      .maximumSize(1000).build(new CacheLoader<Long, List<Team>>() {
        @Override
        public List<Team> load(Long userId) throws Exception {
          List<Team> teams = findTeamsUserManages(userId);
          if (teams == null) {
            teams = new ArrayList<Team>();
          }
          return teams;
        }
      });

  protected abstract List<Team> findTeamsUserPlaysOn(Long userId);

  public List<Team> getTeamsUserPlaysOn(Long userId) throws ExecutionException {
    return playerTeams.get(userId);
  }

  private final LoadingCache<Long, List<Team>> playerTeams = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
      .maximumSize(1000).build(new CacheLoader<Long, List<Team>>() {
        @Override
        public List<Team> load(Long userId) throws Exception {
          List<Team> teams = findTeamsUserPlaysOn(userId);
          if (teams == null) {
            teams = new ArrayList<Team>();
          }
          return teams;
        }
      });

  /**
   * @param team
   * @return
   */
  public abstract Team save(Team team);

  /**
   * @param teamId
   * @param userIds
   */
  public abstract void addManagers(Long teamId, List<Long> userIds);

  /**
   * @param teamId
   * @return
   */
  public abstract List<User> getManagers(Long teamId);

  /**
   * @param teamId
   * @param managerId
   */
  public abstract void removeManager(Long teamId, Long managerId);

  /**
   * @param teamId
   * @param player
   */
  public abstract void addPlayer(Long teamId, Player player);

  /**
   * @param teamId
   * @return
   */
  public abstract List<Player> getPlayers(Long teamId);

  /**
   * @param teamId
   * @param playerId
   */
  public abstract void removePlayer(Long teamId, Long playerId);

  public abstract void updatePlayerType(Long teamId, Long playerId, Long playerTypeId);

  public abstract User getOwnerOfTeam(Long teamId);

  public abstract void deleteTeam(Long teamId);

  public abstract List<Email> getDefaultEmails(Long teamId);

}
