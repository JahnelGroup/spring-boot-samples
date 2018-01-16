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
