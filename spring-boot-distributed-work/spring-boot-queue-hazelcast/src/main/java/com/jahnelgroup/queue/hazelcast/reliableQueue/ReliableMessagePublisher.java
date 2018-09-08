package com.jahnelgroup.queue.hazelcast.reliableQueue;

import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Profile("publisher")
public class ReliableMessagePublisher {

    private Logger logger = LoggerFactory.getLogger(ReliableMessagePublisher.class);

    private IQueue<ReliableMessage> reliableQueue;

    public static String QUEUE_NAME = "reliableQueue";

    public ReliableMessagePublisher(HazelcastInstance hazelcastInstance, ReliableQueueStore store) {
        QueueConfig queueConfig = hazelcastInstance.getConfig().getQueueConfig(QUEUE_NAME);
        queueConfig.setMaxSize(0); // unbounded

        QueueStoreConfig queueStoreConfig = new QueueStoreConfig().setStoreImplementation(store);
        queueStoreConfig.setProperty("memory-limit", "0");

        queueConfig.setQueueStoreConfig(queueStoreConfig);

        this.reliableQueue = hazelcastInstance.getQueue( QUEUE_NAME );
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

        this.reliableQueue = hazelcastInstance.getQueue(QUEUE_NAME);
        logger.info("Starting ReliableMessagePublisher");
    }

    private static AtomicLong count = new AtomicLong();

    @Scheduled(fixedRate = 1000L)
    public void publish() {
        reliableQueue.add(new ReliableMessage("Publishing ReliableMessage " + count.incrementAndGet()));
    }

}