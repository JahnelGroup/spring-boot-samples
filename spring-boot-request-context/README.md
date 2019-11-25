# Spring Boot Web Request Id

**A Jahnel Group sample project demonstrating how to generate unique ID's for each HTTP request.**

## Why

Knowing how your application work is crucial to supporting it. An important part of this in web applications is tracing the flows of each web request. A great way to do this is by generating a unique ID for each request. Among the many things you should do with this ID is to put it in a [Mapped Diagnostic Context](https://www.baeldung.com/mdc-in-log4j-2-logback) so every log statement printed has this unique identifier attached. This sample illustrates one way to do this with Spring Boot. 

## How it works

In this sample we are using a [OncePerRequestFilter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/OncePerRequestFilter.html) to handle the processing of a unique request ID. 

TODO. 

## Run it

Start the application with `gradle bootRun` and use your favorite HTTP tool to submit request. Here I'm using [HTTPie](https://httpie.org/).

```bash
$ http -vvv localhost:8080/hello?name=Steven
GET /hello?name=Steven HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/0.9.9

HTTP/1.1 200 
Content-Length: 12
Content-Type: text/plain;charset=UTF-8
Date: Sun, 24 Nov 2019 19:50:16 GMT
request-id: FAA61773FC7C4DFD8C11832F6A1BFD29

Hello Steven
```

Notice how a response header was set `request-id: FAA61773FC7C4DFD8C11832F6A1BFD29`. If we inspect the log statement you see it was printed there as well:

```bash
13:50:15.999 [http-nio-8080-exec-9] [name=Steven, request-id=FAA61773FC7C4DFD8C11832F6A1BFD29] INFO  c.j.r.RequestContextApplication$$EnhancerBySpringCGLIB$$a132cd9a - Echoing back 'Hello Steven'
```

You can also have the request set this identifier if you're chaining calls together that are related and want to share the ID. Here I'm setting the header `request-id:123` which will also be echo'd back out to me. 

```bash
$ http -vvv localhost:8080/hello?name=Steven request-id:123
GET /hello?name=Steven HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/0.9.9
request-id: 123

HTTP/1.1 200 
Content-Length: 12
Content-Type: text/plain;charset=UTF-8
Date: Sun, 24 Nov 2019 19:50:11 GMT
request-id: 123

Hello Steven
```

The log statement reflects this accordingly:

```bash
13:50:15.123 [http-nio-8080-exec-9] [name=Steven, request-id=123] INFO  c.j.r.RequestContextApplication$$EnhancerBySpringCGLIB$$a132cd9a - Echoing back 'Hello Steven'
```

