# Spring Boot Hazelcast Session Replication

**A Jahnel Group sample project demonstrating session replication with Hazelcast.**

## Getting Started

Please read my blog post [Spring Boot Session Replication with Hazelcast](http://stevenz.io/spring-boot-session-replication-hazelcast-kotlin/) on [stevenz.io](http://stevenz.io) for an indepth guide through this sample.

This [Spring Boot](https://projects.spring.io/spring-boot/) application is written in [Kotlin](https://kotlinlang.org/) and demonstrates how to configure [Hazelcast](https://hazelcast.com/) for session replication via [Multicasting](https://en.wikipedia.org/wiki/Multicast). Provided is a [Docker Compose](https://docs.docker.com/compose/) file that will setup [HAProxy](http://www.haproxy.org/) for routing and a [Prometheus](https://prometheus.io/)/[Grafana](https://grafana.com/) stack for visualization.

Shown here is a useful command-line tool for testing API's called [HTTPie](https://httpie.org/). You don't need this but it provides much better output than curl.

## Build and run two instances of the app

It's important to use ports 9000 and 9001 because HAProxy and Grafana are configured with those defaults.

```bash
mvn clean package
java -jar target/*.jar --server.port=9000 &
java -jar target/*.jar --server.port=9001 &
```

When the applications start take notice of Hazelcast's Member output, it will clearly tell you that both nodes are speaking to each other.

Node 1:

```bash
Members [1] {
    Member [172.22.0.1]:5701 - b60ac97b-020a-4df6-8d03-f68eae7b8fee this
}
```

Node 2:

```bash
Members [2] {
    Member [172.22.0.1]:5701 - b60ac97b-020a-4df6-8d03-f68eae7b8fee
    Member [172.22.0.1]:5702 - f7a5b9b3-35d9-4d06-a3ba-46c0dbe3b223 this
}
```

## Start Docker-Compose : HAProxy and Prometheus / Grafana

Bring up the Docker instances with Docker Compose.

```bash
$ docker-compose up -d
Creating springbootsessionhazelcast_haproxy_1
Creating springbootsessionhazelcast_prometheus_1
Creating springbootsessionhazelcast_grafana_1

$ docker ps -a
CONTAINER ID        IMAGE                    COMMAND                  CREATED             STATUS              PORTS               NAMES
8d9095250780        grafana/grafana:4.4.3    "/run.sh"                11 seconds ago      Up 10 seconds                           springbootsessionhazelcast_grafana_1
25f9f39435aa        prom/prometheus:v1.7.1   "/bin/prometheus -..."   11 seconds ago      Up 10 seconds                           springbootsessionhazelcast_prometheus_1
e60a98b97336        haproxy:1.7              "/docker-entrypoin..."   11 seconds ago      Up 10 seconds                           springbootsessionhazelcast_haproxy_1

```

HAProxy will be running a reverse proxy on port **8080** and a stats console at http://localhost:9091/stats with credentials admin / admin.

Grafana will be running at http://localhost:3000/login with credentials admin / admin.

Prometheus will be running at http://localhost:9090/graph with no credentials.

## Test the Reverse Proxy and Load Balancing

You can use a browser but learning a command line tool can help you out in the long run. Here I will use [HTTPie](https://httpie.org/).

Try the unprotected end-point to prove that the round-robin reverse proxy works. Run the command a couple of times and you'll see the **port** in the response bounce back and forth.

```bash
$ http http://localhost:8080/api/unprotected
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Wed, 30 Aug 2017 16:52:59 GMT
Expires: 0
Pragma: no-cache
Set-Cookie: SESSION=a951796f-4020-48e5-86b3-b7ea9855648f; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Application-Context: application:9000
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "message": "Anyone can see this.",
    "port": 9000,
    "sessionId": "a951796f-4020-48e5-86b3-b7ea9855648f",
    "user": null
}

$ http http://localhost:8080/api/unprotected
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Wed, 30 Aug 2017 16:53:03 GMT
Expires: 0
Pragma: no-cache
Set-Cookie: SESSION=90417d8c-9236-43e1-a868-95380fc54a4f; Path=/; HttpOnly
Transfer-Encoding: chunked
X-Application-Context: application:9001
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "message": "Anyone can see this.",
    "port": 9001,
    "sessionId": "90417d8c-9236-43e1-a868-95380fc54a4f",
    "user": null
}

```

Now try to hit the protected end-point a couple of times and notice that you're redirected back to the login page.

```bash
$ http http://localhost:8080/api/protected
HTTP/1.1 302 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 0
Date: Wed, 30 Aug 2017 16:55:30 GMT
Expires: 0
Location: http://localhost:8080/login
Pragma: no-cache
Set-Cookie: SESSION=61721af1-6a8b-442c-b746-640b3e2bec0a; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

```

## Test the Session Replication

This application has one user with **username = user** and **password = pass** so login and try again.

```bash
$ http --form --session=s1 POST http://localhost:8080/login username=user password=pass
HTTP/1.1 302 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 0
Date: Wed, 30 Aug 2017 16:57:12 GMT
Expires: 0
Location: http://localhost:8080/welcome.html
Pragma: no-cache
Set-Cookie: SESSION=5c6e3486-b8a1-4e71-84ab-e6766acfbb12; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

A couple things to notice about the request is that we're passing in *--form* because the default Spring Security end-point assumes the payload to be a form post. We're also using *--session* and specifying a unique key as *s1* just so that HTTPie can keep track of the session for us and the method is *POST*.

The cool thing is that HTTPie handles arguments in a really slick way and when passed in like this it will automatically make a JSON payload for you like this:

```json
{
  "username" : "user",
  "password" : "pass"
}
```

Notice in the repsonse we're getting a **302** redirect to the location *http://localhost:8080/welcome.html* which means a successful login. Now remember that SESSION cookie (5c6e3486-b8a1-4e71-84ab-e6766acfbb12), you'll see that our app will spit that back out when the load balancer bounces us between servers.

Try hitting the protected end-point a couple of times.

```bash
$ http --session=s1 http://localhost:8080/api/protected
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Wed, 30 Aug 2017 16:58:11 GMT
Expires: 0
Pragma: no-cache
Transfer-Encoding: chunked
X-Application-Context: application:9000
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "message": "You are logged in so you can see this.",
    "port": 9000,
    "sessionId": "5c6e3486-b8a1-4e71-84ab-e6766acfbb12",
    "user": "user"
}

$ http --session=s1 http://localhost:8080/api/protected
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json;charset=UTF-8
Date: Wed, 30 Aug 2017 16:59:16 GMT
Expires: 0
Pragma: no-cache
Transfer-Encoding: chunked
X-Application-Context: application:9001
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

{
    "message": "You are logged in so you can see this.",
    "port": 9001,
    "sessionId": "5c6e3486-b8a1-4e71-84ab-e6766acfbb12",
    "user": "user"
}

```

Notice that the session remains and now a username is returned (user) because you're logged into both instances even when the server changes.

### Logout

Hit the logout end-point and you'll be logged out both servers.

```bash
$ http --session=s1 http://localhost:8080/logout
HTTP/1.1 302 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 0
Date: Wed, 30 Aug 2017 17:00:12 GMT
Expires: 0
Location: http://localhost:8080/login
Pragma: no-cache
Set-Cookie: SESSION=; Max-Age=0; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

Notice that you're redirect back to the login page, try to hit the protected end-point again. You're not logged in and your session has changed.

```bash
$ http --session=s1 http://localhost:8080/api/protected
HTTP/1.1 302 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 0
Date: Fri, 08 Sep 2017 17:44:13 GMT
Expires: 0
Location: http://localhost:8080/login
Pragma: no-cache
Set-Cookie: SESSION=1a8b9356-8788-44d6-91d7-0d582a810b4a; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

```
