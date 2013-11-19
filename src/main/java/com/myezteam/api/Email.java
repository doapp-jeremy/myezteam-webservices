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
public class Email {
  @JsonProperty
  private Long id;
  @JsonProperty
  private String title;
  @JsonProperty
  private int daysBefore;
  @JsonProperty
  private String content;
  @JsonProperty
  private Long eventId;
  @JsonProperty
  private boolean includeRsvpForm;
  @JsonProperty
  private String sendType;
  @JsonProperty
  private String sendOn;
  @JsonProperty("default")
  private boolean defaultEmail;
  @JsonProperty
  private Long teamId;

  private Email() {}

  public Email(Long id, String title, Integer daysBefore, String content, Long eventId, Boolean includeRsvpForm, String sendType,
      String sendOn, Boolean defaultEmail, Long teamId) {
    this.id = id;
    this.title = title;
    this.daysBefore = daysBefore;
    this.content = content;
    this.eventId = eventId;
    this.includeRsvpForm = includeRsvpForm;
    this.sendType = sendType;
    this.sendOn = sendOn;
    this.defaultEmail = defaultEmail;
    this.teamId = teamId;
  }

}
