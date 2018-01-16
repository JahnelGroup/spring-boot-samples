package com.jahnelgroup.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
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
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jpa.Jpa;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.PollableChannel;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@Configuration
public class SqsOutputIntegrationConfig {

    private static final Logger logger = LoggerFactory.getLogger(SqsOutputIntegrationConfig.class);

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
     * The IntegrationFlow that consumes messages from SQS
     * @param jackson2JsonObjectMapper the @Autowired Jackson2JsonObjectMapper
     * @return the IntegrationFlow 'sqsOutputFlow'
     */
    @Bean
    public IntegrationFlow sqsOutputFlow(Jackson2JsonObjectMapper jackson2JsonObjectMapper) {
        // Start the integration flow at the channel the sqs adapter is putting the messages on
        return IntegrationFlows.from(sqsQueue)
                // Transform the JSON payload to our TextMessage Object
                .transform(Transformers.fromJson(TextMessage.class, jackson2JsonObjectMapper),
                        // This is the first endpoint consuming from the queue, so it must
                        // be configured with a poller to initiate the message flow
                        e -> e.poller(Pollers.fixedRate(50).maxMessagesPerPoll(1)))
                // Enrich a property of the payload
                .enrich(e -> e.property("receivedTs", LocalDateTime.now()))
                // Log the message at INFO level
                .log(LoggingHandler.Level.INFO)
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
