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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.json.JsonSnakeCase;


/**
 * @author jeremy
 * 
 */
@JsonSnakeCase
public class User {
  @JsonProperty
  private Long id;
  @JsonProperty
  private String email;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty
  private String password;
  @JsonIgnore
  private Integer passwordForgottenCount;
  @JsonIgnore
  private String passwordChangeKey;

  private User() {}

  /**
   * @param long1
   * @param string
   */
  public User(long id, String email) {
    this.id = id;
    this.email = email;
  }

  public User(long id, String email, String firstName, String lastName, Integer passwordForgottenCount, String passwordChangeKey) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.passwordForgottenCount = passwordForgottenCount;
    this.passwordChangeKey = passwordChangeKey;
  }

  public User(long id, String email, String firstName, String lastName) {
    this(id, email, firstName, lastName, null, null);
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

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return the passwordForgottenCount
   */
  public Integer getPasswordForgottenCount() {
    return passwordForgottenCount;
  }
}
