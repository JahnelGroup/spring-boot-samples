# Spring Boot Hazelcast Queue and Topic

**A Jahnel Group sample project demonstrating distributed queues and topics with Hazelcast.**

## Getting Started

[Hazelcast](https://hazelcast.com/) is a lot of things but its primarily an In-Memory distributed Data Grid. We use it for caching and communicating across nodes in a direct Point-to-Point or Pub/Sub Topic design. It provides a wealth of other features like Distributed Data Structures such as Map, Queue, Ringbuffer, Set, and List.

[Read the documentation here.](http://docs.hazelcast.org/docs/latest/manual/html-single/)

## Java Config v.s. XML Config

You can use either Java or XML configuration, I opted to use both here. I am using the [hazelcast.xml](src/main/resources/hazelcast.xml) file to define only infrastructure config, in this case the multi-casting ip and port. My Java configurations are defined in the code when the Spring Beans are loaded.

## Demos

### [HeartbeatTopic.java](src/main/java/com/jahnelgroup/queue/hazelcast/HeartbeatTopic.java)

A simple topic where a node periodically emits a Heartbeat message to every node. Note that this is a simple pub/sub Topic with the fire-and-forget paradigm - it is not durable and does not gurantee each node receives the Heartbeat. You shouldn't build your own Heartbeat however, [Heartbeat](http://docs.hazelcast.org/docs/latest/manual/html-single/search.html?q=heartbeat) is actually already built into Hazelcast. 

### [AtomicLongCounter.java](src/main/java/com/jahnelgroup/queue/hazelcast/AtomicLongCounter.java)

If you need a simple distributed counter then opt for IAtomicLong. It handles all the dirty work of staying insync across nodes.

### [ReliableMessagePublisher.java](src/main/java/com/jahnelgroup/queue/hazelcast/reliableQueue/ReliableMessagePublisher.java) and [ReliableMessageSubscriber](src/main/java/com/jahnelgroup/queue/hazelcast/reliableQueue/ReliableMessageSubscriber.java)

Hazelcast makes it easy to share a distributed queue across nodes but it's important to realize that by default everything is entirely in-memory and subject to data loss upon failures. It's possible to make the queue durable by configuring a [QueueStore](http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#queueing-with-persistent-datastore) and back the queue with a database or some durable source. 

This demo uses two types of Spring profiles to demo this functionaltiy, use the profile **publisher** to start a node that puts messages onto the queue and backs it with a database, and use the profile **subscriber** to consume the messages. Note that this is a **Queue** and not a Topic so if you start multiple subscriber's then one and only with receive each message. If you're for every node to receive the message then you need a Topic. 

You can view the database through H2's web console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console), make sure to use the url **jdbc:h2:mem:db** with user/pass of **sa/sa**.

## Build and run two instances of the app

```bash
gradle build
java -jar -Dspring.profiles.active=publisher build/libs/*.jar
java -jar -Dspring.profiles.active=subscriber build/libs/*.jar
```
