/**
 * WsConfiguration.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.config;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;


/**
 * @author jeremy
 * 
 */
public class WsConfiguration extends Configuration {
  // I'm not sure what's going on, but if I don't add this, then I can't start the server from the
  // command line, and therefore, can't start it in heroku
  @Valid
  @NotNull
  @JsonProperty("server")
  public Map<String, Object> server;

  @Valid
  @NotNull
  @JsonProperty("aws")
  private final AwsConfiguration aws = new AwsConfiguration();

  public AwsConfiguration getAwsConfiguration() {
    return aws;
  }

  @Valid
  @NotNull
  @JsonProperty("database")
  private final DatabaseConfiguration database = new DatabaseConfiguration();

  /**
   * @return the database
   */
  public DatabaseConfiguration getDatabase() {
    return database;
  }

}
