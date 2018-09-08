package com.jahnelgroup.queue.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#iatomiclong
 */
@Component
public class AtomicLongCounter {

    private Logger logger = LoggerFactory.getLogger(HeartbeatTopic.class);

    private IAtomicLong atomicLong;

    public AtomicLongCounter(HazelcastInstance hazelcastInstance){
        atomicLong = hazelcastInstance.getAtomicLong("counter");
        logger.info("Starting Counter");
    }

    @Scheduled(fixedRate = 5000L)
    public void counter(){
        logger.info("AtomicLongCounter incrementing to {}", atomicLong.addAndGet(1L));
    }
}
