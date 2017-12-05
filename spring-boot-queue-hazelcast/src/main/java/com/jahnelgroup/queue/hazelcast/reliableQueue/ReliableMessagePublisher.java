package com.jahnelgroup.queue.hazelcast.reliableQueue;

import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
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
        queueConfig.setQueueStoreConfig(new QueueStoreConfig().setStoreImplementation(store));





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