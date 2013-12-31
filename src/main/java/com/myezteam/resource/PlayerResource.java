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

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Player;
import com.myezteam.api.User;
import com.myezteam.db.TeamController;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.UserDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/players")
public class PlayerResource extends BaseResource {
  private final TeamController teamController;
  private final TeamACL teamACL;
  private final PlayerDAO playerDAO;
  private final UserDAO userDAO;

  public PlayerResource(TeamController teamController, TeamACL teamACL, PlayerDAO playerDAO, UserDAO userDAO) {
    this.teamController = teamController;
    this.teamACL = teamACL;
    this.playerDAO = playerDAO;
    this.userDAO = userDAO;
  }

  @GET
  public List<Player> myPlayers(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(userId, "User id is empty");
      return playerDAO.getPlayersForUser(userId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @GET
  @Path("/team/{team_id}/me")
  public Player myPlayer(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("team_id") Integer teamId) {
    try {
      checkApiKey(apiKey);
      checkNotNull(userId, "User id is empty");
      checkNotNull(teamId, "Team id is empty");
      return playerDAO.getPlayerForTeam(teamId, userId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @GET
  @Path("/team/{team_id}")
  public List<Player> players(@Auth Long userId, @PathParam("team_id") Long teamId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userId, "User id is empty");
      teamACL.validateReadAccess(userId, teamId);
      return teamController.getPlayers(teamId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  public static class NewPlayer {
    @JsonProperty("team_id")
    private long teamId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("player_type_id")
    private int playerTypeId;

    /**
     * @return the teamId
     */
    public long getTeamId() {
      return teamId;
    }

    /**
     * @return the email
     */
    public String getEmail() {
      return email;
    }

    /**
     * @return the playerTypeId
     */
    public int getPlayerTypeId() {
      return playerTypeId;
    }
  }

  @POST
  public List<Player> addPlayer(@Auth Long userId, @QueryParam(API_KEY) String apiKey, NewPlayer newPlayer) {
    try {
      checkApiKey(apiKey);
      checkNotNull(userId, "User id is empty");
      long teamId = checkNotNull(newPlayer.getTeamId(), "Team id is empty");
      String email = checkNotNull(newPlayer.getEmail(), "Player user email is empty");
      int playerTypeId = checkNotNull(newPlayer.getPlayerTypeId(), "Player type id is empty");
      teamACL.validateWriteAccess(userId, teamId);
      User user = userDAO.findByEmail(email);
      if (user == null) {
        // create new user
        userDAO.createUser(email);
        user = new User(userDAO.getLastInsertId(), email);
      }
      Player player = new Player(teamId, user.getId(), playerTypeId);
      teamController.addPlayer(teamId, player);
      return teamController.getPlayers(teamId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @POST
  @Path("/team/{team_id}")
  public List<Player> addPlayer(@Auth Long userId, @PathParam("team_id") Long teamId, @QueryParam(API_KEY) String apiKey,
      Player player) {
    try {
      checkApiKey(apiKey);
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userId, "User id is empty");
      checkNotNull(player.getUserId(), "Player user id is empty");
      checkNotNull(player.getPlayerTypeId(), "Player type id is empty");
      teamACL.validateWriteAccess(userId, teamId);
      teamController.addPlayer(teamId, player);
      return teamController.getPlayers(teamId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @DELETE
  @Path("/team/{team_id}/{player_id}")
  public void deletePlayerOld(@Auth Long userId, @PathParam("team_id") Long teamId, @PathParam("player_id") Long playerId,
      @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userId, "User id is empty");
      checkNotNull(playerId, "Player id is empty");
      teamACL.validateWriteAccess(userId, teamId);
      teamController.removePlayer(teamId, playerId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @PUT
  @Path("/team/{team_id}/{player_id}/{player_type_id}")
  public void changePlayerTypeOld(@Auth Long userId, @PathParam("team_id") Long teamId, @PathParam("player_id") Long playerId,
      @PathParam("player_type_id") Long playerTypeId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(teamId, "Team id is empty");
      checkNotNull(userId, "User id is empty");
      checkNotNull(playerId, "Player id is empty");
      checkNotNull(playerId, "Player type id is empty");
      teamACL.validateWriteAccess(userId, teamId);
      teamController.updatePlayerType(teamId, playerId, playerTypeId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  // @POST
  // public List<Player> addPlayer(@Auth Long userId, @QueryParam(API_KEY) String apiKey, Player
  // player) {
  // try {
  // checkApiKey(apiKey);
  // checkNotNull(player, "Player is empty");
  // Long teamId = checkNotNull(player.getTeamId(), "Team id is empty");
  // checkNotNull(userId, "User id is empty");
  // checkNotNull(player.getUserId(), "Player user id is empty");
  // checkNotNull(player.getPlayerTypeId(), "Player type id is empty");
  // teamACL.validateWriteAccess(userId, teamId);
  // teamController.addPlayer(teamId, player);
  // return teamController.getPlayers(teamId);
  // } catch (Throwable t) {
  // throw new WebApplicationException(t);
  // }
  // }

  @DELETE
  @Path("/{player_id}")
  public void deletePlayer(@Auth Long userId, @PathParam("player_id") Long playerId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(userId, "User id is empty");
      checkNotNull(playerId, "Player id is empty");
      Player player = checkNotNull(playerDAO.findPlayer(playerId), "Could not find player for id: " + playerId);
      Long teamId = player.getTeamId();
      teamACL.validateWriteAccess(userId, teamId);
      teamController.removePlayer(teamId, playerId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @PUT
  @Path("/{player_id}/{player_type_id}")
  public void changePlayerType(@Auth Long userId, @PathParam("player_id") Long playerId,
      @PathParam("player_type_id") Long playerTypeId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      checkNotNull(userId, "User id is empty");
      checkNotNull(playerId, "Player id is empty");
      checkNotNull(playerId, "Player type id is empty");
      Player player = playerDAO.findPlayer(playerId);
      Long teamId = player.getTeamId();
      teamACL.validateWriteAccess(userId, teamId);
      teamController.updatePlayerType(teamId, playerId, playerTypeId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }
}
