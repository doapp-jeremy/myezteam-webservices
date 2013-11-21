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
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
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

  @SqlUpdate("INSERT INTO emails (created, title, days_before, content, event_id, rsvp, send, send_on, `default`, team_id) VALUES (UTC_TIMESTAMP(), :e.title, :e.daysBefore, :e.content, :e.eventId, :e.includeRsvpForm, :e.sendType, :e.sendOn, :e.defaultEmail, :e.teamId)")
  public abstract void create(@BindBean("e") Email email);

  @SqlUpdate("UPDATE emails SET modified = UTC_TIMESTAMP(), title = :e.title, days_before = :e.daysBefore, content = :e.content, rsvp = :e.includeRsvpForm, send = :e.sendType, send_on = :e.sendOn WHERE id = :e.id LIMIT 1")
  public abstract void update(@BindBean("e") Email email);

  @SqlUpdate("DELETE FROM emails WHERE id = :email_id LIMIT 1")
  public abstract void delete(@Bind("email_id") Long emailId);
}
