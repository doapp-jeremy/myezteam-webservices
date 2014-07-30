/**
 * ResourceUtil.java
 * webservices
 * 
 * Created by jeremy on Jul 29, 2014
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2014 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.resource;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author jeremy
 *
 */
public abstract class ResourceUtil {
  private static MessageDigest md5;
  static {
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String generateResponseKey(Long eventId, Long playerId) throws UnsupportedEncodingException {
    byte messageDigest[] = md5.digest(new String(eventId + "ResponseKeySalt" + playerId).getBytes("UTF-8"));
    String responseKey = new String();
    for (int i = 0; i < messageDigest.length; i++) {
      String byteString = Integer.toHexString(0xFF & messageDigest[i]);
      if (byteString.length() == 1) {
        byteString = "0" + byteString;
      }
      responseKey += byteString;
    }
    return responseKey;
  }
}
