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
package com.adagogo.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.codahale.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author jeremy
 * 
 */
public class WsConfiguration extends Configuration {
  @Valid
  @NotNull
  @JsonProperty
  private final AwsConfiguration aws = new AwsConfiguration();

  public AwsConfiguration getAwsConfiguration() {
    return aws;
  }

}
