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

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class Event {
  @JsonProperty
  private Long id;
  @JsonProperty
  private String name;
  @JsonProperty
  private Long teamId;
  @JsonProperty
  private Date start;
  @JsonProperty
  private Date end;
  @JsonProperty
  private String description;
  @JsonProperty
  private String location;

  private Event() {}

  /**
   * @param long1
   */
  public Event(Long id, String name, Long teamId, Date start, Date end, String description, String location) {
    this.id = id;
    this.name = name;
    this.teamId = teamId;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
  }

  public Long getId() {
    return this.id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return
   */
  public Long getTeamId() {
    return teamId;
  }

}
