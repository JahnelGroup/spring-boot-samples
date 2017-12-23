# Spring Boot Jackson Field Security

A sample application demonstrating how to use [JahnelGroup/jackson-field-security](https://github.com/JahnelGroup/jackson-field-security) to protect access to your your JSON model on fields-by-field basis with Jackson.

## Getting Started

[Jackson](https://github.com/FasterXML/jackson) is the standard library for JSON parsing and modeling in Java. The core library doesn't have the ability to conditional control access to individual fields of the JSON model. Today you have to create entire protected views that can be overwhelming to account for in your design. We created the [JahnelGroup/jackson-field-security](https://github.com/JahnelGroup/jackson-field-security) library to add this missing feature to Jackson.

## How it works

Here we are leveraging [Spring Data REST](https://projects.spring.io/spring-data-rest/) to very quickly provide simple http CRUD operations.

The application is seeded with a couple of users defined in [WebSecurityConfig.kt](https://github.com/JahnelGroup/spring-boot-samples/blob/master/spring-boot-jackson-field-security/src/main/kotlin/com/jahnelgroup/datarestsecurity/WebSecurityConfig.kt), we'll use three for this example **user1/pass**, **user2/pass** and **admin/pass** to create the club.

The two primary data models used in this sample are **Person** and **Club**. To get into a Club a Person would need to know the Club's secret phrase. Only users who are part of the Club will see that field, others will not.

In this sample I wrote a custom Policy called [ClubFieldPolicy](https://github.com/JahnelGroup/spring-boot-samples/blob/master/spring-boot-jackson-field-security/src/main/kotlin/com/jahnelgroup/datarestsecurity/policy/ClubFieldPolicy.kt) that will restrict access to Club fields to only users that are associated with that Club.

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
HTTP/1.1 201 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Sat, 23 Dec 2017 01:56:47 GMT
ETag: "0"
Expires: 0
Last-Modified: Sat, 23 Dec 2017 01:56:46 GMT
Location: http://localhost:8080/clubs/1
Pragma: no-cache
Set-Cookie: JSESSIONID=99084CAFC27069D764E2E7F4313FD830; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "_links": {
        "club": {
            "href": "http://localhost:8080/clubs/1"
        },
        "members": {
            "href": "http://localhost:8080/clubs/1/members"
        },
        "self": {
            "href": "http://localhost:8080/clubs/1"
        }
    },
    "clubName": "40-40 Club",
    "clubSecretPhrase": "the roc",
    "createdBy": "admin",
    "createdDatetime": "2017-12-22T19:56:46.95",
    "domainEvents": [],
    "lastModifiedBy": "admin",
    "lastModifiedDatetime": "2017-12-22T19:56:46.95"
}
```

### Unassociated People cannot see the Secret Phrase

Now try to query the club as either user1 or user2 and you will not see this field. 

```bash
spring-boot-jackson-field-security]$ http -a user1:pass http://localhost:8080/clubs/1
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/hal+json;charset=UTF-8
Date: Sat, 23 Dec 2017 02:01:51 GMT
ETag: "0"
Expires: 0
Last-Modified: Sat, 23 Dec 2017 01:56:46 GMT
Pragma: no-cache
Set-Cookie: JSESSIONID=1EB74BF6151597204B050D54F2B8B4B6; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "_links": {
        "club": {
            "href": "http://localhost:8080/clubs/1"
        },
        "members": {
            "href": "http://localhost:8080/clubs/1/members"
        },
        "self": {
            "href": "http://localhost:8080/clubs/1"
        }
    },
    "clubName": "40-40 Club",
    "createdBy": "admin",
    "createdDatetime": "2017-12-22T19:56:46.95",
    "domainEvents": [],
    "lastModifiedBy": "admin",
    "lastModifiedDatetime": "2017-12-22T19:56:46.95"
}
```

Notice that the field **clubSecretPhrase** isn't displayed for either user. 

### Associate a user2 Person to Club and access the secret

As **user2** create a Person and associate that with the Club.

```bash
$ http -a user2:pass POST http://localhost:8080/persons firstName=Steven lastName=Zgaljic club=http://localhost:8080/clubs/1
HTTP/1.1 201 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Sat, 23 Dec 2017 02:04:31 GMT
ETag: "0"
Expires: 0
Last-Modified: Sat, 23 Dec 2017 02:04:31 GMT
Location: http://localhost:8080/persons/1
Pragma: no-cache
Set-Cookie: JSESSIONID=6E20F05A853FADF1219A8DA21E87A64C; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "_links": {
        "club": {
            "href": "http://localhost:8080/persons/1/club"
        },
        "person": {
            "href": "http://localhost:8080/persons/1"
        },
        "self": {
            "href": "http://localhost:8080/persons/1"
        }
    },
    "createdBy": "user2",
    "createdDatetime": "2017-12-22T20:04:31.402",
    "domainEvents": [],
    "firstName": "Steven",
    "lastModifiedBy": "user2",
    "lastModifiedDatetime": "2017-12-22T20:04:31.402",
    "lastName": "Zgaljic",
    "ssn": ""
}
```

Now as **user2** query the club and you will see the the secret. 

```bash
spring-boot-jackson-field-security]$ http -a user2:pass http://localhost:8080/clubs/1
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/hal+json;charset=UTF-8
Date: Sat, 23 Dec 2017 02:05:18 GMT
ETag: "0"
Expires: 0
Last-Modified: Sat, 23 Dec 2017 01:56:46 GMT
Pragma: no-cache
Set-Cookie: JSESSIONID=9B3A2F3C24FB8BA0D8A68A563AF4F34E; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "_links": {
        "club": {
            "href": "http://localhost:8080/clubs/1"
        },
        "members": {
            "href": "http://localhost:8080/clubs/1/members"
        },
        "self": {
            "href": "http://localhost:8080/clubs/1"
        }
    },
    "clubName": "40-40 Club",
    "clubSecretPhrase": "the roc",
    "createdBy": "admin",
    "createdDatetime": "2017-12-22T19:56:46.95",
    "domainEvents": [],
    "lastModifiedBy": "admin",
    "lastModifiedDatetime": "2017-12-22T19:56:46.95"
}
```

Try again with **user1** and you will notice that they are still unable to see the secret. 
