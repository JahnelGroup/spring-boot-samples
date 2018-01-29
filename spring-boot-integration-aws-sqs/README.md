### Spring Boot Integration AWS SQS Sample App

This app demonstrates both sending and receiving messages using Spring Integration
and AWS SQS.

The app consists of the following:

1. A simple @RestController for TextMessage objects mapped to /textMessages
2. A Spring Integration @MessagingGateway as the entry points into two integration flows
3. The POST endpoint sends the resources to an AWS SQS queue
4. The GET endpoint is a simple RESTful GET implementation using Spring Integration
5. An integration flow stemming from reading an SQS queue and persisting the message payload

#### Running the Service
````
gradle bootRun
````

There is a groovy test that invokes all the integration flows using a real SQS queue
````
gradle test
````

#### Invoking the endpoints
````
$ http POST :8080/textMessages content="Hello, World"
HTTP/1.1 202 
Content-Type: application/json;charset=UTF-8
Date: Sun, 28 Jan 2018 22:15:18 GMT
Transfer-Encoding: chunked

{
    "content": "Hello, World",
    "receivedFromSQSTs": null,
    "sentToSQSTs": "2018-01-28T16:15:09.902",
    "uuid": "12f15236-e2bb-4e7f-a712-01341effc585"
}

$ http :8080/textMessages/12f15236-e2bb-4e7f-a712-01341effc585
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Sun, 28 Jan 2018 22:17:59 GMT
Transfer-Encoding: chunked

{
    "content": "Hello, World",
    "receivedFromSQSTs": "2018-01-28T16:15:09.755",
    "sentToSQSTs": "2018-01-28T16:15:09.902",
    "uuid": "12f15236-e2bb-4e7f-a712-01341effc585"
}
````