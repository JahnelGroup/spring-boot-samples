package com.jahnelgroup.queue.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#topic
 * http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#reliable-topic
 */
@Component
public class HeartbeatTopic {

    private Logger logger = LoggerFactory.getLogger(HeartbeatTopic.class);

    private ITopic hearbeatTopic;

    public HeartbeatTopic(HazelcastInstance hazelcastInstance){
        hearbeatTopic = hazelcastInstance.getTopic( "heartbeatTopic" );
        hearbeatTopic.addMessageListener(msg -> {
            logger.info("Heartbeat Received: {}", msg.getMessageObject());
        });

        logger.info("Starting Heartbeat Topic");
    }

    @Scheduled(fixedRate = 5000L)
    public void heartbeat(){
        hearbeatTopic.publish(App.uuid);
    }
}
