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
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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
      return new User(r.getLong("id"), r.getString("email"), r.getString("first_name"), r.getString("last_name"));
    }

  }

  /**
   * @param userId
   * @return
   */
  @SqlQuery("SELECT * FROM users WHERE id = :id")
  @Mapper(UserMapper.class)
  public User findById(@Bind("id") Long id);

  /**
   * @return
   */
  @SqlQuery("SELECT * FROM users WHERE email = :email")
  @Mapper(UserMapper.class)
  public User findByEmail(@Bind("email") String email);

  @SqlQuery("SELECT * FROM users WHERE email = :email AND password = md5(CONCAT(:password,'PasswordSalt'))")
  @Mapper(UserMapper.class)
  public User authenticate(@Bind("email") String email, @Bind("password") String password);

  @SqlUpdate("UPDATE users SET email = :u.email, first_name = :u.firstName, last_name = :u.lastName, modified = UTC_TIMESTAMP() WHERE id = :u.id LIMIT 1")
  public void updateUser(@BindBean("u") User user);

  @SqlUpdate("INSERT INTO USERS (email) VALUES (:email)")
  public void createUser(@Bind("email") String email);

  @SqlQuery("SELECT LAST_INSERT_ID()")
  public abstract Long getLastInsertId();

}
