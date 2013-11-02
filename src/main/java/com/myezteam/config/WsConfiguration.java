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
