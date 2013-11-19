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
import com.myezteam.api.Event;
import com.myezteam.api.Response;
import com.myezteam.api.Response.ResponseType;
import com.myezteam.db.mysql.ResponseDAO.ResponseMapper;


/**
 * @author jeremy
 * 
 */
// @UseStringTemplate3StatementLocator
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
      return new Event(r.getLong("id"), r.getString("name"), r.getLong("team_id"), r.getString("timezone"), r.getString("start"),
          r.getString("end"),
          r.getString("description"), r.getString("location"), ResponseType.get(r.getLong("response_type_id")));
    }
  }

  void close();

  @SqlQuery("SELECT * FROM events WHERE id = :id")
  @Mapper(EventMapper.class)
  public abstract Event findEventById(@Bind("id") Long id);

  @SqlUpdate("INSERT INTO events (name, team_id, timezone, start, end, description, location, created) VALUES (:e.name, :e.teamId, :e.timezone, :e.start, :e.end, :e.description, :e.location, UTC_TIMESTAMP())")
  public abstract void createEvent(@BindBean("e") Event event);

  @SqlUpdate("DELETE FROM events WHERE id = :id")
  public abstract void deleteEvent(@Bind("id") Long eventId);

  @SqlQuery("SELECT Event.* FROM users AS User LEFT JOIN players AS Player ON (Player.user_id = User.id) LEFT JOIN teams_managers AS TM ON (TM.user_id = User.id) LEFT JOIN teams AS Team ON (Team.id = Player.team_id OR Team.id = TM.team_id OR Team.user_id = User.id) LEFT JOIN events AS Event ON (Event.team_id = Team.id) WHERE User.id = :user_id AND Event.start >= UTC_TIMESTAMP() GROUP BY Event.id ORDER BY Event.start ASC LIMIT 3")
  @Mapper(EventMapper.class)
  public abstract List<Event> findUpcomingEvents(@Bind("user_id") Long userId);

  @SqlQuery("SELECT * FROM responses WHERE event_id = :event_id")
  @Mapper(ResponseMapper.class)
  public abstract List<Response> findResponses(@Bind("event_id") Long eventId);
}
