This blog post is the example application of the following blog posts:

* [Spring Data JPA Tutorial: Getting the Required Dependencies](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-getting-the-required-dependencies/)
* [Spring Data JPA Tutorial: Configuration](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-one-configuration/)
* [Spring Data JPA Tutorial: CRUD](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-two-crud/)
* Spring Data JPA Tutorial: Query Methods (TBD)

**Note:** This application is still work in progress.

Prerequisites
=============

You need to install the following tools if you want to run this application:

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](http://maven.apache.org/) (the application is tested with Maven 3.2.1)

Running the Tests
=================

You can run the unit tests by using the following command:

    mvn clean test -P dev

You can run the integration tests by using the following command:

    mvn clean verify -P integration-test

Running the Application
=======================

You can run the application by using the following command:

    mvn clean jetty:run -P dev
