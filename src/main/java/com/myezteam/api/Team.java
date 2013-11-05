/**
 * Team.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class Team {
  @JsonProperty
  private final Long id;
  @JsonProperty
  private String name;
  @JsonProperty
  private String type;
  @JsonProperty
  private String defaultLocation;

  /**
   * @param long1
   * @param string
   */
  public Team(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * @param long1
   * @param string
   * @param string2
   * @param string3
   */
  public Team(long id, String name, String type, String defaultLocation) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.defaultLocation = defaultLocation;
  }

  /**
   * @param teamId
   */
  public Team(Long id) {
    this.id = id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return 17 + 31 * (int) (long) id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof Team) { return this.hashCode() == object.hashCode(); }
    return super.equals(object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return id + ": " + name;
  }
}
