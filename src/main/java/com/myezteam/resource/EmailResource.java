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
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.EventDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/emails")
public class EmailResource extends BaseResource {
  private final TeamACL teamACL;
  private final EventDAO eventDAO;
  private final EmailDAO emailDAO;

  public EmailResource(TeamACL teamACL, EventDAO eventDAO, EmailDAO emailDAO) {
    this.teamACL = teamACL;
    this.eventDAO = eventDAO;
    this.emailDAO = emailDAO;
  }

  @GET
  @Path("/{id}")
  public Email get(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(id, "Email id is null");

      Email email = emailDAO.findById(id);
      Event event = eventDAO.findById(email.getEventId());

      teamACL.validateWriteAccess(userId, event.getTeamId());

      email.setPlayerTypes(emailDAO.getPlayerTypes(email.getId()));
      email.setResponseTypes(emailDAO.getResponseTypes(email.getId()));

      return email;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @POST
  public Email create(@Auth Long userId, @QueryParam(API_KEY) String apiKey, Email email) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(email, "Email is null");
      checkArgument(email.getId() == null, "Email id must be null");
      checkNotNull(email.getTitle(), "Title is null");
      checkNotNull(email.getContent(), "Content is null");

      checkNotNull(email.getEventId(), "Event id is null");
      Event event = checkNotNull(eventDAO.findById(email.getEventId()), "Could not find event");
      teamACL.validateWriteAccess(userId, event.getTeamId());

      if (email.isDefaultEmail()) {
        checkNotNull(email.getTeamId(), "Team id is null, required for default email");
      }

      List<Integer> playerTypes = checkNotNull(email.getPlayerTypes(), "Player types not set");
      checkArgument(playerTypes.size() > 0, "Must specify at least 1 player type");
      List<Integer> responseTypes = checkNotNull(email.getResponseTypes(), "Response types not set");
      checkArgument(responseTypes.size() > 0, "Must specify at least 1 response type");

      String sendType = checkNotNull(email.getSendType(), "Send type is null");
      if ("now".equals(sendType)) {
        // TODO: send email now
      }
      else if ("days_before".equals(sendType)) {
        checkArgument(email.getDaysBefore() >= 0, "Days before must be > 0");
        // TODO: verify Event.start - days_before > today
      }
      else if ("send_on".equals(sendType)) {
        // TODO: verify send_on is >= now
      }
      else {
        throw new Exception("Invalid send type, must be one of now|days_before|send_on");
      }

      teamACL.validateWriteAccess(userId, event.getTeamId());
      emailDAO.create(email);

      Long emailId = emailDAO.getLastInsertId();
      email.setId(emailId);

      emailDAO.createPlayerTypes(emailId, email.getPlayerTypes());
      emailDAO.createResponseTypes(emailId, email.getResponseTypes());

      return email;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @PUT
  @Path("/{id}")
  public Email update(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id, Email email) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(email, "Email is null");
      checkNotNull(id, "Email id is null");
      checkArgument(id.equals(email.getId()), "Email id's don't match");
      checkNotNull(email.getTitle(), "Title is null");
      checkNotNull(email.getContent(), "Content is null");

      checkNotNull(email.getEventId(), "Event id is null");
      Event event = checkNotNull(eventDAO.findById(email.getEventId()), "Could not find event");
      teamACL.validateWriteAccess(userId, event.getTeamId());

      if (email.isDefaultEmail()) {
        checkNotNull(email.getTeamId(), "Team id is null, required for default email");
      }

      String sendType = checkNotNull(email.getSendType(), "Send type is null");
      if ("now".equals(sendType)) {
        // TODO: send email now
      }
      else if ("days_before".equals(sendType)) {
        checkArgument(email.getDaysBefore() >= 0, "Days before must be > 0");
        // TODO: verify Event.start - days_before > today
      }
      else if ("send_on".equals(sendType)) {
        // TODO: verify send_on is >= now
      }
      else {
        throw new Exception("Invalid send type, must be one of now|days_before|send_on");
      }

      teamACL.validateWriteAccess(userId, event.getTeamId());
      emailDAO.update(email);

      return null;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  @DELETE
  @Path("/{id}")
  public void delete(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(id, "Id is null");
      Email email = emailDAO.findById(id);
      Event event = eventDAO.findById(email.getEventId());

      teamACL.validateWriteAccess(userId, event.getTeamId());

      emailDAO.delete(id);
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

}
