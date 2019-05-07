# Spring Boot API Passthrough

**A Jahnel Group sample project demonstrating an API passthrough of a service that would sit between two REST applications.**

## Use Case

Let's establish some requirements for this hypothetical service. 

> The requirements for this system is a back-end API driven architecture focused on processing requests between two different systems. System A will send requests to this new service which will in-term proxy those requests down to System B. The results of the proxied called will be persisted in this system and available for retrevial via polling against this system. The proxied request should be asynchronous so System A doesn't need to wait until the processing from System B is done. 

This system will conduct the _work_ via a three-hop queue with database polling to ensure performance and data integrity under high load and system failure. 

* The first hop will be simply to accept the work from System A and persist it to the database.
* The second hop will be to process the work against the down stream System B
* The three hop will be to emit the result out, in this case just print it to the log file. 

## Smarty Streets

The scenario used in this sample is to proxy ZIP Code lookups to a system called [SmartyStreets](https://smartystreets.com/). This sample will make requests against their [ZIP Code API](https://smartystreets.com/docs/cloud/us-zipcode-api) to take a ZIP Code and retrieve the City States associated to it. 

### Simulated Latency, Enrichment Status and Timestamps

The lookup is almost instantaneous so to simulate latency a 5 second delay has been built into this sample application. This gives you the opportunity to experience all three statues:

* SUBMITTED - The request has been accepted and will be processed asynchronously.
* SUCCESS - The request has been fulfilled successfully.
* FAILURE - The request has been fulfilled but there was a failure during processing.

There are also three different Timestamps that track the progress of the message through the system.

* submissionDateTime - When the work was accepted by this middleware service.
* enrichmentDateTime - When the work was enriched/processed by the down stream (proxied) service.
* responseDateTime - When the result of the work was emitted out (i.e., printed to the logs).

## Running the app

Using [HTTPie](https://httpie.org/) we'll demonstrate how this works. First start the application with `$ gradle bootRun`

Make a JSON POST request to look up a ZIP Code in this format:

```json
{
    "zipCode": 11105
}
```

To accomplish this with HTTPie do:

```bash
$ http -v POST http://localhost:8080 zipCode=11105
POST / HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 20
Content-Type: application/json
Host: localhost:8080
User-Agent: HTTPie/0.9.9

{
    "zipCode": "11105"
}

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Tue, 07 May 2019 18:26:25 GMT
Transfer-Encoding: chunked

{
    "enrichmentStatus": "SUBMITTED", 
    "id": 1, 
    "submissionDateTime": "2019-05-07T18:26:25.428Z", 
    "zipCode": 11105
}
```

Immediately take the `id` that was returned and look up the status with:

```bash
$ http GET http://localhost:8080/1
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Fri, 19 Apr 2019 17:51:14 GMT
Transfer-Encoding: chunked

{
    "enrichmentStatus": "SUBMITTED", 
    "id": 1, 
    "submissionDateTime": "2019-05-07T18:26:25.428Z", 
    "zipCode": 11105
}
```

Notice that it's **SUBMITTED** and it has **submissionDateTime**. Wait 5 seconds and then try again:

```bash
$ http GET http://localhost:8080/1
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Tue, 07 May 2019 18:27:36 GMT
Transfer-Encoding: chunked

{
    "cityStates": [
        "Astoria", 
        "Long Is City", 
        "Long Island City"
    ], 
    "enrichmentDateTime": "2019-05-07T18:26:30.854Z", 
    "enrichmentStatus": "SUCCESS", 
    "id": 1, 
    "responseDateTime": "2019-05-07T18:26:38.357Z", 
    "submissionDateTime": "2019-05-07T18:26:25.428Z", 
    "zipCode": 11105
}
```

It's now **SUCCESS** with both **enrichmentDateTime** and **responseDateTime**. Try again with a zipCode that doesn't exist:

```bash
$ http -v POST http://localhost:8080 zipCode=00000
POST / HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 20
Content-Type: application/json
Host: localhost:8080
User-Agent: HTTPie/0.9.9

{
    "zipCode": "00000"
}

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Tue, 07 May 2019 18:28:49 GMT
Transfer-Encoding: chunked

{
    "enrichmentStatus": "SUBMITTED", 
    "id": 2, 
    "submissionDateTime": "2019-05-07T18:28:49.382Z", 
    "zipCode": 0
}
```

It will display **FAILURE**. 

```bash
$ http GET http://localhost:8080/2
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Tue, 07 May 2019 18:29:02 GMT
Transfer-Encoding: chunked

{
    "enrichmentDateTime": "2019-05-07T18:28:55.680Z", 
    "enrichmentStatus": "FAILURE", 
    "id": 2, 
    "responseDateTime": "2019-05-07T18:28:58.389Z", 
    "submissionDateTime": "2019-05-07T18:28:49.382Z", 
    "zipCode": 0
}
```

## Mocking the req/response to SmartyStreets

Perhaps you want to provide this service to someone quickly so they can build out the request/response to your system while you're still working on down stream integration (i.e., SmartyStreets). We can leverage Spring [Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) in this scenario and define two types of beans.

In this sample we've defined one interface [SmartyStreetsService](./src/main/java/com/example/api/smartystreets/SmartyStreetsService.java) with two implementations [DefaultSmartyStreetsService](./src/main/java/com/example/api/smartystreets/DefaultSmartyStreetsService.java) which actually makes a REST call and [MockSmartyStreetsService](./src/main/java/com/example/api/smartystreets/MockSmartyStreetsService.java) which looks up data from a local [data.json](./src/main/resources/json/data.json) file.

```java
@Profile("!mock")
@Component
public class DefaultSmartyStreetsService implements SmartyStreetsService{}

@Profile("mock")
@Component
public class MockSmartyStreetsService implements SmartyStreetsService{}
```

We control the loading of these two components by passing in a profile. 

```java
$ gradle build
$ java -Dspring.profiles.active=mock -jar build/libs/api-0.0.1-SNAPSHOT.jar 
```

If running through an IDE you must configure it to pass `-Dspring.profiles.active=mock` as a VM option in for you.