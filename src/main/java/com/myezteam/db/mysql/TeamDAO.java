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
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.myezteam.api.Team;


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
      return new Team(r.getLong("id"), r.getString("name"), r.getString("type"), r.getString("default_location"));
    }

  }

  static final String TEAM_FIELDS = "Team.id,Team.name,Team.type,Team.default_location";

  /**
   * @return
   */
  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM teams AS Team RIGHT JOIN (SELECT pTeam.id AS player_team_id,mTeam.id AS manager_team_id,oTeam.id AS owner_team_id FROM users AS User RIGHT JOIN players AS Player ON (Player.user_id=User.id) RIGHT JOIN teams AS pTeam ON (pTeam.id=Player.team_id) RIGHT JOIN teams_managers AS TM ON (TM.user_id=User.id) RIGHT JOIN teams AS mTeam ON (mTeam.id=TM.team_id) RIGHT JOIN teams AS oTeam ON (oTeam.user_id=User.id) WHERE User.id = :user_id) AS T ON (T.player_team_id = Team.id OR T.manager_team_id = Team.id OR T.owner_team_id = Team.id) GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findUsersTeams(@Bind("user_id") long userId);

  @SqlQuery("SELECT " + TEAM_FIELDS + " FROM teams AS Team WHERE Team.user_id = :user_id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserOwns(@Bind("user_id") long userId);

  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM teams_managers AS TM RIGHT JOIN teams AS Team ON (Team.id=TM.team_id) WHERE TM.user_id = :user_id GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserManages(@Bind("user_id") long userId);

  @SqlQuery("SELECT "
      + TEAM_FIELDS
      + " FROM players AS Player RIGHT JOIN teams AS Team ON (Team.id=Player.team_id) WHERE Player.user_id = :user_id GROUP BY Team.id")
  @Mapper(TeamMapper.class)
  public List<Team> findTeamsUserPlaysOn(@Bind("user_id") long userId);

  /**
   * @param teamId
   * @return
   */
  @SqlQuery("SELECT " + TEAM_FIELDS + " FROM teams AS Team WHERE Team.id = :id")
  public Team findById(@Bind("id") long id);
}
