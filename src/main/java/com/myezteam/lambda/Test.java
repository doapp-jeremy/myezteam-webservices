/**
 * Test.java
 * webservices
 * 
 * Created by jeremy on Nov 14, 2016
 * DoApp, Inc. owns and reserves all rights to the intellectual
 * property and design of the following application.
 *
 * Copyright 2016 - All rights reserved.  Created by DoApp, Inc.
 */
package com.myezteam.lambda;

import java.io.File;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;


/**
 * @author jeremy
 *
 */
public class Test {

  public static void main(String[] args) throws ConfigurationException {

    // Configurations configs = new Configurations();
    // // Read data from this file
    // File propertiesFile = new File("env.properties");
    // PropertiesConfiguration config = configs.properties(propertiesFile);

    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(params.properties().setFile(new File("env.properties")));

  }

}
