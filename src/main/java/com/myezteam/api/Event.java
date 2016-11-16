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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myezteam.api.Response.ResponseType;
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
  private String timezone;
  @JsonIgnore
  private String start;
  @JsonIgnore
  private String end;
  @JsonProperty
  private String description;
  @JsonProperty
  private String location;
  @JsonProperty
  private ResponseType defaultResponse;

  static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4).appendLiteral("-")
      .appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":")
      .appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2).appendLiteral(".").appendMillisOfSecond(1)
      .toFormatter();

  private Event() {}

  /**
   * @param long1
   */
  public Event(Long id, String name, Long teamId, String timezone, String start, String end, String description, String location,
      ResponseType defaultResponse) {
    this.id = id;
    this.name = name;
    this.teamId = teamId;
    this.timezone = timezone;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.defaultResponse = defaultResponse;
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

  @JsonProperty
  public void setStart(String start) {
    this.start = start;
  }

  @JsonProperty
  public void setEnd(String end) {
    this.end = end;
  }

  @JsonIgnore
  public DateTime getStartDateTime() {
    if (start != null) {
      try {
        return formatter.withZone(DateTimeZone.forID(timezone)).parseDateTime(start);
      } catch (Exception e) {}
    }
    return null;
  }

  @JsonProperty
  public String getStart() {
    DateTime startDateTime = getStartDateTime();
    if (startDateTime != null) { return startDateTime.toString(); }
    return start;
  }

  @JsonProperty
  public String getEnd() {
    if (end != null) {
      try {
        return formatter.withZone(DateTimeZone.forID(timezone)).parseDateTime(end).toString();
      } catch (Exception e) {}
    }
    return end;
  }

  /**
   * @return the timezone
   */
  public String getTimezone() {
    return timezone;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @return the defaultResponse
   */
  public ResponseType getDefaultResponse() {
    return defaultResponse;
  }

  /**
   * @param eventId
   */
  public void setId(Long id) {
    this.id = id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return name;
  }
}
