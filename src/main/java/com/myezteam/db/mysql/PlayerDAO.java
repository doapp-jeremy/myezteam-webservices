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
import com.myezteam.api.Team;
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
      User user = null;
      try {
        user = new User(r.getLong("user_id"), r.getString("email"), r.getString("first_name"), r.getString("last_name"));
      } catch (SQLException e) {
        // ignore, we didn't include the user
      }
      Team team = null;
      try {
        team = new Team(r.getLong("team_id"), r.getString("team_name"));
      } catch (SQLException e) {
        // ignore, we didn't include the team
      }
      return new Player(r.getLong("id"), r.getLong("user_id"), r.getLong("team_id"), PlayerType.get(r.getLong("player_type_id")),
          user, team);
    }
  }

  @Mapper(PlayerMapper.class)
  @SqlQuery("SELECT Player.id,Player.user_id,Player.team_id,Player.player_type_id,User.email,User.first_name,User.last_name FROM players AS Player RIGHT JOIN users AS User ON (User.id=Player.user_id) WHERE Player.team_id = :team_id GROUP BY Player.id")
  public abstract List<Player> getPlayersForTeam(@Bind("team_id") Long teamId);

  @SqlUpdate("INSERT INTO players (team_id, user_id, player_type_id, created) VALUES (:team_id, :p.userId, :p.playerTypeId, UTC_TIMESTAMP())")
  public abstract void addPlayer(@Bind("team_id") Long teamId, @BindBean("p") Player player);

  @SqlUpdate("DELETE FROM players WHERE id = :player_id AND team_id = :team_id")
  public abstract void removePlayer(@Bind("team_id") Long teamId, @Bind("player_id") Long playerId);

  @SqlUpdate("UPDATE players SET player_type_id = :player_type_id,modified = UTC_TIMESTAMP() WHERE id = :player_id AND team_id = :team_id AND player_type_id != :player_type_id")
  public abstract void updatePlayerType(@Bind("team_id") Long teamId, @Bind("player_id") Long playerId,
      @Bind("player_type_id") Long playerTypeId);

  @SqlQuery("SELECT Player.id,Player.user_id,Player.team_id,Player.player_type_id,Team.name AS team_name FROM players AS Player RIGHT JOIN users AS User ON (User.id=Player.user_id) LEFT JOIN teams AS Team ON (Team.id=Player.team_id) WHERE Player.user_id = :user_id GROUP BY Player.id")
  @Mapper(PlayerMapper.class)
  public abstract List<Player> getPlayersForUser(@Bind("user_id") Long userId);

  @Mapper(PlayerMapper.class)
  @SqlQuery("SELECT * FROM players WHERE id = :player_id")
  public abstract Player findPlayer(@Bind("player_id") Long playerId);

  @Mapper(PlayerMapper.class)
  @SqlQuery("SELECT Player.*,User.*,Team.name AS team_name FROM players AS Player LEFT JOIN users AS User ON (User.id = Player.user_id) LEFT JOIN teams AS Team ON (Team.id = Player.team_id) WHERE Player.team_id = :team_id AND Player.user_id = :user_id")
  public abstract Player getPlayerForTeam(@Bind("team_id") Integer teamId, @Bind("user_id") Long userId);
}
