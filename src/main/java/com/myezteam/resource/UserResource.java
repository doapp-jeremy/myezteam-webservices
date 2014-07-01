/**
 * TeamResource.java
 * webservices
 * 
 * Created by jeremy on Nov 1, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.myezteam.api.Team;
import com.myezteam.api.User;
import com.myezteam.db.TeamController;
import com.myezteam.db.mysql.UserDAO;
import com.yammer.dropwizard.auth.Auth;


/**
 * @author jeremy
 * 
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/users")
public class UserResource extends BaseResource {
  private final UserDAO userDAO;
  private final TeamController teamController;
  private final AmazonSimpleEmailServiceClient ses;

  public UserResource(UserDAO userDAO, TeamController teamController, AmazonSimpleEmailServiceClient ses) {
    this.userDAO = userDAO;
    this.teamController = teamController;
    this.ses = ses;
  }

  private String createChangePasswordKey(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    // create a password change key
    byte messageDigest[] = md5.digest(new String(user.getEmail() + user.getPasswordForgottenCount() + "EMAIL_SALT").getBytes("UTF-8"));
    String passwordChangeKey = new String();
    for (int i = 0; i < messageDigest.length; i++) {
      String byteString = Integer.toHexString(0xFF & messageDigest[i]);
      if (byteString.length() == 1) {
        byteString = "0" + byteString;
      }
      passwordChangeKey += byteString;
    }

    userDAO.setPasswordChangeKey(user.getEmail(), passwordChangeKey);
    return passwordChangeKey;
  }

  @POST
  @Path("change_password")
  public User changePassword(@QueryParam(API_KEY) String apiKey, Map<String, String> input) {
    try {
      checkApiKey(apiKey);
      String passwordChangeKey = checkNotNull(input.get("password_change_key"), "password_change_key is required");
      String newPassword = checkNotNull(input.get("new_password"), "new_password is required");

      User existingUser = userDAO.findByPasswordChangeKey(passwordChangeKey);
      if (existingUser == null) { throw new Exception("Invalid password change key"); }

      userDAO.updatePassword(existingUser, newPassword);

      return existingUser;
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @POST
  @Path("reset")
  public void resetPassword(@QueryParam(API_KEY) String apiKey, Map<String, String> input) {
    try {
      checkApiKey(apiKey);
      String email = checkNotNull(input.get("email"), "Email is required");
      String redirectUrl = checkNotNull(input.get("redirect_url"), "redirect_url is required");
      User existingUser = userDAO.findByEmail(email);
      if (existingUser == null) { throw new Exception("A user with email " + email + " does not exist"); }
      Map<String, Object> result = new HashMap<>();
      result.put("user", existingUser);
      String passwordChangeKey = createChangePasswordKey(existingUser);
      result.put("password_change_key", passwordChangeKey);
      SendEmailRequest sendEmailRequest = new SendEmailRequest().withSource("myezteam@gmail.com");
      Destination dest = new Destination().withToAddresses(existingUser.getEmail());
      dest.withBccAddresses("admin@myezteam.com");
      Content subjContent = new Content().withData("Password change request");
      Message msg = new Message().withSubject(subjContent);
      Content htmlContent = new Content().withData("<a href='" + redirectUrl + "/" + passwordChangeKey + "'>reset password here</a>");
      Body body = new Body().withHtml(htmlContent);
      msg.setBody(body);
      sendEmailRequest.setDestination(dest);
      sendEmailRequest.setMessage(msg);
      ses.sendEmail(sendEmailRequest);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  public User me(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);
      return userDAO.findById(userId);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @POST
  public User create(@QueryParam(API_KEY) String apiKey, User user) {
    try {
      checkApiKey(apiKey);
      String email = checkNotNull(user.getEmail(), "Email is required");
      checkNotNull(user.getPassword(), "Password is required");
      User existingUser = userDAO.findByEmail(email);
      if (existingUser != null) { throw new Exception("A user with email " + email + " already exists"); }
      userDAO.createUser(user);
      Long newId = userDAO.getLastInsertId();
      return new User(newId, email, user.getFirstName(), user.getLastName());
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @PUT
  public void update(@Auth Long userId, @QueryParam(API_KEY) String apiKey, User user) {
    try {
      checkApiKey(apiKey);
      checkNotNull(user.getId(), "User id is null");
      checkArgument(userId.equals(user.getId()), "Can not update the profile of another user");
      userDAO.updateUser(user);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }

  @GET
  @Path("/friends")
  public List<User> friends(@Auth Long userId, @QueryParam(API_KEY) String apiKey) {
    try {
      checkApiKey(apiKey);

      Set<Long> teamIds = new HashSet<Long>();
      String teamIdsString = "";

      for (Team team : teamController.getTeamsUserManages(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }
      for (Team team : teamController.getTeamsUserOwns(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }
      for (Team team : teamController.getTeamsUserPlaysOn(userId)) {
        if (teamIdsString.length() > 0) {
          teamIdsString += ",";
        }
        teamIdsString += String.valueOf(team.getId());
        teamIds.add(team.getId());
      }

      // this is a hack until I can figure out the correct jdbi IN () call
      return userDAO.findUsersOnTeams(teamIdsString);
    } catch (Throwable e) {
      throw new WebApplicationException(e);
    }
  }
}
