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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class Player {
  public static class PlayerType {
    private static Map<Integer, PlayerType> playerTypes = new HashMap<Integer, Player.PlayerType>();

    private int id;
    private String label;

    private PlayerType() {}

    public PlayerType(int id, String label) {
      this.id = id;
      this.label = label;
      playerTypes.put(id, this);
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

    public static PlayerType REGULAR = new PlayerType(1, "Regular");
    public static PlayerType SUB = new PlayerType(2, "Sub");
    public static PlayerType MEMBER = new PlayerType(3, "Member");

    /**
     * @param long1
     * @return
     */
    public static PlayerType get(long id) {
      return playerTypes.get((int) id);
    }
  }

  @JsonProperty
  private Long id;
  @JsonIgnore
  private Long userId;
  @JsonIgnore
  private PlayerType type;
  @JsonProperty
  private Long teamId;
  @JsonProperty
  private User user;
  @JsonProperty
  private Team team;

  private Player() {}

  public Player(long id, long userId, long teamId, PlayerType playerType, User user, Team team) {
    this.id = id;
    this.userId = userId;
    this.teamId = teamId;
    this.type = playerType;
    this.user = user;
    this.team = team;
  }

  /**
   * @param userId2
   * @param teamId2
   * @param playerTypeId
   * @param id2
   * @param teamId3
   */
  public Player(Long teamId, Long userId, int playerTypeId) {
    this.teamId = teamId;
    this.userId = userId;
    this.type = PlayerType.get(playerTypeId);
  }

  @JsonProperty
  public String getPlayerType() {
    return type.label;
  }

  /**
   * @return the userId
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  @JsonProperty
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @JsonIgnore
  public int getPlayerTypeId() {
    return type.id;
  }

  public void setPlayerTypeId(int playerTypeId) {
    this.type = PlayerType.get(playerTypeId);
  }

  /**
   * @return
   */
  public Long getTeamId() {
    return this.teamId;
  }

  /**
   * @return
   */
  public Long getId() {
    return this.id;
  }

}
