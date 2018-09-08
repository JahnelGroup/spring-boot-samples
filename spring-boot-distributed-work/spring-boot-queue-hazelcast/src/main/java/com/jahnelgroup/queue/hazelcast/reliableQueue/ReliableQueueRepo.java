package com.jahnelgroup.queue.hazelcast.reliableQueue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReliableQueueRepo extends CrudRepository<ReliableMessage, Long>{

}
