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
import com.myezteam.api.Player;
import com.myezteam.api.Player.PlayerType;
import com.myezteam.api.User;


/**
 * @author jeremy
 * 
 */
public interface PlayerDAO {
  public static class PlayerMapper implements ResultSetMapper<Player> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Player map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new Player(r.getLong("id"), r.getLong("user_id"), r.getLong("team_id"), PlayerType.get(r.getLong("player_type_id")),
          new User(r.getLong("user_id"), r.getString("email"), r.getString("first_name"), r.getString("last_name")));
    }
  }

  @Mapper(PlayerMapper.class)
  @SqlQuery("SELECT Player.id,Player.user_id,Player.team_id,Player.player_type_id,User.email,User.first_name,User.last_name FROM players AS Player RIGHT JOIN users AS User ON (User.id=Player.user_id) WHERE Player.team_id = :team_id")
  public abstract List<Player> getPlayersForTeam(@Bind("team_id") Long teamId);

  @SqlUpdate("INSERT INTO players (team_id, user_id, player_type_id, created) VALUES (:team_id, :p.userId, :p.playerTypeId, UTC_TIMESTAMP())")
  public abstract void addPlayer(@Bind("team_id") Long teamId, @BindBean("p") Player player);

  @SqlUpdate("DELETE FROM players WHERE id = :player_id AND team_id = :team_id")
  public abstract void removePlayer(@Bind("team_id") Long teamId, @Bind("player_id") Long playerId);
}
