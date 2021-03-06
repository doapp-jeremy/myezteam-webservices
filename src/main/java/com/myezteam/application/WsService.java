package com.myezteam.application;

/**
 * WsApplication.java
 * adagogo-selfservice-webservices
 * 
 * Created by jeremy on Sep 6, 2013
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2013 - All rights reserved.  Created by DoApp, Inc.
 */

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.DBI;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.myezteam.acl.TeamACL;
import com.myezteam.auth.TokenAuthenticator;
import com.myezteam.config.WsConfiguration;
import com.myezteam.db.TeamController;
import com.myezteam.db.dynamo.TokenDAO;
import com.myezteam.db.mysql.EmailDAO;
import com.myezteam.db.mysql.EventDAO;
import com.myezteam.db.mysql.PlayerDAO;
import com.myezteam.db.mysql.ResponseDAO;
import com.myezteam.db.mysql.TeamControllerMysql;
import com.myezteam.db.mysql.TeamDAO;
import com.myezteam.db.mysql.UserDAO;
import com.myezteam.exception.IllegalArgumentExceptionMapper;
import com.myezteam.exception.WebApplicationExceptionMapper;
import com.myezteam.resource.AuthResource;
import com.myezteam.resource.EmailResource;
import com.myezteam.resource.EventResource;
import com.myezteam.resource.PlayerResource;
import com.myezteam.resource.ResponseResource;
import com.myezteam.resource.TeamResource;
import com.myezteam.resource.UserResource;
import com.mysql.jdbc.Driver;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.jersey.InvalidEntityExceptionMapper;
import com.yammer.dropwizard.jersey.JsonProcessingExceptionMapper;


/**
 * @author jeremy
 * 
 */
public class WsService extends Service<WsConfiguration> {

  private static DatabaseConfiguration databaseConfig = new DatabaseConfiguration();
  static {
    Properties prop = new Properties();
    try (InputStream inputStream = WsService.class.getClassLoader().getResourceAsStream("env.properties")) {
      prop.load(inputStream);
      for (String name : prop.stringPropertyNames()) {
        System.setProperty(name, prop.getProperty(name));
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not load properties file");
    }
    databaseConfig.setUrl(checkNotNull(System.getProperty("dw.database.url"), "dw.database.url is null"));
    databaseConfig.setUser(checkNotNull(System.getProperty("dw.database.user"), "dw.database.user is null"));
    databaseConfig.setPassword(checkNotNull(System.getProperty("dw.database.password"), "dw.database.password is null"));
  }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new WsService().run(args);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.codahale.dropwizard.Application#initialize(com.codahale.dropwizard.setup.Bootstrap)
   */
  @Override
  public void initialize(Bootstrap<WsConfiguration> bootstrap) {
    DateTimeZone.setDefault(DateTimeZone.UTC);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.yammer.dropwizard.Service#run(com.yammer.dropwizard.config.Configuration, com.yammer.dropwizard.config.Environment)
   */
  @Override
  public void run(WsConfiguration configuration, Environment environment) throws Exception {
    final DBIFactory factory = new DBIFactory();
    databaseConfig.setDriverClass(Driver.class.getName());
    final DBI jdbi = factory.build(environment, databaseConfig, "mysql");

    final TeamDAO teamDAO = jdbi.onDemand(TeamDAO.class);
    final PlayerDAO playerDAO = jdbi.onDemand(PlayerDAO.class);
    final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
    final EventDAO eventDAO = jdbi.onDemand(EventDAO.class);
    final EmailDAO emailDAO = jdbi.onDemand(EmailDAO.class);
    final ResponseDAO responseDAO = jdbi.onDemand(ResponseDAO.class);

    AmazonDynamoDB dynamoDB = new AmazonDynamoDBClient();
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDB, new DynamoDBMapperConfig(
        TableNameOverride.withTableNamePrefix("DEV-")));

    AmazonSimpleEmailServiceClient ses = new AmazonSimpleEmailServiceClient();

    TokenDAO tokenDAO = new TokenDAO(dynamoDBMapper);

    TeamController teamController = new TeamControllerMysql(teamDAO, playerDAO, eventDAO);
    TeamACL teamACL = new TeamACL(teamController);

    environment.addResource(new OAuthProvider<Long>(new TokenAuthenticator(tokenDAO), "token"));

    environment.addResource(new EventResource(teamController, teamACL, eventDAO, emailDAO));
    environment.addResource(new TeamResource(teamController, teamACL));
    environment.addResource(new PlayerResource(teamController, teamACL, playerDAO, userDAO));
    environment.addResource(new UserResource(userDAO, teamController, ses));
    environment.addResource(new AuthResource(userDAO, tokenDAO));
    environment.addResource(new ResponseResource(teamACL, responseDAO, eventDAO, playerDAO));
    environment.addResource(new EmailResource(teamACL, teamDAO, playerDAO, eventDAO, emailDAO, responseDAO, ses));

    configureExceptionMappers(environment);

    // Allow CORS: https://groups.google.com/forum/#!msg/dropwizard-user/QYknyWOZmns/6YA8SmHSGu8J
    // and http://wiki.eclipse.org/Jetty/Feature/Cross_Origin_Filter
    environment.addFilter(CrossOriginFilter.class, "/*")
        .setInitParam("allowedOrigins", "*")
        .setInitParam("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin")
        .setInitParam("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS")
        .setInitParam("preflightMaxAge", "5184000") // 2 months
        .setInitParam("allowCredentials", "true");
  }

  private void configureExceptionMappers(Environment environment) {
    final ResourceConfig config = environment.getJerseyResourceConfig();
    final Set<Object> singletons = config.getSingletons();
    final List<Object> singletonsToRemove = new ArrayList<Object>();

    // remove default InvalidEntityException mapper
    for (Object s : singletons) {
      if (s instanceof com.yammer.dropwizard.jersey.InvalidEntityExceptionMapper) {
        singletonsToRemove.add(s);
      }
      else if (s instanceof JsonProcessingExceptionMapper) {
        singletonsToRemove.add(s);
      }
    }

    for (Object s : singletonsToRemove) {
      config.getSingletons().remove(s);
    }

    environment.addProvider(new InvalidEntityExceptionMapper());
    environment.addProvider(new WebApplicationExceptionMapper());
    environment.addProvider(new IllegalArgumentExceptionMapper());
  }
}
