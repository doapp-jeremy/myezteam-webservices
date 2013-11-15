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
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.myezteam.api.Response;
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
      return new Response(r.getLong("id"), r.getLong("event_id"), r.getLong("player_id"), ResponseType.get(r
          .getLong("response_type_id")), r.getDate("created"));
    }
  }

  void close();

}
