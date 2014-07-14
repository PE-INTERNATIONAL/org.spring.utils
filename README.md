Common Spring Utils
============

Inject a property file into the Spring Environment. This is needed if you want to use properties from a file while importing xml in Spring managed beans xml files, like so:

```xml
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>
```

The variable ${customer} should now be read from a variable.

According to a [http://spring.io/blog/2011/02/15/spring-3-1-m1-unified-property-management/](blog entry from spring) this should be possible.

Unfortunately it does not work when you use a property-placeholder:

```xml
<context:property-placeholder ignore-resource-not-found="true" ignore-unresolvable="false" location="some.properties"/>
```

The file property resources are resolved after the xml imports are resolved.

Because of this you need to use a class, which registers a new ResourcePropertySource by hand into the application context. This can be done using the class "org.spring.common.utils.CustomApplicationContextInitializer" from this repository.

In a ServletContainer you can configure web.xml like so to use this class:

```xml
  <context-param>
    <param-name>contextInitializerClasses</param-name>
    <param-value>org.spring.common.utils.CustomApplicationContextInitializer</param-value>
  </context-param>
  <context-param>
    <param-name>propertiesFile</param-name>
    <param-value>/some/folder/some.properties</param-value>
    <!-- 
      Here you can also use spring variables like "file:${some.dir.variable}"
    -->
  </context-param>
  <!--
  <context-param>
    <param-name>propertiesFileLoadOrder</param-name>
    <param-value>FIRST|LAST</param-value>
  </context-param>
  This parameter controls the order in which the property file parameters are loaded.
  "FIRST": The parameters of the property file are loaded first and win against other sources (System, JVM, JNDI Parameters).
  "LAST":  The parameters of the property file are loaded last and loose against other sources (System, JVM, JNDI Parameters).

  An example: Parameter "myParameter" is passed in the JVM with "JVM Parameter" and saved in the property file with "Property File Parameter"
  "FIRST": "myParameter" is "Property File Parameter", because the value is first loaded from the property file.
  "LAST": "myParameter" is "JVM Parameter", because the value is last loaded from the property file and the JVM Parameter is loaded first.

  The default value is "FIRST".
  -->
```

Comments, questions and feedback are very welcome.