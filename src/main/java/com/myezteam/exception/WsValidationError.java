/**
 * ReadfulValidationError.java
 * readful-ws
 * 
 * Created by jeremy on Jun 12, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.exception;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;


/**
 * @author jeremy
 * 
 */
public class WsValidationError extends WsError {
  @JsonProperty
  private final Map<String, String> violations = new HashMap<String, String>();

  /**
   * @param t
   */
  public WsValidationError(ConstraintViolationException e) {
    super("Validation Exception", WsExceptionMessage.VALIDATION_ERROR.getCode());
    for (ConstraintViolation<?> c : e.getConstraintViolations()) {

      // com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES
      LowerCaseWithUnderscoresStrategy snakeCase = new com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy();
      violations.put(snakeCase.translate(c.getPropertyPath().toString()), c.getMessage());
    }
  }
}
