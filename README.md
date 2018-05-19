# TechIT 2

TechIT 2 is a rewrite of the work order system
[TechIT](https://github.com/cysun/techit) developed by Brandon Ung, Minh Ha,
Duc Le, Kevin Castillo, and Marjorie Zelaya as their senior design project at
Cal State LA. In this rewrite we split the system into a backend that provides
a REST API and a SPA frontend.

This repository hosts the backend implemented in Java with Spring and Hibernate.

## Software Requirements

JDK 1.8+ and Maven are required to build the project, and MySQL 5.5+ and a
Java EE application server that supports the Servlet 3.1 Specification
(e.g. Tomcat 8.5+) are required to run it.

## Development Using Eclipse

1. Clone the project from its GitHub repository.
2. In Eclipse, select *File* -> *Import* ... -> *Existing Maven Projects*
   to import the project into Eclipse.
3. Create an empty MySQL database.
4. Populate the database using the following SQL scripts:
   * `src/main/scripts/techit-create.sql` - create all the schema
     elements.
   * `src/main/scripts/techit-test-insert.sql` - insert some sample
     data to play with.
5. Copy `build.properties.sample` to `build.properties`, and change
   the values in `build.properties` to match your environment.
6. In Eclipse, right click on the project and select *Maven* ->
   *Update Project ...* to update the project.
7. In Eclipse, right click on the project and select *Run As* ->
   *Run on Server* to run the project.

