package com.jahnelgroup.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import java.time.LocalDateTime;

@Configuration
public class SqsOutputIntegrationConfig {

    private static final Logger logger = LoggerFactory.getLogger(SqsOutputIntegrationConfig.class);

    @Autowired
    private AmazonSQSAsync amazonSQS;

    @Autowired
    private MessageChannel sqsReplyChannel;

    @Value("${aws.queue}")
    private String queue;

    @Bean
    public MessageChannel sqsReceiveChannel() {
        return MessageChannels.direct("sqsReceiveChannel").get();
    }

    @Bean
    public SqsMessageDrivenChannelAdapter sqsMessageDrivenChannelAdapter() {
        final SqsMessageDrivenChannelAdapter sqsAdapter = new SqsMessageDrivenChannelAdapter(amazonSQS, queue);
        sqsAdapter.setOutputChannel(sqsReceiveChannel());
        return sqsAdapter;
    }

    @Bean
    public IntegrationFlow sqsOutputFlow() {
        return IntegrationFlows.from(sqsReceiveChannel())
                .enrich(e -> e.property("receivedTs", LocalDateTime.now()))
                .handle(m -> {
                    logger.info(
                            "Received message with payload {} after {}ms",
                            ((TextMessage) m.getPayload()).getContent(),
                            ((TextMessage) m.getPayload()).computeSendReceiveDiff());
                    sqsReplyChannel.send(m);
                })
                .get();
    }

}
