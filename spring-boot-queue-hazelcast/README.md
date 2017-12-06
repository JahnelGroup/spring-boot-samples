# Spring Boot Hazelcast Queue and Topic

**A Jahnel Group sample project demonstrating distributed queues and topics with Hazelcast.**

## Getting Started

[Hazelcast](https://hazelcast.com/) is a lot of things but its primarily an In-Memory distributed Data Grid. We use it for caching and communicating data across nodes in a direct Point-to-Point or Pub/Sub Topic design. It provides a wealth of other features like distributed common Java Models like queues, stacks, sets, list, maps, etc.

[Read the documentation here.](http://docs.hazelcast.org/docs/latest/manual/html-single/)

## Java Config v.s. XML Config

You can use either Java or XML configuration, I opted to use both here. The [hazelcast.xml](src/main/resources/hazelcast.xml) file is used to define infrastructure config, in this case the multi-casting ip and port. The Java configurations are defined in the code when the Spring Beans are loaded by the Application Context.

## Samples Demonstrated

[HeartbeatTopic.java](src/main/java/com/jahnelgroup/queue/hazelcast/HeartbeatTopic.java):

    Demonstrates a simple Topic, every node that joins the cluster will periodically emit a Heartbeat message to every other node. You shouldn't build your own Heartbeat however, (Heartbeat)[http://docs.hazelcast.org/docs/latest/manual/html-single/search.html?q=heartbeat] is actually already built into Hazelcast.

## Build and run two instances of the app

```bash
gradle build
java -jar -Dspring.profiles.active=publisher build/libs/*.jar
java -jar -Dspring.profiles.active=subscriber target/*.jar --server.port=9001 &
```