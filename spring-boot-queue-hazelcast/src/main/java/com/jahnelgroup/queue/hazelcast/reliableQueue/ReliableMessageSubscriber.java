package com.jahnelgroup.queue.hazelcast.reliableQueue;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@Profile("subscriber")
public class ReliableMessageSubscriber {

    private Logger logger = LoggerFactory.getLogger(ReliableMessageSubscriber.class);

    private IQueue<ReliableMessage> reliableQueue;

    public ReliableMessageSubscriber(HazelcastInstance hazelcastInstance) {
        this.reliableQueue = hazelcastInstance.getQueue(ReliableMessagePublisher.QUEUE_NAME);
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
        logger.info("Starting ReliableMessageSubscriber");
    }

    private static AtomicLong count = new AtomicLong();

    @Scheduled(fixedRate = 5000L)
    public void publish() {
        List<ReliableMessage> receivedMessages = new ArrayList<ReliableMessage>();
        reliableQueue.drainTo(receivedMessages);

        logger.info("Drained {} messages: {}",
                receivedMessages.size(),
                receivedMessages.stream()
                        .map(ReliableMessage::getMessage)
                        .collect(Collectors.joining(",")));
    }

}
