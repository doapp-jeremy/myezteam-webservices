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
import com.myezteam.api.Team;
import com.myezteam.api.User;
import com.myezteam.db.mysql.UserDAO.UserMapper;


/**
 * @author jeremy
 * 
 */
public interface TeamDAO {
  public static class TeamMapper implements ResultSetMapper<Team> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public Team map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new Team(r.getLong("id"), r.getString("name"), r.getString("type"), r.getString("default_location"),
          r.getString("description"));
    }

  }

  static final String TEAM_FIELDS = "Team.id,Team.user_id,Team.name,Team.type,Team.default_location,Team.description";

  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM users AS User LEFT JOIN players AS Player ON (Player.user_id = User.id) LEFT JOIN teams_managers AS TM ON (TM.user_id = User.id) LEFT JOIN teams AS Team ON (Team.id = Player.team_id OR Team.id = TM.team_id OR Team.user_id = User.id) WHERE User.id = :user_id GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findUsersTeams(@Bind("user_id") long userId);

  @SqlQuery("SELECT " + TEAM_FIELDS + " FROM teams AS Team WHERE Team.user_id = :user_id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserOwns(@Bind("user_id") long userId);

  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM teams_users AS TM RIGHT JOIN teams AS Team ON (Team.id=TM.team_id) WHERE TM.user_id = :user_id GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserManages(@Bind("user_id") long userId);

  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM players AS Player RIGHT JOIN teams AS Team ON (Team.id=Player.team_id) WHERE Player.user_id = :user_id GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserPlaysOn(@Bind("user_id") long userId);

  @SqlQuery("SELECT " + TEAM_FIELDS + " FROM teams AS Team WHERE Team.id = :id")
  @Mapper(TeamMapper.class)
  public Team findById(@Bind("id") long id);

  @SqlQuery("SELECT " + TEAM_FIELDS + " FROM teams AS Team WHERE Team.user_id = :owner_id ORDER BY Team.created DESC LIMIT 1")
  @Mapper(TeamMapper.class)
  public Team getLastCreatedTeam(@Bind("owner_id") long userId);

  @SqlUpdate("INSERT INTO teams (name, user_id, type, default_location, description, created) VALUES (:t.name, :t.ownerId, :t.type, :t.defaultLocation, :t.description, UTC_TIMESTAMP())")
  public int create(@BindBean("t") Team team);

  @SqlUpdate("UPDATE teams SET name = :t.name, type = :t.type, default_location = :t.defaultLocation, description = :t.description, modified = UTC_TIMESTAMP() WHERE id = :t.id LIMIT 1")
  public void update(@BindBean("t") Team team);

  @SqlUpdate("INSERT INTO teams_users (team_id,user_id) VALUES (:team_id,:user_id)")
  public void addManager(@Bind("team_id") Long teamId, @Bind("user_id") Long managerId);

  @SqlQuery("SELECT User.id,User.email,User.first_name,User.last_name FROM teams_users AS Manager LEFT JOIN users AS User ON (User.id=Manager.user_id) WHERE Manager.team_id = :team_id GROUP BY User.id")
  @Mapper(UserMapper.class)
  public List<User> getManagers(@Bind("team_id") Long teamId);

  @SqlUpdate("DELETE FROM teams_users WHERE team_id = :team_id AND user_id = :user_id")
  public void removeManager(@Bind("team_id") Long teamId, @Bind("user_id") Long managerId);
}
