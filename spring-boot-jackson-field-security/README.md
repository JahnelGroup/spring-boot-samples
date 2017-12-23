# Spring Boot Jackson Field Security

A sample application demonstrating how to use [JahnelGroup/jackson-field-security](https://github.com/JahnelGroup/jackson-field-security) to protect access to your your JSON model on fields-by-field basis with Jackson.

## Getting Started

[Jackson](https://github.com/FasterXML/jackson) is the standard library for JSON parsing and modeling in Java. The core library doesn't have the ability to conditional control access to individual fields of the JSON model. Today you have to create entire protected views for your that can be overwhelming to absorb in to your design. We created a library to add this missing to Jackson and this sample illustrates how simple it is to use. 

## How it works

Here we are leveraging [Spring Data REST](https://projects.spring.io/spring-data-rest/) to very quickly provide simple http CRUD operations.

The application is seeded with a couple of users defined in [WebSecurityConfig.kt](https://github.com/JahnelGroup/spring-boot-samples/blob/master/spring-boot-jackson-field-security/src/main/kotlin/com/jahnelgroup/datarestsecurity/WebSecurityConfig.kt), we'll use three for this example **user1/pass**, **user2/pass** and **admin/pass** to create the club.

The two primary data models used in this sample are **Person** and **Club**. To get into a Club the Person would need the Club's secret phrase. Only users who are part of the Club will see that field, others will not.

Let's start the application and use [HTTPie](https://httpie.org/) to demonstrate it's use. 

```bash
[spring-boot-jackson-field-security]$ gradle bootRun
```

It will start on the default port of 8080, hit that you'll see the root Spring Data REST end-point.

```bash
$ http localhost:8080
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/hal+json;charset=UTF-8
Date: Sat, 23 Dec 2017 01:49:26 GMT
Expires: 0
Pragma: no-cache
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "_links": {
        "clubs": {
            "href": "http://localhost:8080/clubs"
        },
        "persons": {
            "href": "http://localhost:8080/persons"
        },
        "profile": {
            "href": "http://localhost:8080/profile"
        }
    }
}
```

### Create a Club with a Secret Phase

As the admin let's first create a Club called the **40-40 Club** with the secret phrase of **the roc**. 

```bash
$ http -a admin:pass POST http://localhost:8080/clubs clubName="40-40 Club" clubSecretPhrase="the roc"
```
