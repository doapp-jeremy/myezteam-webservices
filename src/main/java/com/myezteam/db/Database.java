/**
 * Database.java
 * webservices
 * 
 * Created by jeremy on Nov 14, 2016
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2016 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.db;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.myezteam.config.Config;


/**
 * @author jeremy
 *
 */
public abstract class Database {
  private static final Logger log = LoggerFactory.getLogger(Database.class);

  public static DBI getDBI() {
    String dbUrl = Config.instance.getString("dw.database.url");
    String dbUser = Config.instance.getString("dw.database.user");
    log.debug("Connecting to db: " + dbUser + "@" + dbUrl);
    return new DBI(dbUrl, dbUser, Config.instance.getString("dw.database.password"));
  }

  /**
   * @param clazz
   * @return
   */
  public static <SqlObjectType> SqlObjectType open(Class<SqlObjectType> clazz) {
    return getDBI().open(clazz);
  }
}
