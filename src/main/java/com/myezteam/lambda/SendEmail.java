/**
 * SendEmail.java
 * webservices
 * 
 * Created by jeremy on Nov 14, 2016
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2016 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.lambda;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.myezteam.api.Email;
import com.myezteam.api.EmailHelper;
import com.myezteam.api.Event;
import com.myezteam.db.Database;
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.EventDAO;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.ResponseDAO;


/**
 * @author jeremy
 *
 */
public class SendEmail {
  private static final Logger log = LoggerFactory.getLogger(SendEmail.class);

  public static void send_days_before() throws NoSuchAlgorithmException {
    PlayerDAO playerDAO = Database.open(PlayerDAO.class);
    ResponseDAO responseDAO = Database.open(ResponseDAO.class);
    EmailDAO emailDAO = Database.open(EmailDAO.class);
    EventDAO eventDAO = Database.open(EventDAO.class);
    List<Email> emails = emailDAO.getEmailsForDaysBefore();
    if (emails.isEmpty()) {
      log.debug("No emails to send");
    } else {
      for (Email email : emails) {
        Event event = eventDAO.getEvent(email.getEventId());
        log.debug("Sending email: " + event.toString() + " - " + email.toString());
        EmailHelper.sendEmail(emailDAO, playerDAO, responseDAO, email, event);
      }
    }
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    send_days_before();
  }

}
