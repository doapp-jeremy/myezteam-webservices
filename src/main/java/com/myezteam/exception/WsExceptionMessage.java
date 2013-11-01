package com.myezteam.exception;

import java.util.ArrayList;
import java.util.List;


public final class WsExceptionMessage {
  private static List<Integer> codes = new ArrayList<Integer>();

  private final int code;
  private final String message;

  private WsExceptionMessage(int code, String message) {
    this.code = code;
    this.message = message;
    if (codes.contains(code)) { throw new RuntimeException("Duplicate code: " + message); }
    codes.add(code);
  }

  public String getMessage() {
    return message;
  }

  public int getCode() {
    return code;
  }

  public static final WsExceptionMessage UNKNOWN_ERROR = new WsExceptionMessage(0, "Unknown error");
  public static final WsExceptionMessage INVALID_CREDS = new WsExceptionMessage(1, "Invalid credentials");
  public static final WsExceptionMessage DATA_ERROR = new WsExceptionMessage(2, "Data error");
  public static final WsExceptionMessage CACHE_ERROR = new WsExceptionMessage(3, "Cache error");
  public static final WsExceptionMessage QUERY_ERROR = new WsExceptionMessage(4, "Query error");
  public static final WsExceptionMessage SAVE_ERROR = new WsExceptionMessage(5, "Data save error");
  public static final WsExceptionMessage USER_ERROR = new WsExceptionMessage(6, "User error");
  public static final WsExceptionMessage SOCIAL_NETWORK_VALID_CREDS_NO_USER = new WsExceptionMessage(7,
      "Social network creds are valid, but user does not exist");
  public static final WsExceptionMessage SOCIAL_NETWORK_USER_MISMATCH = new WsExceptionMessage(9,
      "The social network user id does not match the social network id in the datastore");
  public static final WsExceptionMessage SOCIAL_NETWORK_VALIDATION_ERROR = new WsExceptionMessage(10,
      "The social validation failed");
  public static final WsExceptionMessage VALIDATION_ERROR = new WsExceptionMessage(11, "Validation Error");

  public static final WsExceptionMessage AUTHORIZATION_ERROR = new WsExceptionMessage(12, "Not Authorized");

  public static final WsExceptionMessage NOT_YET_IMPLEMENTED = new WsExceptionMessage(99, "Not Yet Implemented");

  @Override
  public int hashCode() {
    return code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof WsExceptionMessage)
      return (((WsExceptionMessage) obj).getCode() == this.code);
    else
      return false;
  }
}
