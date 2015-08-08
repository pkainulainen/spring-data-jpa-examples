This blog post is the example application of the following blog posts:

* [Spring Data JPA Tutorial: Creating Database Queries With the JPA Criteria API](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/)
* [Spring Data JPA Tutorial: Sorting]() - Not published yet

You might also want to read the other parts of my Spring Data JPA Tutorial:


* [Spring Data JPA Tutorial: Getting the Required Dependencies](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-getting-the-required-dependencies/)
* [Spring Data JPA Tutorial: Configuration](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-one-configuration/)
* [Spring Data JPA Tutorial: CRUD](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-two-crud/)
* [Spring Data JPA Tutorial: Introduction to Query Methods](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/)
* [Spring Data JPA Tutorial: Creating Database Queries From Method Names](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-creating-database-queries-from-method-names/)
* [Spring Data JPA Tutorial: Creating Database Queries With the @Query Annotation](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-creating-database-queries-with-the-query-annotation/)
* [Spring Data JPA Tutorial: Creating Database Queries With Named Queries](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-creating-database-queries-with-named-queries/)
* [Spring Data JPA Tutorial: Creating Database Queries With Querydsl](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-five-querydsl/)
* [Spring Data JPA Tutorial: Auditing, Part One](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/)
* [Spring Data JPA Tutorial: Auditing, Part Two](http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-two/)


**Note:** This application is still work in progress.

Prerequisites
=============

You need to install the following tools if you want to run this application:

Backend
---------

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](http://maven.apache.org/) (the application is tested with Maven 3.2.1)

Frontend
----------

* [Node.js](http://nodejs.org/)
* [NPM](https://www.npmjs.org/)
* [Bower](http://bower.io/)
* [Gulp](http://gulpjs.com/)

You can install these tools by following these steps:

1.  Install Node.js by using a [downloaded binary](http://nodejs.org/download/) or a [package manager](https://github.com/joyent/node/wiki/Installing-Node.js-via-package-manager).
    You can also read this blog post: [How to install Node.js and NPM](http://blog.nodeknockout.com/post/65463770933/how-to-install-node-js-and-npm)

2.  Install Bower by using the following command:

        npm install -g bower

3. Install Gulp by using the following command:

        npm install -g gulp


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
    
Credits
=========

* Kyösti Herrala. The Gulp build script and its Maven integration are based on Kyösti's ideas.
* [Techniques for authentication in AngularJS applications](https://medium.com/opinionated-angularjs/techniques-for-authentication-in-angularjs-applications-7bbf0346acec)

Known Issues
============

* If you refresh the login page, you aren't redirected away from it after successful login.