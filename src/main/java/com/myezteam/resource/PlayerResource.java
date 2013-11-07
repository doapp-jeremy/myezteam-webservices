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
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Player;
import com.myezteam.db.TeamController;
import com.myezteam.db.mysql.PlayerDAO;
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

  public PlayerResource(TeamController teamController, TeamACL teamACL, PlayerDAO playerDAO) {
    this.teamController = teamController;
    this.teamACL = teamACL;
    this.playerDAO = playerDAO;
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
  public void deletePlayer(@Auth Long userId, @PathParam("team_id") Long teamId, @PathParam("player_id") Long playerId,
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
  public void changePlayerType(@Auth Long userId, @PathParam("team_id") Long teamId, @PathParam("player_id") Long playerId,
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
}
