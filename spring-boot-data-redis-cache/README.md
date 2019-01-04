# Spring Boot w/ Redis Cache

This sample demonstrates Spring Boot with an external Redis as a caching layer.

## Logging

Logging for Spring's CacheInterceptor is set to TRACE in [application.properties](./src/main/resources/application.properties) so you can see if a request is a cache hit or miss.

```
logging.level.org.springframework.cache.interceptor.CacheInterceptor=TRACE
```

## Docker

This sample uses [docker](https://docs.docker.com/install/) and [docker-compose](https://docs.docker.com/compose/install/) so you must install those first.  

## Build and Run

This samples uses a gradle plugin to build a docker container around the spring-boot application then launches it together with Redis in a docker-compose file.

```bash
$ gradle buildDocker
$ docker-compose up 
```

## Seed Data

The sample is pre-seeded with [data.sql](./src/main/resources/data.sql) with three messages that will demonstrates cache misses.

```h2
-- These will all be cache misses.
insert into message(id, text) values
 (1, 'Database Inserted 1'),
 (2, 'Database Inserted 2'),
 (3, 'Database Inserted 3');
```

## Testing 

Watch the docker terminal for log messages printed from the application that indicates if a message was a cache hit or miss.

### Message does not exist

First try getting a message that does not exist. You will received a 404 NOT FOUND: 

```bash
$ curl -XGET http://localhost:8080/messages/100 
{"timestamp":"2019-01-04T16:56:28.662+0000","status":404,"error":"Not Found","message":"No message available","path":"/messages/5"}
```

Among other things you'll see this in the logs:

```
No cache entry for key '5' in cache(s) [messages]
Cache miss for id 5 - going to the database.
```

### Message exits but a Cache Miss, then a Cache Hit

Now try to grab one of those pre-seeded messages from the database, it will be returned:

```bash
$ curl -XGET http://localhost:8080/messages/1
{"id":1,"text":"Database Inserted 1"}
```

Among other things you'll see that initially it's a cache miss:

```text
No cache entry for key '1' in cache(s) [messages]
Cache miss for id 1 - going to the database.
```

Now try it again and you'll see it's a cache hit:

```
Cache entry for key '1' found in cache 'messages'
```

### Create new Message and proactively update cache

Create a new message:

```bash
$ curl -XPOST http://localhost:8080/messages?text=TestMessage
{"id":4,"text":"TestMessage"}
```

Now try to get it and you'll see it's immediately cached.

```bash
$ curl -XGET http://localhost:8080/messages/4
{"id":4,"text":"TestMessage"}
```

```
Cache entry for key '4' found in cache 'messages'
```