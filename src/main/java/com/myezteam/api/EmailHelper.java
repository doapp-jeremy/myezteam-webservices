/**
 * EmailHelper.java
 * webservices
 * 
 * Created by jeremy on Nov 14, 2016
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2016 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.api;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.myezteam.api.Response.ResponseType;
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.ResponseDAO;
import com.myezteam.resource.ResourceUtil;


/**
 * @author jeremy
 *
 */
public class EmailHelper {
  private static final Logger log = LoggerFactory.getLogger(EmailHelper.class);
  private static final AmazonSimpleEmailServiceClient ses = new AmazonSimpleEmailServiceClient();

  private static String getEmailTemplate() {
    String emailTemplate = null;
    if (emailTemplate == null) {
      InputStream inputStream = EmailHelper.class.getResourceAsStream("/email_inline.html");
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

  public static void sendEmail(EmailDAO emailDAO, PlayerDAO playerDAO, ResponseDAO responseDAO, Email email, Event event) throws NoSuchAlgorithmException {

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
        toAddresses.add(toEmail);
        // toAddresses.add("junker37+testing@gmail.com");
        // toAddresses.add("tomcaflisch@gmail.com");
        log.debug(toAddresses.toString());

        Destination dest = new Destination().withToAddresses(toAddresses);
        dest.withBccAddresses("admin@myezteam.com");
        sendEmailRequest.setDestination(dest);
        String title = event.getName() + ": " + email.getTitle();
        Content subjContent = new Content().withData(title);
        Message msg = new Message().withSubject(subjContent);

        if (email.isIncludeRsvpForm()) {
          String urlBase = "http://myezteam.com/index.html#/responses/email_rsvp/" + event.getId() + "/" + player.getId();
          String responseKey = ResourceUtil.generateResponseKey(event.getId(), player.getId());
          for (ResponseType responseType : ResponseType.instances()) {
            if (false == ResponseType.NO_RESPONSE.equals(responseType)) {
              String url = urlBase + "/" + responseType.id + "/" + responseKey;
              template = template.replaceFirst("\\{RSVP HREF " + responseType.label.toUpperCase() + "\\}", url);
            }
          }
        }

        Content htmlContent = new Content().withData(template);
        Body body = new Body().withHtml(htmlContent);
        msg.setBody(body);
        sendEmailRequest.setMessage(msg);
        SendEmailResult result = ses.sendEmail(sendEmailRequest);
        log.debug("SES message id: " + result.getMessageId());
        // max 1 per second
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    emailDAO.setEmailSentNow(email.getId());
  }
}
