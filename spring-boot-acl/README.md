# Spring Boot ACL 

## spring-security-acl-annotations

This sample requires [spring-security-acl-annotations](https://github.com/JahnelGroup/spring-security-acl-annotations) which hasn't been published to any repository yet. Pull down that repo and then install to you local maven repo.

```bash
$ git clone git@github.com:JahnelGroup/spring-security-acl-annotations.git
$ cd spring-security-acl-annotations
$ mvn clean install
``` 

## Start

```bash
$ gradle bootRun
```

Launch http://localhost:8080

## Database

[H2 Console](http://localhost:8080/h2-console) with these settings:

* Driver Class: org.h2.Driver
* JDBC Url: jdbc:h2:mem:db
* User Name: sa
* Password: sa 
