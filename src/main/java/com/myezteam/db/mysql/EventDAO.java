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
      try {
        return new Event(r.getLong("id"), r.getString("name"), r.getLong("team_id"), r.getString("timezone"),
            r.getString("start"),
            r.getString("end"),
            r.getString("description"), r.getString("location"), ResponseType.get(r.getLong("response_type_id")));
      } catch (SQLException e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  void close();

  @SqlQuery("SELECT * FROM events WHERE id = :id")
  @Mapper(EventMapper.class)
  public abstract Event findById(@Bind("id") Long id);

  @SqlUpdate("INSERT INTO events (name, team_id, timezone, start, end, description, location, created) VALUES (:e.name, :e.teamId, :e.timezone, :e.start, :e.end, :e.description, :e.location, UTC_TIMESTAMP())")
  public abstract void create(@BindBean("e") Event event);

  @SqlUpdate("UPDATE events SET modified = UTC_TIMESTAMP(), timezone = :e.timezone, :e.start, :e.end, :e.description, :e.location")
  public abstract void update(@BindBean("e") Event event);

  @SqlUpdate("DELETE FROM events WHERE id = :id")
  public abstract void delete(@Bind("id") Long eventId);

  @SqlQuery("SELECT Event.* FROM users AS User LEFT JOIN players AS Player ON (Player.user_id = User.id) LEFT JOIN teams_managers AS TM ON (TM.user_id = User.id) LEFT JOIN teams AS Team ON (Team.id = Player.team_id OR Team.id = TM.team_id OR Team.user_id = User.id) LEFT JOIN events AS Event ON (Event.team_id = Team.id) WHERE User.id = :user_id AND DATEDIFF(Event.start, UTC_TIMESTAMP()) > -1 GROUP BY Event.id ORDER BY Event.start ASC LIMIT 10")
  @Mapper(EventMapper.class)
  public abstract List<Event> findUpcomingEvents(@Bind("user_id") Long userId);

  @SqlQuery("SELECT User.id AS user_id,User.first_name,User.last_name,User.email,Player.user_id,Player.player_type_id,Response.id,Response.created,Player.id AS player_id,Event.id AS event_id,Response.comment,COALESCE(Response.response_type_id,Event.response_type_id) AS response_type_id FROM events AS Event LEFT JOIN teams AS Team ON (Team.id = Event.team_id) LEFT JOIN players AS Player ON (Player.team_id = Team.id) LEFT JOIN responses AS Response INNER JOIN (SELECT player_id,MAX(created) AS created FROM responses AS Response WHERE Response.event_id = :event_id GROUP BY Response.player_id) LatestResponse ON (LatestResponse.player_id = Response.player_id AND LatestResponse.created = Response.created) ON (Response.player_id = Player.id) LEFT JOIN users AS User ON (User.id = Player.user_id) WHERE Event.id = :event_id AND (Player.player_type_id = 1 OR Response.id IS NOT NULL) GROUP BY Player.id ORDER BY Response.created DESC")
  @Mapper(ResponseMapper.class)
  public abstract List<Response> findResponses(@Bind("event_id") Long eventId);

  @Mapper(EventMapper.class)
  @SqlQuery("SELECT * FROM events WHERE team_id = :team_id")
  List<Event> getEventsForTeam(@Bind("team_id") Long teamId);

  @Mapper(EventMapper.class)
  @SqlQuery("SELECT * FROM events WHERE team_id = :team_id AND start >= DATE_SUB(UTC_TIMESTAMP(), INTERVAL 1 DAY) ORDER BY start ASC, end ASC")
  List<Event> getUpcomingEventsForTeam(@Bind("team_id") Long teamId);

  @Mapper(EventMapper.class)
  @SqlQuery("SELECT * FROM events WHERE team_id = :team_id AND start < UTC_TIMESTAMP() ORDER BY start DESC, end DESC")
  List<Event> getPastEventsForTeam(@Bind("team_id") Long teamId);

}
