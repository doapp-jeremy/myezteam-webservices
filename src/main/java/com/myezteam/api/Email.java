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

import java.util.List;
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

  @JsonProperty
  private List<Integer> playerTypes;
  @JsonProperty
  private List<Integer> responseTypes;

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

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the daysBefore
   */
  public int getDaysBefore() {
    return daysBefore;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @return
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * @return the includeRsvpForm
   */
  public boolean isIncludeRsvpForm() {
    return includeRsvpForm;
  }

  /**
   * @return the sendType
   */
  public String getSendType() {
    return sendType;
  }

  /**
   * @return the sendOn
   */
  public String getSendOn() {
    return sendOn;
  }

  /**
   * @return the defaultEmail
   */
  public boolean isDefaultEmail() {
    return defaultEmail;
  }

  /**
   * @return the teamId
   */
  public Long getTeamId() {
    return teamId;
  }

  /**
   * @return the playerTypes
   */
  public List<Integer> getPlayerTypes() {
    return playerTypes;
  }

  /**
   * @param playerTypes the playerTypes to set
   */
  public void setPlayerTypes(List<Integer> playerTypes) {
    this.playerTypes = playerTypes;
  }

  /**
   * @return the responseTypes
   */
  public List<Integer> getResponseTypes() {
    return responseTypes;
  }

  /**
   * @param responseTypes the responseTypes to set
   */
  public void setResponseTypes(List<Integer> responseTypes) {
    this.responseTypes = responseTypes;
  }
}
