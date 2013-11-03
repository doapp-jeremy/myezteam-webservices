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
public class User {
  @JsonProperty
  private final Long id;
  @JsonProperty
  private final String email;
  @JsonProperty
  private String firstName;
  @JsonProperty
  private String lastName;

  /**
   * @param long1
   * @param string
   */
  public User(long id, String email) {
    this.id = id;
    this.email = email;
  }

  /**
   * @param long1
   * @param string
   * @param string2
   * @param string3
   */
  public User(long id, String email, String firstName, String lastName) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return id + ": " + email;
  }

  /**
   * @return
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }
}
