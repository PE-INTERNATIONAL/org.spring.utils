/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.spring.common.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

   private static Logger LOG = LoggerFactory.getLogger(CustomApplicationContextInitializer.class);

   private final static String PROPERTIES_FILE_LOAD_ORDER_FIRST= "FIRST";

   private final static String PROPERTIES_FILE_LOAD_ORDER_LAST = "LAST";

   /**
    * Reminder: Document this in the applications web.xml or other Application Container.
    */
   private final static String PROPERTIES_FILE_LOAD_ORDER_DEFAULT = PROPERTIES_FILE_LOAD_ORDER_FIRST;

   @Override
   public void initialize(ConfigurableApplicationContext applicationContext) {
      ConfigurableEnvironment environment = applicationContext.getEnvironment();

      String propertiesFilePath = environment.getProperty("propertiesFile");
      String propertiesFileLoadOrder = getPropertiesFileLoadOrderFrom(environment);

      try {
          MutablePropertySources propertySources = environment.getPropertySources();
          if (propertiesFileLoadOrder.equalsIgnoreCase(PROPERTIES_FILE_LOAD_ORDER_LAST)) {
            propertySources.addLast(new ResourcePropertySource(propertiesFilePath));
         } else {
            propertySources.addFirst(new ResourcePropertySource(propertiesFilePath));
         }
         LOG.info(String.format("'%s' loaded into ApplicationContext with load order '%s'", propertiesFilePath, propertiesFileLoadOrder));
      } catch (IOException ioException) {
         LOG.info(String.format("Could not load '%s' with load order '%s' while trying to load into the ApplicationContext", propertiesFilePath, propertiesFileLoadOrder));
      }
   }

   private String getPropertiesFileLoadOrderFrom(final ConfigurableEnvironment environment) {
      String propertiesFileLoadOrder = environment.getProperty("propertiesFileLoadOrder");
      if (Strings.isNullOrEmpty(propertiesFileLoadOrder)) {
         return PROPERTIES_FILE_LOAD_ORDER_DEFAULT;
      }
      if ( !propertiesFileLoadOrder.equalsIgnoreCase(PROPERTIES_FILE_LOAD_ORDER_FIRST) &&
           !propertiesFileLoadOrder.equalsIgnoreCase(PROPERTIES_FILE_LOAD_ORDER_LAST) ) {
         return PROPERTIES_FILE_LOAD_ORDER_DEFAULT;
      }
      return null;
   }
}
