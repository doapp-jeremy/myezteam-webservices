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

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class Response {
  public static class ResponseType {
    private static Map<Integer, ResponseType> responseTypes = new HashMap<Integer, Response.ResponseType>();
    public final int id;
    public final String label;

    private ResponseType(int id, String label) {
      this.id = id;
      this.label = label;
      responseTypes.put(id, this);
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

  @JsonProperty
  private Long id;
  @JsonProperty
  private Long eventId;
  @JsonProperty
  private Long playerId;
  @JsonProperty
  private ResponseType response;
  @JsonProperty
  private Date created;

  private Response() {}

  /**
   * @param long1
   */
  public Response(Long id, Long eventId, Long playerId) {
    this.id = id;
    this.eventId = eventId;
    this.playerId = playerId;
  }

  public Response(long id, long eventId, long playerId, ResponseType responseType, Date created) {
    this.id = id;
    this.eventId = eventId;
    this.playerId = playerId;
    this.response = responseType;
    this.created = created;
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
}
