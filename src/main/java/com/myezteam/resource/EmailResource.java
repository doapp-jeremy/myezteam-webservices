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
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
import org.joda.time.DateTime;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.myezteam.acl.TeamACL;
import com.myezteam.api.Email;
import com.myezteam.api.Event;
import com.myezteam.api.Player;
import com.myezteam.api.Response;
import com.myezteam.api.Response.ResponseType;
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.EventDAO;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.ResponseDAO;
import com.myezteam.db.mysql.TeamDAO;
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
  private final TeamDAO teamDAO;
  private final PlayerDAO playerDAO;
  private final EventDAO eventDAO;
  private final EmailDAO emailDAO;
  private final ResponseDAO responseDAO;
  private final AmazonSimpleEmailServiceClient ses;

  public EmailResource(TeamACL teamACL, TeamDAO teamDAO, PlayerDAO playerDAO, EventDAO eventDAO, EmailDAO emailDAO,
      ResponseDAO responseDAO, AmazonSimpleEmailServiceClient ses) {
    this.teamACL = teamACL;
    this.teamDAO = teamDAO;
    this.playerDAO = playerDAO;
    this.eventDAO = eventDAO;
    this.emailDAO = emailDAO;
    this.responseDAO = responseDAO;
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
      Event event = checkNotNull(eventDAO.findById(email.getEventId()), "Invalid event: " + email.getEventId());

      teamACL.validateWriteAccess(userId, event.getTeamId());

      sendEmail(email, event);

      return email;
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  private String getEmailTemplate() {
    String emailTemplate = null;
    if (emailTemplate == null) {
      InputStream inputStream = ClassLoader.getSystemResourceAsStream("email_inline.html");
      Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
      emailTemplate = scanner.hasNext() ? scanner.next() : "could not load template, please contact admin@myezteam.com";
      scanner.close();
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return emailTemplate;
  }

  private void sendEmail(Email email, Event event) throws NoSuchAlgorithmException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");

    List<Integer> playerTypes = emailDAO.findPlayerTypes(email.getId());
    List<Integer> responseTypes = emailDAO.findResponseTypes(email.getId());

    List<Player> players = playerDAO.getPlayersForTeam(event.getTeamId());
    for (Player player : players) {
      try {
        if (false == playerTypes.contains(player.getPlayerTypeId())) {
          continue;
        }
        ResponseType usersResponse = ResponseType.NO_RESPONSE;
        Response response = responseDAO.findUsersLastResponsesForEvent(player.getUserId(), email.getEventId());
        if (response != null) {
          usersResponse = ResponseType.get(response.getResponseTypeId());
        }
        if (false == responseTypes.contains(usersResponse.id)) {
          continue;
        }
        String template = getEmailTemplate();
        template = template.replaceAll("\\{EMAIL TITLE\\}", email.getTitle());
        template = template.replaceAll("\\{EMAIL DESCRIPTION\\}", (email.getContent() != null) ? email.getContent() : "");
        template = template.replaceAll("\\{EVENT NAME\\}", event.getName());
        template = template
            .replaceAll("\\{EVENT TIME\\}", DateTime.parse(event.getStart()).toString("hh:mma 'on' EEEE, MMM d"));
        template = template.replaceAll("\\{EVENT DESCRIPTION\\}", (event.getDescription() != null) ? event.getDescription() : "");
        template = template.replaceAll("\\{EVENT LOCATION\\}", (event.getLocation() != null) ? event.getLocation() : "");

        String toEmail = player.getUser().getEmail();
        SendEmailRequest sendEmailRequest = new SendEmailRequest().withSource("myezteam@gmail.com");
        // Collection<String> replyToAddresses = new ArrayList<String>();
        // TODO: add team managers as reply to
        // sendEmailRequest.withReplyToAddresses(replyToAddresses);
        List<String> toAddresses = new ArrayList<String>();
        // toAddresses.add(toEmail);
        // toAddresses.add("junker37@gmail.com");
        // toAddresses.add("tomcaflisch@gmail.com");

        Destination dest = new Destination().withToAddresses(toAddresses);
        dest.withBccAddresses("admin@myezteam.com");
        sendEmailRequest.setDestination(dest);
        String title = event.getName() + ": " + email.getTitle();
        Content subjContent = new Content().withData(title);
        Message msg = new Message().withSubject(subjContent);

        // String content = "";
        // content += "Start: " + event.getStart();
        // content += "<br>";
        // content += "Location: " + event.getLocation();
        // content += "<br>";
        // content += email.getContent();
        // content += "<br>";

        if (email.isIncludeRsvpForm()) {
          String urlBase = "http://www.myezteam.com/responses/email_rsvp/" + event.getId() + "/" + player.getId();
          byte messageDigest[] = md5.digest(new String(event.getId() + "ResponseKeySalt" + player.getId()).getBytes("UTF-8"));
          String responseKey = new String();
          for (int i = 0; i < messageDigest.length; i++) {
            String byteString = Integer.toHexString(0xFF & messageDigest[i]);
            if (byteString.length() == 1) {
              byteString = "0" + byteString;
            }
            responseKey += byteString;
          }
          for (ResponseType responseType : ResponseType.instances()) {
            if (false == ResponseType.NO_RESPONSE.equals(responseType)) {
              String url = urlBase + "/" + responseType.id + "/" + responseKey;
              // content += "<br>";
              // content += "<a href='" + url + "'>" + responseType.label + "</a>";
              template = template.replaceFirst("\\{RSVP HREF " + responseType.label.toUpperCase() + "\\}", url);
            }
          }
        }

        Content htmlContent = new Content().withData(template);
        Body body = new Body().withHtml(htmlContent);
        msg.setBody(body);
        sendEmailRequest.setMessage(msg);
        ses.sendEmail(sendEmailRequest);
        // max 5 per second
        Thread.sleep(200);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    emailDAO.setEmailSentNow(email.getId());
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

      teamACL.validateWriteAccess(userId, event.getTeamId());
      emailDAO.create(email);

      Long emailId = emailDAO.getLastInsertId();
      email.setId(emailId);

      emailDAO.createPlayerTypes(emailId, email.getPlayerTypes());
      emailDAO.createResponseTypes(emailId, email.getResponseTypes());

      if ("now".equalsIgnoreCase(sendType)) {
        sendEmail(email, event);
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
