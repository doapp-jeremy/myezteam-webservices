/**
 * AwsConfiguration.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.adagogo.config;

import static com.google.common.base.Preconditions.checkNotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author jeremy
 * 
 */
public class AwsConfiguration {
  @NotEmpty
  @JsonProperty
  private String accessKey;

  @NotEmpty
  @JsonProperty
  private String secretKey;

  @NotEmpty
  @JsonProperty
  private String tablePrefix;
  private AWSCredentials awsCredentials;

  public AwsConfiguration() {}

  public String getSecretKey() {
    return secretKey;
  }

  public String getAccessKey() {
    return accessKey;
  }

  /**
   * @return the tablePrefix
   */
  public String getTablePrefix() {
    return tablePrefix;
  }

  public AwsConfiguration withTablePrefix(String tablePrefix) {
    this.tablePrefix = tablePrefix;
    return this;
  }

  public AwsConfiguration withAccessKey(String accessKey) {
    this.accessKey = accessKey;
    return this;
  }

  public AwsConfiguration withSecretKey(String secretKey) {
    this.secretKey = secretKey;
    return this;
  }

  public AWSCredentials getAWSCredentials() {
    if (null == this.awsCredentials) {
      checkNotNull(this.accessKey, "AWS access key is null");
      checkNotNull(this.secretKey, "AWS secret key is null");

      this.awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    }

    return this.awsCredentials;
  }

}
