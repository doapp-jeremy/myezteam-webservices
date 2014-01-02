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
import java.util.ArrayList;
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
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
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
  private final AmazonSimpleEmailServiceClient ses;

  public EmailResource(TeamACL teamACL, EventDAO eventDAO, EmailDAO emailDAO, AmazonSimpleEmailServiceClient ses) {
    this.teamACL = teamACL;
    this.eventDAO = eventDAO;
    this.emailDAO = emailDAO;
    this.ses = ses;
  }

  @POST
  @Path("/{id}/send")
  public Email send(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(id, "Email id is null");

      Email email = emailDAO.findById(id);
      Event event = eventDAO.findById(email.getEventId());

      teamACL.validateWriteAccess(userId, event.getTeamId());

      sendEmail(email);

      return email;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  private void sendEmail(Email email) {
    SendEmailRequest sendEmailRequest = new SendEmailRequest().withSource("myezteam@gmail.com");
    List<String> toAddresses = new ArrayList<String>();
    toAddresses.add("junker37@gmail.com");
    toAddresses.add("tomcaflisch@gmail.com");
    Destination dest = new Destination().withToAddresses(toAddresses);
    sendEmailRequest.setDestination(dest);
    Content subjContent = new Content().withData(email.getTitle());
    Message msg = new Message().withSubject(subjContent);
    Content htmlContent = new Content().withData(email.getContent());
    Body body = new Body().withHtml(htmlContent);
    msg.setBody(body);
    sendEmailRequest.setMessage(msg);
    ses.sendEmail(sendEmailRequest);

    // TODO: update sent time
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

      if (email.getEventId() == null) {
        checkArgument(true == email.isDefaultEmail(), "Must be a default email if event id is null");
        checkNotNull(email.getTeamId(), "Team id is required for default email");
      }
      if (email.isDefaultEmail()) {
        checkNotNull(email.getTeamId(), "Team id is null, required for default email");
      }

      List<Integer> playerTypes = checkNotNull(email.getPlayerTypes(), "Player types not set");
      checkArgument(playerTypes.size() > 0, "Must specify at least 1 player type");
      List<Integer> responseTypes = checkNotNull(email.getResponseTypes(), "Response types not set");
      checkArgument(responseTypes.size() > 0, "Must specify at least 1 response type");

      String sendType = checkNotNull(email.getSendType(), "Send type is null");
      if ("now".equals(sendType)) {
        sendEmail(email);
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

      if (email.getEventId() == null) {
        checkArgument(true == email.isDefaultEmail(), "Must be a default email if event id is null");
        checkNotNull(email.getTeamId(), "Team id is required for default email");
      }
      if (email.isDefaultEmail()) {
        checkNotNull(email.getTeamId(), "Team id is null, required for default email");
      }

      Event event = checkNotNull(eventDAO.findById(email.getEventId()), "Could not find event");
      teamACL.validateWriteAccess(userId, event.getTeamId());

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
      emailDAO.update(email);

      // delete existing player and response types, then insert the new ones
      emailDAO.deletePlayerTypes(email.getId());
      emailDAO.createPlayerTypes(email.getId(), email.getPlayerTypes());
      emailDAO.deleteResponseTypes(email.getId());
      emailDAO.createResponseTypes(email.getId(), email.getResponseTypes());

      return email;
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

  @PUT
  @Path("/{id}/make_default")
  public Email makeDefault(@Auth Long userId, @QueryParam(API_KEY) String apiKey, @PathParam("id") Long id) {
    try {
      checkNotNull(userId, "Invalid auth");
      checkApiKey(apiKey);
      checkNotNull(id, "Id is null");
      Email email = emailDAO.findById(id);
      checkArgument("days_before".equals(email.getSendType()), "Can only make emails with send type days_before default");
      Event event = eventDAO.findById(email.getEventId());

      teamACL.validateWriteAccess(userId, event.getTeamId());

      Email defaultEmail = email;
      defaultEmail.setId(null);
      defaultEmail.setDefaultEmail(true);
      defaultEmail.setTeamId(event.getTeamId());

      emailDAO.create(defaultEmail);
      Long defaultEmailId = emailDAO.getLastInsertId();
      defaultEmail.setId(defaultEmailId);

      List<Integer> playerTypes = emailDAO.getPlayerTypes(id);
      List<Integer> responseTypes = emailDAO.getResponseTypes(id);

      emailDAO.createPlayerTypes(defaultEmailId, playerTypes);
      emailDAO.createResponseTypes(defaultEmailId, responseTypes);

      defaultEmail.setPlayerTypes(playerTypes);
      defaultEmail.setResponseTypes(responseTypes);

      return defaultEmail;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }
}
