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
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.myezteam.api.User;


/**
 * @author jeremy
 * 
 */
public interface UserDAO {
  public static class UserMapper implements ResultSetMapper<User> {

    /*
     * (non-Javadoc)
     * 
     * @see org.skife.jdbi.v2.tweak.ResultSetMapper#map(int, java.sql.ResultSet,
     * org.skife.jdbi.v2.StatementContext)
     */
    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new User(r.getLong("id"), r.getString("email"));
    }

  }

  /**
   * @return
   */
  @SqlQuery("SELECT id,email FROM users WHERE email = :email")
  @Mapper(UserMapper.class)
  public User findByEmail(@Bind("email") String email);

  @SqlQuery("SELECT * FROM users WHERE email = :email AND password = md5(CONCAT(:password,'PasswordSalt'))")
  @Mapper(UserMapper.class)
  public User authenticate(@Bind("email") String email, @Bind("password") String password);

}
