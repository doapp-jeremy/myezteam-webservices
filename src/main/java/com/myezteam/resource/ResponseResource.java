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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Event;
import com.myezteam.api.Player;
import com.myezteam.api.Response;
import com.myezteam.db.mysql.EventDAO;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.ResponseDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/responses")
public class ResponseResource extends BaseResource {
  private final TeamACL teamACL;
  private final ResponseDAO responseDAO;
  private final EventDAO eventDAO;
  private final PlayerDAO playerDAO;

  public ResponseResource(TeamACL teamACL, ResponseDAO responseDAO, EventDAO eventDAO, PlayerDAO playerDAO) {
    this.teamACL = teamACL;
    this.responseDAO = responseDAO;
    this.eventDAO = eventDAO;
    this.playerDAO = playerDAO;
  }

  @GET
  @Path("/{event_id}")
  public List<Response> get(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("event_id") Long eventId) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(eventId, "Event id is null");

      Event event = eventDAO.findEventById(eventId);

      teamACL.validateReadAccess(userId, event.getTeamId());

      return responseDAO.findUsersResponsesForEvent(userId, eventId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @POST
  public void create(@Auth Long userId, @QueryParam(API_KEY) String apiKey, Response response) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(response, "Response is null");
      checkNotNull(response.getEventId(), "Event id is null");
      checkNotNull(response.getPlayerId(), "Player is null");
      checkNotNull(response.getResponseTypeId(), "Response type id is null");

      Player player = playerDAO.findPlayer(response.getPlayerId());
      checkNotNull(player, "Invalid player id");
      if (false == userId.equals(player.getUserId())) {
        teamACL.validateWriteAccess(userId, player.getTeamId());
      }

      responseDAO.create(response);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

}
