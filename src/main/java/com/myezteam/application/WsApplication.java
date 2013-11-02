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



/**
 * @author jeremy
 * 
 */

//public class WsApplication extends Service<WsConfiguration> {
//
//  /**
//   * @param args
//   * @throws Exception
//   */
//  public static void main(String[] args) throws Exception {
//    new WsApplication().run(args);
//  }
//
//  // private final HibernateBundle<WsConfiguration> hibernateBundle =
//  // new HibernateBundle<WsConfiguration>(Team.class) {
//  // @Override
//  // public DataSourceFactory getDataSourceFactory(WsConfiguration configuration) {
//  // return configuration.getDataSourceFactory();
//  // }
//  // };
//
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.codahale.dropwizard.Application#initialize(com.codahale.dropwizard.setup.Bootstrap)
//   */
//  @Override
//  public void initialize(Bootstrap<WsConfiguration> bootstrap) {
//    // bootstrap.addBundle(hibernateBundle);
//  }
//
//  /*
//   * (non-Javadoc)
//   * 
//   * @see com.codahale.dropwizard.Application#run(com.codahale.dropwizard.Configuration,
//   * com.codahale.dropwizard.setup.Environment)
//   */
//  @Override
//  public void run(WsConfiguration configuration, Environment environment) throws Exception {
//
//    final JerseyEnvironment jerseyEnv = environment.jersey();
//
//    // final TeamDAO teamDao = new TeamDAO(hibernateBundle.getSessionFactory());
//    final DBIFactory factory = new DBIFactory();
//    final DBI jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "postgresql");
//    final TeamDAO dao = jdbi.onDemand(TeamDAO.class);
//
//    // resources
//    jerseyEnv.register(new TeamResource(teamDao));
//
//    // 0.6.2 - Allow CORS:
//    // https://groups.google.com/forum/#!msg/dropwizard-user/QYknyWOZmns/6YA8SmHSGu8J
//    // and http://wiki.eclipse.org/Jetty/Feature/Cross_Origin_Filter
//    // 0.7.0 -
//    // https://groups.google.com/forum/#!searchin/dropwizard-user/0.7.0$20crossoriginfilter/dropwizard-user/xl5dc_i8V24/tbE9geZkJTcJ
//    Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
//    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//    filter.setInitParameter("allowedOrigins", "*");
//    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
//    filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS");
//    filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
//    filter.setInitParameter("allowCredentials", "true");
//
//    /** START of handling exceptions always as JSON ***/
//    Set<Object> dwSingletons = ImmutableSet.copyOf(jerseyEnv.getResourceConfig().getSingletons());
//    for (Object s : dwSingletons) {
//      if (s instanceof ExceptionMapper) {
//        jerseyEnv.getResourceConfig().getSingletons().remove(s);
//      }
//    }
//
//    // Register the custom ExceptionMapper(s)
//    jerseyEnv.register(new WsExceptionMapper());
//    /** END of handling exceptions always as JSON ***/
//
//  }
// }
