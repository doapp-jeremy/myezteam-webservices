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
import com.myezteam.api.Player.PlayerType;
import com.myezteam.api.Response;
import com.myezteam.api.Response.PlayerInfo;
import com.myezteam.api.Response.ResponseType;


/**
 * @author jeremy
 * 
 */
// @UseStringTemplate3StatementLocator
public interface ResponseDAO {
  public static class ResponseMapper implements ResultSetMapper<Response> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Response map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      try {
        PlayerInfo playerInfo = new PlayerInfo(r.getLong("user_id"), r.getString("email"), r.getString("first_name"),
            r.getString("last_name"), PlayerType.get(r.getLong("player_type_id")));
        return new Response(r.getLong("id"), r.getLong("event_id"), r.getLong("player_id"), playerInfo,
            ResponseType.get(r.getLong("response_type_id")), r.getString("comment"), r.getString("created"));
      } catch (SQLException e) {
        e.printStackTrace();
        throw e;
      }

    }
  }

  void close();

  @Mapper(ResponseMapper.class)
  @SqlQuery("SELECT Response.*,User.*,Player.user_id,Player.player_type_id FROM users AS User RIGHT JOIN players AS Player ON (Player.user_id = User.id) RIGHT JOIN responses AS Response ON (Response.player_id = Player.id AND Response.event_id = :event_id) WHERE User.id = :user_id ORDER BY Response.created DESC")
  public abstract List<Response> findUsersResponsesForEvent(@Bind("user_id") Long userId, @Bind("event_id") Long eventId);

  @Mapper(ResponseMapper.class)
  @SqlQuery("SELECT Response.*,User.*,Player.user_id,Player.player_type_id FROM users AS User RIGHT JOIN players AS Player ON (Player.user_id = User.id) RIGHT JOIN responses AS Response ON (Response.player_id = Player.id AND Response.event_id = :event_id) WHERE User.id = :user_id ORDER BY Response.created DESC LIMIT 1")
  public abstract Response findUsersLastResponsesForEvent(@Bind("user_id") Long userId, @Bind("event_id") Long eventId);

  @Mapper(ResponseMapper.class)
  @SqlUpdate("INSERT INTO responses (created, response_type_id, player_id, event_id, comment) VALUES (UTC_TIMESTAMP(), :r.responseTypeId, :r.playerId, :r.eventId, :r.comment)")
  public abstract void create(@BindBean("r") Response response);

}
