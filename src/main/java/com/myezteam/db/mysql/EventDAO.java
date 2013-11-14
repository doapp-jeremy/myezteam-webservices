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
      return new Event(r.getLong("id"), r.getString("name"), r.getLong("team_id"), r.getDate("start"), r.getDate("end"),
          r.getString("description"), r.getString("location"));
    }
  }

  @SqlQuery("SELECT * FROM events WHERE id = :id")
  @Mapper(EventMapper.class)
  public abstract Event findEventById(@Bind("id") Long id);

  @SqlUpdate("INSERT INTO events (name, team_id, created) VALUES (:e.name, e:teamId, UTC_TIMESTAMP())")
  public abstract void createEvent(@BindBean("e") Event event);

  @SqlUpdate("DELETE FROM events WHERE id = :id")
  public abstract void deleteEvent(@Bind("id") Long eventId);

  // @SqlQuery("SELECT * FROM events WHERE start >= UTC_TIMESTAMP() AND team_id IN (:team_ids) ORDER BY start ASC LIMIT 3")
  // @SqlQuery("SELECT Event.* FROM teams AS Team RIGHT JOIN (SELECT pTeam.id AS player_team_id,mTeam.id AS manager_team_id,oTeam.id AS owner_team_id FROM users AS User RIGHT JOIN players AS Player ON (Player.user_id=User.id) RIGHT JOIN teams AS pTeam ON (pTeam.id=Player.team_id) RIGHT JOIN teams_users AS TM ON (TM.user_id=User.id) RIGHT JOIN teams AS mTeam ON (mTeam.id=TM.team_id) RIGHT JOIN teams AS oTeam ON (oTeam.user_id=User.id) WHERE User.id = :user_id) AS T ON (T.player_team_id = Team.id OR T.manager_team_id = Team.id OR T.owner_team_id = Team.id) LEFT JOIN events AS Event ON (Event.team_id=Team.id) WHERE Event.start >= UTC_TIMESTAMP() GROUP BY Event.id ORDER BY Event.start ASC LIMIT 3")
  @SqlQuery("SELECT Event.* FROM players AS Player RIGHT JOIN teams AS Team ON (Team.id=Player.team_id) LEFT JOIN events AS Event ON (Event.team_id=Team.id) WHERE Player.user_id = :user_id AND Event.start >= UTC_TIMESTAMP() GROUP BY Event.id ORDER BY Event.start ASC LIMIT 3")
  @Mapper(EventMapper.class)
  public abstract List<Event> findUpcomingEvents(@Bind("user_id") Long userId);

  void close();
}
