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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.skife.jdbi.v2.DBI;
import com.adagogo.config.WsConfiguration;
import com.myezteam.db.TeamDAO;
import com.myezteam.exception.IllegalArgumentExceptionMapper;
import com.myezteam.exception.WebApplicationExceptionMapper;
import com.myezteam.resource.TeamResource;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.jersey.InvalidEntityExceptionMapper;
import com.yammer.dropwizard.jersey.JsonProcessingExceptionMapper;


/**
 * @author jeremy
 * 
 */
public class WsService extends Service<WsConfiguration> {

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
  public void initialize(Bootstrap<WsConfiguration> bootstrap) {}

  /*
   * (non-Javadoc)
   * 
   * @see com.yammer.dropwizard.Service#run(com.yammer.dropwizard.config.Configuration,
   * com.yammer.dropwizard.config.Environment)
   */
  @Override
  public void run(WsConfiguration configuration, Environment environment) throws Exception {
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDatabase(), "mysql");
    final TeamDAO teamDAO = jdbi.onDemand(TeamDAO.class);

    environment.addResource(new TeamResource(teamDAO));

    configureExceptionMappers(environment);
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
