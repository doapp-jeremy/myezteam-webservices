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

import static com.google.common.base.Preconditions.checkArgument;
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
import com.myezteam.api.Email;
import com.myezteam.api.Event;
import com.myezteam.api.Response;
import com.myezteam.db.TeamController;
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.EventDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/events")
public class EventResource extends BaseResource {
  private final TeamController teamController;
  private final TeamACL teamACL;
  private final EventDAO eventDAO;
  private final EmailDAO emailDAO;

  public EventResource(TeamController teamController, TeamACL teamACL, EventDAO eventDAO, EmailDAO emailDAO) {
    this.teamController = teamController;
    this.teamACL = teamACL;
    this.eventDAO = eventDAO;
    this.emailDAO = emailDAO;
  }

  @GET
  public List<Event> list(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);

      List<Event> events = eventDAO.findUpcomingEvents(userId);

      return events;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @GET
  @Path("/{id}")
  public Event get(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long eventId) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(eventId, "Event id is null");

      Event event = eventDAO.findById(eventId);

      teamACL.validateReadAccess(userId, event.getTeamId());

      return event;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @POST
  public Event create(@Auth Long userId, @QueryParam(API_KEY) String apiKey, Event event) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(event, "Event is null");
      checkArgument(event.getId() == null, "Event id must be null");
      checkNotNull(event.getName(), "Name is null");
      checkNotNull(event.getTeamId(), "Team id is null");

      teamACL.validateWriteAccess(userId, event.getTeamId());
      eventDAO.create(event);

      return null;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @PUT
  @Path("/{id}")
  public void update(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id, Event event) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(event, "Event is null");
      checkNotNull(id, "Event id is null");
      checkArgument(id.equals(event.getId()), "Id's don't match");
      checkNotNull(event.getName(), "Name is null");
      checkNotNull(event.getTeamId(), "Team id is null");

      teamACL.validateWriteAccess(userId, event.getTeamId());
      eventDAO.update(event);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @DELETE
  @Path("/{id}")
  public void delete(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long eventId) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(eventId, "Event id is null");
      Event event = eventDAO.findById(eventId);

      teamACL.validateWriteAccess(userId, event.getTeamId());

      eventDAO.delete(eventId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @GET
  @Path("/{event_id}/responses")
  public List<Response> responses(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("event_id") Long eventId) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(eventId, "Event id is null");
      Event event = checkNotNull(eventDAO.findById(eventId), "Invalid event id: " + eventId);

      teamACL.validateReadAccess(userId, event.getTeamId());

      return eventDAO.findResponses(eventId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @GET
  @Path("/{event_id}/emails")
  public List<Email> emails(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("event_id") Long eventId) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(eventId, "Event id is null");
      Event event = eventDAO.findById(eventId);

      teamACL.validateWriteAccess(userId, event.getTeamId());

      return emailDAO.findEmailsForEvent(eventId);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }
}
