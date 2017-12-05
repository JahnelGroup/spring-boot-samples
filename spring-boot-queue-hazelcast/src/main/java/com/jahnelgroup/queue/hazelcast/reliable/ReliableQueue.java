package com.jahnelgroup.queue.hazelcast.reliable;

import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.*;
import com.jahnelgroup.queue.hazelcast.App;
import com.jahnelgroup.queue.hazelcast.HeartbeatTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReliableQueue {

    private Logger logger = LoggerFactory.getLogger(HeartbeatTopic.class);

    private IQueue<ReliableMessage> reliableQueue;

    public ReliableQueue(HazelcastInstance hazelcastInstance, ReliableQueueStore store){
        QueueConfig queueConfig = hazelcastInstance.getConfig().getQueueConfig("reliableQueue");
        queueConfig.setMaxSize(0); // unbounded
        new QueueStoreConfig().setStoreImplementation(store)

        this.reliableQueue = hazelcastInstance.getQueue( "reliableQueue" );
        this.reliableQueue.addItemListener(
            new ItemListener<ReliableMessage>() {
                @Override
                public void itemAdded(ItemEvent<ReliableMessage> item) {
                    logger.info("ReliableQueue itemAdded: {}", item);
                }

                @Override
                public void itemRemoved(ItemEvent<ReliableMessage> item) {
                    logger.info("ReliableQueue itemRemoved: {}", item);
                }
            }, true);

        logger.info("Starting ReliableQueue");
    }

    private static AtomicLong count = new AtomicLong();

    @Scheduled(fixedRate = 1000L)
    public void heartbeat(){
        reliableQueue.add(new ReliableMessage(count.incrementAndGet()));
    }

}
