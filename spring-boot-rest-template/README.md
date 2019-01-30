# Spring Boot RestTemplate

Extremely simple example of using [RestTemplate](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html). 

## Structure 

### File Structure 

The overall file structure is as follows:

```text
/spring-boot-rest-template/
├── src/main
│   └── java/com/jahnelgroup/resttemplate
│       └── App.java
│   └── resources
│       └── application.properties
├── build.gradle
└── docker-compose.yml
```

### Instances

This example uses docker to spin up two instances of the sample application with different parameters. They both use RestTemplate to make a basic HTTP request to each other. 

```dockerfile
jg-rest-tempate-1:
    # <other configs>
    command: --instance-id=jg-rest-tempate-1 --hello-host=jg-rest-tempate-1:8080
    
jg-rest-tempate-2:
    # <other configs>
    command: --instance-id=jg-rest-tempate-2 --hello-host=jg-rest-tempate-2:8080
```

## Prerequisites

You will need to install [docker](https://docs.docker.com/install/) and [docker-compose](https://docs.docker.com/compose/install).

## Run 

Bring up the entire stack with:

```bash
$ gradle composeUp
```

You can then monitoring the log outputs with:

```bash
$ docker logs -f jg-rest-tempate-1
$ docker logs -f jg-rest-tempate-2
```