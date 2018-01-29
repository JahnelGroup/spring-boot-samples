package com.jahnelgroup.integration.message.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.jahnelgroup.integration.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jpa.Jpa;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

/**
 * Inbound Integration Flow polling for incoming messages from SQS
 * into this Application.
 */
@Configuration
public class SqsInboundIntegrationConfig {

    private static final Logger logger = LoggerFactory.getLogger(SqsInboundIntegrationConfig.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PollableChannel sqsQueue;

    @Autowired
    private AmazonSQSAsync amazonSQS;

    @Value("${aws.queue}")
    private String queue;

    /**
     * The SqsMessageDrivenChannelAdapter - this will consume messages off of the specified AWS SQS
     * queue and send them on the configured message channel.  In this case, a QueueChannel which must
     * then be polled.
     * @return the SqsMessageDrivenChannelAdapter
     */
    @Bean
    public SqsMessageDrivenChannelAdapter sqsMessageDrivenChannelAdapter() {
        final SqsMessageDrivenChannelAdapter adapter = new SqsMessageDrivenChannelAdapter(amazonSQS, queue);
        adapter.setOutputChannel(sqsQueue);
        return adapter;
    }

    /**
     * A synchronous pub-sub channel.  Since no task executor has been set for the pub-sub
     * channel all subscribers will execute synchronously one at a time.
     * @return the pub-sub channel
     */
    @Bean
    public MessageChannel sqsInboundPubSubChannel() {
        return MessageChannels.publishSubscribe("sqsInboundPubSubChannel").get();
    }

    /**
     * The IntegrationFlow that consumes messages from SQS and publishes them on a pub-sub channel
     * @param jackson2JsonObjectMapper the @Autowired Jackson2JsonObjectMapper
     * @return the IntegrationFlow 'sqsOutputFlow'
     */
    @Bean
    public IntegrationFlow sqsReceiveFlow(Jackson2JsonObjectMapper jackson2JsonObjectMapper) {
        // Start the integration flow at the channel the sqs adapter is putting the messages on
        return IntegrationFlows.from(sqsQueue)
                // Transform the JSON payload to our TextMessage Object
                .transform(Transformers.fromJson(TextMessage.class, jackson2JsonObjectMapper),
                        // This is the first endpoint consuming from the queue, so it must
                        // be configured with a poller to initiate the message flow
                        e -> e.poller(Pollers.fixedRate(50).maxMessagesPerPoll(1)))
                // Enrich a property of the payload
                .enrich(e -> e.propertyFunction("receivedFromSQSTs", m -> LocalDateTime.now()))
                // Log the message at INFO level
                .log(LoggingHandler.Level.INFO)
                // Send the message on a pub-sub channel for any subscribers who care to consume
                .channel(sqsInboundPubSubChannel())
                .get();
    }

    @Bean
    public IntegrationFlow sqsPersistenceFlow() {
        // Subscribe the the pub-sub channel
        return IntegrationFlows.from(sqsInboundPubSubChannel())
                // Persist the TextMessage using a Jpa.outboundAdapter.  Note
                // that outbound adapters do NOT produce a reply so therefore this is
                // the end of the message flow.  If we wanted to continue processing
                // the result we would use the updatingGateway instead.  Gateways
                // produce replies.
                .handle(Jpa.outboundAdapter(entityManagerFactory)
                                .entityClass(TextMessage.class)
                                .persistMode(PersistMode.PERSIST)
                                .flush(Boolean.TRUE),
                        // We need to make this endpoint transactional
                        // since it is persisting data to the database
                        GenericEndpointSpec::transactional)
                .get();
    }

}
