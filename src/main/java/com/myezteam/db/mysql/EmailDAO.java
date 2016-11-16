/**
 * TeamDAO.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.db.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.myezteam.api.Email;


/**
 * @author jeremy
 * 
 */
// @UseStringTemplate3StatementLocator
public interface EmailDAO {
  public static class EmailMapper implements ResultSetMapper<Email> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet, org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Email map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new Email(r.getLong("id"), r.getString("title"), r.getInt("days_before"), r.getString("content"),
          r.getLong("event_id"), r.getBoolean("rsvp"), r.getString("send"), r.getString("send_on"), r.getBoolean("default"),
          r.getLong("team_id"));
    }
  }

  void close();

  @Mapper(EmailMapper.class)
  @SqlQuery("SELECT * FROM emails WHERE event_id = :event_id")
  public abstract List<Email> findEmailsForEvent(@Bind("event_id") Long geventId);

  @Mapper(EmailMapper.class)
  @SqlQuery("SELECT * FROM emails WHERE id = :email_id")
  public abstract Email findById(@Bind("email_id") Long emailId);

  @SqlQuery("SELECT player_type_id FROM email_player_types WHERE email_id = :email_id")
  public abstract List<Integer> findPlayerTypes(@Bind("email_id") Long emailId);

  @SqlQuery("SELECT response_type_id FROM email_response_types WHERE email_id = :email_id")
  public abstract List<Integer> findResponseTypes(@Bind("email_id") Long emailId);

  @SqlUpdate("INSERT INTO emails (created, title, days_before, content, event_id, rsvp, send, send_on, `default`, team_id) VALUES (UTC_TIMESTAMP(), :e.title, :e.daysBefore, :e.content, :e.eventId, :e.includeRsvpForm, :e.sendType, :e.sendOn, :e.defaultEmail, :e.teamId)")
  public abstract void create(@BindBean("e") Email email);

  @SqlQuery("SELECT LAST_INSERT_ID()")
  public abstract Long getLastInsertId();

  @SqlUpdate("UPDATE emails SET modified = UTC_TIMESTAMP(), title = :e.title, days_before = :e.daysBefore, content = :e.content, rsvp = :e.includeRsvpForm, send = :e.sendType, send_on = :e.sendOn, `default` = :e.defaultEmail WHERE id = :e.id LIMIT 1")
  public abstract void update(@BindBean("e") Email email);

  @SqlUpdate("DELETE FROM emails WHERE id = :email_id LIMIT 1")
  public abstract void delete(@Bind("email_id") Long emailId);

  @SqlBatch("INSERT INTO email_player_types (email_id, player_type_id) VALUES (:email_id, :pt)")
  public abstract void createPlayerTypes(@Bind("email_id") Long emailId, @Bind("pt") List<Integer> playerTypes);

  @SqlBatch("INSERT INTO email_response_types (email_id, response_type_id) VALUES (:email_id, :rt)")
  public abstract void createResponseTypes(@Bind("email_id") Long emailId, @Bind("rt") List<Integer> responseTypes);

  @SqlQuery("SELECT player_type_id FROM email_player_types WHERE email_id = :email_id")
  public abstract List<Integer> getPlayerTypes(@Bind("email_id") Long emailId);

  @SqlQuery("SELECT response_type_id FROM email_response_types WHERE email_id = :email_id")
  public abstract List<Integer> getResponseTypes(@Bind("email_id") Long emailId);

  @SqlUpdate("DELETE FROM email_player_types WHERE email_id = :email_id")
  public abstract void deletePlayerTypes(@Bind("email_id") Long emailId);

  @SqlUpdate("DELETE FROM email_response_types WHERE email_id = :email_id")
  public abstract void deleteResponseTypes(@Bind("email_id") Long emailId);

  @SqlUpdate("UPDATE emails SET sent = UTC_TIMESTAMP() WHERE id = :email_id LIMIT 1")
  public abstract void setEmailSentNow(@Bind("email_id") Long id);

  @Mapper(EmailMapper.class)
  @SqlQuery("SELECT * FROM emails WHERE `default` = 1 AND team_id = :team_id")
  public abstract List<Email> findDefaultEmailsForTeam(@Bind("team_id") Long teamId);

  @SqlUpdate("INSERT INTO email_player_types (email_id,player_type_id) SELECT :dest_email_id,player_type_id FROM email_player_types WHERE email_id = :source_email_id")
  public abstract void copyPlayerTypes(@Bind("source_email_id") long sourceEmailId, @Bind("dest_email_id") long destEmailId);

  @SqlUpdate("INSERT INTO email_response_types (email_id,response_type_id) SELECT :dest_email_id,response_type_id FROM email_response_types WHERE email_id = :source_email_id")
  public abstract void copyResponseTypes(@Bind("source_email_id") long sourceEmailId, @Bind("dest_email_id") long destEmailId);

  @Mapper(EmailMapper.class)
  @SqlQuery("SELECT Email.* FROM emails AS Email RIGHT JOIN events AS Event ON (Event.id = Email.event_id) WHERE Event.start >= UTC_TIMESTAMP() AND Email.send = 'days_before' AND Email.sent IS NULL AND Email.days_before >= DATEDIFF(Event.start,UTC_TIMESTAMP())")
  public abstract List<Email> getEmailsForDaysBefore();
}
