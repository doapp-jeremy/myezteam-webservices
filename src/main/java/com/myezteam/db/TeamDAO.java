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
package com.myezteam.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.skife.jdbi.v2.StatementContext;
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
      return new Team(r.getLong("id"), r.getString("name"));
    }

  }

  /**
   * @return
   */
  @SqlQuery("SELECT id,name FROM teams ORDER BY id LIMIT 5")
  @Mapper(TeamMapper.class)
  public List<Team> findAll();

}
