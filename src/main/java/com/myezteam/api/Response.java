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

import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myezteam.api.Player.PlayerType;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class Response {
  public static class ResponseType {
    private static Map<Integer, ResponseType> responseTypes = new HashMap<Integer, Response.ResponseType>();
    public int id;
    public String label;

    private ResponseType() {}

    private ResponseType(int id, String label) {
      this.id = id;
      this.label = label;
      responseTypes.put(id, this);
    }

    /**
     * @return the id
     */
    public int getId() {
      return id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
      return label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return label;
    }

    public static final ResponseType NO_RESPONSE = new ResponseType(1, "No Response");
    public static final ResponseType YES = new ResponseType(2, "Yes");
    public static final ResponseType PROBABLE = new ResponseType(3, "Probably");
    public static final ResponseType MAYBE = new ResponseType(4, "Maybe");
    public static final ResponseType NO = new ResponseType(5, "No");

    public static ResponseType get(long id) {
      return responseTypes.get((int) id);
    }
  }

  public static class PlayerInfo {
    @JsonProperty
    public final String email;
    @JsonProperty
    public final String firstName;
    @JsonProperty
    public final String lastName;
    @JsonProperty
    public final Long userId;
    @JsonProperty
    public final PlayerType playerType;

    public PlayerInfo(Long userId, String email, String firstName, String lastName, PlayerType playerType) {
      this.userId = userId;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.playerType = playerType;
    }
  }

  @JsonProperty
  private Long id;
  @JsonProperty
  private Long eventId;
  @JsonProperty
  private Long playerId;
  @JsonProperty
  private PlayerInfo playerInfo;
  @JsonIgnore
  private Long responseTypeId;
  @JsonProperty
  private ResponseType response;
  @JsonIgnore
  private DateTime created;
  @JsonProperty
  private String comment;

  static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4).appendLiteral("-")
      .appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":")
      .appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2).appendLiteral(".").appendMillisOfSecond(1)
      .toFormatter();

  private Response() {}

  /**
   * @param long1
   */
  // public Response(Long id, Long eventId, Long playerId) {
  // this.id = id;
  // this.eventId = eventId;
  // this.playerId = playerId;
  // }

  public Response(long id, long eventId, long playerId, PlayerInfo playerInfo, ResponseType responseType, String comment,
      String created) {
    this.id = id;
    this.eventId = eventId;
    this.playerId = playerId;
    this.playerInfo = playerInfo;
    this.response = responseType;
    this.comment = comment;
    if (created != null) {
      this.created = formatter.parseDateTime(created);
    }
  }

  public Long getId() {
    return this.id;
  }

  /**
   * @return the eventId
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * @return the userId
   */
  public Long getPlayerId() {
    return playerId;
  }

  /**
   * @return the created
   */
  @JsonProperty("created")
  public String getCreated() {
    if (created != null) { return created.toString(); }
    return null;
  }

  /**
   * @return
   */
  public Long getResponseTypeId() {
    if (response != null) { return (long) response.id; }
    return responseTypeId;
  }

  @JsonProperty("response_type_id")
  public void setResponseTypeId(Long responseTypeId) {
    this.responseTypeId = responseTypeId;
  }

  /**
   * @return the comment
   */
  public String getComment() {
    return comment;
  }
}
