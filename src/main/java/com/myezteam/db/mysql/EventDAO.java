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
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.myezteam.api.Event;


/**
 * @author jeremy
 * 
 */
public interface EventDAO {
  public static class EventMapper implements ResultSetMapper<Event> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Event map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new Event(r.getLong("id"), r.getString("name"), r.getLong("team_id"));
    }
  }

  @SqlQuery("SELECT * FROM events WHERE id = :id")
  @Mapper(EventMapper.class)
  public abstract Event findEventById(@Bind("id") Long id);

  @SqlUpdate("INSERT INTO events (name, team_id, created) VALUES (:e.name, e:teamId, UTC_TIMESTAMP())")
  public abstract void createEvent(@BindBean("e") Event event);

  @SqlUpdate("DELETE FROM events WHERE id = :id")
  public abstract void deleteEvent(@Bind("id") Long eventId);
}
