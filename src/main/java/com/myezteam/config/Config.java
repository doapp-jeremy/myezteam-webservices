package com.myezteam.config;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Config {
  private static final Logger log = LoggerFactory.getLogger(Config.class);
  public static final CompositeConfiguration instance = new CompositeConfiguration();

  static {
    instance.setThrowExceptionOnMissing(true);
    instance.addConfiguration(new SystemConfiguration());
    instance.addConfiguration(new EnvironmentConfiguration());
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(params.properties().setFileName(Config.class.getResource("/env.properties").getFile()));
    try {
      instance.addConfiguration(builder.getConfiguration());
    } catch (ConfigurationException e) {
      log.warn(e.getMessage());
    }

  }
}
