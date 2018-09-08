# Hazelcast Distributed Work

This sample demonstrates Spring Boot with Hazelcast to process partitioned work distributed across a cluster. 

## Testing

Use [Apache ab](https://httpd.apache.org/docs/2.4/programs/ab.html) to test this.

```bash
$ ab -T 'application/json' -p ./ab/work-partA-groupOne.json -m POST -c 1 -n 1 http://localhost:8080/
```
