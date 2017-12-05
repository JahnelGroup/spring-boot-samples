package com.jahnelgroup.queue.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * http://docs.hazelcast.org/docs/latest-development/manual/html/Distributed_Data_Structures/IAtomicLong.html
 */
//@Component
public class AtomicLongCounter {

    private Logger logger = LoggerFactory.getLogger(HeartbeatTopic.class);

    private IAtomicLong atomicLong;

    public AtomicLongCounter(HazelcastInstance hazelcastInstance){
        atomicLong = hazelcastInstance.getAtomicLong("counter");
        logger.info("Starting Counter");
    }

    @Scheduled(fixedRate = 2000L)
    public void counter(){
        logger.info("Incrementing to {}", atomicLong.addAndGet(1L));
    }
}
